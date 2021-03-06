package com.mzc.db.mysql;

import com.mzc.db.DatabaseEntityMgrFactory;
import com.mzc.db.DbEntityInfo;
import com.mzc.db.IEntityManager;
import com.mzc.db.annotation.EntityIndexes;
import com.mzc.db.annotation.SimpleEntity;
import com.mzc.db.annotation.SimpleId;
import com.mzc.db.annotation.TableAlias;
import com.mzc.db.mysql.data.EntityData;
import com.mzc.db.mysql.data.EntityField;
import com.mzc.db.mysql.page.TablePage;
import com.mzc.utils.CommonUtil;
import com.mzc.utils.SqlUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.mzc.db.DatabaseEntityMgrFactory.BASE_SEQUENCE_TABLE;
import static com.mzc.db.DatabaseEntityMgrFactory.CREATE_TABLE_TAIL;
import static com.mzc.utils.ClassUtil.*;

/**
 * mysql数据库增删改查具体实现类
 *
 * @param <T>
 */
public class MysqlEntityManager<T> implements IEntityManager<T>, Runnable {

    private static final long BASE_ID_SEQUENCE = 1000000000000000000L;
    private static final long SERVERID_SEQUENCE = 1000000000000000L;

    public static final String CLASSNAME_FIELD = "CLASSNAME";

    private static final long SEQUENCE_INCREASE_STEP = 1024;

    private static final String FIELD_DICTIONARY_TABLE_HEAD = "FIELD_DICTIONARY_";
    private static final String TABLE_NAME_HEAD = "ENTITY_";

    private AtomicLong curEntityId = null;
    private long databaseEntityId = 1;
    private TablePageManager tablePageManager;
    private String tableName;
    //表
    private EntityField entityField = null;      //包含父类及所有子类的变量（需要限制各子类不能有重名变量）
    //有修改的数据
    private ConcurrentHashMap<Long, EntityData> entityMap = new ConcurrentHashMap<>();

    private DbEntityInfo info;
    /**
     * 同步数据库间隔，单位：ms (后续根据配置调整)
     */
    private long period = 120_000L;


    public MysqlEntityManager(DbEntityInfo info) {
        this.info = info;
    }

    @Override
    public long nextId() throws Exception {
        long id = curEntityId.incrementAndGet();
        if (databaseEntityId <= id) {
            this.pollDatabaseEntityId(id, false);
        }
        return BASE_ID_SEQUENCE + DatabaseEntityMgrFactory.getInst().getServerId() * SERVERID_SEQUENCE + id;
    }

    private synchronized void pollDatabaseEntityId(long id, boolean initial) throws SQLException {
        if (!initial && databaseEntityId > id) {
            return;
        }
        String classname = info.getClassName();
        Connection conn = null;
        ResultSet resultSet = null;
        Statement statement = null;
        String sql = String.format("SELECT curId FROM %s WHERE classname = '%s'", BASE_SEQUENCE_TABLE, classname);
        System.out.println(sql);
        try {
            conn = DatabaseEntityMgrFactory.getInst().getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                resultSet.close();
                sql = String.format("INSERT INTO %s (classname, curId) VALUES (?, ?)", BASE_SEQUENCE_TABLE);
                statement = conn.prepareStatement(sql);
                curEntityId = new AtomicLong(0);
                databaseEntityId = SEQUENCE_INCREASE_STEP;
                ((PreparedStatement) statement).setString(1, classname);
                ((PreparedStatement) statement).setLong(2, databaseEntityId);
                ((PreparedStatement) statement).execute();
            } else {
                long curId = resultSet.getLong(1);
                if (initial) {
                    curEntityId = new AtomicLong(curId);
                }
                resultSet.close();
//            curId += SEQUENCE_INCREASE_STEP;
                databaseEntityId = curId + SEQUENCE_INCREASE_STEP;
                sql = String.format("UPDATE %s SET curId=%d WHERE classname='%s'", BASE_SEQUENCE_TABLE, databaseEntityId, classname);
                int updateCnt = statement.executeUpdate(sql);
                System.out.println("[update sequenceId] [classname:" + classname + "] [curId:" + curId + "] [updatecnt:" + updateCnt + "]");
            }
        } finally {
            try {
                statement.close();
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public T getEntity(long id) throws Exception {
        EntityData data = entityMap.get(id);
        if (data != null && data.get() != null) {
            return (T) data.get();
        }
        String sql = tablePageManager.getSelectSql(id);
        System.out.printf("select sql: %s\n", sql);
        try (Connection conn = DatabaseEntityMgrFactory.getInst().getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return this.fullfileObject(resultSet);
            }
        }
        return null;
    }

    private T fullfileObject(ResultSet resultSet) throws Exception {
        String classname = resultSet.getString(CLASSNAME_FIELD);
        long id = resultSet.getLong(entityField.getIdField().getName());
        Class clazz = entityField.getClassByClassname(classname);
        T t = (T) clazz.newInstance();
        HashMap<String, Integer> nameIndexMap = new HashMap<>();
        entityField.getIdField().set(t, id);
        entityField.fullfillObject(f -> {
            try {
                String fieldName = ((Field) f).getName();
                nameIndexMap.put(fieldName, resultSet.findColumn(fieldName));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        SqlUtil.constructObject(t, nameIndexMap, resultSet, entityField.getOtherFieldMap().values());
        entityMap.put(id, new EntityData<T>(id, clazz, entityField, t, period));
        return t;
    }
    private T fullfileObject(ResultSet resultSet, List<String> fieldnames) throws Exception {
        List<Field> fields = fieldnames.stream().map(s->entityField.getFieldByFieldName(s)).collect(Collectors.toList());
        String classname = resultSet.getString(CLASSNAME_FIELD);
        Class clazz = entityField.getClassByClassname(classname);
        T t = (T) clazz.newInstance();
        HashMap<String, Integer> nameIndexMap = new HashMap<>();
        entityField.fullfillObject(f -> {
            try {
                String fieldName = ((Field) f).getName();
                nameIndexMap.put(fieldName, resultSet.findColumn(fieldName));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, fields);
        SqlUtil.constructObject(t, nameIndexMap, resultSet, fields);
        return t;
    }

    @Override
    public List<T> getEntitys(long[] ids) throws Exception {
        List<T> result = new ArrayList<>();
        Map<TablePage, long[]> idMaps = new HashMap<>();
        Arrays.stream(ids).distinct().forEach(id -> {
            EntityData data = entityMap.get(id);
            if (data != null && data.get() != null) {
                result.add((T) data.get());
                return;
            }
            TablePage page = tablePageManager.getPageByEntityId(id);
            long[] value = idMaps.get(page);
            long[] resultValue = null;
            if (value == null) {
                resultValue = new long[]{id};
            } else {
                resultValue = new long[value.length + 1];
                System.arraycopy(value, 0, resultValue, 0, value.length);
                resultValue[resultValue.length - 1] = id;
            }
            idMaps.put(page, resultValue);
        });
        try (Connection conn = DatabaseEntityMgrFactory.getInst().getConnection()) {
            idMaps.entrySet().stream().forEach(e -> {
                long[] pageIds = e.getValue();
                String sql = tablePageManager.generageSelectSql(pageIds, e.getKey());
//                System.out.printf("mul select sql : %s\n", sql);
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    for (int i = 0, len = pageIds.length; i < len; i++) {
                        statement.setLong((i + 1), pageIds[i]);
                    }
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        T t = fullfileObject(resultSet);
                        result.add(t);
                    }
                    resultSet.close();
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            });
        }
        return result;
    }

    @Override
    public List<T> getEntitysWithCondition(Class<?> clazz, String prepareWhere, String order, Object[] parameter, long start, long end) throws Exception {
        return null;
    }

    @Override
    public <S> List<S> queryFields(Class<S> clazz, long[] ids) throws Exception {
        if (!clazz.isInterface()) {
            throw new RuntimeException(String.format("[传入的class不是接口: %s]", clazz.getName()));
        }
        List<S> result = new ArrayList<>();
        Map<TablePage, long[]> idMaps = new HashMap<>();
        Arrays.stream(ids).distinct().forEach(id -> {
            EntityData data = entityMap.get(id);
            if (data != null && data.get() != null) {
                result.add((S) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new EntityDataFieldHandler((T) data.get(), entityField)));
                return;
            }
            TablePage page = tablePageManager.getPageByEntityId(id);
            long[] value = idMaps.get(page);
            long[] resultValue = null;
            if (value == null) {
                resultValue = new long[]{id};
            } else {
                resultValue = new long[value.length + 1];
                System.arraycopy(value, 0, resultValue, 0, value.length);
                resultValue[resultValue.length - 1] = id;
            }
            idMaps.put(page, resultValue);
        });
        List<String> fieldnames = CommonUtil.getFieldnames(clazz);
        try (Connection conn = DatabaseEntityMgrFactory.getInst().getConnection()) {
            idMaps.entrySet().stream().forEach(e -> {
                long[] pageIds = e.getValue();
                System.out.println(Arrays.toString(pageIds));
                String sql = tablePageManager.generageSelectSql(pageIds, e.getKey(), fieldnames);
//                System.out.printf("mul select sql : %s\n", sql);
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    for (int i = 0, len = pageIds.length; i < len; i++) {
                        statement.setLong((i + 1), pageIds[i]);
                    }
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        T t = fullfileObject(resultSet, fieldnames);
                        result.add((S) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new EntityDataFieldHandler(t, entityField)));
                    }
                    resultSet.close();
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            });
        }
        return result;
    }

    class EntityDataFieldHandler implements InvocationHandler{

        T t;
        EntityField entityField;

        public EntityDataFieldHandler(T t, EntityField entityField) {
            this.t = t;
            this.entityField = entityField;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodname = method.getName();
            String fieldname = CommonUtil.getFieldname(methodname);
            Field field = entityField.getFieldByFieldName(fieldname);
            return field == null ? null : field.get(t);
        }
    }

    @Override
    public void insert(T t, boolean saveNow) throws Exception {
        long id = entityField.getIdField().getLong(t);
        EntityData data = entityMap.get(id);
        if (data != null) {
            throw new RuntimeException(String.format("[存储对象id重复:%d]", id));
        }
        data = new EntityData(id, t.getClass(), entityField, t, period);
        data.notifyInsert();
        entityMap.put(id, data);
        if (saveNow) {
            insertEntity2DB(id);
        }
    }

    @Override
    public void update(T t, String fieldname) throws Exception {
        long id = (long) entityField.getIdField().get(t);
        EntityData data = entityMap.get(id);
        if (data == null) {
            System.out.printf("[需要先执行insert才可以更新数据] [id: %d]\n", id);
            return;
        }
        Field field = entityField.getFieldByFieldName(fieldname);
        if (field == null) {
            System.out.printf("[找不到对应field: %s] [id: %d]\n", fieldname, id);
            return;
        }
        data.notifyChanged(field);
    }

    @Override
    public long count() throws Exception {
        long cnt = 0;
        try (Connection conn = DatabaseEntityMgrFactory.getInst().getConnection(); Statement statement = conn.createStatement()) {
            ResultSet resultSet = null;
            for (String sql : tablePageManager.getCountSqls()) {
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    cnt += resultSet.getLong(1);
                }
                resultSet.close();
            }
        }
        return cnt;
    }

    @Override
    public long count(Class<?> clazz, String prepareWhere, Object[] parameter) throws Exception {
        long cnt = 0;
        try(Connection conn = DatabaseEntityMgrFactory.getInst().getConnection(); Statement statement = conn.createStatement();) {
            List<String> sqlList = tablePageManager.countAllSql();
            for (String sql : sqlList) {
                ResultSet resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    cnt += resultSet.getLong(1);
                }
            }
        }
        return cnt;
    }

    @Override
    public void init() throws Exception {
        this.pollDatabaseEntityId(0, true);
        this.initTable();
        this.initSubClass();
        DatabaseEntityMgrFactory.getInst().executeTask(this);
    }

    private void initTable() throws Exception {
        Class<?> clazz = info.getClazz();
        if (clazz.getAnnotation(SimpleEntity.class) == null) {
            throw new Exception("[指定的类不是SimpleEntity:" + info.getClassName() + "]");
        }
        if (!haveNoneParamConstructor(clazz)) {
            throw new Exception("[指定的类没有无参构造方法] [" + info.getClassName() + "]");
        }
        tableName = clazz.getSimpleName().toUpperCase();
        {
            Annotation annotation = clazz.getAnnotation(TableAlias.class);       //表名
            if (annotation != null) {
                tableName = ((TableAlias) annotation).value().toUpperCase();
            }
        }
        Annotation annotation = clazz.getAnnotation(EntityIndexes.class);       //索引
        if (annotation != null) {
            // TODO: 2019/4/17 需要读取注解中配置的索引，在数据表中添加相应的索引，考虑新增或者删除索引？
        }

        Field[] fields = getSerializableFields(clazz);
        Field idField = null;
        HashMap<String, Field> otherFieldMap = new HashMap<>();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.getAnnotation(SimpleId.class) != null) {
                if (idField != null) {
                    throw new Exception("[重复定义SimpleId] [" + info.getClassName() + "] [" + idField.getName() + "] [" + f.getName() + "]");
                }
                idField = f;
            } else {
                otherFieldMap.put(f.getName(), f);
            }
            if (!this.checkFieldType(f.getGenericType())) {
                throw new Exception("[不支持的参数类型] [class:" + info.getClassName() + "] [field:" + f.getName() + "]");
            }
        }
        if (idField == null) {
            throw new Exception("[没有定义SimpleId] [" + info.getClassName() + "]");
        }
        this.entityField = new EntityField(clazz, null, idField, otherFieldMap);
        this.checkDictionaryTable(clazz, fields);
        this.checkTable();
    }

    /**
     * 子类初始化
     */
    private void initSubClass() {

    }

    private void checkTable() throws SQLException {
        this.tablePageManager = new TablePageManager();
        this.tablePageManager.init(this, info.getClazz(), this.entityField);
    }

    /**
     * 变量类型检查
     *
     * @param type
     * @return
     */
    private boolean checkFieldType(Type type) {
        if (type instanceof ParameterizedType) {
            checkCollection((ParameterizedType) type);
            return true;
        } else if (type instanceof Class) {
            Class clazz = (Class) type;
            if (isBasicType(clazz) || isEntitySerialzable(clazz)) {
                return true;
            }
            if (clazz.isArray()) {
                while ((clazz = clazz.getComponentType()).isArray()) {
                }
                if (isBasicType(clazz) || isEntitySerialzable(clazz) || isCollectionField(clazz)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void checkCollection(ParameterizedType t) {
        Type[] types = t.getActualTypeArguments();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                checkCollection((ParameterizedType) type);
            } else if (type instanceof Class) {
                Class clazz = (Class) type;
                if (clazz.isArray()) {
                    throw new RuntimeException("[不支持的类型] [" + clazz.getName() + "]");
                } else if (clazz.isPrimitive() || isEntitySerialzable(clazz)) {
                    continue;
                }
            }
        }
    }

    /**
     * 字典表检查
     *
     * @param clazz
     * @param seralzableFields
     * @throws Exception
     */
    private void checkDictionaryTable(Class clazz, Field[] seralzableFields) throws Exception {
        String fullTableName = FIELD_DICTIONARY_TABLE_HEAD + tableName;
        Connection conn = null;
        Statement statement = null;
        try {
            conn = DatabaseEntityMgrFactory.getInst().getConnection();
            statement = conn.createStatement();
            //检查字典表是否存在
            DatabaseMetaData md = conn.getMetaData();
            String sql;
            ResultSet resultSet = md.getTables(null, null, fullTableName, null);
            if (!resultSet.next()) {        //字典表不存在，需要创建
                resultSet.close();
                sql = String.format("CREATE TABLE %S (id int not null primary key auto_increment, classname varchar(200) not null, fieldname varchar(200) not null, fieldType varchar(200) not null)%s", fullTableName, CREATE_TABLE_TAIL);
                statement.executeUpdate(sql);
            }
            sql = String.format("SELECT * FROM %s WHERE className='%s'", fullTableName, clazz.getSimpleName());
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {            //没有对应classname的数据，需要插入新的字典数据
                resultSet.close();
                this.initalDictionaryData(conn, fullTableName, clazz, seralzableFields);
            } else {
                this.checkDictionaryData(conn, fullTableName, resultSet, seralzableFields, clazz);
                resultSet.close();
            }
        } finally {
            try {
                statement.close();
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 初始化字典表数据
     *
     * @param conn
     * @param dictionaryTablename
     * @param clazz
     * @param seralzableFields
     * @throws SQLException
     */
    private void initalDictionaryData(Connection conn, String dictionaryTablename, Class clazz, Field[] seralzableFields) throws SQLException {
        String sql = String.format("INSERT INTO %S (classname, fieldname, fieldType) values(?, ?, ?)", dictionaryTablename);
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        for (Field f : seralzableFields) {
            preparedStatement.setString(1, clazz.getSimpleName());
            preparedStatement.setString(2, f.getName());
            preparedStatement.setString(3, f.getType().getName());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
    }

    /**
     * 检查字段修改（不允许删除字段，只能加，防止上线后误删数据）
     *
     * @param conn
     * @param dictionaryTablename
     * @param resultSet
     * @param fields
     * @param clazz
     * @throws Exception
     */
    private void checkDictionaryData(Connection conn, String dictionaryTablename, ResultSet resultSet, Field[] fields, Class clazz) throws Exception {
        HashMap<String, TableFieldInfo> tableFieldMap = new HashMap<>();
        do {
            String fieldName = resultSet.getString(3);
            String fieldType = resultSet.getString(4);
            tableFieldMap.put(fieldName, new TableFieldInfo(fieldType));
        } while (resultSet.next());
        List<Field> newFields = new ArrayList<>();
        for (Field f : fields) {
            TableFieldInfo info = tableFieldMap.get(f.getName());
            if (info == null) {
                newFields.add(f);
                System.out.printf("[新增字段] [%s] [%s, %s]\n", clazz.getName(), f.getName(), f.getType());
            } else {
                if (!f.getType().getName().equalsIgnoreCase(info.type)) {
                    throw new RuntimeException("[不允许修改字段类型] [" + clazz.getName() + "] [原类型:" + info.type + "] [修改后类型:" + f.getType().getName() + "]");
                }
                info.exist = true;
            }
        }
        tableFieldMap.forEach((k, v) -> {
            if (!v.exist) {
                throw new RuntimeException("[不允许删除字段] [" + clazz.getName() + "] [fieldname：" + k + ", fieldType:" + v.type + "]");
            }
        });
        this.initalDictionaryData(conn, dictionaryTablename, clazz, newFields.toArray(new Field[0]));
    }

    public String getTableBaseName() {
        return TABLE_NAME_HEAD + this.tableName;
    }

    @Override
    public void run() {
        try {
            this.saveChanged(false);
        } finally {
            DatabaseEntityMgrFactory.getInst().executeTask(this);
        }
    }

    public void destory() {
        this.saveChanged(true);
    }

    private void saveChanged(boolean saveAll) {
        long now = System.currentTimeMillis();
        entityMap.entrySet().stream().filter(e -> {
//            System.out.printf("[update, data: %s ]",e);
            return (saveAll || e.getValue().saveTime <= now) && e.getValue().changed.get();
        }).forEach(e -> {
            EntityData data = e.getValue();
            if (data.get() == null) {
                System.out.printf("[有数据丢失，可能是上层没有持有引用在垃圾回收时被回收了] [id : %d]", data.getId());
            } else {
                if (data.needInsert.get()) {
                    //insert
                    // TODO: 2019/4/26 此处之后可以调整为先统计出来所有需要insert的数据，新增一个batchInsert方法，批量插入
                    try {
                        this.insertEntity2DB(data.getId());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    //update
                    try {
                        this.updateEntity2DB(data.getId());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public EntityField getEntityField() {
        return entityField;
    }

    private void updateEntity2DB(long id) throws Exception {
        EntityData data = entityMap.get(id);
        if (data == null || data.get() == null) {
            System.out.printf("[更新数据异常，不存在id[%d]的对象, 或者数据丢失, data[%s]]\n", id, data);
            return;
        }
        if (!data.compareAndSetChanged(true, false)) {
            System.out.println("已经更新过");
            return;
        }
        try (Connection conn = DatabaseEntityMgrFactory.getInst().getConnection()) {
            String sql = this.generateUpdateSql(data);
            System.out.printf("[update sql: %s]\n", sql);
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                data.fullFillParamater(statement, false);
                conn.setAutoCommit(false);
                statement.execute();
                try {
                    conn.commit();
                    data.clearChanged();
                } catch (Exception e) {
                    conn.rollback();
                    e.printStackTrace();
                } finally {
                    conn.setAutoCommit(true);
                }
            }
        }

    }

    private void insertEntity2DB(long id) throws Exception {
        EntityData data = null;
        try (Connection conn = DatabaseEntityMgrFactory.getInst().getConnection()) {
            conn.setAutoCommit(false);
            data = entityMap.get(id);
            if (data == null) {
                throw new RuntimeException(String.format("[需要先insert才可以存库:%d]", id));
            }
            if (!data.compareAndSetNeedInsert(true, false)) {
                return;
            }
            data.compareAndSetChanged(true, false);
            String sql = generateInsertSql(data);
//            System.out.printf("insert sql: %s\n", sql);
            PreparedStatement statement = conn.prepareStatement(sql);
            data.fullFillParamater(statement, true);
            statement.execute();
            try {
                conn.commit();
                data.clearChanged();
                tablePageManager.notifyAddNum(new long[]{id});
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                statement.close();
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            if (data != null) {
                data.compareAndSetNeedInsert(false, true);
                data.compareAndSetChanged(false, true);
            }
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 生成insert语句
     *
     * @return
     */
    private String generateInsertSql(EntityData data) {
        StringBuffer sb = new StringBuffer();
        tablePageManager.appendInsertStr(sb);
        data.appendInsertStr(sb);
        return sb.toString();
    }

    private String generateUpdateSql(EntityData data) {
        StringBuffer sb = new StringBuffer();
        tablePageManager.appendUpdateStr(data.getId(), sb);
        data.appendUPdataStr(sb);
        return sb.toString();
    }


    class TableFieldInfo {
        public boolean exist = false;
        public String type;

        public TableFieldInfo(String type) {
            this.type = type;
        }
    }
}

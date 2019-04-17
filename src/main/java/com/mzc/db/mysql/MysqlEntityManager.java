package com.mzc.db.mysql;

import com.mzc.db.DatabaseEntityMgrFactory;
import com.mzc.db.DbEntityInfo;
import com.mzc.db.IEntityManager;
import com.mzc.db.annotation.EntityIndexes;
import com.mzc.db.annotation.SimpleEntity;
import com.mzc.db.annotation.SimpleId;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.mzc.utils.ClassUtil.*;
import static com.mzc.utils.ClassUtil.isEntitySerialzable;

/**
 * mysql数据库增删改查具体实现类
 *
 * @param <T>
 */
public class MysqlEntityManager<T> implements IEntityManager<T>, Runnable {

    private static final long BASE_ID_SEQUENCE = 1000000000000000000L;
    private static final long SERVERID_SEQUENCE = 1000000000000000L;

    private static final long SEQUENCE_INCREASE_STEP = 1024;

    private static final long INIT_DELAY_TIME = 60L;

    private AtomicLong curEntityId;
    private long databaseEntityId = 1;

    private DbEntityInfo info;

    public MysqlEntityManager(DbEntityInfo info) {
        this.info = info;
    }

    /**
     * 同步数据库间隔，单位：s
     */
    private long period = 120L;

    @Override
    public long nextId() throws Exception {
        long id = curEntityId.incrementAndGet();
        if (databaseEntityId <= id) {
            this.pollDatabaseEntityId(id, false);
        }
        return BASE_ID_SEQUENCE + DatabaseEntityMgrFactory.getInst().getServerId() * SERVERID_SEQUENCE + id;
    }

    private synchronized void pollDatabaseEntityId(long id, boolean initial) throws SQLException {
        if (databaseEntityId > id) {
            return;
        }
        String classname = info.getClassName();
        Connection conn = null;
        ResultSet resultSet = null;
        Statement statement = null;
        String sql = String.format("SELECT curId FROM %s WHERE classname = '%s'", DatabaseEntityMgrFactory.BASE_SEQUENCE_TABLE, classname);
        try {
            conn = DatabaseEntityMgrFactory.getInst().getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            long curId = resultSet.getLong(1);
            if (initial) {
                curEntityId = new AtomicLong(curId);
            }
//            curId += SEQUENCE_INCREASE_STEP;
            databaseEntityId = curId + SEQUENCE_INCREASE_STEP;
            sql = String.format("UPDATE %s SET curId=%d WHERE classname='%s'", DatabaseEntityMgrFactory.BASE_SEQUENCE_TABLE, databaseEntityId, classname);
            int updateCnt = statement.executeUpdate(sql);
            System.out.println("[update sequenceId] [classname:" + classname + "] [curId:" + curId + "] [updatecnt:" + updateCnt + "]");
        } finally {
            try {
                statement.close();
                resultSet.close();
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public T getEntity(long id) throws Exception {
        return null;
    }

    @Override
    public List<T> getEntitys(long[] ids) throws Exception {
        return null;
    }

    @Override
    public List<T> getEntitysWithCondition(Class<?> clazz, String prepareWhere, String order, Object[] parameter, long start, long end) throws Exception {
        return null;
    }

    @Override
    public <S> List<S> queryFields(Class<S> clazz, long[] ids) throws Exception {
        return null;
    }

    @Override
    public void insert(T t) throws Exception {

    }

    @Override
    public void update(T t, String field) throws Exception {

    }

    @Override
    public long count() throws Exception {
        return 0;
    }

    @Override
    public long count(Class<?> clazz, String prepareWhere, Object[] parameter) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseEntityMgrFactory.getInst().getConnection();
            Statement stat = conn.createStatement();

        } finally {
            conn.close();
        }
        return 0;
    }

    @Override
    public void init() throws Exception {
        DatabaseEntityMgrFactory.getInst().exexutor.scheduleAtFixedRate(this, INIT_DELAY_TIME, period, TimeUnit.SECONDS);
        this.pollDatabaseEntityId(0, true);
        this.initTable();
    }

    private void initTable() throws Exception {
        Class<?> clazz = info.getClazz();
        if (clazz.getAnnotation(SimpleEntity.class) == null) {
            throw new Exception("[指定的类不是SimpleEntity:" + info.getClassName() + "]");
        }
        if (!haveNoneParamConstructor(clazz)) {
            throw new Exception("[指定的类没有无参构造方法] ["+info.getClassName()+"]");
        }
        Annotation annotation = clazz.getAnnotation(EntityIndexes.class);       //索引
        if (annotation != null) {

        }
        Field[] fields = getSerializableFields(clazz);
        Field idField = null;
        HashMap<String, Field> otherFieldMap = new HashMap<>();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.getAnnotation(SimpleId.class) != null){
                if (idField != null) {
                    throw new Exception("[重复定义SimpleId] ["+info.getClassName()+"] [" + idField.getName() + "] [" + f.getName() + "]");
                }
                idField = f;
            } else{
                otherFieldMap.put(f.getName(), f);
            }
            if (!this.checkFieldType(f.getGenericType())){
                throw new Exception("[不支持的参数类型] [class:"+info.getClassName()+"] [field:" + f.getName() + "]");
            }
        }
        if (idField == null){
            throw new Exception("[没有定义SimpleId] [" + info.getClassName() + "]");
        }
    }

    /**
     * 变量类型检查
     * @param type
     * @return
     */
    private boolean checkFieldType(Type type){
        if (type instanceof Class){
            Class clazz = (Class) type;
            if (clazz.isPrimitive() || isEntitySerialzable(clazz)){
                return true;
            }
            if (clazz.isArray()){
                Class cl = clazz;
                while ((cl = clazz.getComponentType()).isArray()){}
                if (cl.isPrimitive() || isEntitySerialzable(cl) || isCollectionField(cl)){
                    return true;
                }
            }
            return false;
        } else if (type instanceof ParameterizedType){
            // TODO: 2019/4/17 参数类型需要额外逻辑判定支持的类型
        }
        return false;
    }

    @Override
    public void run() {

    }
}

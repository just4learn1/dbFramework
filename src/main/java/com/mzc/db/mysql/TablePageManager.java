package com.mzc.db.mysql;

import com.mzc.db.DatabaseEntityMgrFactory;
import com.mzc.db.mysql.data.EntityField;
import com.mzc.db.mysql.page.TablePage;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.mzc.db.DatabaseEntityMgrFactory.CREATE_TABLE_TAIL;
import static com.mzc.db.mysql.MysqlEntityManager.CLASSNAME_FIELD;
import static com.mzc.db.mysql.page.TablePage.TABLE_INDEX_TAG;
import static com.mzc.utils.CommonUtil.replaceStr;
import static com.mzc.utils.SqlUtil.parseClassType2DbType;

public class TablePageManager {

    private ReentrantLock lock = new ReentrantLock();

    private static final String MAIN_TABLE_HEAD = "";       //主表中存储简单对象，利用innodb聚簇索引优势，增加查询效率， 支持添加索引，支持where条件查询
    private static final String SECOND_TABLE_HEAD = "";     //副表中存储复杂数据类型，统一转为json格式存储，不支持索引/where查询

    private int tableMaxCount = 200_0000;               //单表最大存储数据量
    private float loadFactor = 0.9f;                    //负载因子，单表数据量达到 Math.ceil(loadFactor * tableMaxCount) 就会创建preparePage，等达到单表上限后启用新表

    private volatile TablePage currentPage = null;           //当前使用的分页 (设置为volatile，主要是为了在使用的时候不加锁，只有在切换为准备表的时候加锁，volatile保证了线程可见性，切换时使用直接赋值(原子操作))
    private volatile TablePage preparePage = null;           //预备使用的分页

    private List<TablePage> allPages = new ArrayList<>();
    private EntityField entityField;

    public void init(MysqlEntityManager entityManager, Class clazz, EntityField entityField) throws SQLException {
        this.entityField = entityManager.getEntityField();
        Connection conn = DatabaseEntityMgrFactory.getInst().getConnection();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            Statement statement = conn.createStatement();
            ResultSet resultSet = metaData.getTables(null, null, entityManager.getTableBaseName() + TABLE_INDEX_TAG + "%", null);
            List<String> tableNames = new ArrayList<>();
            while (resultSet.next()) {
                tableNames.add(resultSet.getString(3));
            }
            resultSet.close();
            // TODO: 2019/4/24 暂时都先只考虑主表，副表后面再看怎么弄
            if (tableNames.size() == 0) {
                // TODO: 2019/4/18  创建初始表  主表，副表都需要
                String page1TableName = entityManager.getTableBaseName() + TablePage.TABLE_INDEX_TAG + "0";
                TablePage page = new TablePage(page1TableName, 0);
                allPages.add(page);
                this.currentPage = page;
                this.createMainTable(conn, page);
            } else {
                // TODO: 2019/4/18 检查主表索引是否有修改（副表不需要检查，由于副表中都存储的是json类型的大数据，不支持添加索引）
                for (String tbname : tableNames) {
                    TablePage page = this.checkPagetable(conn, tbname);
                    allPages.add(page);
                    if (currentPage == null || currentPage.getPageIndex() < page.getPageIndex()) {
                        currentPage = page;
                    }
                }
            }
        } finally {
            conn.close();
        }
    }

    /**
     * 切换当前currentPage为preparePage
     */
    private synchronized void switchPage() {
        if (preparePage != null) {
            this.currentPage = preparePage;
            this.preparePage = null;
        }
    }

    /**
     * 检查表中字段，新增字段
     *
     * @param conn
     * @param tbname
     * @return
     */
    private TablePage checkPagetable(Connection conn, String tbname) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet resultSet = metaData.getColumns(null, null, tbname, null);
        List<String> existNames = new ArrayList<>();
        while (resultSet.next()) {
            String fieldName = resultSet.getString(4);
            existNames.add(fieldName);
        }
        resultSet.close();
        List<Field> addColumn = entityField.needAddFields(existNames);
        String lastFieldName = existNames.get(existNames.size() - 1);
        if (addColumn.size() > 0) {
            StringBuffer addColumnSql = new StringBuffer();
            addColumnSql.append("ALTER TABLE ").append(tbname).append("\n");
            for (Field columnField : addColumn) {
                addColumnSql.append("ADD COLUMN ").append(" ").append(parseClassType2DbType(columnField)).append(" AFTER ").append(lastFieldName).append(",");
                lastFieldName = columnField.getName();
            }
            String sql = replaceStr(addColumnSql.toString(), addColumnSql.toString().length() - 1, ";");
            try (Statement statement = conn.createStatement()) {
                statement.executeUpdate(sql);
            }
        }
        String countSql = String.format("SELECT COUNT(*),MIN(id),MAX(id) FROM %s", tbname);
        int cnt = 0;
        long minId = 0;
        long maxId = 0;
        try (Statement statement = conn.createStatement()) {
            resultSet = statement.executeQuery(countSql);
            resultSet.next();
            cnt = resultSet.getInt(1);
            minId = resultSet.getLong(2);
            maxId = resultSet.getLong(3);
            resultSet.close();
        }
        return new TablePage(tbname, cnt, minId, maxId);        //单表存储数量一般限制在200W以内
    }

    private void createMainTable(Connection conn, TablePage page) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE ").append(page.getPageTablename()).append(" (");
        sb.append(parseClassType2DbType(this.entityField.getIdField())).append(" not null primary key");
        sb.append(",").append(CLASSNAME_FIELD).append(" varchar(256) not null");
        this.entityField.getOtherFieldMap().forEach((k, v) -> {
            sb.append(",");
            sb.append(parseClassType2DbType(v)).append(" not null ");
        });
        sb.append(")").append(CREATE_TABLE_TAIL);
        try (Statement statement = conn.createStatement();) {
            System.out.println(sb.toString());
            statement.executeUpdate(sb.toString());
        }
    }

    /**
     * 数据表中插入新的数据
     *
     * @param ids 新增数据的主键id
     */
    public void notifyAddNum(long[] ids) {
        int addNum = ids.length;
        Arrays.sort(ids);
        int newNum = currentPage.notifyAddData(addNum, ids[0], ids[ids.length - 1]);

        if (preparePage == null && newNum >= tableMaxCount * loadFactor) {              //创建准备表
            lock.lock();
            try {
                if (preparePage == null) {
                    String prepareTableName = replaceStr(currentPage.getPageTablename(), currentPage.getPageTablename().length() - 1, String.valueOf(currentPage.getPageIndex() + 1));
                    TablePage page = new TablePage(prepareTableName, 0);
                    try (Connection conn = DatabaseEntityMgrFactory.getInst().getConnection()) {
                        this.createMainTable(conn, page);
                        this.preparePage = page;
                        this.allPages.add(page);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }
        } else if (newNum >= tableMaxCount) {
            this.switchPage();
        }
    }

    public void appendInsertStr(StringBuffer sb) {
        sb.append("INSERT INTO ").append(currentPage.getPageTablename()).append("(");
    }

    public void appendUpdateStr(long id, StringBuffer sb) {
        TablePage page = this.getPageByEntityId(id);
        sb.append("UPDATE ").append(page.getPageTablename());
    }

    public List<String> getCountSqls() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT COUNT(*) FROM %s";
        allPages.forEach(page -> {
            list.add(String.format(sql, page.getPageTablename()));
        });
        return list;
    }

    public TablePage getPageByEntityId(long id) {
        for (TablePage page : allPages) {
            if (page.contains(id)) {
                return page;
            }
        }
        throw new RuntimeException(String.format("[没找到%d对应的tablepage]", id));
    }

    public String getSelectSql(long id) {
        TablePage page = this.getPageByEntityId(id);
        StringBuffer sb = new StringBuffer();
        sb.append(this.selectSql(page)).append(" LIMIT 1");
        return sb.toString();
    }

    public String generageSelectSql(long[] ids, TablePage page) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.selectSql(page));
        for (int i = 1, len = ids.length; i < len; i++) {
            sb.append(" or ").append(entityField.getIdField().getName()).append("=?");
        }
        return sb.toString();
    }

    public String generageSelectSql(long[] ids, TablePage page, List<String> fieldnames) {
        List<Field> list = fieldnames.stream().map(s->entityField.getFieldByFieldName(s)).collect(Collectors.toList());
        StringBuffer sb = new StringBuffer();
        sb.append(this.selectSql(page, list));
        for (int i = 1; i < ids.length; i++) {
            sb.append(" or ").append(entityField.getIdField().getName()).append("=?");
        }
        return sb.toString();
    }

    public List<String> countAllSql() {
        List<String> list = new ArrayList<>();
        String str = "SELECT COUNT(*) FROM %s";
        allPages.forEach(page->{
            list.add(String.format(str, page.getPageTablename()));
        });
        return list;
    }

    private String selectSql(TablePage page) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ").append(CLASSNAME_FIELD).append(",");
        entityField.getOtherFieldMap().forEach((k, v) -> {
            sb.append(k).append(",");
        });
        sb.append(entityField.getIdField().getName()).append(" FROM ").append(page.getPageTablename()).append(" WHERE ").append(entityField.getIdField().getName()).append("=?");
        return sb.toString();
    }

    private String selectSql(TablePage page, List<Field> fields) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ");
        fields.forEach((f) -> {
            sb.append(f.getName()).append(",");
        });
        sb.append(CLASSNAME_FIELD).append(" FROM ").append(page.getPageTablename()).append(" WHERE ").append(entityField.getIdField().getName()).append("=?");
        return sb.toString();
    }
}

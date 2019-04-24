package com.mzc.db.mysql;

import com.mzc.db.DatabaseEntityMgrFactory;
import com.mzc.db.mysql.data.EntityField;
import com.mzc.db.mysql.page.TablePage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.mzc.db.mysql.page.TablePage.TABLE_INDEX_TAG;
import static com.mzc.utils.SqlUtil.parseClassType2DbType;
import static com.mzc.db.DatabaseEntityMgrFactory.CREATE_TABLE_TAIL;

public class TablePageManager {

    private static final String MAIN_TABLE_HEAD = "";       //主表中存储简单对象，利用innodb聚簇索引优势，增加查询效率， 支持添加索引，支持where条件查询
    private static final String SECOND_TABLE_HEAD = "";     //副表中存储复杂数据类型，统一转为json格式存储，不支持索引/where查询

    private int tableMaxCount = 200_0000;               //单表最大存储数据量
    private float loadFactor = 0.9f;                    //负载因子，单表数据量达到 Math.ceil(loadFactor * tableMaxCount) 就会创建preparePage，等达到单表上限后启用新表

    private volatile TablePage currentPage = null;           //当前使用的分页 (设置为volatile，主要是为了在使用的时候不加锁，只有在切换为准备表的时候加锁，volatile保证了线程可见性，切换时使用直接赋值(原子操作))
    private TablePage preparePage = null;           //预备使用的分页

    private List<TablePage> allPages = new ArrayList<>();
    private EntityField entityField;

    public void init(MysqlEntityManager entityManager, Class clazz, EntityField entityField) throws SQLException {
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
                this.entityField = entityManager.getEntityField();
                this.createMainTable(conn, page);
            } else {
                // TODO: 2019/4/18 检查主表索引是否有修改（副表不需要检查，由于副表中都存储的是json类型的大数据，不支持添加索引）
                for (String tbname : tableNames){
                    resultSet = metaData.getColumns(null, null, tbname, null);
                    do {
                        String fieldName = resultSet.getString(3);
                    } while (resultSet.next());
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

    }

    private void checkTables(Connection conn, TablePage page) {

    }

    private void createMainTable(Connection conn, TablePage page) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE ").append(page.getPageTablename()).append(" (");
        sb.append(parseClassType2DbType(this.entityField.getIdField())).append(" not null primary key");
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
}

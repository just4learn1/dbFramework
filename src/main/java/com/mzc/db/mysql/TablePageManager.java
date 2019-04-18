package com.mzc.db.mysql;

import com.mzc.db.mysql.page.TablePage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.mzc.db.mysql.page.TablePage.*;

public class TablePageManager {

    private static final String MAIN_TABLE_HEAD = "";       //主表中存储简单对象，利用innodb聚簇索引优势，增加查询效率， 支持添加索引，支持where条件查询
    private static final String SECOND_TABLE_HEAD = "";     //副表中存储复杂数据类型，统一转为json格式存储，不支持索引/where查询

    private int tableMaxCount = 200_0000;               //单表最大存储数据量
    private float loadFactor = 0.9f;                    //负载因子，单表数据量达到 Math.ceil(loadFactor * tableMaxCount) 就会创建preparePage，等达到单表上限后启用新表

    private TablePage currentPage = null;           //当前使用的分页
    private TablePage preparePage = null;           //预备使用的分页

    public void init(Connection conn, MysqlEntityManager entityManager, Class clazz) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        Statement statement = conn.createStatement();
        ResultSet resultSet = metaData.getTables(null, null, entityManager.getTableBaseName() + TABLE_INDEX_TAG + "%", null);
        List<String> tableNames = new ArrayList<>();
        while (resultSet.next()) {
            tableNames.add(resultSet.getString(3));
        }
        resultSet.close();
        if (tableNames.size() == 0) {
            // TODO: 2019/4/18  创建初始表  主表，副表都需要


        } else {
            // TODO: 2019/4/18  检查表字段（增加。 禁止删除字段及修改字段类型）检查主表索引是否有修改（副表不需要检查，由于副表中都存储的是json类型的大数据，不支持添加索引）
        }
    }
}

package com.mzc.db.mysql.page;

import java.util.List;

public class TablePageMgr {
    private int tableMaxCnt = 1000000;  //单表最多存储多少条数据

    private List<TablePage> pages;

    private TablePage currentPage;      //当前正在使用的分页

    private TablePage preparePage;      //预备分页


    
}

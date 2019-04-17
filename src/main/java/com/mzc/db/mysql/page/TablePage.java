package com.mzc.db.mysql.page;

public class TablePage {
    private static final String TABLE_INDEX_TAG = "I";

    private int pageIndex;      //当前表分页索引

    private long minId  =   0L;     //当前分页主键id最小值
    private long maxId  =   0L;     //当前分页主键id最大值

    public TablePage(String tableName) {
        int index = tableName.lastIndexOf(TABLE_INDEX_TAG);
        this.pageIndex = Integer.parseInt(tableName.substring(index+1));
    }

    public String getName(){
        return TABLE_INDEX_TAG + pageIndex;
    }

    @Override
    public String toString() {
        return "TablePage{" +
                "pageIndex=" + pageIndex +
                ", minId=" + minId +
                ", maxId=" + maxId +
                '}';
    }
}

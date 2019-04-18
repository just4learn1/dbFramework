package com.mzc.db.mysql.page;

import java.util.concurrent.atomic.AtomicInteger;

public class TablePage {
    public static final String TABLE_INDEX_TAG = "I";

    private int pageIndex;      //当前表分页索引

    private long minId = 0L;     //当前分页主键id最小值
    private long maxId = 0L;     //当前分页主键id最大值

    private AtomicInteger count;        //当前分页已存储的数据条数

    public TablePage(String tableName, int cnt) {
        int index = tableName.lastIndexOf(TABLE_INDEX_TAG);
        this.pageIndex = Integer.parseInt(tableName.substring(index + 1));
        this.count = new AtomicInteger(cnt);
    }

    public String getName() {
        return TABLE_INDEX_TAG + pageIndex;
    }

    public int notifyAddData(int addNum) {
        int oldNum = count.get();
        int newNum = oldNum + addNum;
        for (;;){
            if (count.compareAndSet(oldNum, newNum)) {
                return newNum;
            }
            oldNum = count.get();
            newNum = oldNum + addNum;
        }
    }

    public int notifyAddData(){
        return count.incrementAndGet();
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

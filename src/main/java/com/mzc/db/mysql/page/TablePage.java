package com.mzc.db.mysql.page;

import java.util.concurrent.atomic.AtomicInteger;

public class TablePage {
    public static final String TABLE_INDEX_TAG = "I";

    private int pageIndex;      //当前表分页索引

    private String pageTablename;

    private volatile long minId = 0L;     //当前分页主键id最小值
    private volatile long maxId = 0L;     //当前分页主键id最大值

    private AtomicInteger count;        //当前分页已存储的数据条数

    public TablePage(String tableName, int cnt, long minId, long maxId) {
        this(tableName, cnt);
        this.minId = minId;
        this.maxId = maxId;
    }
    public TablePage(String tableName, int cnt) {
        this.pageTablename = tableName;
        int index = tableName.lastIndexOf(TABLE_INDEX_TAG);
        this.pageIndex = Integer.parseInt(tableName.substring(index + 1));
        this.count = new AtomicInteger(cnt);
    }

    public String getName() {
        return TABLE_INDEX_TAG + pageIndex;
    }

//    /**
//     * 增加一条数据
//     * @param id
//     * @return
//     */
//    public int notifyAddData(long id) {
//        for (int oldNum, newNum;;){
//            oldNum = count.get();
//            newNum = oldNum + 1;
//            if (count.compareAndSet(oldNum, newNum)) {
//                if (minId > id) {
//                    minId = id;
//                }
//                if (maxId < id) {
//                    maxId = id;
//                }
//                return newNum;
//            }
//        }
//    }

    /**
     * 批量增加多条数据
     * @param addNum
     * @param minId
     * @param maxId
     * @return
     */
    public int notifyAddData(int addNum, long minId, long maxId) {
        for (int oldNum, newNum;;){
            oldNum = count.get();
            newNum = oldNum + addNum;
            if (count.compareAndSet(oldNum, newNum)) {
                if (this.minId > minId) {
                    this.minId = minId;
                }
                if (this.maxId < maxId) {
                    this.maxId = maxId;
                }
                return newNum;
            }
        }
    }

    public boolean contains(long id) {
        return id >= minId && id <= maxId;
    }

    public int notifyAddData(){
        return count.incrementAndGet();
    }

    public String getPageStr(){
        return TABLE_INDEX_TAG + this.pageIndex;
    }

    public String getPageTablename() {
        return pageTablename;
    }

    public int getPageIndex() {
        return pageIndex;
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

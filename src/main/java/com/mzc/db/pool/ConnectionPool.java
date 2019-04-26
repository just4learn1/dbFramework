package com.mzc.db.pool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPool {
    private static final int MAX_CONNEC_CNT = 100;
    private static final int MIN_CONNECT_CNT = 10;
    private int maxConnNum = 10;        //最大连接数
    private String url;
    private Properties properties;
    private Driver driver;

    private AtomicInteger currentConnNum = new AtomicInteger(0);     //当前开启的数据库连接数

    private BlockingQueue<MyConnection> queue;            //数据库连接队列


    public ConnectionPool(String url, String username, String password, int maxConnNum, Driver driver) {
        this.url = url;
        this.properties = new Properties();
        this.properties.setProperty("user", username);
        this.properties.setProperty("password", password);
        this.driver = driver;
        this.queue = new LinkedBlockingQueue<>();
        if (maxConnNum < MIN_CONNECT_CNT) {
            this.maxConnNum = MIN_CONNECT_CNT;
        } else if (maxConnNum > MAX_CONNEC_CNT) {
            this.maxConnNum = MAX_CONNEC_CNT;
        } else {
            this.maxConnNum = maxConnNum;
        }
    }

    /**
     * 获取数据库链接
     *
     * @param timeout
     * @return
     * @throws Exception
     */
    public MyConnection getConn(long timeout) throws Exception {
        MyConnection conn = queue.poll();
        if (conn != null) {
            return conn;
        }
        if (currentConnNum.get() < maxConnNum) {
            for (int currentNum = currentConnNum.get(), newNum = currentNum + 1; currentNum < maxConnNum; currentNum = currentConnNum.get(), newNum = currentNum + 1) {
                if (currentConnNum.compareAndSet(currentNum, newNum)) {
                    try {
                        Connection realConn = driver.connect(url, properties);
                        conn = new MyConnection(realConn, this);
//                        System.out.printf("create new conn: %s", conn);
                        return conn;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
            }
        }
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }

    protected void back2Poll(MyConnection conn) {
        queue.offer(conn);
    }


}

package com.mzc.db.mysql;

import com.mzc.db.DatabaseEntityMgrFactory;
import com.mzc.db.IEntityManager;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * mysql数据库增删改查具体实现类
 * @param <T>
 */
public class MysqlEntityManager<T> implements IEntityManager<T>, Runnable {

    public static Logger logger = DatabaseEntityMgrFactory.logger;

    private static final long BASE_ID_SEQUENCE = 1000000000000000000L;
    private static final long SERVERID_SEQUENCE = 1000000000000000L;

    private static final long INIT_DELAY_TIME = 60L;

    private AtomicLong curEntityId;
    private long databaseEntityId = 1;

    private Thread thread;
    private String classname;
    /**
     * 同步数据库间隔，单位：s
     */
    private long period = 120L;

    @Override
    public long nextId() throws Exception {
        long id = curEntityId.incrementAndGet();
        if(databaseEntityId <= id){
            this.pollDatabaseEntityId();
        }
        return BASE_ID_SEQUENCE + DatabaseEntityMgrFactory.getInst().getServerId() * SERVERID_SEQUENCE + id;
    }

    private void pollDatabaseEntityId(){
        String sql = "SELECT curId FROM" + DatabaseEntityMgrFactory.BASE_SEQUENCE_TABLE + " WHERE classname = '" + classname + "'";
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
        try{
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

    }

    @Override
    public void run() {

    }
}

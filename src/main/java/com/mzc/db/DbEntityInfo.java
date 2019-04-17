package com.mzc.db;

import com.mzc.db.mysql.MysqlEntityManager;

import java.util.HashMap;

public class DbEntityInfo {
    private int id;
    private String className;
    private Class<?> clazz;
//    private IEntityManager<?> dbem = null;
    private HashMap<Integer, Class<?>> subClasses = null;

    public DbEntityInfo(int id, String className, HashMap<Integer, Class<?>> subClasses) {
        this.id = id;
        this.className = className;
        this.subClasses = subClasses;
        try {
            this.clazz = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public HashMap<Integer, Class<?>> getSubClasses() {
        return subClasses;
    }
}

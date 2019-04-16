package com.mzc.db.config;

import java.util.ArrayList;
import java.util.List;

public class EmfConfig {
    private int serverId;

    private String serverName;

    private DbConfig dbConfig;

    private ArrayList<EntityConfig> entityConfigs = new ArrayList<>();

    static class DbConfig{
        public String dbUrl;

        public String username;

        public String password;

        public DbConfig(String dbUrl, String username, String password) {
            this.dbUrl = dbUrl;
            this.username = username;
            this.password = password;
        }

        @Override
        public String toString() {
            return "DbConfig{" +
                    "dbUrl='" + dbUrl + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    static class EntityConfig{
        public int id;
        public String classPath;

        public EntityConfig(int id, String classPath) {
            this.id = id;
            this.classPath = classPath;
        }

        @Override
        public String toString() {
            return "EntityConfig{" +
                    "id=" + id +
                    ", classPath='" + classPath + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "EmfConfig{" +
                "serverId=" + serverId +
                ", serverName='" + serverName + '\'' +
                ", dbConfig=" + dbConfig +
                ", entityConfigs=" + entityConfigs +
                '}';
    }

    public void addEntityConfig(int id, String classpath){
        entityConfigs.forEach(a->{
            if(a.id == id || a.classPath.equalsIgnoreCase(classpath)){
                throw new RuntimeException("[entity id 或者 classpath 重复：" + id + ", " + classpath + "]");
            }
        });
        entityConfigs.add(new EntityConfig(id, classpath));
    }

    public List<EntityConfig> getEntityConfigs() {
        return entityConfigs;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public DbConfig getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(String dbUrl, String username,String password ) {
        this.dbConfig = new DbConfig(dbUrl, username, password);
    }
}

package com.mzc.db.testEntity;

import com.mzc.db.annotation.SimpleEntity;

@SimpleEntity
public class Player {
    private long id;
    private String username;
    private String accountName;
    private long[] tmpIds;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public long[] getTmpIds() {
        return tmpIds;
    }

    public void setTmpIds(long[] tmpIds) {
        this.tmpIds = tmpIds;
    }
}

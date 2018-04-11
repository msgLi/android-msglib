package com.msg.android.lib.db.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msg on 16/11/7.
 */
public class MsgDbConfig {

    private int dbVersion;

    private List<DataSourceConfig> dbSourceList = new ArrayList<>();

    public int getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
    }

    public List<DataSourceConfig> getDbSourceList() {
        return dbSourceList;
    }

    public void setDbSourceList(List<DataSourceConfig> dbSourceList) {
        this.dbSourceList = dbSourceList;
    }

    public void registerTable(String dbName,Class table){
        for(DataSourceConfig dbConfig : dbSourceList){
            if(dbConfig.getDbName().equals(dbName)){
                dbConfig.getDbTableList().add(table);
                return;
            }
        }

        DataSourceConfig dbConfig = new DataSourceConfig();

        dbConfig.setDbName(dbName);
        dbConfig.getDbTableList().add(table);
        dbSourceList.add(dbConfig);
    }
}

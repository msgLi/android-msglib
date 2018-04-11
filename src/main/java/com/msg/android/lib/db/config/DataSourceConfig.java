package com.msg.android.lib.db.config;

import com.msg.android.lib.db.core.MsgSqliteDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msg on 16/11/7.
 */
public class DataSourceConfig {

    private String dbName;

    private List<Class> dbTableList = new ArrayList<>();

    private MsgSqliteDatabaseHelper dbHelper;

    public MsgSqliteDatabaseHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(MsgSqliteDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<Class> getDbTableList() {
        return dbTableList;
    }

    public void setDbTableList(List<Class> dbTableList) {
        this.dbTableList = dbTableList;
    }
}

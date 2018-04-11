package com.msg.android.lib.db;

import android.content.Context;
import android.text.TextUtils;

import com.msg.android.lib.db.config.DataSourceConfig;
import com.msg.android.lib.db.config.MsgDbConfig;
import com.msg.android.lib.db.core.MsgSqliteDatabaseHelper;

/**
 * Created by msg on 16/11/7.
 */
public final class MsgSqliteDatabaseManager {

    private static MsgDbConfig dbConfig;

    private static Context context;

    private static String usingDbName;

    public static String defaultDbName;

    private MsgSqliteDatabaseManager(){}

    public static void init(Context context,MsgDbConfig dbConfig){
        if(dbConfig == null){
            return;
        }

        MsgSqliteDatabaseManager.dbConfig = dbConfig;
        MsgSqliteDatabaseManager.context = context;

        initDbHelper();
    }

    private static void initDbHelper(){

        if(dbConfig == null || dbConfig.getDbSourceList() == null || dbConfig.getDbSourceList().size() <= 0){
            return;
        }

        defaultDbName = dbConfig.getDbSourceList().get(0).getDbName();
        usingDbName = defaultDbName;
        for(DataSourceConfig dataSourceConfig : dbConfig.getDbSourceList()){
            if(TextUtils.isEmpty(dataSourceConfig.getDbName())){
                continue;
            }
            MsgSqliteDatabaseHelper dbHelper = new MsgSqliteDatabaseHelper(context,dataSourceConfig.getDbName(),dbConfig.getDbVersion(),dataSourceConfig.getDbTableList());
            dataSourceConfig.setDbHelper(dbHelper);
        }
    }

    public static MsgSqliteDatabaseHelper getDbHelper(String dbName){
        if(TextUtils.isEmpty(dbName) || dbConfig == null || dbConfig.getDbSourceList() == null || dbConfig.getDbSourceList().size() <= 0){
            return null;
        }

        for(DataSourceConfig dataSourceConfig : dbConfig.getDbSourceList()){
            if(dbName.equals(dataSourceConfig.getDbName())){
                return dataSourceConfig.getDbHelper();
            }
        }

        return null;
    }

    public static MsgSqliteDatabaseHelper getDefaultDbHelper(){
        return getDbHelper(defaultDbName);
    }

    public static MsgSqliteDatabaseHelper getUsingDbHelper(){
        return getDbHelper(usingDbName);
    }

    public static void setUsingDb(String dbName){
        usingDbName = dbName;
    }

    public static void clearUsingDb(){
        usingDbName = defaultDbName;
    }

}

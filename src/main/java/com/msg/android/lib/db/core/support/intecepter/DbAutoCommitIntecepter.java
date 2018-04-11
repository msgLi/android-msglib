package com.msg.android.lib.db.core.support.intecepter;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.msg.android.lib.core.ioc.service.Intecepter;
import com.msg.android.lib.db.MsgSqliteDatabaseManager;
import com.msg.android.lib.db.core.MsgSqliteDatabaseHelper;

import java.sql.SQLException;

/**
 * Created by msg on 16/11/7.
 */
public class DbAutoCommitIntecepter extends Intecepter {

    private DatabaseConnection databaseConnection = null;

    @Override
    public void before(Object originalParams) {
        MsgSqliteDatabaseHelper dbHelper = MsgSqliteDatabaseManager.getUsingDbHelper();
        if(dbHelper != null){
            ConnectionSource connectionSource = dbHelper.getConnectionSource();
            try {
                databaseConnection = connectionSource.getReadWriteConnection(dbHelper.getDatabaseName());
                if (databaseConnection.isAutoCommitSupported()){
                    if (databaseConnection.isAutoCommit()){
                        databaseConnection.setAutoCommit(false);
                    }
                }
                databaseConnection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void end(Object originalParams,Exception error) {
        try {
            if (error == null){
                if (databaseConnection != null){
                    databaseConnection.commit(null);
                }

            }else{
                if (databaseConnection != null){
                    databaseConnection.rollback(null);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.msg.android.lib.db.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.msg.android.lib.core.ioc.kernel.KernelClass;
import com.msg.android.lib.db.core.support.BaseDao;
import com.msg.android.lib.db.core.support.annotation.TableDao;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by msg on 16/11/7.
 */
public class MsgSqliteDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Map<String,Dao> dbTableDaos = new HashMap<>();

    private Map<String,BaseDao> dbSupportDaos = new HashMap<>();

    private Context context;

    private List<Class> dbTableList = null;

    public MsgSqliteDatabaseHelper(Context context, String dbName, int dbViersion,List<Class> dbTableList){
        super(context.getApplicationContext(),dbName,null,dbViersion);
        this.dbTableList = dbTableList;
        this.context = context.getApplicationContext();
        initTabaleDao();
    }

    protected void initTabaleDao(){
        if(dbTableList != null && dbTableList.size() > 0 && context != null){
            for(Class clzz : dbTableList){
                try {
                    Dao dao = getDao(clzz);

                    BaseDao processDao = null;

                    TableDao tableDao = KernelClass.getAnnotation(clzz,TableDao.class);

                    if(tableDao != null){
                        Constructor constructor = tableDao.clz().getDeclaredConstructor(new Class[]{Context.class});
                        processDao = (BaseDao) constructor.newInstance(context);
                    }else{
                        processDao = new BaseDao(context);
                    }
                    processDao.setDaoSuppert(dao);
                    dbSupportDaos.put(clzz.getName(),processDao);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        if(dbTableList != null && dbTableList.size() > 0){
            for(Class tableClass : dbTableList){
                try {
                    TableUtils.createTableIfNotExists(connectionSource,tableClass);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        if(dbTableList != null && dbTableList.size() > 0){
            for(Class tableClass : dbTableList){
                try {
                    TableUtils.dropTable(connectionSource,tableClass,true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public <D extends BaseDao<T>,T> D getSupportDao(Class<T> clzz){

        if(clzz == null){
            return null;
        }

        return (D)dbSupportDaos.get(clzz.getName());
    }

    @Override
    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
        if(dbTableDaos.containsKey(clazz.getName())){
            return (D)dbTableDaos.get(clazz.getName());
        }

        Dao dao = super.getDao(clazz);

        dbTableDaos.put(clazz.getName(),dao);

        return (D)dao;
    }

    @Override
    public void close() {
        super.close();
        if(dbTableList != null){
            dbTableList.clear();
        }
        dbSupportDaos.clear();
        dbTableDaos.clear();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Map<String, BaseDao> getDbSupportDaos() {
        return dbSupportDaos;
    }

    public List<Class> getDbTableList() {
        return dbTableList;
    }

    public Map<String, Dao> getDbTableDaos() {
        return dbTableDaos;
    }

    public void setDbTableDaos(Map<String, Dao> dbTableDaos) {
        this.dbTableDaos = dbTableDaos;
    }
}

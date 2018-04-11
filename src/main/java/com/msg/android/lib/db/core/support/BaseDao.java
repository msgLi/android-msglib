package com.msg.android.lib.db.core.support;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by msg on 16/11/7.
 */
public class BaseDao<T> implements DbOperatInterface<T>{

    private Dao<T,Integer> daoSuppert;

    public BaseDao(Context context){

    }

    public Dao<T, Integer> getDaoSuppert() {
        return daoSuppert;
    }

    public void setDaoSuppert(Dao<T, Integer> daoSuppert) {
        this.daoSuppert = daoSuppert;
    }

    @Override
    public void insert(T row) throws SQLException {
        daoSuppert.create(row);
    }

    @Override
    public void update(T row) throws SQLException {
        daoSuppert.update(row);
    }

    @Override
    public void delete(T row) throws SQLException {
        daoSuppert.delete(row);
    }

    @Override
    public T selectById(Integer id) throws SQLException {
        return daoSuppert.queryForId(id);
    }

    @Override
    public List<T> selectByCo(T row) throws SQLException{
        return daoSuppert.queryForMatchingArgs(row);
    }
}

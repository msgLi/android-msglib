package com.msg.android.lib.db.core.support;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by msg on 16/11/7.
 */
public interface DbOperatInterface<T> {

    void insert(T row)throws SQLException;

    void update(T row)throws SQLException;

    void delete(T row)throws SQLException;

    T selectById(Integer id)throws SQLException;

    List<T> selectByCo(T row)throws SQLException;

}

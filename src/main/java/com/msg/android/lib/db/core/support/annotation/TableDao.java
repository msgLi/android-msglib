package com.msg.android.lib.db.core.support.annotation;

import com.msg.android.lib.db.core.support.BaseDao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by msg on 16/11/7.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableDao {
    Class<? extends BaseDao> clz();
}

package com.msg.android.lib.core.annotation.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by msg on 16/11/4.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContextUI {

    public String titleViewIdName() default "topTitle";

    public int contextLayout() default 0;

    public String initView() default "";

    public String initListener() default "";

    public String initData() default "";
}

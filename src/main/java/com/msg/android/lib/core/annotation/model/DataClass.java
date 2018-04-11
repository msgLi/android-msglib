package com.msg.android.lib.core.annotation.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by msg on 16/11/5.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataClass {
    String fmt() default "json";
    DataType type();
    String dataSection() default "data";
}

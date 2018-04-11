package com.msg.android.lib.core.annotation.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by msg on 16/11/4.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UISet {
    String value() default "";
    UIElement type() default UIElement.UI_VIEW;
}

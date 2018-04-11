package com.msg.android.lib.core.ioc.template.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by msg on 16/10/16.
 */
public class IocBean {

    private String id;

    private Annotation templateType;

    private Class type;

    private Object instance;

    public Annotation getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Annotation templateType) {
        this.templateType = templateType;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

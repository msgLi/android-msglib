package com.msg.android.lib.core.ioc.template.bean.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by msg on 16/10/16.
 */
@XStreamAlias("bean")
public class XmlBeanTemplate {

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

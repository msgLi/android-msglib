package com.msg.android.lib.core.ioc.template.bean.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by msg on 16/10/16.
 */
@XStreamAlias("ioc")
public class IOCXmlTemplate {

    @XStreamImplicit(itemFieldName = "bean")
    private List<XmlBeanTemplate> beans;

    public List<XmlBeanTemplate> getBeans() {
        return beans;
    }

    public void setBeans(List<XmlBeanTemplate> beans) {
        this.beans = beans;
    }
}

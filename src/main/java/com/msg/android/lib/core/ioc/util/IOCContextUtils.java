package com.msg.android.lib.core.ioc.util;

import android.app.Application;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import com.msg.android.lib.core.ioc.template.annotation.Service;
import com.msg.android.lib.core.ioc.template.annotation.ViewControl;
import com.msg.android.lib.core.ioc.template.bean.IocBean;
import com.msg.android.lib.core.ioc.template.bean.xml.IOCXmlTemplate;
import com.msg.android.lib.core.ioc.template.bean.xml.XmlBeanTemplate;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by msg on 16/10/16.
 */
public final class IOCContextUtils {

    private static final String TAG = IOCContextUtils.class.getName();
    private static final Logger logger = Logger.getLogger(TAG);
    private static final String CONTEXT_XML = "ioc";

    private static IOCXmlTemplate xmlTemplate = null;

    private static List<IocBean> iocBeanList = new ArrayList<IocBean>();

    public static void scanIocBeanByXml(Application application){
        parseIOCXml(application);
        if(xmlTemplate == null){
            return;
        }

        for(XmlBeanTemplate template : xmlTemplate.getBeans()){
            try {
                Class beanClass = Class.forName(template.getType());
                Object bean = beanClass.newInstance();
                IocBean iocBean = new IocBean();
                iocBean.setInstance(bean);
                iocBean.setType(beanClass);
                if(TextUtils.isEmpty(template.getId())){
                    iocBean.setId(beanClass.getSimpleName());
                }else{
                    iocBean.setId(template.getId());
                }
                Annotation annotation = beanClass.getAnnotation(Service.class);
                if (annotation == null){
                    annotation = beanClass.getAnnotation(ViewControl.class);
                }
                iocBean.setTemplateType(annotation);
                iocBeanList.add(iocBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }

    private static void parseIOCXml(Application application){
        int xmlId = application.getResources().getIdentifier(CONTEXT_XML,"xml",application.getPackageName());
        XmlResourceParser xmlResourceParser = application.getResources().getXml(xmlId);
        if(xmlResourceParser == null){
            logger.warning("there is no ioc.xml,please run your script to create it!");
            return;
        }
        int eventType = -1;
        XmlBeanTemplate xmlBeanTemplate = null;
        try {
            while(eventType != XmlResourceParser.END_DOCUMENT){
                if (eventType == xmlResourceParser.START_TAG){
                    if(xmlResourceParser.getName().equals("ioc")){
                        xmlTemplate = new IOCXmlTemplate();
                    }else if (xmlResourceParser.getName().equals("bean")){
                        xmlBeanTemplate = new XmlBeanTemplate();
                        xmlBeanTemplate.setId(xmlResourceParser.getAttributeValue(null,"id"));
                        xmlBeanTemplate.setType(xmlResourceParser.getAttributeValue(null,"type"));
                    }
                }else if (eventType == xmlResourceParser.END_TAG){
                    if(xmlResourceParser.getName().equals("bean")){
                        if(xmlTemplate.getBeans() == null){
                            xmlTemplate.setBeans(new ArrayList<XmlBeanTemplate>());
                        }
                        xmlTemplate.getBeans().add(xmlBeanTemplate);
                    }
                }
                eventType = xmlResourceParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object getBean(Class type){

        if(iocBeanList.size() == 0 || type == null){
            return null;
        }

        for(IocBean iocBean : iocBeanList){
            if(type.isAssignableFrom(iocBean.getType())){
                return iocBean.getInstance();
            }
        }

        return null;
    }

    public static Object getBean(String id){

        if(iocBeanList.size() == 0 || TextUtils.isEmpty(id)){
            return null;
        }

        for (IocBean iocBean : iocBeanList){
            if(iocBean.getId().equals(id)){
                return iocBean.getInstance();
            }
        }

        return null;
    }
}

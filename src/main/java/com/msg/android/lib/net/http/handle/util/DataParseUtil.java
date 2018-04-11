package com.msg.android.lib.net.http.handle.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.msg.android.lib.core.annotation.model.DataClass;
import com.msg.android.lib.core.annotation.model.DataType;
import com.msg.android.lib.core.annotation.template.model.Model;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class DataParseUtil {

	public static String getJsonKey(Class responseClass,String defaultKey){
		String dataKey = defaultKey;
		if(responseClass.getAnnotation(DataClass.class) != null){
			DataClass dataClass = (DataClass) responseClass.getAnnotation(DataClass.class);
			dataKey = dataClass.dataSection();
			if(dataKey == null){
				dataKey = defaultKey;
			}
		}
		return dataKey;
	}

	public static void complieDataParse(String defaultDataKey, String jsonString, Model<Object> model, Class responseClass){
		try {
			if(responseClass.getAnnotation(DataClass.class) != null){
				DataClass dataClass = (DataClass) responseClass.getAnnotation(DataClass.class);
				String fmt = dataClass.fmt();
				String dataKey = DataParseUtil.getJsonKey(responseClass, defaultDataKey);

				if("json".equalsIgnoreCase(fmt)){
					JSONObject json = JSONObject.parseObject(jsonString);
					if(!json.containsKey(dataKey)){
						return;
					}
					if(dataClass.type() == DataType.JSONOBJECT){
						model.setData(JSON.parseObject(String.valueOf(json.getJSONObject(dataKey)), responseClass));
					}else if(dataClass.type() == DataType.JSONLIST){
						model.setData(JSON.parseArray(String.valueOf(json.getJSONArray(dataKey)), responseClass));
					}
				}else if("xml".equalsIgnoreCase(fmt)){
					XStream xstream = new XStream(new XppDriver(new NoNameCoder()));
					xstream.autodetectAnnotations(true);
					xstream.processAnnotations(responseClass);
					Object bean=xstream.fromXML(jsonString);
					model.setData(bean);
				}

			}else{
				JSONObject json = JSONObject.parseObject(jsonString);
				model.setData(JSON.parseObject(String.valueOf(json.getJSONObject(defaultDataKey)), responseClass));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}

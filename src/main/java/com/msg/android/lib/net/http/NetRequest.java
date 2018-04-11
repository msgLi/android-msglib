package com.msg.android.lib.net.http;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;

import com.msg.android.lib.core.annotation.template.model.Model;
import com.msg.android.lib.net.http.handle.CommonModelNetResponseDataParse;
import com.msg.android.lib.net.http.handle.DefaultNetResponseDataParse;
import com.msg.android.lib.net.http.handle.INetResponseDataParse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetRequest {
	
	protected Map<String,String> requestParams = new HashMap<String, String>();
	protected Activity hostActivity;
	protected CallBackAsync callBackAsync;
	protected String responseCharacterSet = null;
	protected Class<? extends INetResponseDataParse> dataParseHandle = CommonModelNetResponseDataParse.class;
	public interface CallBackAsync<T>{
		void callBackAsync(Model<T> response);
	}

	public static class NetParam{
		public String key;
		public String value;

		public NetParam(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	public NetRequest(Activity hostActivity,CallBackAsync callBackAsync) {
		super();
		this.hostActivity = hostActivity;
		this.callBackAsync = callBackAsync;
	}

	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}
	public Map<String,String> getRequestParams(){
		return requestParams;
	}
	
	public String getParameter(String attrname){
		return requestParams.get(attrname);
	}
	
	public void addParameter(String attrname,Object attrvalue){
		if(attrvalue != null){
			requestParams.put(attrname, String.valueOf(attrvalue));
		}
	}
	public Activity getHostActivity() {
		return hostActivity;
	}
	public void setHostActivity(Activity hostActivity) {
		this.hostActivity = hostActivity;
	}

	public void clearParams(){
		requestParams.clear();
	}
	
	public String complieGet(){
		
		List<NetParam> params = complieParams();
		
		if(params.size() <= 0){
			return null;
		}
		
		String httpparams = "";
		for (NetParam valuePair : params) {
			try {
				httpparams += valuePair.getKey();
				httpparams += "=";
				httpparams += URLEncoder.encode(valuePair.getValue(),"UTF-8")+"&";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}		
		httpparams = httpparams.substring(0,httpparams.length() - 1);
		return httpparams;
	}
	
	public List<NetParam> complieParams(){
		List<NetParam> params = new ArrayList<NetParam>();
		Set<String> keySet = getRequestParams().keySet();
		if (null != keySet && 0 != keySet.size()) {
			for (String key : keySet) {
				if(!TextUtils.isEmpty(getParameter(key))){
					params.add(new NetParam(key,getParameter(key)));
				}
			}
		}
		return params;
	}
	
	public String getResponseCharacterSet() {
		return responseCharacterSet;
	}

	public void setResponseCharacterSet(String responseCharacterSet) {
		this.responseCharacterSet = responseCharacterSet;
	}


	public Class<? extends INetResponseDataParse> getDataParseHandle() {
		return dataParseHandle;
	}

	public void setDataParseHandle(Class<? extends INetResponseDataParse> dataParseHandle) {
		this.dataParseHandle = dataParseHandle;
	}

	// 十六进制下数字到字符的映射数组
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };
	
	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param originString
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	protected static String encodeByMD5(String originString) {
		if (originString != null) {
			try {
				// 创建具有指定算法名称的信息摘要
				MessageDigest md = MessageDigest.getInstance("MD5");
				// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
				byte[] results = md.digest(originString.getBytes());
				// 将得到的字节数组变成字符串返回
				String resultString = byteArrayToHexString(results);
				return resultString.toLowerCase();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @param 字节数组
	 * @return 十六进制字符串
	 */
	protected static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}
	
	/** 将一个字节转化成十六进制形式的字符串 */
	protected static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
}

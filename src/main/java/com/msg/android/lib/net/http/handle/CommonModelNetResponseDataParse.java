package com.msg.android.lib.net.http.handle;

import android.text.TextUtils;

import com.msg.android.lib.core.annotation.template.model.Model;
import com.msg.android.lib.net.http.NetRequest;
import com.msg.android.lib.net.http.handle.util.DataParseUtil;


public class CommonModelNetResponseDataParse implements INetResponseDataParse{
	private static final String DEFAULT_DATA_KEY = "data";
	@Override
	public <T> Model<T> responseDataParse(NetRequest request, String result, Class responseClass) throws Exception {
		if(request == null){
			return null;
		}
		Model ret = new Model();
		ret.setErrorCode(0);
		ret.setSuccess(true);
		ret.setErrorMessage(null);
		if(responseClass == null || TextUtils.isEmpty(result)){
			ret.setData(result);
			return ret;
		}

		if(null != request.getResponseCharacterSet()){
			result = new String(result.getBytes(request.getResponseCharacterSet()),"UTF-8");
		}

		DataParseUtil.complieDataParse(DEFAULT_DATA_KEY, result, ret, responseClass);
		return (Model<T>) ret;

	}

}

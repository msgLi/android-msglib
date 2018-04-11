package com.msg.android.lib.net.http.handle;


import com.msg.android.lib.core.annotation.template.model.Model;
import com.msg.android.lib.net.http.NetRequest;

public class DefaultNetResponseDataParse implements INetResponseDataParse {

	@Override
	public Model responseDataParse(NetRequest request, String result, Class responseClass) throws Exception {
		// TODO Auto-generated method stub
		Model response = new Model();
		response.setData(result);
		return response;
	}

}

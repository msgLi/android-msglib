package com.msg.android.lib.net.http.handle;


import com.msg.android.lib.core.annotation.template.model.Model;
import com.msg.android.lib.net.http.NetRequest;

public interface INetResponseDataParse {

	<T> Model<T> responseDataParse(NetRequest request, String result, Class responseClass) throws Exception;
	
}

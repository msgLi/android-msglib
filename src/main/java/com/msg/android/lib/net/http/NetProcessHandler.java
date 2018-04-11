package com.msg.android.lib.net.http;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.msg.android.lib.core.annotation.template.model.Model;
import com.msg.android.lib.net.http.ui.INetPostAsyncProgress;

import java.util.HashMap;


public class NetProcessHandler extends Handler {
	ProgressDialog progressDialog;
	AlertDialog alertDialog;
	private HashMap<Activity, ProgressDialog> progressDialogCache = new HashMap<Activity, ProgressDialog>();
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		NetRequestTask task = (NetRequestTask)msg.obj;

		if(msg.what == NetConst.HANDLE_MESSAGE_FLAG_START_TASK){
			if(task.request.getHostActivity() != null && task.request.getHostActivity() instanceof INetPostAsyncProgress){
				((INetPostAsyncProgress)task.request.getHostActivity()).startProgress();
			}else{
				if(null != task.request.getHostActivity()){
					startProgress(task.request.getHostActivity());
				}
			}
		}else{
			if(task.request.getHostActivity() != null && task.request.getHostActivity() instanceof INetPostAsyncProgress){
				((INetPostAsyncProgress)task.request.getHostActivity()).stopProgress();
			}else{
				if(null != task.request.getHostActivity()){
					progressDialog = progressDialogCache.get(task.request.getHostActivity());
					if(progressDialog != null){
						stopProgress();
					}
					progressDialogCache.remove(task.request.getHostActivity());
				}
			}
			Model<String> errorResponse =null;
			switch(msg.what){
			case NetConst.HANDLE_MESSAGE_FLAG_200:
			case NetConst.HANDLE_MESSAGE_FLAG_SUCCESS:
				Model response = (Model)task.response;
				if(response == null){
					response = new Model();
				}
				response.setErrorCode(0);
				response.setSuccess(true);
				task.request.callBackAsync.callBackAsync(response);
				break;
			case NetConst.HANDLE_MESSAGE_FLAG_500:
				showAlertDialog(task.request.getHostActivity(), "服务器返回错误:500");
				errorResponse = new Model<String>();
				errorResponse.setSuccess(false);
				errorResponse.setData(null);
				errorResponse.setErrorMessage("服务器返回错误:500");
				errorResponse.setErrorCode(NetConst.HANDLE_MESSAGE_FLAG_500);
				task.request.callBackAsync.callBackAsync(errorResponse);
				break;
			case NetConst.HANDLE_MESSAGE_FLAG_400:
				showAlertDialog(task.request.getHostActivity(), "未找到请求的内容");
				errorResponse = new Model<String>();
				errorResponse.setSuccess(false);
				errorResponse.setData(null);
				errorResponse.setErrorMessage("未找到请求的内容");
				errorResponse.setErrorCode(NetConst.HANDLE_MESSAGE_FLAG_400);
				task.request.callBackAsync.callBackAsync(errorResponse);
				break;
			case NetConst.HANDLE_MESSAGE_FLAG_ERROR:
				showAlertDialog(task.request.getHostActivity(), "数据处理存在异常");
				errorResponse = new Model<String>();
				errorResponse.setSuccess(false);
				errorResponse.setData(null);
				errorResponse.setErrorMessage("数据处理存在异常");
				errorResponse.setErrorCode(NetConst.HANDLE_MESSAGE_FLAG_ERROR);
				task.request.callBackAsync.callBackAsync(errorResponse);
				break;
			case NetConst.HANDLE_MESSAGE_FLAG_NETWORK_ERROR:
				showAlertDialog(task.request.getHostActivity(), "未找到目标主机");
				errorResponse = new Model<String>();
				errorResponse.setSuccess(false);
				errorResponse.setData(null);
				errorResponse.setErrorMessage("未找到目标主机");
				errorResponse.setErrorCode(NetConst.HANDLE_MESSAGE_FLAG_NETWORK_ERROR);
				task.request.callBackAsync.callBackAsync(errorResponse);
				break;
			case NetConst.HANDLE_MESSAGE_FLAG_URL_ERROR:
				showAlertDialog(task.request.getHostActivity(), "请求地址有误");
				errorResponse = new Model<String>();
				errorResponse.setSuccess(false);
				errorResponse.setData(null);
				errorResponse.setErrorMessage("请求地址有误");
				errorResponse.setErrorCode(NetConst.HANDLE_MESSAGE_FLAG_URL_ERROR);
				task.request.callBackAsync.callBackAsync(errorResponse);
				break;
			default:
				break;
			}
		}
	}
	
	private void startProgress(Activity activity){
		if(progressDialogCache.get(activity) != null){
			return;
		}
		progressDialog = new ProgressDialog(activity);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("请求处理中");  
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
		progressDialogCache.put(activity, progressDialog);
	}
	
	private void stopProgress(){
		if(progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}
	
	private void showAlertDialog(Activity activity,String message){
		if(null == activity){
			return;
		}
		//alertDialog = new AlertDialog.Builder(activity).setTitle("提示").setMessage(message).setNegativeButton("确定", null).show();
	}
}

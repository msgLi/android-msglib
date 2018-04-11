package com.msg.android.lib.net.http;

import android.os.Message;
import android.text.TextUtils;

import com.msg.android.lib.core.threadpool.intf.IThreadRequest;
import com.msg.android.lib.net.http.handle.INetResponseDataParse;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by msg on 16/11/5.
 */
public class NetRequestTask implements Runnable,IThreadRequest{

    protected static NetProcessHandler handler = new NetProcessHandler();

    public String url;
    public NetRequest request;
    public Class<? extends NetResponse> responseClass;
    public Object response;
    public HttpRequestMethod requestMethod;

    protected static void sendMessage(int code, NetRequestTask task) {
        Message msg = Message.obtain(handler);
        msg.what = code;
        msg.obj = task;
        msg.sendToTarget();
    }

    @Override
    public void run() {
        String result = null;

        try {
            String httpparams = this.request.complieGet();
            if(requestMethod == HttpRequestMethod.GET){
                if(!"".equals(httpparams.trim())){
                    this.url += "?";
                    this.url += httpparams;
                }
            }
            URL url = new URL(this.url);
            URLConnection rulConnection = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
            httpUrlConnection.setRequestProperty("accept", "*/*");
            httpUrlConnection.setRequestProperty("connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setConnectTimeout(10000);
            httpUrlConnection.setReadTimeout(10000);
            httpUrlConnection.setRequestMethod(requestMethod.getValue());
            OutputStream outStrm = null;
            if(TextUtils.isEmpty(httpparams) || requestMethod == HttpRequestMethod.GET){
                httpUrlConnection.connect();
            }else{
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.connect();
                outStrm = httpUrlConnection.getOutputStream();
                outStrm.write(httpparams.getBytes());
                outStrm.flush();
            }

            InputStream inStrm = null;

            int responseStatusCode = httpUrlConnection.getResponseCode();
            if (responseStatusCode == 200) {
                inStrm = httpUrlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStrm));
                String line = null;
                String content = "";
                while((line = reader.readLine()) != null){
                    content += line;
                }
                result = content;
                System.out.println("URL:"+this.url);
                System.out.println("result:" + result);
                try {
                    INetResponseDataParse dataParse = this.request.dataParseHandle.newInstance();
                    this.response = dataParse.responseDataParse(this.request, result, this.responseClass);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage(NetConst.HANDLE_MESSAGE_FLAG_ERROR, this);
                }
                sendMessage(NetConst.HANDLE_MESSAGE_FLAG_200, this);
            } else if (responseStatusCode == 500) {
                sendMessage(NetConst.HANDLE_MESSAGE_FLAG_500, this);
            } else if (responseStatusCode == 400
                    || responseStatusCode == 404
                    || responseStatusCode == 405) {
                sendMessage(NetConst.HANDLE_MESSAGE_FLAG_400, this);
            }

            if (outStrm != null){
                outStrm.close();
            }

            if(inStrm != null){
                inStrm.close();
            }
            httpUrlConnection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            sendMessage(NetConst.HANDLE_MESSAGE_FLAG_ERROR, this);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            sendMessage(NetConst.HANDLE_MESSAGE_FLAG_URL_ERROR, this);
        } catch (ParseException e) {
            e.printStackTrace();
            sendMessage(NetConst.HANDLE_MESSAGE_FLAG_URL_ERROR, this);
        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(NetConst.HANDLE_MESSAGE_FLAG_NETWORK_ERROR, this);
        }
    }

    @Override
    public Runnable getThreadTask() {
        return this;
    }
}

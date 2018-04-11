package com.msg.android.lib.net.http;

/**
 * Created by msg on 16/11/5.
 */
public enum HttpRequestMethod {
    GET("GET"),
    POST("POST");

    private String value;

    HttpRequestMethod(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

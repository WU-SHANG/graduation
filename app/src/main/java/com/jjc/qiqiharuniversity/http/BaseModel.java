package com.jjc.qiqiharuniversity.http;

import android.text.TextUtils;

import com.google.gson.JsonNull;

import java.io.Serializable;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public class BaseModel<T> implements Serializable {
    public static final String STATUS_SUCCESS = "00000";
    public String errorMessage;
    public T data;
    public String statusCode;
    public String userHint;

    public boolean isIllegal() {
        return !STATUS_SUCCESS.equals(statusCode) || data instanceof JsonNull;
    }

    public boolean isUserIllegal() {
        return "A0101".equals(statusCode) || "A0102".equals(statusCode);
    }

    public String getHint() {
        return !TextUtils.isEmpty(userHint) ? userHint : !TextUtils.isEmpty(errorMessage) ? errorMessage : "";
    }

}

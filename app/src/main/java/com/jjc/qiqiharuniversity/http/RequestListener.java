package com.jjc.qiqiharuniversity.http;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public interface RequestListener<T> {

    void onResponse(T t);

    void onFailure(Throwable t);
}

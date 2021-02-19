package com.jjc.qiqiharuniversity.view;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 * @param <V>
 */
public interface ViewCallBack<V> {

    /**
     * @param code code:0:有数据，1：数据为空,2:加载失败
     * @param data 定义好的数据类型
     */

    void refreshView(int code, V data);
}
package com.jjc.qiqiharuniversity.base;

import com.jjc.qiqiharuniversity.view.ViewCallBack;

import java.util.HashMap;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */

public abstract class BasePresenter {
    protected BaseBeanManager mBeanManager;
    public HashMap<String, String> paramMap = new HashMap<>();
    protected ViewCallBack mViewCallBack;

    public BasePresenter(ViewCallBack viewCallBack) {
        mViewCallBack = viewCallBack;
    }

    public void add(ViewCallBack viewCallBack) {
        this.mViewCallBack = viewCallBack;
    }

    public void remove() {
        this.mViewCallBack = null;
    }

    protected abstract void getData();

}

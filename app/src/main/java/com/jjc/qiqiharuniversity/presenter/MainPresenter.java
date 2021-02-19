package com.jjc.qiqiharuniversity.presenter;


import com.jjc.qiqiharuniversity.base.BasePresenter;
import com.jjc.qiqiharuniversity.manager.MainManager;
import com.jjc.qiqiharuniversity.view.ViewCallBack;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */

public class MainPresenter extends BasePresenter {

    public MainPresenter(ViewCallBack viewCallBack) {
        super(viewCallBack);
    }

    public void getData() {
        mBeanManager = new MainManager(mViewCallBack);
        mBeanManager.getData();
    }
}

package com.jjc.qiqiharuniversity.presenter;


import com.jjc.qiqiharuniversity.base.BasePresenter;
import com.jjc.qiqiharuniversity.manager.LoginManager;
import com.jjc.qiqiharuniversity.view.ViewCallBack;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */

public class LoginPresenter extends BasePresenter {

    public LoginPresenter(ViewCallBack viewCallBack) {
        super(viewCallBack);
    }

    public void getData() {
        mBeanManager = new LoginManager(mViewCallBack);
        mBeanManager.mParamMap = paramMap;
        mBeanManager.getData();
    }
}

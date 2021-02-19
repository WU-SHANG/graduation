package com.jjc.qiqiharuniversity.manager;


import com.jjc.qiqiharuniversity.base.BaseBeanManager;
import com.jjc.qiqiharuniversity.view.ViewCallBack;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */
public class MainManager extends BaseBeanManager {

    public MainManager(ViewCallBack modelCallBack) {
        super(modelCallBack);
    }

    public void getData() {
        mViewCallBack.refreshView(1, "MVP返回的数据");
    }
}

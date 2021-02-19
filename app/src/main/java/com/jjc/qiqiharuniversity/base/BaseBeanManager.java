package com.jjc.qiqiharuniversity.base;




import com.jjc.qiqiharuniversity.view.ViewCallBack;

import java.util.HashMap;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */

public abstract class BaseBeanManager {

    protected ViewCallBack mViewCallBack;
    public HashMap<String, String> mParamMap;

    public BaseBeanManager(ViewCallBack modelCallBack) {
        mViewCallBack = modelCallBack;
    }
    public abstract void getData();
}

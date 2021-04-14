package com.jjc.qiqiharuniversity.common.component;


import com.jjc.qiqiharuniversity.common.base.BaseComponent;

/**
 * Author jiajingchao
 * Created on 2021/4/13
 * Description:网络异常组件
 */
public class NetFailComponent extends BaseComponent {

    public NetFailComponent() {
        fragment = new NetFailFragment();
    }

    public void setRefreshListener(NetFailFragment.RefreshListener listener) {
        if (listener != null) {
            ((NetFailFragment) fragment).setRefreshListener(listener);
        }
    }

    public void setNetFailBgColor(int color) {
        ((NetFailFragment) fragment).setBgColor(color);
    }

}

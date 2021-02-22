package com.jjc.qiqiharuniversity.common;

import androidx.fragment.app.FragmentManager;

import com.jjc.qiqiharuniversity.common.view.LoadingFragment;


/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description: 加载进度条封装类
 */
public class LoadingHelper {

    private LoadingFragment loadingFragment;

    public void show(FragmentManager fragmentManager) {
        loadingFragment = new LoadingFragment();
        loadingFragment.show(fragmentManager, LoadingFragment.class.getSimpleName());
    }

    public void show(FragmentManager fragmentManager, boolean cancelable) {
        loadingFragment = new LoadingFragment(cancelable);
        loadingFragment.show(fragmentManager, LoadingFragment.class.getSimpleName());
    }

    public void dismiss() {
        if (loadingFragment == null) {
            return;
        }

        loadingFragment.dismissAllowingStateLoss();
        loadingFragment = null;
    }

    public boolean isShowing() {
        if (loadingFragment == null) {
            return false;
        }
        return loadingFragment.isAdded();
    }

}

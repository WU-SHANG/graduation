package com.jjc.qiqiharuniversity.common.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jjc.qiqiharuniversity.common.PageManager;

/**
 * Author jiajingchao
 * Created on 2021/4/13
 * Description:基础组件
 */
public class BaseComponent {

    public Fragment fragment;

    public void add(FragmentManager fragmentManager, int container) {
        PageManager.addFragment(fragment, fragmentManager, container);
    }

    public void add(Fragment parent, int container) {
        if (parent == null) {
            return;
        }
        if (parent.isAdded()) {
            add(parent.getChildFragmentManager(), container);
        }
    }

    public void remove(FragmentManager fragmentManager) {
        PageManager.removeFragment(fragment, fragmentManager);
    }

    public Fragment getFragment() {
        return fragment;
    }
}

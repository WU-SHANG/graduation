package com.jjc.qiqiharuniversity.common;


import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jjc.qiqiharuniversity.R;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description: fragment的管理类
 */
public class PageManager {

    public static void addFragment(Fragment fragment, FragmentManager fragmentManager, int container) {
        if (fragment == null || fragmentManager == null) {
            return;
        }
        fragmentManager.executePendingTransactions();
        if (fragment.isAdded()) {
            return;
        }
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.add(container, fragment);
        beginTransaction.commitAllowingStateLoss();
    }

    public static void replaceFragment(Fragment fragment, FragmentManager fragmentManager, int container) {
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        beginTransaction.replace(container, fragment);
        beginTransaction.commitAllowingStateLoss();
    }

    public static void addFragmentByBackStack(Fragment fragment, FragmentManager fragmentManager, int container) {
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.addToBackStack(null);
        beginTransaction.add(container, fragment);
        beginTransaction.commitAllowingStateLoss();
    }

    public static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(context, intent);
    }

    public static void startActivity(Context context, Intent intent) {
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void showFragment(Fragment fragmentHide, Fragment fragmentShow, FragmentManager fragmentManager, int container) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fragmentShow.isAdded()) {
            transaction.add(container, fragmentShow);
            transaction.commitAllowingStateLoss();
        } else {
            if (fragmentHide != null && fragmentHide.isAdded()) {
                transaction.hide(fragmentHide);
            }
            transaction.show(fragmentShow);
            transaction.commitAllowingStateLoss();
        }
    }

    public static void hideFragment(Fragment fragment, FragmentManager fragmentManager) {
        if (fragment != null && fragment.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    public static void removeFragment(Fragment fragment, FragmentManager fragmentManager) {
        if (fragment != null && fragment.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragment);
            transaction.commitAllowingStateLoss();
            transaction.remove(fragment);
        }
    }
}

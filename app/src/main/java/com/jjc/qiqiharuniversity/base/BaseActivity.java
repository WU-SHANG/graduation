package com.jjc.qiqiharuniversity.base;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.view.ViewCallBack;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */
public abstract class BaseActivity<T extends BasePresenter, V> extends AppCompatActivity implements ViewCallBack<V> {
    public T presenter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue));
        }
        setContentView(getLayoutId());
        presenter = initPresenter();
        initViews();
        initListener();
    }


    protected abstract int getLayoutId();


    @Override
    protected void onResume() {
        super.onResume();
        if (presenter == null)
            presenter = initPresenter();
        presenter.add((ViewCallBack) this);
    }


    protected abstract void initViews();

    protected abstract void initListener();//初始化监听事件

    protected abstract T initPresenter();//初始化Presenter


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.remove();
            presenter = null;
        }
    }


}

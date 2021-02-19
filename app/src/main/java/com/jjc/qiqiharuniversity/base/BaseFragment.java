package com.jjc.qiqiharuniversity.base;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.view.ViewCallBack;

import butterknife.ButterKnife;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */
public abstract class BaseFragment<T extends BasePresenter, V> extends Fragment implements ViewCallBack<V> {
    public T presenter;
    protected View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        view = inflater.inflate(getLayoutId(), container, false);
        presenter = initPresenter();
        initViews();
        initListener();
        return view;
    }


    protected abstract int getLayoutId();


    @Override
    public void onResume() {
        super.onResume();
        if (presenter == null)
            presenter = initPresenter();
        presenter.add((ViewCallBack) this);
    }


    protected abstract void initViews();

    protected abstract void initListener();//初始化监听事件

    protected abstract T initPresenter();//初始化Presenter


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.remove();
            presenter = null;
        }
    }


}

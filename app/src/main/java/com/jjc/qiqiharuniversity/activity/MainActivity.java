package com.jjc.qiqiharuniversity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.base.BaseActivity;
import com.jjc.qiqiharuniversity.fragment.HomeFragment;
import com.jjc.qiqiharuniversity.fragment.MineFragment;
import com.jjc.qiqiharuniversity.presenter.LoginPresenter;
import com.jjc.qiqiharuniversity.presenter.MainPresenter;
import com.jjc.qiqiharuniversity.view.ViewCallBack;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */
public class MainActivity extends BaseActivity<MainPresenter, String> implements ViewCallBack<String> {

    private RelativeLayout rl_home;
    private ImageView iv_home;
    private TextView tv_home;

    private RelativeLayout rl_discover;
    private ImageView iv_discover;
    private TextView tv_discover;

    private RelativeLayout rl_mine;
    private ImageView iv_mine;
    private TextView tv_mine;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        rl_home = findViewById(R.id.rl_home);
        iv_home = findViewById(R.id.iv_home);
        tv_home = findViewById(R.id.tv_home);
        rl_discover = findViewById(R.id.rl_discover);
        iv_discover = findViewById(R.id.iv_discover);
        tv_discover = findViewById(R.id.tv_discover);
        rl_mine = findViewById(R.id.rl_mine);
        iv_mine = findViewById(R.id.iv_mine);
        tv_mine = findViewById(R.id.tv_mine);

        replaceFragment(new HomeFragment());
    }

    @Override
    protected void initListener() {
        rl_home.setOnClickListener(v -> {
            replaceSelectStatus(0);
            replaceFragment(new HomeFragment());
        });
        rl_discover.setOnClickListener(v -> {
            replaceSelectStatus(1);
            replaceFragment(new HomeFragment());
        });
        rl_mine.setOnClickListener(v -> {
            replaceSelectStatus(2);
            replaceFragment(new MineFragment());
        });
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void refreshView(int code, String data) {

    }

    /**
     * 替换碎片
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    /**
     * 点击导航栏按钮切换图标和文字颜色
     * @param index
     */
    private void replaceSelectStatus(int index) {
        switch (index) {
            case 0:
                iv_home.setImageResource(R.drawable.ic_home_selected);
                tv_home.setTextColor(getResources().getColor(R.color.darkblue));
                iv_discover.setImageResource(R.drawable.ic_discover);
                tv_discover.setTextColor(getResources().getColor(R.color.darkgray));
                iv_mine.setImageResource(R.drawable.ic_mine);
                tv_mine.setTextColor(getResources().getColor(R.color.darkgray));
                break;
            case 1:
                iv_home.setImageResource(R.drawable.ic_home);
                tv_home.setTextColor(getResources().getColor(R.color.darkgray));
                iv_discover.setImageResource(R.drawable.ic_discover_selected);
                tv_discover.setTextColor(getResources().getColor(R.color.darkblue));
                iv_mine.setImageResource(R.drawable.ic_mine);
                tv_mine.setTextColor(getResources().getColor(R.color.darkgray));
                break;
            case 2:
                iv_home.setImageResource(R.drawable.ic_home);
                tv_home.setTextColor(getResources().getColor(R.color.darkgray));
                iv_discover.setImageResource(R.drawable.ic_discover);
                tv_discover.setTextColor(getResources().getColor(R.color.darkgray));
                iv_mine.setImageResource(R.drawable.ic_mine_selected);
                tv_mine.setTextColor(getResources().getColor(R.color.darkblue));
                break;
        }
    }

}
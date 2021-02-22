package com.jjc.qiqiharuniversity.biz.me;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.biz.login.LoginActivity;
import com.jjc.qiqiharuniversity.common.base.BaseFragment;

/**
 * Author jiajingchao
 * Created on 2021/1/4
 * Description:
 */
public class MineFragment extends BaseFragment {

    private TextView tvLogin;

    @Override
    public int getRootLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvLogin = view.findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });
    }

}
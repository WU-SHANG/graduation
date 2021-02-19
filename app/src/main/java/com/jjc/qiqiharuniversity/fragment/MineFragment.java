package com.jjc.qiqiharuniversity.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.activity.LoginActivity;
import com.jjc.qiqiharuniversity.activity.MainActivity;

/**
 * Author jiajingchao
 * Created on 2021/1/4
 * Description:
 */
public class MineFragment extends Fragment {

    private View view;

    private Button btn_toLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        initListener();
        return view;
    }

    private void initView() {
        btn_toLogin = view.findViewById(R.id.btn_toLogin);
    }

    private void initListener() {
        btn_toLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
}
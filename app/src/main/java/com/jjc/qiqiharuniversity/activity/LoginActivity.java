package com.jjc.qiqiharuniversity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.base.BaseActivity;
import com.jjc.qiqiharuniversity.presenter.LoginPresenter;
import com.jjc.qiqiharuniversity.presenter.MainPresenter;
import com.jjc.qiqiharuniversity.view.ViewCallBack;

/**
 * Author jiajingchao
 * Created on 2021/1/1
 * Description:
 */
public class LoginActivity extends BaseActivity<LoginPresenter, String> implements ViewCallBack<String> {

    private EditText et_login_account;
    private EditText et_login_password;
    private Button btn_login;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        et_login_account = findViewById(R.id.et_login_account);
        et_login_password = findViewById(R.id.et_login_password);
        btn_login = findViewById(R.id.btn_login);
    }

    @Override
    protected void initListener() {
        btn_login.setOnClickListener(v -> {
            presenter.paramMap.put("act", et_login_account.getText().toString());
            presenter.paramMap.put("pwd", et_login_password.getText().toString());
            presenter.getData();
        });
    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void refreshView(int code, String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        if (code == 1) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            et_login_account.setText("");
            et_login_password.setText("");
        }
    }
}

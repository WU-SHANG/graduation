package com.jjc.qiqiharuniversity.biz.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;

/**
 * Author jiajingchao
 * Created on 2021/1/1
 * Description:
 */
public class LoginActivity extends BaseActivity {

    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    public int getRootLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        etAccount = findViewById(R.id.et_login_account);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(btnLogin.getId())) {
                return;
            }
            LoginController.processLogin(this, etAccount.getText().toString(), etPassword.getText().toString());
        });
    }
}

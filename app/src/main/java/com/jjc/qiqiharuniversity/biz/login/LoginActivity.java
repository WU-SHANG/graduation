package com.jjc.qiqiharuniversity.biz.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Author jiajingchao
 * Created on 2021/1/1
 * Description:
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvForget;

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

            handleLogin();
        });

        tvRegister = findViewById(R.id.tv_login_register);
        tvRegister.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(tvRegister.getId())) {
                return;
            }
            RegisterActivity.start(this, RegisterActivity.class);
        });
        tvForget = findViewById(R.id.tv_login_forget);
        tvForget.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(tvForget.getId())) {
                return;
            }
            // todo send code and check then modify password
            ToastManager.show(this, "功能暂未开发，请联系开发者找回密码...");
        });
    }

    private void handleLogin() {
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            ToastManager.show(this, "账号或密码不能为空");
            return;
        }
        // todo 判断当前有没有用户登陆 case 顶号 多处登陆

        // 账号密码登陆
        BmobUser.loginByAccount(account, password, new LogInListener<UserModel>() {
            @Override
            public void done(UserModel userModel, BmobException e) {
                if (e == null) {
                    ToastManager.show(LoginActivity.this, "登陆成功：" + userModel.getNickname());
                    LoginController.login(userModel);
                    finish();
                } else {
                    if (e.getErrorCode() == 101) {
                        ToastManager.show(LoginActivity.this, "账号或密码不正确");
                    } else {
                        ToastManager.show(LoginActivity.this, "登陆失败：" + e.getErrorCode());
                    }
                    LogHelper.i(TAG, e.getMessage());
                }
            }
        });
    }
}

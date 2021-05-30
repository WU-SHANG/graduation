package com.jjc.qiqiharuniversity.biz.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.ImageManager;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Author jiajingchao
 * Created on 2021/1/1
 * Description:
 */
public class RegisterActivity extends BaseActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int COUNTDOWN_DURATION = 60; //验证码间隔时间60s

    private EditText etAccount, etPassword, etPasswordConfirm;
    private Button btnRegister;
    private String account, password;

    @Override
    public int getRootLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setTransparent();
        titleBarView.setLeftButtonWhite();
        ImageView ivBg = findViewById(R.id.iv_register_bg);
        ImageManager.loadBlur(this, R.drawable.register_background, ivBg);
        etAccount = findViewById(R.id.et_register_account);
        etPassword = findViewById(R.id.et_register_password);
        etPasswordConfirm = findViewById(R.id.et_register_password_confirm);

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(btnRegister.getId())) {
                return;
            }
            verifyInput();
        });
    }

    private void verifyInput() {
        account = etAccount.getText().toString();
        password = etPassword.getText().toString();
        String passwordConfirm = etPasswordConfirm.getText().toString();
        if (TextUtils.isEmpty(account)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(passwordConfirm)) {
            ToastManager.show(this, "内容不能为空");
            return;
        }
        if (!password.equals(passwordConfirm)) {
            ToastManager.show(this, "两次密码输入不一致");
            return;
        }
        if (password.length() < 4) {
            ToastManager.show(this, "密码由4-10位数字组成");
            return;
        }
        handleRegister();
    }

    private void handleRegister() {
        UserModel user = BmobUser.getCurrentUser(UserModel.class);
        user.setUsername(account);
        user.setPassword(password);
        // 注册
        user.signUp(new SaveListener<UserModel>() {
            @Override
            public void done(UserModel model, BmobException e) {
                if (e == null) {
                    ToastManager.show(RegisterActivity.this, "注册成功");
                    finish();
                } else {
                    if (e.getErrorCode() == 108) {
                        ToastManager.show(RegisterActivity.this, "用户名或密码不能为空");
                    } else if (e.getErrorCode() == 202) {
                        ToastManager.show(RegisterActivity.this, "此学号已经注册过");
                    } else if (e.getErrorCode() == 209) {
                        ToastManager.show(RegisterActivity.this, "手机号已存在");
                    } else {
                        ToastManager.show(RegisterActivity.this, "注册失败：" + e.getErrorCode() + "\n" + e.getMessage());
                    }
                    LogHelper.i(TAG, e.getMessage());
                }
            }
        });
    }
}

package com.jjc.qiqiharuniversity.biz.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.biz.login.LoginController;
import com.jjc.qiqiharuniversity.common.AlertManager;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;


/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:系统设置页
 */
public class SettingActivity extends BaseActivity {

    private TextView tvLogout;

    @Override
    public int getRootLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        tvLogout = findViewById(R.id.tv_logout);
        tvLogout.setVisibility(LoginController.isLogin() ? View.VISIBLE : View.GONE);
        tvLogout.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(tvLogout.getId())) {
                return;
            }
            AlertManager.show(getSupportFragmentManager(), "确认退出登录", "退出登录后个人信息数据将被清空",
                    "取消", "确认", new AlertManager.AlertListener() {
                        @Override
                        public void onPositive() {
                            LoginController.logout();
                        }

                        @Override
                        public void onNegative() {

                        }
                    });
        });
    }
}

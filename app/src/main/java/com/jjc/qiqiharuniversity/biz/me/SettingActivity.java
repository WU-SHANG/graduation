package com.jjc.qiqiharuniversity.biz.me;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.biz.login.LoginController;
import com.jjc.qiqiharuniversity.biz.me.model.Version;
import com.jjc.qiqiharuniversity.common.AlertManager;
import com.jjc.qiqiharuniversity.common.AppManager;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.ObjectHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;
import com.jjc.qiqiharuniversity.common.util.DisplayUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:系统设置页
 */
public class SettingActivity extends BaseActivity {

    private static final String TAG = SettingActivity.class.getSimpleName();

    private TextView tvLogout, tvAppUpdate;
    private RelativeLayout rlAppUpdate, rlPrivacyPolicy;//隐私政策

    @Override
    public int getRootLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        initTitleBar();
        titleBarView.setCenterText("系统设置");
        tvLogout = findViewById(R.id.tv_logout);
        tvLogout.setVisibility(LoginController.isLogin() ? View.VISIBLE : View.GONE);
        tvLogout.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(tvLogout.getId())) {
                return;
            }
            AlertManager.show(getSupportFragmentManager(), "确认退出登录",
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

        rlAppUpdate = findViewById(R.id.rl_app_update);
        rlAppUpdate.setOnClickListener(v -> {
            if (DisplayUtils.isFastDoubleClickNew(rlAppUpdate.getId())) {
                return;
            }
            getVersionList();
        });
        tvAppUpdate = findViewById(R.id.tv_app_update);
        tvAppUpdate.setText(String.format("V%s", AppManager.getVersionName(this)));

        rlPrivacyPolicy = findViewById(R.id.rl_privacy_policy);
        rlPrivacyPolicy.setOnClickListener(v -> {
            PrivacyPolicyActivity.start(this, PrivacyPolicyActivity.class);
        });
    }

    private void getVersionList() {
        BmobQuery<Version> query = new BmobQuery<>();
        query.order("-versionCode");
        query.findObjects(new FindListener<Version>() {
            @Override
            public void done(List<Version> list, BmobException e) {
                if (e == null) {
                    if (ObjectHelper.isIllegal(list)
                            || AppManager.getVersionCode(SettingActivity.this).equals(String.valueOf(list.get(0).getVersionCode()))) {
                        ToastManager.show(SettingActivity.this, "已是最新版本");
                        return;
                    }
                    Version version = list.get(0);
                    // 内容能换行
                    String updateContent = version.getUpdateContent().replace("\\n", "\n");
                    AlertManager.show(getSupportFragmentManager(),
                            "发现新版本" + version.getVersionName(),
                            updateContent, "忽略", "下载", new AlertManager.AlertListener() {
                                @Override
                                public void onPositive() {
                                    // todo
                                }

                                @Override
                                public void onNegative() {

                                }
                            });
                } else {
                    ToastManager.show(SettingActivity.this, "获取失败：" + e.getErrorCode());
                    LogHelper.i(TAG, e.getMessage());
                }
            }
        });
    }
}

package com.jjc.qiqiharuniversity.biz.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;


import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.biz.main.MainActivity;
import com.jjc.qiqiharuniversity.common.DeviceManager;
import com.jjc.qiqiharuniversity.common.PermissionHelper;
import com.jjc.qiqiharuniversity.common.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sunhuaxiao on 2019/3/22.
 */
public class SplashActivity extends BaseActivity {

    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE };

    private PermissionHelper.PermissionsListener permissionsListener;

    @Override
    public int getRootLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBarMode(true, getResources().getColor(R.color.dark_blue));
        if (DeviceManager.getSDKint() < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (PermissionHelper.isGranted(this, PERMISSIONS)) {
            doPermissionCheckSuccess();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 0);
            permissionsListener = new PermissionHelper.PermissionsListener() {
                @Override
                public void onGranted() {
                    doPermissionCheckSuccess();
                }

                @Override
                public void onDenied() {
                    if (PermissionHelper.isGranted(SplashActivity.this, PERMISSIONS)) {
                        doPermissionCheckSuccess();
                    } else {
                        String msg = "检测到您未授权相关权限，部分功能将无法使用";
                        PermissionHelper.showPermissionsDeniedAlert(SplashActivity.this, msg);
                    }
                }
            };
        }
    }

    private void doPermissionCheckSuccess() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsListener == null) {
            return;
        }

        if (PermissionHelper.isGranted(grantResults)) {
            permissionsListener.onGranted();
        } else {
            permissionsListener.onDenied();
        }
    }

    @Override
    public void onBackPressed() {
        // Empty
    }
}

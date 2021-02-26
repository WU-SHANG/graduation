package com.jjc.qiqiharuniversity.common;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:权限
 */
public class PermissionHelper {

    public interface PermissionsListener {
        void onGranted();

        void onDenied();
    }

    public static boolean isGranted(@NonNull int[] grantResults) {
        boolean granted = true;

        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    granted = false;
                    break;
                }
            }
        }

        return granted;
    }

    public static boolean isGranted(@NonNull Context context, @NonNull String[] permissions) {
        boolean granted = true;

        if (permissions.length > 0) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission) == PackageManager.PERMISSION_DENIED) {
                    granted = false;
                    break;
                }
            }
        }

        return granted;
    }

    public static void showPermissionsDeniedAlert(final AppCompatActivity activity, String msg) {
        if (activity == null) {
            return;
        }

        AlertManager.show(activity.getSupportFragmentManager(), msg, new AlertManager.AlertListener() {
            @Override
            public void onPositive() {
                AppManager.toAppSetting(activity);
            }

            @Override
            public void onNegative() {

            }
        });
    }

}

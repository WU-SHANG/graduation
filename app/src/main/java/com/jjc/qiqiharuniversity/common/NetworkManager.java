package com.jjc.qiqiharuniversity.common;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Author jiajingchao
 * Created on 2021/4/16
 * Description: 网络管理类
 */
public class NetworkManager {

    /**
     * wifi检测
     *
     * @param context Context
     * @return 默认返回true
     */
    public static boolean wifiConnected(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) {
                return true;
            }

            NetworkInfo wifiNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiNetworkInfo.isConnected();
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean isWifiConnect(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi != null) {
                return mWifi.isConnected();
            }
        }
        return false;
    }

    public static void skip2WiFiSetting(Context context) {
        if (context == null) {
            return;
        }

        Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(i);
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        final NetworkInfo network = cm.getActiveNetworkInfo();
        if (network != null) {
            return network.isConnected() || network.isAvailable();
        } else {
            return false;
        }
    }


}

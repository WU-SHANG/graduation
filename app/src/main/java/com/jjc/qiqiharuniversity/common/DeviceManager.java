package com.jjc.qiqiharuniversity.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;



import com.jjc.qiqiharuniversity.common.util.MD5Utils;

import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Author jiajingchao
 * Created on 2021/2/26
 * Description:设备管理类
 */
public class DeviceManager {

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static int getSDKint() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isHuawei() {
        String manufacturer = Build.MANUFACTURER;
        return "huawei".equalsIgnoreCase(manufacturer);
    }

    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getAndroidIdByMd5(Context context) {
        return MD5Utils.getMd5Value(getAndroidId(context));
    }

    @SuppressLint("HardwareIds")
    public static String getMac(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            @SuppressLint("WifiManagerPotentialLeak") WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo.getMacAddress();
            }
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            return getMacAddressByInterface();
        }

        return "";
    }

    public static String getMacByMd5(Context context) {
        try {
            String mac = getMac(context);
            if (TextUtils.isEmpty(mac)) {
                return "";
            }

            mac = mac.replace(":", "");
            return MD5Utils.getMd5Value(mac.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String getMacAddressByInterface() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface ni = en.nextElement();
                if (ni.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes == null) {
                        return null;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02X:", b));
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

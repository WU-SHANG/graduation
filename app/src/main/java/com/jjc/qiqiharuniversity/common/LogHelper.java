package com.jjc.qiqiharuniversity.common;

import android.util.Log;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description: 日志封装类
 */
public class LogHelper {

    private static final String TAG = LogHelper.class.getSimpleName();

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (AppManager.isDebug()) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (AppManager.isDebug()) {
            Log.e(tag, msg);
        }
    }

}

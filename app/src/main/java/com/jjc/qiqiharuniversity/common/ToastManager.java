package com.jjc.qiqiharuniversity.common;

import android.content.Context;
import android.widget.Toast;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description: Toast封装类
 */
public class ToastManager {

    public static void show(Context context, String text) {
        // 采用此方式，用于解决小米机型，toast自带应用名的问题
        Toast mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        mToast.setText(text);
        mToast.show();
    }

    public static void showLengthLong(Context context, String text) {
        // 采用此方式，用于解决小米机型，toast自带应用名的问题
        Toast mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        mToast.setText(text);
        mToast.show();
    }
}

package com.jjc.qiqiharuniversity.common.util;

import android.content.Context;

import com.jjc.qiqiharuniversity.common.LogHelper;

/**
 * Author jiajingchao
 * Created on 2021/2/19
 * Description:
 */
public class DisplayUtils {

    private static long lastClickTime;
    private static long delayTime = 1000;

    private static int lastViewId;
    private static long lastClickTimeNew;

    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5);
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5);
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < delayTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClickNew(int viewId) {
        boolean isfast = viewId == lastViewId;
        LogHelper.i("DisplayUtils", "DisplayUtils+" + viewId + "lastViewId" + lastViewId + "viewId == lastViewId:" + isfast);
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTimeNew;

        if (viewId == lastViewId) {
            if (0 < timeD && timeD < delayTime) {
                LogHelper.i("DisplayUtils", "DisplayUtils+" + viewId + "   lastViewId" + lastViewId + "   viewId == lastViewId:");
                return true;
            }
        }
        lastViewId = viewId;
        lastClickTimeNew = time;
        return false;
    }

}

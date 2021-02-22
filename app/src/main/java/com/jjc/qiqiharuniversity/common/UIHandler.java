package com.jjc.qiqiharuniversity.common;

import android.os.Handler;
import android.os.Looper;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public class UIHandler {
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public UIHandler() {
    }

    public static Handler getHandler() {
        return sHandler;
    }

    public static void postDelayed(long delay, Runnable runnable) {
        sHandler.postDelayed(runnable, delay);
    }

    public static void post(Runnable run) {
        sHandler.post(run);
    }

    public static void removeCallbacks(Runnable r) {
        sHandler.removeCallbacks(r);
    }
}

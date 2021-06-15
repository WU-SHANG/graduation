package com.jjc.qiqiharuniversity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;


import com.jjc.qiqiharuniversity.common.AppManager;
import com.jjc.qiqiharuniversity.common.BizSPConstants;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.util.MD5Utils;
import com.jjc.qiqiharuniversity.common.SPManager;

import cn.bmob.v3.Bmob;


/**
 * Author jiajingchao
 * Created on 2021/2/22
 * Description:全局上下文
 */
public class BizApplication extends Application {

    private static final String TAG = BizApplication.class.getSimpleName();

    private static BizApplication mInstance;

    public static BizApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppManager.syncIsDebug(this);

        LogHelper.i(TAG, "onCreate pid " + android.os.Process.myPid());

//        WechatManager.getInstance().init(this);

        // 初始化Bmob云后端
        Bmob.initialize(this, "1f4347e7e8d62bf89dbbf3531db591d0");
        // 解决项目方法数大于64K的bug
//        MultiDex.install(this);

        String sessionIdForH5 = MD5Utils.getMd5Value(String.valueOf(System.currentTimeMillis()));
        SPManager.getInstance().putString(this, BizSPConstants.KEY_APP_LAUNCH_SESSION, sessionIdForH5);//每次启动app都保存一下时间戳
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }
}

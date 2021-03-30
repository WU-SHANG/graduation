package com.jjc.qiqiharuniversity.biz.login;


import android.app.Activity;
import android.text.TextUtils;

import com.jjc.qiqiharuniversity.BizApplication;
import com.jjc.qiqiharuniversity.biz.main.MainActivity;
import com.jjc.qiqiharuniversity.common.BizSPConstants;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.FileManager;
import com.jjc.qiqiharuniversity.common.LoadingHelper;
import com.jjc.qiqiharuniversity.common.SPManager;
import com.jjc.qiqiharuniversity.common.util.MD5Utils;
import com.jjc.qiqiharuniversity.common.ToastManager;

import java.io.File;


/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */
public class LoginController {

    public static void login(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        setUserId(userId);
        setUserNickname(true);
        EventBusManager.postSticky(new EventBusEvents.LoginSuccessEvent());
    }

    public static void logout() {
        setUserId(null);
        setUserNickname(false);
        FileManager.deleteFile(new File(LoginController.getAvatarLocalPath()));
        EventBusManager.postSticky(new EventBusEvents.LogoutEvent());
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(getUserId());
    }

    public static String getUserId() {
        return SPManager.getInstance().getString(BizApplication.getInstance(), BizSPConstants.KEY_USER_ID, null);
    }

    public static void setUserId(String userId) {
        SPManager.getInstance().putString(BizApplication.getInstance(), BizSPConstants.KEY_USER_ID, userId);
    }

    public static void setUserNickname(boolean isCreate) {
        SPManager.getInstance().putString(BizApplication.getInstance(), BizSPConstants.KEY_USER_NICKNAME, isCreate? "用户_" + MD5Utils.getCharAndNumStr(8) : "");
    }

    public static String getUserNickname() {
        return SPManager.getInstance().getString(BizApplication.getInstance(), BizSPConstants.KEY_USER_NICKNAME, null);
    }

    public static String getAvatarLocalPath() {
        return FileManager.getExternalFilesDir(BizApplication.getInstance(), "avatar") + File.separator + "local.jpg";
    }

    static LoadingHelper loadingHelper;

    /**
     * 处理登录请求
     */
    public static void processLogin(final Activity activity, String act, String pwd) {
        if (loadingHelper == null) {
            loadingHelper = new LoadingHelper();
        }
        if (isLegal(act, pwd)) {
            // 开启子线程访问服务器接口查询用户
            if ("2021".equals(act) && "123".equals(pwd)) {
                if (loadingHelper != null) {
                    loadingHelper.dismiss();
                    loadingHelper = null;
                }
                ToastManager.show(activity, "登录成功");
                LoginController.login(act);
                MainActivity.start(activity, MainActivity.class);
                activity.finish();
            } else {
                if (loadingHelper != null) {
                    loadingHelper.dismiss();
                    loadingHelper = null;
                }
                ToastManager.show(activity, "账号或密码错误");
            }
        } else {
            if (loadingHelper != null) {
                loadingHelper.dismiss();
                loadingHelper = null;
            }
            ToastManager.show(activity, "账号或密码格式不正确");
        }
    }

    /**
     * 校验是否合法
     * @param act
     * @param pwd
     * @return
     */
    private static boolean isLegal(String act, String pwd) {
        if (act == null || pwd == null) {
            return false;
        }
        return act.length() > 0 && pwd.length() > 0;
    }
}

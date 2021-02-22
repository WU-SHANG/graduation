package com.jjc.qiqiharuniversity.biz.login;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.jjc.qiqiharuniversity.BizApplication;
import com.jjc.qiqiharuniversity.biz.main.MainActivity;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.LoadingHelper;
import com.jjc.qiqiharuniversity.common.util.MD5Utils;
import com.jjc.qiqiharuniversity.common.ObjectHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.http.BizRequest;


/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */
public class LoginController {

    private static final String SP_NAME = "login";

    public static void login(LoginModel response) {
        LoginController.save(response);
        BizRequest.getInstance().refresh();
        EventBusManager.postSticky(new EventBusEvents.LoginSuccessEvent());
    }

    public static String getLoginModel() {
        return getSPF().getString(LoginConstants.LOGINMODEL, "");
    }

    public static void logout(Activity activity) {
        LoginController.clear();
        BizRequest.getInstance().refresh();
        LoginActivity.start(activity, LoginActivity.class);
    }

    public static void save(LoginModel response) {
        if (ObjectHelper.isIllegal(response)) {
            return;
        }

        SharedPreferences.Editor editor = getEditor();
        editor.putString(LoginConstants.TOKEN, response.token);
        editor.putString(LoginConstants.USERNAME, response.userName);
        editor.putString(LoginConstants.AVATAR, response.avatarUrl);
        editor.putInt(LoginConstants.GENDER, response.gender);
        editor.putString(LoginConstants.USERID, MD5Utils.getMd5Value(String.valueOf(response.userId)));//userid保存成md5
        editor.apply();
    }

    public static void clear() {
        SharedPreferences.Editor editor = getEditor();
        editor.clear();
        editor.apply();
    }

    private static SharedPreferences.Editor getEditor() {
        SharedPreferences sharedPreferences = getSPF();
        return sharedPreferences.edit();
    }

    private static SharedPreferences getSPF() {
        return BizApplication.getInstance().getSharedPreferences(
                SP_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isLogin() {
        return !ObjectHelper.isIllegal(getToken());
    }

    //debug时直接用token登录
    public static String getToken() {
        return getSPF().getString(LoginConstants.TOKEN, "");
    }

    public static void setToken(String token) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(LoginConstants.TOKEN, token);
        editor.apply();
    }

    public static String getUserId() {
        return getSPF().getString(LoginConstants.USERID, "");
    }

    public static String getUserName() {
        return getSPF().getString(LoginConstants.USERNAME, "");
    }

    public static String getAvatar() {
        return getSPF().getString(LoginConstants.AVATAR, "");
    }

    /**
     * @return 1：男  2：女   0：未知
     */
    public static int getGender() {
        return getSPF().getInt(LoginConstants.GENDER, 2);
    }

    public static void setAvatar(String avatar) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(LoginConstants.AVATAR, avatar);
        editor.apply();
    }

    public static void setGender(int gender) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(LoginConstants.GENDER, gender);
        editor.apply();
    }

    static LoadingHelper loadingHelper;

    /**
     * 处理登录请求
     * @return
     */
    public static void processLogin(final Activity activity, String act, String pwd) {
        if (loadingHelper == null) {
            loadingHelper = new LoadingHelper();
        }
        if (isLegal(act, pwd)) {
            // 开启子线程访问服务器接口查询用户
            if ("2017021064".equals(act) && "19991020".equals(pwd)) {
                if (loadingHelper != null) {
                    loadingHelper.dismiss();
                    loadingHelper = null;
                }
                ToastManager.show(activity, "登录成功");
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
        if (act.length() != 10 || pwd.length() != 8) {
            return false;
        }
        return (act.charAt(0) == '1' || act.charAt(0) == '2') && (pwd.charAt(0) == '1' || pwd.charAt(0) == '2');
    }
}

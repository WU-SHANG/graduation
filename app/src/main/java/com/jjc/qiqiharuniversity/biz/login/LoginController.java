package com.jjc.qiqiharuniversity.biz.login;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.jjc.qiqiharuniversity.BizApplication;
import com.jjc.qiqiharuniversity.biz.main.MainActivity;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.FileManager;
import com.jjc.qiqiharuniversity.common.ObjectHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;

import java.io.File;

import cn.bmob.v3.BmobUser;


/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */
public class LoginController {

    private static final String SP_NAME = "login";

    public static void login(UserModel model) {
        LoginController.save(model);
        EventBusManager.postSticky(new EventBusEvents.LoginSuccessEvent());
    }

    public static void logout() {
        //清除缓存用户对象
        BmobUser.logOut();
        LoginController.clear();
        FileManager.deleteFile(new File(LoginController.getAvatarLocalPath()));
        EventBusManager.postSticky(new EventBusEvents.LogoutEvent());
    }

    public static void clear() {
        SharedPreferences.Editor editor = getEditor();
        editor.clear();
        editor.apply();
    }

    public static SharedPreferences.Editor getEditor() {
        SharedPreferences sharedPreferences = getSPF();
        return sharedPreferences.edit();
    }

    public static SharedPreferences getSPF() {
        return BizApplication.getInstance().getSharedPreferences(
                SP_NAME, Context.MODE_PRIVATE);
    }

    private static void save(UserModel model) {
        if (ObjectHelper.isIllegal(model)) {
            return;
        }
        SharedPreferences.Editor editor = getEditor();
        editor.putString(LoginConstants.OBJECT_ID, model.getObjectId());
        editor.putString(LoginConstants.USERNAME, model.getUsername());
        editor.putString(LoginConstants.NICKNAME, model.getNickname());
        editor.putString(LoginConstants.GENDER, model.getGender());
        editor.putString(LoginConstants.MOBILE_PHONE, model.getMobilePhoneNumber());
        editor.putString(LoginConstants.DEPT, model.getDept());
        editor.putString(LoginConstants.INTRODUCE, model.getIntroduce());
        editor.apply();
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(getObjectId()) || !ObjectHelper.isIllegal(BmobUser.getCurrentUser(UserModel.class));
    }

    public static String getObjectId() {
        return getSPF().getString(LoginConstants.OBJECT_ID, null);
    }

    public static void setUserNickname(String nickname) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(LoginConstants.NICKNAME, nickname);
        editor.apply();
    }

    public static String getUserNickname() {
        return getSPF().getString(LoginConstants.NICKNAME, null);
    }

    public static String getAvatarLocalPath() {
        return FileManager.getExternalFilesDir(BizApplication.getInstance(), "avatar") + File.separator + "local.jpg";
    }

    public static void setUserGender(String gender) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(LoginConstants.GENDER, gender);
        editor.apply();
    }

    public static String getUserGender() {
        return getSPF().getString(LoginConstants.GENDER, null);
    }

    public static void setUserDept(String dept) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(LoginConstants.DEPT, dept);
        editor.apply();
    }

    public static String getUserDept() {
        return getSPF().getString(LoginConstants.DEPT, null);
    }

    public static void setUserIntroduce(String introduce) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(LoginConstants.INTRODUCE, introduce);
        editor.apply();
    }

    public static String getUserIntroduce() {
        return getSPF().getString(LoginConstants.INTRODUCE, null);
    }

    public static void setUserMobilePhone(String phone) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(LoginConstants.MOBILE_PHONE, phone);
        editor.apply();
    }

    public static String getUserMobilePhone() {
        return getSPF().getString(LoginConstants.MOBILE_PHONE, null);
    }

    /**
     * 单机模拟处理登录请求
     */
    public static void processLogin(final Activity activity, String act, String pwd) {
        // 开启子线程访问服务器接口查询用户
        if ("2021".equals(act) && "1234".equals(pwd)) {
            ToastManager.show(activity, "登录成功");
            MainActivity.start(activity, MainActivity.class);
            activity.finish();
        } else {
            ToastManager.show(activity, "账号或密码错误");
        }
    }
}

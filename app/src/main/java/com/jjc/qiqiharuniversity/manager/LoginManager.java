package com.jjc.qiqiharuniversity.manager;


import com.jjc.qiqiharuniversity.base.BaseBeanManager;
import com.jjc.qiqiharuniversity.view.ViewCallBack;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */
public class LoginManager extends BaseBeanManager {

    private int code;

    public LoginManager(ViewCallBack modelCallBack) {
        super(modelCallBack);
    }

    public void getData() {
        String act = mParamMap.get("act");
        String pwd = mParamMap.get("pwd");
        String data = processLogin(act, pwd);
        if ("".equals(data) || data == null) {
            data = "服务器错误";
            code = 2;
        }
        mViewCallBack.refreshView(code, data);
    }

    /**
     * 处理登录请求
     * @return
     */
    private String processLogin(String act, String pwd) {
        String data = "";
        code = 2;
        if (isLegal(act, pwd)) {
            //开启子线程访问服务器接口查询用户
            if ("2017021064".equals(act) && "19991020".equals(pwd)) {
                data = "登录成功";
                code = 1;
            } else {
                data = "账号或密码错误";
            }
        } else {
            data = "账号或密码格式不正确";
        }
        return data;
    }

    /**
     * 校验是否合法
     * @param act
     * @param pwd
     * @return
     */
    private boolean isLegal(String act, String pwd) {
        if (act.length() != 10 || pwd.length() != 8) {
            return false;
        } else if (act.charAt(0) != '1' && act.charAt(0) != '2' || pwd.charAt(0) != '1' && pwd.charAt(0) != '2') {
            return false;
        }
        return true;
    }
}

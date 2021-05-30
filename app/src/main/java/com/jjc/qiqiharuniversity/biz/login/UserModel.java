package com.jjc.qiqiharuniversity.biz.login;


import cn.bmob.v3.BmobUser;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:Bmob云后端对应_User用户表
 * ps:数据类型为封装类
 */
public class UserModel extends BmobUser {
    private String nickname;//用户名
    private String gender;
    private String avatarUrl;
    private String dept;//学院
    private String introduce;//简介
    private Integer userState;//用户状态 0正常 1注销 (vip，冻结等扩展)

    public String getNickname() {
        return nickname;
    }

    public UserModel setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public UserModel setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserModel setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getDept() {
        return dept;
    }

    public UserModel setDept(String dept) {
        this.dept = dept;
        return this;
    }

    public String getIntroduce() {
        return introduce;
    }

    public UserModel setIntroduce(String introduce) {
        this.introduce = introduce;
        return this;
    }

    public Integer getUserState() {
        return userState;
    }

    public UserModel setUserState(Integer userState) {
        this.userState = userState;
        return this;
    }
}

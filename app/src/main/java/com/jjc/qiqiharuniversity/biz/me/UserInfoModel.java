package com.jjc.qiqiharuniversity.biz.me;

import io.reactivex.annotations.NonNull;

/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:用户个人信息模型
 */
public class UserInfoModel {

    public String userId;
    public String avatarUrl;
    public String nickname;
    public String gender;// 1男 2女 0未知
    public String college;
    public String introduction;

    @NonNull
    @Override
    public String toString() {
        return "UserInfoModel{" +
                "userId='" + userId + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender=" + gender +
                ", college='" + college + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}

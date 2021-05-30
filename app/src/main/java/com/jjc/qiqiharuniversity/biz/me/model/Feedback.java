package com.jjc.qiqiharuniversity.biz.me.model;

import cn.bmob.v3.BmobObject;

/**
 * Author jiajingchao
 * Created on 2021/5/25
 * Description:用户反馈model
 */
public class Feedback extends BmobObject {

    private String type;
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.jjc.qiqiharuniversity.biz.me.model;

import cn.bmob.v3.BmobObject;

/**
 * Author jiajingchao
 * Created on 2021/5/25
 * Description:版本控制model
 */
public class Version extends BmobObject {

    private String versionName;
    private Integer versionCode;
    private String updateContent;
    private String downloadUrl;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}

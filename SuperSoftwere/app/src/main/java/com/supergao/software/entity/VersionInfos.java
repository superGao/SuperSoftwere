package com.supergao.software.entity;

import com.avos.avoscloud.AVObject;

import java.io.Serializable;

/**
 * 版本信息
 * Created by YangJie on 2015/11/12.
 */
public class VersionInfos extends AVObject implements Serializable{
    private String versionCode;//版本号
    private String url;//版本地址
    private String appName;//Apk名称

    VersionInfos(String versionCode, String url, String appName) {
        this.versionCode = versionCode ;
        this.url = url ;
        this.appName = appName ;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}

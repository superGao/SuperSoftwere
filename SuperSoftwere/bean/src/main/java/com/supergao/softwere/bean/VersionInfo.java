package com.supergao.softwere.bean;

import java.io.Serializable;

/**
 * 版本信息
 * Created by YangJie on 2015/11/12.
 */
public class VersionInfo implements Serializable{
    private String type;    //类别（1：客户端，2：）
    private String version;//版本号
    private String url;//版本地址
    private String times;//时间
    private String name;//Apk名称

    VersionInfo(String type, String version,String url,String times,String name) {
        this.type = type ;
        this.version = version ;
        this.url = url ;
        this.times = times ;
        this.name = name ;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public String getTimes() {
        return times;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public void setName(String name) {
        this.name = name;
    }
}

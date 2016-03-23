package com.supergao.software.entity;

import com.avos.avoscloud.AVUser;

/**
 * author：superGao on 2016/3/15.
 * note：借用请注明来源，侵权必究！
 */
public class UserInfo extends AVUser {

    private String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPortrait() {
        return this.getString("portrait");
    }

    public void setPortrait(String portrait) {
        this.put("portrait",portrait);
    }
}

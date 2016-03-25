package com.supergao.software.entity;

import com.avos.avoscloud.AVUser;
import com.supergao.software.bean.BaseUserInfo;
import com.supergao.software.bean.RegisterType;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * Created by YangJie on 2015/11/11.
 */
public class AppConfig {

    /**
     * 用户类型
     */
    public static RegisterType sRegisterType;

    /**
     * 当前APP登录的用户
     */
    public static BaseUserInfo sUserInfo ;

    public static UserInfo userInfo;
    /**
     * 当前登录的用户信息
     */
    public static AVUser avUser;

    public static String getUserId() {
        return sUserInfo.getId() ;
    }

    public static String getUserKey() {
        return sUserInfo.getKey();
    }

}

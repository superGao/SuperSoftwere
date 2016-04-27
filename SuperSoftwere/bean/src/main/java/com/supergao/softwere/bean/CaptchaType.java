package com.supergao.softwere.bean;

/**
 * 验证码类型
 * Created by YangJie on 2015/11/10.
 */
public enum CaptchaType {
    /**
     * 注册
     */
    REGISTER("0"),
    /**
     * 更改信息
     */
    UPDATE("1") ;

    /**
     * 值
     */
    private final String value ;

    CaptchaType(String value) {
        this.value = value ;
    }

    public String getValue() {
        return value;
    }
}

package com.supergao.software.bean;

/**
 * 注册用户角色类型
 * Created by YangJie on 2015/11/10.
 */
public enum RegisterType {
    /**
     * 物流公司注册
     */
    LOGISTICS("0", "物流公司"),
    /**
     * 司机注册
     */
    DRIVER("1", "司机") ;

    private final String value ;

    private final String name ;

    RegisterType(String value, String name) {
        this.value = value ;
        this.name = name ;
    }

    public String getName() {
        return name ;
    }

    public String getValue() {
        return value;
    }

    public static RegisterType parse(String value) {
        if ("0".equals(value)) {
            return LOGISTICS ;
        } else if ("1".equals(value)) {
            return DRIVER ;
        } else {
            return null ;
        }
    }
}

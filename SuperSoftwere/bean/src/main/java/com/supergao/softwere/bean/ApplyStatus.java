package com.supergao.softwere.bean;

/**
 * 审核状态
 * Created by YangJie on 2015/12/16.
 */
public enum ApplyStatus {
    /**
     * 未审核
     */
    UNAUDITED("0", "未审核"),
    AUDITING("1", "审核中"),
    AUDITED("2", "审核通过")
    ;

    private String value ;
    private String name ;

    ApplyStatus(String value, String name) {
        this.value = value ;
        this.name = name ;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ApplyStatus parse(String val) {
        if (UNAUDITED.getValue().equals(val)) {
            return UNAUDITED ;
        } else if(AUDITING.getValue().equals(val)) {
            return AUDITING ;
        } else if(AUDITED.getValue().equals(val)) {
            return AUDITED ;
        }

        return null ;
    }
}

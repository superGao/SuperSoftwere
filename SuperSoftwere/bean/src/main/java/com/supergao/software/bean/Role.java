package com.supergao.software.bean;

import android.text.TextUtils;

/**
 * 角色
 * Created by YangJie on 2015/11/12.
 */
public enum Role {
    /**
     * 物流公司
     */
    LOGISTICS("1"),
    /**
     * 普通员工（集货员、普通员工）
     */
    EMPLOYEE("2"),
    /**
     * 司机
     */
    DRIVER("3") ,
    /**
     * 网点管理者，接单，分配
     */
    STATION_MANAGER("4") ,
    /**
     * 未完善信息的用户
     */
    IMPERFECT_USER("8");

    /**
     * 值
     */
    private final String mValue ;

    Role(String value) {
        this.mValue = value ;
    }

    public String getValue() {
        return this.mValue ;
    }

    /**
     * 根据value值解析得到Role枚举对象
     * @param value 值
     * @return 枚举对象
     */
    public static Role parse(String value) {
        if (TextUtils.isEmpty(value)) {
            return null ;
        }
        if (value.equals(LOGISTICS.getValue())) {
            return LOGISTICS ;
        } else if (value.equals(EMPLOYEE.getValue())) {
            return EMPLOYEE ;
        } else if (value.equals(DRIVER.getValue())) {
            return DRIVER ;
        } else if (value.equals(STATION_MANAGER.getValue())) {
            return STATION_MANAGER;
        } else if (value.equals(IMPERFECT_USER.getValue())) {
            return IMPERFECT_USER ;
        }

        return null ;
    }

}

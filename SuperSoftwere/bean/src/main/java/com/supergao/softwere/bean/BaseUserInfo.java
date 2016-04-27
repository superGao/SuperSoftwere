package com.supergao.softwere.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 公共用户信息
 * Created by YangJie on 2015/11/11.
 */
public class BaseUserInfo implements Parcelable, Serializable {
    private static final long serialVersionUID = -1142994808775420304L;

    public static final String  KEY_BASEUSERINFO = "BaseUserInfo" ;

    /**
     * 用户ID
     */
    protected String id;

    /**
     * 服务器授权访问key
     */
    protected String key;

    /**
     * 电话号码
     */
    protected String mobile ;

    /**
     * 真实姓名（司机），公司名称（物流公司）
     */
    protected String name ;

    /**
     * 注册类型：0 物流公司； 1 司机，用来接收数据，并解析成 ResgisterType 对象
     */
    private String register_type ;

    /**
     * 类型
     */
    protected RegisterType registerType ;

    /**
     * 角色，用来接收服务器的role字符串，并生产RoleObj对象
     */
    private String role ;

    /**
     * 当前用户角色
     */
    protected Role roleObj ;

    /**
     * 头像信息（司机）
     */
    protected String avatar ;

    /**
     * 物流公司名称,服务器返回数据：-> name
     */
    protected String lname ;

    /**
     * 物流公司logo
     */
    protected String logo ;

    /**
     * 审核状态，0：未审核；1：审核中；2：审核通过，服务器返回数据： -> applyStatus
     */
    private String apply ;

    /**
     * 审核状态
     */
    private ApplyStatus applyStatus ;
    /**
     * 是否有未读消息
     * 1：有  0：没有
     */
    private String unread;

    public void attach() {
        registerType = RegisterType.parse(register_type) ;
        roleObj = Role.parse(role) ;

        if (roleObj == Role.LOGISTICS) {
            avatar = logo ;
            name = lname ;
        }

        if (roleObj == Role.LOGISTICS) { // 物流公司审核状态
            applyStatus = ApplyStatus.parse(apply);
        } else if (roleObj == Role.DRIVER) { // 司机审核状态
            if ("0".equals(apply)) { // 审核通过
                applyStatus = ApplyStatus.AUDITED ;
            } else { // 审核未通过
                applyStatus = ApplyStatus.UNAUDITED ;
            }
        }
    }


    public String getRegister_type() {
        return register_type;
    }

    public void setRegister_type(String register_type) {
        this.register_type = register_type;
        setRegisterType(RegisterType.parse(register_type));
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public RegisterType getRegisterType() {
        return registerType;
    }

    public void setRegisterType(RegisterType registerType) {
        this.registerType = registerType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String roleValue) {
        this.role = roleValue;
        setRoleObj(Role.parse(roleValue));
    }

    public Role getRoleObj() {
        return roleObj;
    }

    public void setRoleObj(Role roleObj) {
        this.roleObj = roleObj;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ApplyStatus getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(ApplyStatus applyStatus) {
        this.applyStatus = applyStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.mobile);
        dest.writeString(this.name);
        dest.writeInt(this.registerType == null ? -1 : this.registerType.ordinal());
        dest.writeInt(this.roleObj == null ? -1 : this.roleObj.ordinal());
        dest.writeString(this.avatar);
        dest.writeString(this.unread);
    }

    public BaseUserInfo() {
    }

    protected BaseUserInfo(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.mobile = in.readString();
        this.name = in.readString();
        int tmpRegisterType = in.readInt();
        this.registerType = tmpRegisterType == -1 ? null : RegisterType.values()[tmpRegisterType];
        int tmpRoleObj = in.readInt();
        this.roleObj = tmpRoleObj == -1 ? null : Role.values()[tmpRoleObj];
        this.avatar = in.readString();
        this.unread=in.readString();
    }

    public static final Creator<BaseUserInfo> CREATOR = new Creator<BaseUserInfo>() {
        public BaseUserInfo createFromParcel(Parcel source) {
            return new BaseUserInfo(source);
        }

        public BaseUserInfo[] newArray(int size) {
            return new BaseUserInfo[size];
        }
    };
}

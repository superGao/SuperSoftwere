package com.supergao.software.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 公共信息
 * Created by YangJie on 2015/11/11.
 */
public class Common implements Parcelable {

    /**
     * 拒单理由列表
     */
    public final static String KEY_REFUSAL_REASON_LIST = "refusal_reason_list" ;

    /**
     * 头像信息
     */
    private String avatar ;

    /**
     * 拒单原因id
     */
    private String reasonId ;

    /**
     * 拒单原因信息
     */
    private String reason ;

    /**
     * 估算出来的运费，服务器返回数据： -> deliverFee
     */
    private String buttom_price ;

    /**
     * 运费
     */
    private String deliverFee ;

    /**
     * 保险费用
     */
    private float oprice ;

    /**
     * 保险费用
     */
    private String insuranceFee ;

    /**
     * 地理位置上传时间间隔，单位：秒
     */
    private int interval ;

    public Common(String reasonId, String reason) {
        this.reasonId = reasonId ;
        this.reason = reason ;
    }

    public void attach() {
        this.deliverFee = buttom_price ;
        this.insuranceFee = String.valueOf(oprice) ;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDeliverFee() {
        return deliverFee;
    }

    public void setDeliverFee(String deliverFee) {
        this.deliverFee = deliverFee;
    }

    public String getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(String insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reasonId);
        dest.writeString(this.reason);
    }

    public Common() {
    }

    protected Common(Parcel in) {
        this.reasonId = in.readString();
        this.reason = in.readString();
    }

    public static final Creator<Common> CREATOR = new Creator<Common>() {
        public Common createFromParcel(Parcel source) {
            return new Common(source);
        }

        public Common[] newArray(int size) {
            return new Common[size];
        }
    };
}

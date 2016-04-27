package com.supergao.softwere.bean.logistics;

import android.os.Parcel;
import android.text.TextUtils;

import com.supergao.softwere.bean.BaseUserInfo;

/**
 * 物流公司实体
 * Created by YangJie on 2015/11/10.
 */
public class LogisticsInfo extends BaseUserInfo {

    private static final long serialVersionUID = 6509807508679046013L;

    /**
     * 所在公司网点 所在区域
     */
    private String area ;

    /**
     * 所在公司网点 所在地址
     */
    private String address ;

    /**
     * 线路id
     */
    private String line_id ;

    private String lineId ;

    private String lid ;
    private String aid ;

    /**
     * 物流公司ID
     */
    private String logistics_id ;
    private String logisticsId ;

    /**
     * 物流公司名称
     */
    private String logistics_name ;
    private String logisticsName ;

    private String logistics_area_id ;
    private String logisticsAreaId ;

    private String e_logistics_area_id ;
    private String elogisticsAreaId ;

    private String weight ;
    private String bulk ;
    private String distance ;

    private String buttom_price ;
    private String buttomPrice ;


    private String lat ;
    private String lng ;

    private String level ;

    /**
     * 服务器返回数据：-> takeTime
     */
    private String take_time ;

    private String takeTime ;

    public void attach() {
        this.logisticsId = logistics_id ;
        if (!TextUtils.isEmpty(lid)) {
            this.logisticsId = lid ;
        }

        this.logisticsName = logistics_name;

        this.logisticsAreaId = logistics_area_id ;
        if (!TextUtils.isEmpty(aid)) {
            this.logisticsAreaId = aid ;
        }

        this.elogisticsAreaId = e_logistics_area_id ;
        this.buttomPrice = buttom_price ;

        this.lineId = line_id ;
        this.takeTime = take_time ;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getLogisticsAreaId() {
        return logisticsAreaId;
    }

    public void setLogisticsAreaId(String logisticsAreaId) {
        this.logisticsAreaId = logisticsAreaId;
    }

    public String getElogisticsAreaId() {
        return elogisticsAreaId;
    }

    public void setElogisticsAreaId(String elogisticsAreaId) {
        this.elogisticsAreaId = elogisticsAreaId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBulk() {
        return bulk;
    }

    public void setBulk(String bulk) {
        this.bulk = bulk;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getButtomPrice() {
        return buttomPrice;
    }

    public void setButtomPrice(String buttomPrice) {
        this.buttomPrice = buttomPrice;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.logo);
        dest.writeString(this.area);
        dest.writeString(this.address);
        dest.writeString(this.lineId);
        dest.writeString(this.logisticsId);
        dest.writeString(this.logisticsName);
        dest.writeString(this.logisticsAreaId);
        dest.writeString(this.elogisticsAreaId);
        dest.writeString(this.weight);
        dest.writeString(this.bulk);
        dest.writeString(this.distance);
        dest.writeString(this.buttomPrice);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeString(this.lname);
        dest.writeString(this.level);
    }

    public LogisticsInfo() {
    }

    protected LogisticsInfo(Parcel in) {
        super(in);
        this.logo = in.readString();
        this.area = in.readString();
        this.address = in.readString();
        this.lineId = in.readString();
        this.logisticsId = in.readString();
        this.logisticsName = in.readString();
        this.logisticsAreaId = in.readString();
        this.elogisticsAreaId = in.readString();
        this.weight = in.readString();
        this.bulk = in.readString();
        this.distance = in.readString();
        this.buttomPrice = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
        this.lname = in.readString();
        this.level = in.readString();
    }

    public static final Creator<LogisticsInfo> CREATOR = new Creator<LogisticsInfo>() {
        public LogisticsInfo createFromParcel(Parcel source) {
            return new LogisticsInfo(source);
        }

        public LogisticsInfo[] newArray(int size) {
            return new LogisticsInfo[size];
        }
    };
}

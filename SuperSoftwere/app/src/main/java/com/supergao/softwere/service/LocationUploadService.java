package com.supergao.softwere.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import lib.support.utils.LogUtils;

/**
 *位置信息上传Service
 *@author superGao
 *creat at 2016/3/14
 */
public class LocationUploadService extends Service {

    public LocationClientOption mLocationClientOption;
    public LocationClient mLocationClient;
    private BDLocation mBDLocation ;

    public BDLocationListener mMyLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {// 接受返回的定位结果
            if (location == null) {
                return;
            }
            mBDLocation = location;
        }
    } ;

    /**
     * 上传时间间隔，单位：秒
     */
    private int mUploadInterval;

    private Handler mHandler = new Handler();



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClientOption = new LocationClientOption(); // 声明LocationClient的参数类
        mLocationClient.registerLocationListener(mMyLocationListener); // 注册监听函数
//		mGeofenceClient = new GeofenceClient(getApplicationContext()); // 低功耗地理围栏

        mLocationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); // 设置定位模式为高精度
        mLocationClientOption.setIsNeedAddress(true); // 设置返回地址信息
        mLocationClientOption.setCoorType("bd09ll"); // 设置返回坐标系类型
        mLocationClientOption.setScanSpan(1000);
        mLocationClientOption.setOpenGps(true);
        mLocationClient.setLocOption(mLocationClientOption); // 将参数设置进定位类中

        mLocationClient.start();

        LogUtils.d("->");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("->");
    }
}

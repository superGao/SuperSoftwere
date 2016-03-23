package com.supergao.software.entity;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.supergao.software.core.AppAction;
import com.supergao.software.core.impl.AppActionBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.supergao.software.entity.UserInfo;

/**
 *app 执行入口类
 *@author superGao
 *creat at 2016/3/14
 */
public class App extends Application {

    public MyLocationListener mMyLocationListener;
    //	public GeofenceClient mGeofenceClient;
    public BDLocation mBdLocation;
    public LocationClientOption mLocationClientOption;
    public LocationClient mLocationClient;

    private AppAction appAction ;


    @Override
    public void onCreate() {
        super.onCreate();

        //初始化语音
        // 将“12345678”替换成您申请的 APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与 appid 之间添加任务空字符或者转义符
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=56e8e974"+","+SpeechConstant.ENGINE_MODE + "=msc");

        //初始化leanCloud
        AVOSCloud.useAVCloudCN();
        // U need your AVOS key and so on to run the code.
        AVUser.alwaysUseSubUserClass(UserInfo.class);
        AVOSCloud.initialize(this,
                "LNGV2SXXXeO27Tn3Bk2xDBlR-gzGzoHsz",
                "0VnBqrDdNiJ6jrgXKEln3zUo");

        appAction = new AppActionBean() ;

        // 初始化ImageLoader配置
        initImageLoader(this);

        mMyLocationListener = new MyLocationListener(); // 声明监听函数类
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
    }

    /**
     * 初始化ImageLoader配置
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    public AppAction getAppAction() {
        return appAction;
    }

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {// 接受返回的定位结果
            if (location == null) {
                return;
            }
            mBdLocation = location;
        }

    }

}

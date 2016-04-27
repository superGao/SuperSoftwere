package com.supergao.softwere.entity;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.text.TextUtils;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.ConversationEventHandler;
import com.avoscloud.leanchatlib.utils.ThirdPartUserUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.supergao.softwere.core.AppAction;
import com.supergao.softwere.core.impl.AppActionBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.supergao.softwere.friends.AddRequest;
import com.supergao.softwere.model.UpdateInfo;
import com.supergao.softwere.service.PushManager;
import com.supergao.softwere.utils.LeanchatUserProvider;

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
    public static boolean debug = true;
    public static App ctx;

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        //初始化语音
        // 将“12345678”替换成您申请的 APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与 appid 之间添加任务空字符或者转义符
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=56e8e974"+","+SpeechConstant.ENGINE_MODE + "=msc");
        appAction = new AppActionBean() ;
        initAVOSCloud(ctx);
    }

    /**
     * 初始化leanCloud
     * @param ctx
     */
    private void initAVOSCloud(App ctx){
        AVOSCloud.useAVCloudCN();
        /*String appId = "x3o016bxnkpyee7e9pa5pre6efx2dadyerdlcez0wbzhw25g";
        String appKey = "057x24cfdzhffnl3dzk14jh9xo2rq6w1hy1fdzt5tv46ym78";*/
        String appId = "LNGV2SXXXeO27Tn3Bk2xDBlR-gzGzoHsz";
        String appKey = "0VnBqrDdNiJ6jrgXKEln3zUo";

        UserInfo.alwaysUseSubUserClass(UserInfo.class);
        AVOSCloud.initialize(this, appId, appKey);
        AVObject.registerSubclass(AddRequest.class);
        AVObject.registerSubclass(UpdateInfo.class);
        // 节省流量
        AVOSCloud.setLastModifyEnabled(true);
        PushManager.getInstance().init(ctx);
        AVOSCloud.setDebugLogEnabled(debug);
        AVAnalytics.enableCrashReport(this, !debug);
        initImageLoader(ctx);
        initBaiduMap();
        if (App.debug) {
            openStrictMode();
        }

        ThirdPartUserUtils.setThirdPartUserProvider(new LeanchatUserProvider());
        initChatManager();
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
     * 初始化百度SDK
     */
    private void initBaiduMap() {
        SDKInitializer.initialize(this);

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

    public void openStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                        //.penaltyDeath()
                .build());
    }

    private void initChatManager() {
        final ChatManager chatManager = ChatManager.getInstance();
        chatManager.init(this);
        String currentUserId = UserInfo.getCurrentUserId();
        if (!TextUtils.isEmpty(currentUserId)) {
            chatManager.setupManagerWithUserId(this, currentUserId);
        }
        chatManager.setConversationEventHandler(ConversationEventHandler.getInstance());
        ChatManager.setDebugEnabled(App.debug);
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

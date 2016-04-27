package com.supergao.softwere.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.SaveCallback;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.utils.LogUtils;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.supergao.circledimageview.CircledImageView;
import com.supergao.softwere.R;
import com.supergao.softwere.activity.user.LoginActivity;
import com.supergao.softwere.activity.user.UserInfoActivity;
import com.supergao.softwere.entity.App;
import com.supergao.softwere.entity.UserInfo;
import com.supergao.softwere.event.LoginFinishEvent;
import com.supergao.softwere.fragment.ConversationRecentFragment;
import com.supergao.softwere.fragment.DiscoverFragment;
import com.supergao.softwere.fragment.HomeLeftmenuFragment;
import com.supergao.softwere.fragment.HomeSoftwFragment;
import com.supergao.softwere.friends.ContactFragment;
import com.supergao.softwere.service.PreferenceMap;
import com.supergao.softwere.utils.Log;
import com.supergao.softwere.utils.SpeechUtils;
import com.supergao.softwere.utils.UserCacheUtils;
import com.supergao.softwere.utils.Utils;
import com.supergao.softwere.view.DragLayout;

import de.greenrobot.event.EventBus;

/**
 *主界面Activity
 *@author superGao
 *creat at 2016/3/14
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 侧滑栏组件
     */
    public static DragLayout mDragLayout;
    /**
     * 左侧菜单头像
     */
    public CircledImageView mLeftmenuHeaderImg;
    /**
     * 左侧菜单 用户名 控件
     */
    public TextView usernameLeftMenuTxt ;
    /**
     * 彩蛋功能Fragment对象
     */
    public HomeSoftwFragment mHomeSoftwFragment;
    /**
     * 老板功能左侧菜单Fragment对象
     */
    public Fragment mHomeBossLeftmenuFragment;
    /**
     * 联系人Fragment
     */
    public ContactFragment contactFragment;
    /**
     * 消息Fragment
     */
    public ConversationRecentFragment conversationRecentFragment;
    /**
     * 附近fragment
     */
    public DiscoverFragment discoverFragment;
    /**
     * 判断是否首次登录
     */
    private long firstTime;
    private boolean isLogin = true;
    private Bitmap bitmap;  //用户头像bitmap
    /**
     * 扫描二维码
     */
    public static final int REQUEST_CODE_SCANNIN_QRCODE = 10 ;
    /**
     * 底部标签栏
     */
    private Button conversationBtn, contactBtn, discoverBtn, mySpaceBtn;
    public static final int[] tabsNormalBackIds = new int[]{R.drawable.tabbar_chat,
            R.drawable.tabbar_contacts, R.drawable.tabbar_discover, R.drawable.tabbar_me};
    public static final int[] tabsActiveBackIds = new int[]{R.drawable.tabbar_chat_active,
            R.drawable.tabbar_contacts_active, R.drawable.tabbar_discover_active,
            R.drawable.tabbar_me_active};
    private Button[] tabs;
    private static final String FRAGMENT_TAG_CONVERSATION = "conversation";
    private static final String FRAGMENT_TAG_CONTACT = "contact";
    private static final String FRAGMENT_TAG_DISCOVER = "discover";
    private static final String FRAGMENT_TAG_PROFILE = "profile";
    private static final String[] fragmentTags = new String[]{FRAGMENT_TAG_CONVERSATION, FRAGMENT_TAG_CONTACT,
            FRAGMENT_TAG_DISCOVER, FRAGMENT_TAG_PROFILE};
    public static final int FRAGMENT_N = 4;
    public LocationClient locClient;
    public MyLocationListener locationListener;

    public static void goMainActivityFromActivity(Activity fromActivity) {
        EventBus eventBus = EventBus.getDefault();
        eventBus.post(new LoginFinishEvent());

        ChatManager chatManager = ChatManager.getInstance();
        chatManager.setupManagerWithUserId(fromActivity, UserInfo.getCurrentUserId());
        chatManager.openClient(null);
        Intent intent = new Intent(fromActivity, MainActivity.class);
        fromActivity.startActivity(intent);

        updateUserLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化View
        initSelfView();
        // 初始化侧滑栏
        initDragLayout();
        SpeechUtils.initSpeech(this);//初始化讯飞语音
        Log.setLoggable(true);//log开关
        //AVAnalytics.trackAppOpened(getIntent());
        conversationBtn.performClick();//设置初始显示的fragment
        initBaiduLocClient();
        UserCacheUtils.cacheUser(UserInfo.getCurrentUser());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 初始化显示数据
        initShowData() ;
    }

    /**
     * 初始化view
     */
    private void initSelfView() {
        // 更多
        // 初始化左侧menu view
        initLeftMenuView();
        mHomeSoftwFragment = new HomeSoftwFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_function_container, mHomeSoftwFragment)
                .commit();
        mHomeBossLeftmenuFragment = new HomeLeftmenuFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ll_fragment_leftmenu_container, mHomeBossLeftmenuFragment)
                .commit();

        conversationBtn = (Button) findViewById(R.id.btn_message);
        contactBtn = (Button) findViewById(R.id.btn_contact);
        discoverBtn = (Button) findViewById(R.id.btn_discover);
        mySpaceBtn = (Button) findViewById(R.id.btn_my_space);
        tabs = new Button[]{conversationBtn, contactBtn, discoverBtn, mySpaceBtn};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu_header: // 左侧菜单头像部分
                Intent intent = new Intent(this, UserInfoActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    /**
     * 底部选项卡点击事件
     * @param v
     */
    public void onTabSelect(View v) {
        int id = v.getId();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        setNormalBackgrounds();
        switch (id){
            case R.id.btn_message://消息
                if (conversationRecentFragment == null) {
                    conversationRecentFragment = new ConversationRecentFragment();
                    transaction.add(R.id.fragment_function_container, conversationRecentFragment, FRAGMENT_TAG_CONVERSATION);
                }
                transaction.show(conversationRecentFragment);
                break;
            case R.id.btn_contact://联系人
                if (contactFragment == null) {
                    contactFragment = new ContactFragment();
                    transaction.add(R.id.fragment_function_container, contactFragment, FRAGMENT_TAG_CONTACT);
                }
                transaction.show(contactFragment);
                break;
            case R.id.btn_discover://附近
                if (discoverFragment == null) {
                    discoverFragment = new DiscoverFragment();
                    transaction.add(R.id.fragment_function_container, discoverFragment, FRAGMENT_TAG_DISCOVER);
                }
                transaction.show(discoverFragment);
                break;
            case R.id.btn_my_space://其它
                if (mHomeSoftwFragment == null) {
                    mHomeSoftwFragment = new HomeSoftwFragment();
                    transaction.add(R.id.fragment_function_container, mHomeSoftwFragment, FRAGMENT_TAG_PROFILE);
                }
                transaction.show(mHomeSoftwFragment);
                break;
        }
        int pos;
        for (pos = 0; pos < FRAGMENT_N; pos++) {
            if (tabs[pos] == v) {
                break;
            }
        }
        transaction.commit();
        setTopDrawable(tabs[pos], tabsActiveBackIds[pos]);
    }
    private void setNormalBackgrounds() {
        for (int i = 0; i < tabs.length; i++) {
            Button v = tabs[i];
            setTopDrawable(v, tabsNormalBackIds[i]);
        }
    }
    private void setTopDrawable(Button v, int resId) {
        v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(resId), null, null);
    }
    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (int i = 0; i < fragmentTags.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags[i]);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }

    /**
     * 初始化百度LocationClient
     */
    private void initBaiduLocClient() {
        locClient = new LocationClient(this.getApplicationContext());
        locClient.setDebug(true);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setScanSpan(2000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        locClient.setLocOption(option);

        locationListener = new MyLocationListener();
        locClient.registerLocationListener(locationListener);
        locClient.start();//开始定位
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            int locType = location.getLocType();
            Log.d("myLocation:   ","onReceiveLocation latitude=" + latitude + " longitude=" + longitude
                    + " locType=" + locType + " address=" + location.getAddrStr());
            String currentUserId = UserInfo.getCurrentUserId();
            if (!TextUtils.isEmpty(currentUserId)) {
                PreferenceMap preferenceMap = new PreferenceMap(MainActivity.this, currentUserId);
                AVGeoPoint avGeoPoint = preferenceMap.getLocation();
                if (avGeoPoint != null && avGeoPoint.getLatitude() == location.getLatitude()
                        && avGeoPoint.getLongitude() == location.getLongitude()) {
                    updateUserLocation();
                    locClient.stop();
                } else {
                    AVGeoPoint newGeoPoint = new AVGeoPoint(location.getLatitude(),
                            location.getLongitude());
                    if (newGeoPoint != null) {
                        preferenceMap.setLocation(newGeoPoint);
                    }
                }
            }
        }

    }


    public static void updateUserLocation() {
        PreferenceMap preferenceMap = PreferenceMap.getCurUserPrefDao(App.ctx);
        AVGeoPoint lastLocation = preferenceMap.getLocation();
        if (lastLocation != null) {
            final UserInfo user = UserInfo.getCurrentUser();
            final AVGeoPoint location = user.getAVGeoPoint(UserInfo.LOCATION);
            if (location == null || !Utils.doubleEqual(location.getLatitude(), lastLocation.getLatitude())
                    || !Utils.doubleEqual(location.getLongitude(), lastLocation.getLongitude())) {
                user.put(UserInfo.LOCATION, lastLocation);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            LogUtils.logException(e);
                        } else {
                            AVGeoPoint avGeoPoint = user.getAVGeoPoint(UserInfo.LOCATION);
                            if (avGeoPoint == null) {
                                LogUtils.e("avGeopoint is null");
                            } else {
                                LogUtils.v("save location succeed latitude " + avGeoPoint.getLatitude()
                                        + " longitude " + avGeoPoint.getLongitude());
                            }
                        }
                    }
                });
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) { // 用户infoactivity返回
            if (resultCode == RESULT_OK) { // 用户注销
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                initShowData();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 退出程序
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(MainActivity.this, "再按一次可退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;// 更新firstTime
                return true;
            } else { // System.exit(0);// 否则退出程序
                System.exit(0);
                //finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 初始化显示数据
     * 用户名
     */
    private void initShowData() {
        UserInfo userInfo=UserInfo.getCurrentUser();
        if (null != userInfo) {
            if(null!=userInfo.getAvatarUrl()){
                Log.d("avatarUrl：",userInfo.getAvatarUrl());
            }
            ImageLoader.getInstance().displayImage(userInfo.getAvatarUrl(), mLeftmenuHeaderImg, PhotoUtils.avatarImageOptions);

            usernameLeftMenuTxt.setText(userInfo.getUsername());
            // 标题栏 头像*/
            /*bitmap=AppConfig.userInfo.getPortraitBit();
            if(bitmap==null){
                //mLeftmenuHeaderImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_leftmenu_header));
                mLeftmenuHeaderImg.setImageResource(R.drawable.icon_leftmenu_header);
            }else{
                mLeftmenuHeaderImg.setImageBitmap(bitmap);
                *//*Ion.with(mLeftmenuHeaderImg)
                    .placeholder(R.drawable.icon_leftmenu_header).error(R.drawable.icon_leftmenu_header)
                    .load(avatarPath) ;*//*
            }*/
        } else {
            usernameLeftMenuTxt.setText("登录用户");
            mLeftmenuHeaderImg.setImageResource(R.drawable.icon_leftmenu_header);
        }
    }

    /**
     * 初始化侧滑
     */
    private void initDragLayout() {
        mDragLayout = (DragLayout) findViewById(R.id.dl);
        mDragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
//                if (AppConfig.userInfo != null) {
//                    tv_leftmenu_username.setText(AppConfig.userInfo.getUsername());
//                }
            }

            @Override
            public void onClose() {
                //shake();
            }

            @Override
            public void onDrag(float percent) {
                //ViewHelper.setAlpha(mTitleHeaderImg, 1 - percent);
            }
        });
    }

    /**
     * 初始化左侧menu view
     */
    private void initLeftMenuView() {
        mLeftmenuHeaderImg = (CircledImageView) findViewById(R.id.iv_leftmenu_header) ;
        mLeftmenuHeaderImg.setOval(true);
        mLeftmenuHeaderImg.setOnClickListener(this);

        usernameLeftMenuTxt = (TextView) findViewById(R.id.tv_leftmenu_username) ;
    }

    /**
     * 头像晃动效果
     */
   /* private void shake() {
        mTitleHeaderImg.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.shake));
    }*/

    /**
     * 任务栈模式 设置为singleTask 在栈底 再次启动mainActivity会调用该方法
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ("reLogin".equals(intent.getStringExtra("reLogin"))) {
            if (isLogin) {
                isLogin = false;
                startActivity(new Intent(MainActivity.this, LoginActivity.class).putExtra("isReLogin", "isReLogin"));
                finish();
            }
        }
    }
}

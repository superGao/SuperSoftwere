package com.supergao.softwere.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
import com.supergao.softwere.entity.AppConfig;
import com.supergao.softwere.R;
import com.supergao.softwere.entity.UserInfo;
import com.supergao.softwere.activity.user.LoginActivity;
import com.supergao.softwere.utils.DoCacheUtil;
import com.supergao.softwere.utils.Log;

/**
 *欢迎界面
 *@author superGao
 *creat at 2016/3/14
 */
public class WelcomeActivity extends Activity {
    /**
     * 背景图片
     */
    private ImageView ivSplashBg;
    private DoCacheUtil doCacheUtil; // 存储工具类
    SharedPreferences sharePreference;
    boolean isProcting;
    public static final int SPLASH_DURATION = 2000;
    private static final int GO_MAIN_MSG = 1;
    private static final int GO_LOGIN_MSG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sharePreference = getSharedPreferences("config", MODE_PRIVATE);
        // 初始化view
        initView();
        doCacheUtil = DoCacheUtil.get(this);
        AppConfig.userInfo=UserInfo.getCurrentUser();
    }

    /**
     * 初始化view关联
     */
    private void initView() {
        isProcting = sharePreference.getBoolean("isProcting", false);
        ivSplashBg = (ImageView) findViewById(R.id.ivSplashBg);
        // 动画效果
        AnimationSet animationset = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 1);
        alphaAnimation.setDuration(4000);
        animationset.addAnimation(alphaAnimation);
        ivSplashBg.startAnimation(animationset);
        if(!isProcting){
            startActivity(new Intent(WelcomeActivity.this, AppStart.class));
        }else{
            if (UserInfo.getCurrentUser()!= null) {
                UserInfo.getCurrentUser().updateUserInfo();
                if(null!=doCacheUtil){
                    Bitmap bitmap= doCacheUtil.getAsBitmap("avatar");
                    AppConfig.userInfo.setPortraitBit(bitmap);
                }
                handler.sendEmptyMessageDelayed(GO_MAIN_MSG, SPLASH_DURATION);
            } else {
                handler.sendEmptyMessageDelayed(GO_LOGIN_MSG, SPLASH_DURATION);
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_MAIN_MSG:
                    MainActivity.goMainActivityFromActivity(WelcomeActivity.this);
                    finish();
                    break;
                case GO_LOGIN_MSG:
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                    break;
            }
        }
    };
}

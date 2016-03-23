package com.supergao.software.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.avos.avoscloud.AVUser;
import com.supergao.software.entity.AppConfig;
import com.supergao.software.R;
import com.supergao.software.entity.UserInfo;
import com.supergao.software.activity.user.LoginActivity;
import com.supergao.software.utils.DoCacheUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sharePreference = getSharedPreferences("config", MODE_PRIVATE);
        // 初始化view
        initView();
        doCacheUtil = DoCacheUtil.get(this);
        AppConfig.userInfo=new UserInfo();
        //AVUser.logOut();
        AppConfig.avUser= AVUser.getCurrentUser();
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

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent mainIntent;
                if (!isProcting) {
                    mainIntent = new Intent(WelcomeActivity.this,AppStart.class);
                    startActivity(mainIntent);
                } else {
                    if(AppConfig.avUser!=null){
                        AppConfig.userInfo.setHeader(doCacheUtil.getAsString("header"));
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    }else{
                        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                    }
                }
                finish();
            }

        }, 1000);
    }
}

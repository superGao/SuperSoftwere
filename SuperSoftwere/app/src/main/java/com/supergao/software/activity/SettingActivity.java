package com.supergao.software.activity;

import android.os.Bundle;

import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.fragment.SettingFragment;
/**
 *
 *@author superGao
 *creat at 2016/3/14
 */
public class SettingActivity extends BaseSingleFragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setTitleText("系统设置");
    }

    @Override
    protected ContentFragment createContentFragment() {
        return new SettingFragment();
    }

}

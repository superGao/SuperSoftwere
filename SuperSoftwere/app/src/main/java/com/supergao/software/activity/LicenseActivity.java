package com.supergao.software.activity;

import android.content.Intent;
import android.os.Bundle;

import com.supergao.software.R;
import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.fragment.LicenseFragment;

/**
 *服务协议
 *@author superGao
 *creat at 2016/3/14
 */
public class LicenseActivity extends BaseSingleFragmentActivity {

    /**
     * 协议类型
     */
    private int type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (1 == type) {
            setTitleText(R.string.title_terms_of_service);
        } else {
            setTitleText(R.string.title_terms_of_trade);
        }
    }

    @Override
    protected ContentFragment createContentFragment() {

        Intent intent = getIntent() ;
        type = intent.getIntExtra("type", 1) ;

        return LicenseFragment.create(type);
    }
}

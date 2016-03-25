package com.supergao.software.activity.user;

import android.os.Bundle;

import com.supergao.software.R;
import com.supergao.software.activity.BaseSingleFragmentActivity;
import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.fragment.user.IQRCodeFragment;

/**
 *我的二维码
 *@author superGao
 *creat at 2016/3/25
 */
public class IQRCodeActivity extends BaseSingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText(R.string.title_i_qrcode);
    }

    @Override
    protected ContentFragment createContentFragment() {
        return new IQRCodeFragment();
    }
}

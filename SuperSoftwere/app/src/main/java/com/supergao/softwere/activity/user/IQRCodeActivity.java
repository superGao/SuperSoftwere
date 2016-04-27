package com.supergao.softwere.activity.user;

import android.os.Bundle;

import com.supergao.softwere.R;
import com.supergao.softwere.activity.BaseSingleFragmentActivity;
import com.supergao.softwere.fragment.ContentFragment;
import com.supergao.softwere.fragment.user.IQRCodeFragment;

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

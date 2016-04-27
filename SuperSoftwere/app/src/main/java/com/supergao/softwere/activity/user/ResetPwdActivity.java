package com.supergao.softwere.activity.user;

import android.os.Bundle;

import com.supergao.softwere.R;
import com.supergao.softwere.activity.BaseSingleFragmentActivity;
import com.supergao.softwere.fragment.ContentFragment;
import com.supergao.softwere.fragment.user.ResetPwdFragment;

/**
 *重置密码 activity
 *@author superGao
 *creat at 2016/3/28
 */
public class ResetPwdActivity extends BaseSingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleText(R.string.title_reset_pwd);
    }

    @Override
    protected ContentFragment createContentFragment() {
        return new ResetPwdFragment();
    }
}

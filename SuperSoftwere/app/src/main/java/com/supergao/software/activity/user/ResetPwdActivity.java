package com.supergao.software.activity.user;

import android.os.Bundle;

import com.supergao.software.R;
import com.supergao.software.activity.BaseSingleFragmentActivity;
import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.fragment.user.ResetPwdFragment;

/**
 * 重置密码 activity
 * Created by YangJie on 2015/12/2.
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

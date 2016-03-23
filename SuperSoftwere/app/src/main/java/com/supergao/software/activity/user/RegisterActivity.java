package com.supergao.software.activity.user;

import android.os.Bundle;

import com.supergao.software.R;
import com.supergao.software.activity.BaseSingleFragmentActivity;
import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.fragment.user.RegisterFragment;

/**
 * 注册Activity
 * Created by YangJie on 2015/11/11.
 */
public class RegisterActivity extends BaseSingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 注册标题
        setTitleText(R.string.title_register);
    }

    @Override
    protected ContentFragment createContentFragment() {
        return new RegisterFragment();
    }
}

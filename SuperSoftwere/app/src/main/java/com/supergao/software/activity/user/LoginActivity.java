package com.supergao.software.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.supergao.software.R;
import com.supergao.software.activity.BaseSingleFragmentActivity;
import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.fragment.user.LoginFragment;

/**
 * 登录Activity
 * Created by YangJie on 2015/11/12.
 */
public class LoginActivity extends BaseSingleFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleText(R.string.title_login);

        setRightButtonVisibility(View.VISIBLE);
        setRightButton(R.string.btn_register, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class) ;
                startActivity(intent);

                finish();
            }
        });
    }

    @Override
    protected ContentFragment createContentFragment() {
        return new LoginFragment();
    }
}

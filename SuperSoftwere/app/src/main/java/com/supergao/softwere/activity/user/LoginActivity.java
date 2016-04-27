package com.supergao.softwere.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.supergao.softwere.R;
import com.supergao.softwere.activity.BaseSingleFragmentActivity;
import com.supergao.softwere.fragment.ContentFragment;
import com.supergao.softwere.fragment.user.LoginFragment;

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

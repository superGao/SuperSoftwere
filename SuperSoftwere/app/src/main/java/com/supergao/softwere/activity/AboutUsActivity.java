package com.supergao.softwere.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.supergao.softwere.fragment.AboutUsFragment;
import com.supergao.softwere.fragment.ContentFragment;
/**
 *
 *@author superGao
 *creat at 2016/3/14
 */
public class AboutUsActivity extends BaseSingleFragmentActivity{
    private TextView tv_version_num;
    //private WebView webView;
    private RelativeLayout rlFeedback, rlScore, rlIntroduced;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setTitleText("关于我们");
    }

    @Override
    protected ContentFragment createContentFragment() {
        return new AboutUsFragment();
    }


}

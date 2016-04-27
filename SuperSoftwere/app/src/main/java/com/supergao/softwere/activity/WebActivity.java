package com.supergao.softwere.activity;

import android.os.Bundle;

import com.supergao.softwere.fragment.ContentFragment;
import com.supergao.softwere.fragment.WebFragment;

/**
 * author：superGao on 2016/4/22.
 */
public class WebActivity extends BaseSingleFragmentActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("游戏");
    }

    @Override
    protected ContentFragment createContentFragment() {
        return new WebFragment();
    }
}

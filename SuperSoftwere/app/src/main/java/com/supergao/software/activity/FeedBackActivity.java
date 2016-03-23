package com.supergao.software.activity;

import android.os.Bundle;

import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.fragment.FeedBackFragment;

/**
 *意见反馈
 *@author superGao
 *creat at 2016/3/14
 */
public class FeedBackActivity extends BaseSingleFragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitleText("意见反馈");
	}

	@Override
	protected ContentFragment createContentFragment() {
		return new FeedBackFragment();
	}

}

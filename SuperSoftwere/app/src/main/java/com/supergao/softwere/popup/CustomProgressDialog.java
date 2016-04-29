package com.supergao.softwere.popup;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.supergao.softwere.R;


/**
 *自定义加载动画
 *@author superGao
 *creat at 2016/4/29
 */
public class CustomProgressDialog extends ProgressDialog {

	private AnimationDrawable mAnimation;
	//private Context mContext;
	private ImageView mImageView;
	private String mLoadingTip;
	private TextView mLoadingTv;
	private int mResid;

	public CustomProgressDialog(Context context, String content, int id) {
		super(context);
		//this.mContext = context;
		this.mLoadingTip = content;
		this.mResid = id;
		setCanceledOnTouchOutside(true);
	}

	public CustomProgressDialog(Context context, int id) {
		super(context);
		//this.mContext = context;
		this.mResid = id;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		setContentView(R.layout.progress_dialog);
		mLoadingTv = (TextView) findViewById(R.id.loadingTv);
		mImageView = (ImageView) findViewById(R.id.loadingIv);
	}
	private void initData() {
		mImageView.setBackgroundResource(mResid);
		// 通过ImageView对象拿到背景显示的AnimationDrawable
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		// 为了防止在onCreate方法中只显示第一帧的解决方案之一
		mImageView.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();
			}
		});
		if(!TextUtils.isEmpty(mLoadingTip)){
			mLoadingTv.setText(mLoadingTip);
		}else{
			mLoadingTv.setVisibility(View.GONE);
		}
	}

}

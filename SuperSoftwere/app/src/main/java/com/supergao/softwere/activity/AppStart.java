package com.supergao.softwere.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.supergao.softwere.R;
import com.supergao.softwere.fragment.TranslateFragment;
import com.supergao.softwere.entity.TranslatePageTransformer;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

/**
 *
 *@author superGao
 *creat at 2016/3/14
 */
public class AppStart extends FragmentActivity {
	SharedPreferences sharePreference;
	private ViewPager vp;
	private int[] lauoutId={R.layout.welcome1,R.layout.welcome2,R.layout.welcome3};
	private static final int ENTERPROCTING = 0;
	private static final int ENTERHOME = 1;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appstart);
		sharePreference = getSharedPreferences("config", MODE_PRIVATE);
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
					case ENTERPROCTING:
						startAnimation();
						break;
					case ENTERHOME:
						Intent intent = new Intent(AppStart.this, WelcomeActivity.class);
						startActivity(intent);
						AppStart.this.finish();
						break;

				}
			}
		};

		XGPushClickedResult result = XGPushManager
				.onActivityStarted(AppStart.this);
		if (result != null) {

		} else {
			initalView();
		}
	}

	private void startAnimation(){
		vp=(ViewPager) findViewById(R.id.main_vp);

		MyAdapter adapter=new MyAdapter(getSupportFragmentManager());
		// 页面滑动监听器
		vp.setPageTransformer(true, new TranslatePageTransformer());
		vp.setAdapter(adapter);
	}

	class MyAdapter extends FragmentStatePagerAdapter{

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment f=new TranslateFragment();
			Bundle bundle=new Bundle();
			bundle.putInt("layoutId", lauoutId[position]);
			f.setArguments(bundle);
			return f;
		}

		@Override
		public int getCount() {
			return 3;
		}

	}

	private void initalView() {

		boolean isProcting = sharePreference.getBoolean("isProcting", false);
		if (!isProcting) {
			Message msg = Message.obtain();
			msg.what = ENTERPROCTING;
			handler.sendMessageDelayed(msg, 0);
		} else {
			Message msg = Message.obtain();
			msg.what = ENTERHOME;
			handler.sendMessageDelayed(msg, 0);
		}

	}
}

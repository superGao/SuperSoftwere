package com.supergao.software.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.supergao.software.entity.AppConfig;
import com.supergao.software.R;
import com.supergao.software.activity.MainActivity;
import com.supergao.software.activity.user.LoginActivity;

/**
 *引导页面fragment
 *@author superGao
 *creat at 2016/3/14
 */
public class TranslateFragment extends Fragment {
	SharedPreferences sharePreference;
	public static final int MODE_PRIVATE = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		int layoutId=arguments.getInt("layoutId");
		View view = View.inflate(getActivity(), layoutId, null);

		sharePreference = getActivity().getSharedPreferences("config", MODE_PRIVATE);
		if(layoutId== R.layout.welcome3){
			view.findViewById(R.id.enter_btn_id).setOnClickListener(new BtnClickListener());
		}
		return view ;
	}

	private final class BtnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if(AppConfig.avUser!=null){
				startActivity(new Intent(getActivity(),MainActivity.class));
			}else{
				startActivity(new Intent(getActivity(),LoginActivity.class));
			}
			SharedPreferences.Editor editor = sharePreference.edit();
			editor.putBoolean("isProcting", true);
			editor.commit();
			getActivity().finish();
		}
	}
}

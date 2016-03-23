package com.supergao.software.popup;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.supergao.software.R;
import com.supergao.software.bean.Constant;

import java.io.File;

public class CameraPop extends PopupWindow {
	private Context mContext;
	private Fragment mFragment ;

	public CameraPop(final Context mContext, View parent) {
		this.mContext = mContext;
		View view = View.inflate(mContext, R.layout.popup_camara_pic, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.fade_in));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.in_to_top));

		// 设置popupWindow的宽和高
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		update();

		Button camera = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button photoPic = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button cancle = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		camera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Constant.avatarFileName = SystemClock.currentThreadTimeMillis() + ".jpg";
				// 下面这句指定调用相机拍照后的照片存储的路径
				if (!new File(Constant.PATH).exists()) {
					new File(Constant.PATH).mkdir();
				}
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
						Constant.PATH, Constant.avatarFileName)));
				((Activity) mContext).startActivityForResult(intent, 2);
				dismiss();
			}
		});
		photoPic.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				((Activity) mContext).startActivityForResult(intent, 1);
				dismiss();
			}
		});
		cancle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
	}

    /**
     * 使用应用上下文context和fragment初始化popup对话框，使用fragment启动图片获取
     * @param context 应用上下文
     * @param fragment
     */
	public CameraPop(final Context context, final Fragment fragment, View parent) {
		this.mContext = context ;
		this.mFragment = fragment;
		View view = View.inflate(mContext, R.layout.popup_camara_pic, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.fade_in));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.in_to_top));

		// 设置popupWindow的宽和高
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		update();

		Button camera = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button photoPic = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button cancle = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		camera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Constant.avatarFileName = SystemClock.currentThreadTimeMillis() + ".jpg";
				// 下面这句指定调用相机拍照后的照片存储的路径
				if (!new File(Constant.PATH).exists()) {
					new File(Constant.PATH).mkdir();
				}
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
						Constant.PATH, Constant.avatarFileName)));
				fragment.startActivityForResult(intent, 2);
				dismiss();
			}
		});
		photoPic.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				fragment.startActivityForResult(intent, 1);
				dismiss();
			}
		});
		cancle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}

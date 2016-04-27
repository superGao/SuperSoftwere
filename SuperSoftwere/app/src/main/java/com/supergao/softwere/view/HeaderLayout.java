package com.supergao.softwere.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.supergao.circledimageview.CircledImageView;
import com.supergao.softwere.R;
import com.supergao.softwere.activity.MainActivity;
import com.supergao.softwere.entity.UserInfo;


/**
 *标题栏
 *@author superGao
 *creat at 2016/3/29
 */
public class HeaderLayout extends LinearLayout {
  LayoutInflater mInflater;
  RelativeLayout header;
  TextView titleView;
  LinearLayout leftContainer, rightContainer;
  Button backBtn;
  CircledImageView circledImageView;
  Bitmap bitmap;

  public HeaderLayout(Context context) {
    super(context);
    init();
  }

  public HeaderLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    mInflater = LayoutInflater.from(getContext());
    header = (RelativeLayout) mInflater.inflate(R.layout.chat_common_base_header, null, false);
    titleView = (TextView) header.findViewById(R.id.titleView);
    leftContainer = (LinearLayout) header.findViewById(R.id.leftContainer);
    rightContainer = (LinearLayout) header.findViewById(R.id.rightContainer);
    backBtn = (Button) header.findViewById(R.id.backBtn);
    circledImageView=(CircledImageView)header.findViewById(R.id.iv_home_header);
    addView(header);
  }

  public void showTitle(int titleId) {
    titleView.setText(titleId);
  }

  public void showTitle(String s) {
    titleView.setText(s);
  }

  public void showLeftBackButton(OnClickListener listener) {
    showLeftBackButton(R.string.chat_common_emptyStr, listener);
  }

  public void showLeftBackButton() {
    showLeftBackButton(null);
  }

  public void showLeftBackButton(int backTextId, OnClickListener listener) {
    backBtn.setVisibility(View.VISIBLE);
    backBtn.setText(backTextId);
    if (listener == null) {
      listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
          ((Activity) getContext()).finish();
        }
      };
    }
    backBtn.setOnClickListener(listener);
  }

  /**
   * 标题栏左侧头像
   * @param bitmap
   * @param listener
   */
  public void showLeftCircledImage(Bitmap bitmap,OnClickListener listener){
    View imageViewLayout = mInflater.inflate(R.layout.chat_common_base_header_left_image, null, false);
    CircledImageView circledImageView=(CircledImageView)imageViewLayout.findViewById(R.id.iv_home_header);
    circledImageView.setOval(true);
    circledImageView.setImageBitmap(bitmap);
    circledImageView.setOnClickListener(listener);
    leftContainer.addView(imageViewLayout);
  }

  public void showLeftCircledImageById(int picId,OnClickListener listener){
    View imageViewLayout = mInflater.inflate(R.layout.chat_common_base_header_left_image, null, false);
    CircledImageView circledImageView=(CircledImageView)imageViewLayout.findViewById(R.id.iv_home_header);
    circledImageView.setOval(true);
    circledImageView.setImageResource(picId);
    circledImageView.setOnClickListener(listener);
    leftContainer.addView(imageViewLayout);
  }

  public void showLeftImageView(Bitmap bitmap,OnClickListener listener){
    circledImageView.setVisibility(VISIBLE);
    if(bitmap!=null){
      circledImageView.setImageBitmap(bitmap);
    }else{
      circledImageView.setImageResource(R.drawable.icon_header);
    }
    circledImageView.setOval(true);
    circledImageView.setOnClickListener(listener);
  }

  public void showLeftImageByUrl(OnClickListener listener){
    circledImageView.setVisibility(VISIBLE);
    circledImageView.setOval(true);
    circledImageView.setOnClickListener(listener);
    UserInfo userInfo=UserInfo.getCurrentUser();
    ImageLoader.getInstance().displayImage(userInfo.getAvatarUrl(), circledImageView, PhotoUtils.avatarImageOptions);
  }

  public void showRightImageButton(int rightResId, OnClickListener listener) {
    View imageViewLayout = mInflater.inflate(R.layout.chat_common_base_header_right_image_btn, null, false);
    ImageButton rightButton = (ImageButton) imageViewLayout.findViewById(R.id.imageBtn);
    rightButton.setImageResource(rightResId);
    rightButton.setOnClickListener(listener);
    rightContainer.addView(imageViewLayout);
  }

  public void showRightTextButton(int rightResId, OnClickListener listener) {
    View imageViewLayout = mInflater.inflate(R.layout.chat_common_base_header_right_btn, null, false);
    Button rightButton = (Button) imageViewLayout.findViewById(R.id.textBtn);
    rightButton.setText(rightResId);
    rightButton.setOnClickListener(listener);
    rightContainer.addView(imageViewLayout);
  }
}

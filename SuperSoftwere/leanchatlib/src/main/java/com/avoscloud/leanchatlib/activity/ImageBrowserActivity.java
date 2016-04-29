package com.avoscloud.leanchatlib.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.avoscloud.leanchatlib.R;
import com.avoscloud.leanchatlib.utils.Constants;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.avoscloud.leanchatlib.utils.SaveImageUtils;
import com.supergao.softwere.popup.SelectPicPopupWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *显示大图
 *@author superGao
 */
public class ImageBrowserActivity extends Activity {

  private ImageView imageView;
  //自定义的弹出框类
  private SelectPicPopupWindow menuWindow;
  private Bitmap bitmap;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chat_image_brower_layout);
    imageView = (ImageView) findViewById(R.id.imageView);
    Intent intent = getIntent();
    String path = intent.getStringExtra(Constants.IMAGE_LOCAL_PATH);
    String url = intent.getStringExtra(Constants.IMAGE_URL);
    PhotoUtils.displayImageCacheElseNetwork(imageView, path, url);
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    imageView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        //实例化SelectPicPopupWindow
        menuWindow = new SelectPicPopupWindow(ImageBrowserActivity.this, itemsOnClick);
        //显示窗口
        menuWindow.showAtLocation(ImageBrowserActivity.this.findViewById(R.id.ll_image), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        return false;
      }
    });
  }

  private View.OnClickListener itemsOnClick = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      menuWindow.dismiss();
      int i = v.getId();
      if (i == R.id.btn_save_photo) {//保存图片
        bitmap= SaveImageUtils.getBitmap(imageView.getDrawable());
        SaveImageUtils.SavePicInLocal(bitmap,ImageBrowserActivity.this);
        //Toast.makeText(ImageBrowserActivity.this, "保存图片", Toast.LENGTH_LONG).show();
      }
    }
  };

}
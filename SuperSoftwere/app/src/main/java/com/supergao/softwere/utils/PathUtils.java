package com.supergao.softwere.utils;

import android.os.Environment;

import com.supergao.softwere.entity.App;

import java.io.File;

/**
 *
 *@author superGao
 *creat at 2016/4/12
 */
public class PathUtils {

  private static boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state);
  }

  private static File getAvailableCacheDir() {
    if (isExternalStorageWritable()) {
      return App.ctx.getExternalCacheDir();
    } else {
      return App.ctx.getCacheDir();
    }
  }

  public static String checkAndMkdirs(String dir) {
    File file = new File(dir);
    if (file.exists() == false) {
      file.mkdirs();
    }
    return dir;
  }

  public static String getAvatarCropPath() {
    return new File(getAvailableCacheDir(), "avatar_crop").getAbsolutePath();
  }

  public static String getAvatarTmpPath() {
    return new File(getAvailableCacheDir(), "avatar_tmp").getAbsolutePath();
  }

}
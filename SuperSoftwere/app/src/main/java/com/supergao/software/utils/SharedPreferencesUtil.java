package com.supergao.software.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.supergao.software.bean.Constant;
import com.supergao.software.bean.ImageInfo;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesUtil {

    final static String regularEx = ",";

    /**
     * 保存轮番图的信息
     */
    public static void saveRollImage(Context context, List<ImageInfo> list) {
        SharedPreferences sp = context.getSharedPreferences(Constant.ROLLIMAGE_KEY, 0);
        Editor editor = sp.edit();
        if (list != null) {
            String strImage = "";
            String strUrl = "";
            for (ImageInfo imageInfo : list) {
                strImage += imageInfo.getUrl();
                strImage += regularEx;

                strUrl += imageInfo.getUrl();
                strUrl += regularEx;
            }
            editor.putString(Constant.ROLLIMAGE.IMAGE_URL, strImage);
            editor.putString(Constant.ROLLIMAGE.AD_URL, strUrl);
            editor.commit();
        }
    }

    /**
     * 获取轮番图的信息
     */
    public static List<ImageInfo> getRollImage(Context context) {
        List<ImageInfo> list = new ArrayList<ImageInfo>();
        SharedPreferences sp = context.getSharedPreferences(Constant.ROLLIMAGE_KEY, 0);
        String strImage = sp.getString(Constant.ROLLIMAGE.IMAGE_URL, "");
        String strUrl = sp.getString(Constant.ROLLIMAGE.AD_URL, "");
        if (!(TextUtils.isEmpty(strImage) || TextUtils.isEmpty(strUrl))) {
            String[] valuesImage = strImage.split(regularEx);
            String[] valuesUrl = strUrl.split(regularEx);
            for (int i = 0; i < valuesImage.length; i++) {
                ImageInfo info = new ImageInfo();
                info.setUrl(valuesImage[i]);
                info.setUrl(valuesUrl[i]);
                list.add(info);
            }
        }
        return list;
    }

}

package com.supergao.softwere.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 *@author superGao
 *creat at 2016/3/24
 */
public class BitmapUtils {
    /**
     * bitmap转换成输入流
     * @param bitmap
     * @return
     */
    public static InputStream getInputStream(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 上传头像模式
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();

        try {
            outputStream.close();
        } catch (IOException e) {
            Log.e("BitmapUtil", "close output stream error.", e) ;
        }

        return new ByteArrayInputStream(bytes) ;
    }

    /**
     * bitmap转换成字节数组
     * @param bitmap
     * @return
     */
    public static byte [] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 上传头像模式
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();

        try {
            outputStream.close();
        } catch (IOException e) {
            Log.e("BitmapUtil", "close output stream error.", e) ;
        }

        return bytes ;
    }

    /**
     * 字节数组转换成bitmap
     * @param bytes
     * @return
     */
    public static Bitmap getBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    /**
     * Drawable转bitmap
     * @param drawable
     * @return
     */
    public static Bitmap getBitmap(Drawable drawable){
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    /**
     * bitmap转Drawable
     * @param bitmap
     * @return
     */
    public static BitmapDrawable getDrawable(Bitmap bitmap){
        return new BitmapDrawable(bitmap);
    }
}

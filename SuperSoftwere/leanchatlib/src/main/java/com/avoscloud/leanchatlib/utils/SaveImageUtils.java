package com.avoscloud.leanchatlib.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 保存图片到手机工具类
 * author：superGao on 2016/4/28.
 */
public class SaveImageUtils {
    private static String fileName;

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
     * 保存拍摄的照片到手机的sd卡
     * @param bitmap
     * @param context
     */
    public static void SavePicInLocal(Bitmap bitmap, Context context) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null; // 字节数组输出流
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
            String saveDir = Environment.getExternalStorageDirectory()
                    + "/superSoftwere";
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdir(); // 创建文件夹
            }
            fileName = saveDir + "/" + System.currentTimeMillis() + ".jpg";
            File file = new File(fileName);
            file.delete();
            if (!file.exists()) {
                file.createNewFile();// 创建文件
                Toast.makeText(context,"已保存图片到"+fileName, Toast.LENGTH_SHORT)
                        .show();
                //发送广播刷新相册
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
            }
            // 将字节数组写入到刚创建的图片文件中
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }
}

package lib.support.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by YangJie on 2015/11/11.
 */
public class BitmapUtils {
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
}

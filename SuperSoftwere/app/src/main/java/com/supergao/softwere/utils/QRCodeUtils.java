package com.supergao.softwere.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *二维码工具类
 *@author superGao
 */
public class QRCodeUtils {

    /**
     * 解析二维码扫描获取的信息，解析成map对象
     * @param content 二维码扫描获取的字符串信息
     * @return
     */
    public static Map<String, String> parseContent(String content) {
        Map<String, String> params = new HashMap<>() ;

        if (TextUtils.isEmpty(content)) {
            return params ;
        }

        String[] keyValues = content.split(";") ;
        String[] tempKV = null ;
        for (String s : keyValues) {
            tempKV = s.split(":") ;
            if (tempKV.length>1) {
                params.put(tempKV[0], tempKV[1]);
            } else {
                params.put(tempKV[0], "");
            }
        }

        return params ;
    }
}

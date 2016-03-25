package com.supergao.software.fragment.user;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.supergao.software.entity.AppConfig;
import com.supergao.software.R;
import com.supergao.software.bean.Role;
import com.supergao.software.core.listener.ActionCallbackListener;
import com.supergao.software.core.listener.DefaultActionCallbackListener;
import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.utils.UnRegisterUserUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lib.support.utils.LogUtils;

/**
 *我的二维码 展示内容Fragment
 *@author superGao
 *creat at 2016/3/25
 */
public class IQRCodeFragment extends ContentFragment implements View.OnClickListener {

    private RelativeLayout contentRlayout ;

    /**
     * 二维码展示信息
     */
    private ImageView qrcodeImg ;

    /**
     * 二维码加载提示
     */
    private TextView qrcodeLoadTipTxt ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_i_qrcode, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView() ;

        registerClickListener() ;

        // 加载数据
        getIQRCodeContent();
    }

    private void initView() {
        contentRlayout = (RelativeLayout) getView().findViewById(R.id.rlayout_content) ;
        qrcodeImg = (ImageView) getView().findViewById(R.id.img_qrcode) ;
        qrcodeLoadTipTxt = (TextView) getView().findViewById(R.id.txt_qrcode_load_tip) ;
    }

    private void registerClickListener() {
        qrcodeLoadTipTxt.setOnClickListener(this);
    }

    /**
     * 获取二维码内容
     */
    private void getIQRCodeContent() {
        String QRCode="userId:"+AppConfig.avUser.getObjectId()+";"+"userName:"+AppConfig.avUser.getUsername();
        showIQRCodeImage(QRCode);
    }


    /**
     * 显示二维码信息
     * @param content
     */
    private void showIQRCodeImage(String content) {
        if (TextUtils.isEmpty(content)) {
            qrcodeLoadTipTxt.setVisibility(View.VISIBLE);
            qrcodeLoadTipTxt.setText(R.string.label_i_qrcode_data_reload); // 显示重新加载字样
            return ;
        }
        qrcodeLoadTipTxt.setVisibility(View.GONE);
        Bitmap bitmap = generateQRCode(content);
        if (null != bitmap) {
            qrcodeImg.setImageBitmap(bitmap);
        }

    }

    /**
     * 根据字符串内容生成二维码
     * @param content 字符串内容
     * @return
     */
    private Bitmap generateQRCode(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            // MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 500, 500);
            return bitMatrix2Bitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_qrcode_load_tip: {
                getIQRCodeContent();
                break ;
            }
        }
    }
}

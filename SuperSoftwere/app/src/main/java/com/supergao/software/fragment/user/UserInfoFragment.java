package com.supergao.software.fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.supergao.circledimageview.CircledImageView;
import com.supergao.software.activity.BaseSingleFragmentActivity;
import com.supergao.software.entity.AppConfig;
import com.supergao.software.R;
import com.supergao.software.activity.user.IQRCodeActivity;
import com.supergao.software.bean.BaseUserInfo;
import com.supergao.software.bean.Common;
import com.supergao.software.bean.Constant;
import com.supergao.software.bean.RegisterType;
import com.supergao.software.core.listener.DefaultActionCallbackListener;
import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.popup.CameraPop;
import com.supergao.software.utils.DoCacheUtil;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.support.utils.LogUtils;
import lib.support.utils.ToastUtil;

/**
 * 用户信息 Fragment
 * Created by YangJie on 2015/11/21.
 */
public class UserInfoFragment extends ContentFragment implements View.OnClickListener{
    @Bind(R.id.img_user_header)
    CircledImageView userHeaderImg;
    public static EditText realnameEdt;
    @Bind(R.id.rlayout_qrcode_image)
    RelativeLayout qrcodeImageRlayout;
    @Bind(R.id.btn_logout)
    Button logoutBtn;
    private CircledImageView headerPicImg;

    /**
     * 选择照片类型
     */
    private int selectPicType = 0 ;
    /**
     * 选择头像
     */
    private final static int SELECT_HEAD_PIC = 1;
    private DoCacheUtil doCacheUtil ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_userinfo, container, false);
        ButterKnife.bind(this, contentView);
        userHeaderImg.setOval(true);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        headerPicImg = (CircledImageView) getView().findViewById(R.id.img_user_header);
        realnameEdt=(EditText)getView().findViewById(R.id.edt_realname);
        initShowData() ;
        headerPicImg.setOnClickListener(this);
        doCacheUtil = DoCacheUtil.get(getActivity()) ;
    }

    /**
     * 初始化显示数据
     */
    private void initShowData() {
        AVUser userInfo = AppConfig.avUser ;
        realnameEdt.setText(userInfo.getUsername());
        // 显示头像
        Ion.with(userHeaderImg)
                .placeholder(R.drawable.ic_default).error(R.drawable.ic_default)
                .load(AppConfig.userInfo.getHeader()) ;
    }

    /**
     * 注销登录
     */
    @OnClick(R.id.btn_logout)
     void onLogout(View view) {
        AppConfig.avUser.logOut();
        DoCacheUtil.get(getActivity()).remove(RegisterType.class.getSimpleName()) ;
        DoCacheUtil.get(getActivity()).remove(BaseUserInfo.class.getSimpleName()) ;
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }


    @OnClick(R.id.rlayout_qrcode_image)
    void onQrcodeImageRlayoutClick(View view) {
        // 打开二维码activity
        Intent intent = new Intent(getActivity(), IQRCodeActivity.class) ;
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_user_header: { // 头像
                onHeaderPicImgClick(v) ;
                break ;
            }
            default:
                break;
        }
    }
    /**
     * 选择头像
     */
    private void onHeaderPicImgClick(View v) {
        selectPicType = SELECT_HEAD_PIC ; // 选择头像
        CameraPop cameraPop = new CameraPop(getActivity(), this, v) ;
    }

    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");

            switch(selectPicType) {
                case SELECT_HEAD_PIC: { // 选择的是头像
                    BitmapDrawable drawable = new BitmapDrawable(photo);
                    headerPicImg.setImageDrawable(drawable);
                    doUploadHead(photo); // 上传头像
                    break ;
                }
                default:
                    break;
            }
        }
    }

    /**
     * 上传头像
     */
    private void doUploadHead(Bitmap photo) {
        showLoadingDialog("正在上传头像，请稍候.");
        BaseUserInfo driverInfo = AppConfig.sUserInfo ;
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 上传头像模式
        photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        getApp().getAppAction().uploadHead(driverInfo.getId(), driverInfo.getKey(), new ByteArrayInputStream(bytes), new DefaultActionCallbackListener<Common>(getActivity()) {
            @Override
            public void onSuccess(Common data) {
                dismissLoadingDialog();
                ToastUtil.showShort(getActivity(),"上传头像成功");
                LogUtils.i(" -> upload head success. avatar:" + data.getAvatar());
                AppConfig.sUserInfo.setAvatar(data.getAvatar());
                doCacheUtil.put(BaseUserInfo.class.getSimpleName(), AppConfig.sUserInfo);

                if (null != outputStream) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        LogUtils.e("close output stream error.", e);
                    }
                }
            }

            @Override
            public void onAfterFailure() {
                dismissLoadingDialog();
                if (null != outputStream) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        LogUtils.e("close output stream error.", e);
                    }
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: // 直接从相册获取
                if (data != null) {
                    startPhotoZoomActivityForResult(data.getData()) ;
                }
                break;
            case 2:  // 调用相机拍照时
                File temp = new File(Constant.PATH, Constant.avatarFileName);
//                File file = new File("1212.jpg");
                if (temp.exists()) {
                    startPhotoZoomActivityForResult(Uri.fromFile(temp)) ;
                }
                break;

            case 3: // 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;

            case 5:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

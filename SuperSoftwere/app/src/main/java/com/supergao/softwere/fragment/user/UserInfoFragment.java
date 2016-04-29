package com.supergao.softwere.fragment.user;

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

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.supergao.circledimageview.CircledImageView;
import com.supergao.softwere.entity.AppConfig;
import com.supergao.softwere.R;
import com.supergao.softwere.bean.Constant;
import com.supergao.softwere.entity.UserInfo;
import com.supergao.softwere.fragment.ContentFragment;
import com.supergao.softwere.popup.CameraPop;
import com.supergao.softwere.service.PushManager;
import com.supergao.softwere.utils.AVService;
import com.supergao.softwere.utils.BitmapUtil;
import com.supergao.softwere.utils.BitmapUtils;
import com.supergao.softwere.utils.DoCacheUtil;
import com.supergao.softwere.utils.PathUtils;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.supergao.softwere.utils.ToastUtil;

/**
 *
 *@author superGao
 *creat at 2016/3/25
 */
public class UserInfoFragment extends ContentFragment implements View.OnClickListener{
    @Bind(R.id.img_user_header)
    CircledImageView userHeaderImg;
    public static EditText realnameEdt;
    @Bind(R.id.btn_logout)
    Button logoutBtn;
    private CircledImageView headerPicImg;
    private Bitmap bitmap;

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
        headerPicImg.setOnClickListener(this);
        doCacheUtil = DoCacheUtil.get(getActivity()) ;
        initShowData();
    }

    /**
     * 初始化显示数据
     */
    private void initShowData() {
        realnameEdt.setText(AppConfig.userInfo.getUsername());
        UserInfo userInfo=UserInfo.getCurrentUser();
        ImageLoader.getInstance().displayImage(userInfo.getAvatarUrl(), headerPicImg, PhotoUtils.avatarImageOptions);
        /*bitmap=AppConfig.userInfo.getPortraitBit();
        // 显示头像
        if(bitmap==null){
            headerPicImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_default));
        }else{
            headerPicImg.setImageBitmap(bitmap);
        }*/
        //Ion.with(userHeaderImg).placeholder(R.drawable.ic_default).error(R.drawable.ic_default).load(AppConfig.userInfo.getHeader()) ;//根据获取到的图片链接显示图片

    }

    /**
     * 注销登录
     */
    @OnClick(R.id.btn_logout)
     void onLogout(View view) {
        PushManager.getInstance().unsubscribeCurrentUserChannel();//注销推送
        UserInfo.logOut();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
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
            final Bitmap photo = extras.getParcelable("data");
            String path = null;
            switch(selectPicType) {
                case SELECT_HEAD_PIC: { // 选择的是头像
                    BitmapDrawable drawable = new BitmapDrawable(photo);
                    headerPicImg.setImageDrawable(drawable);
                    if (photo != null) {
                        bitmap = PhotoUtils.toRoundCorner(photo, 10);
                        path = PathUtils.getAvatarCropPath();
                        AppConfig.userInfo.setPortraitBit(bitmap);
                        PhotoUtils.saveBitmap(path, bitmap);
                        UserInfo user = UserInfo.getCurrentUser();
                        user.saveAvatar(getActivity(), path, null);
                        if (bitmap != null && bitmap.isRecycled() == false) {
                            bitmap.recycle();
                        }
                    }
                    /*SaveCallback saveCallback=new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            AppConfig.userInfo.setPortraitBit(photo);
                            DoCacheUtil doCacheUtil=DoCacheUtil.get(getActivity());
                            doCacheUtil.put("avatar",photo);
                            ToastUtil.showShort(getActivity(),"上传成功");

                        }
                    };
                    ProgressCallback progressCallback=new ProgressCallback() {
                        @Override
                        public void done(Integer integer) {
                            ToastUtil.showShort(getActivity(),"上传进度："+integer);
                        }
                    };
                    try {
                        AVService.updateHeader("portrait",BitmapUtils.getBytes(photo),AppConfig.userInfo.getObjectId(),saveCallback,progressCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    break ;
                }
                default:
                    break;
            }
        }
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

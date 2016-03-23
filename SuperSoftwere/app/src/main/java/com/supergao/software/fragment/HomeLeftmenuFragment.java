package com.supergao.software.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.supergao.software.entity.AppConfig;
import com.supergao.software.R;
import com.supergao.software.activity.SettingActivity;
import com.supergao.software.activity.user.IQRCodeActivity;
import com.supergao.software.activity.user.LoginActivity;
import com.supergao.software.bean.VersionInfo;
import com.supergao.software.core.listener.DefaultActionCallbackListener;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import lib.support.utils.LogUtils;

/**
 * 左侧菜单展示Fragment
 * Created by YangJie on 2015/11/6.
 */
public class HomeLeftmenuFragment extends TabContentFragment implements View.OnClickListener {
    @Bind(R.id.rlayout_contact_phone)
    RelativeLayout contactPhoneRLayout;
    @Bind(R.id.rlayout_setting)
    RelativeLayout settingRLayout;
    @Bind(R.id.rl_share)
    RelativeLayout rl_share;
    @Bind(R.id.rl_qrcode)
    RelativeLayout rl_qrcode;
    private String url_net;
    private Boolean isFirst=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_leftmenu, container, false);
        ButterKnife.bind(this, contentView);

        //initView(contentView) ;

        registerListener() ;

        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*private void initView(View contentView) {
        rl_share=(RelativeLayout)getView().findViewById(R.id.rl_share);
    }*/

    private void registerListener() {
        contactPhoneRLayout.setOnClickListener(this);
        settingRLayout.setOnClickListener(this);
        rl_share.setOnClickListener(this);
        rl_qrcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlayout_contact_phone:  // 联系电话
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "58952553")));
                break;
            case R.id.rlayout_setting:  // 系统设置
                if (AppConfig.avUser != null) {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break ;
            case R.id.rl_share:     //分享
                version_checking_net("100");
                break;
            case R.id.rl_qrcode:     //我的二维码
                startActivity(new Intent(getActivity(), IQRCodeActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 版本检测网络请求
     */
    public void version_checking_net(String version) {
        if(isFirst){
            showLoadingDialog(R.string.dialog_share);
        }
        getApp().getAppAction().getVersionCode(version,
                new DefaultActionCallbackListener<VersionInfo>(getActivity()) {
                    @Override
                    public void onSuccess(VersionInfo data) {
                        dismissLoadingDialog();
                        LogUtils.d("data:" + JSON.toJSONString(data));
                        if (null != data) {
                            url_net = data.getUrl();
                            /*if(data.getType().equals("2")){
                                shareMsg("我要分享", "我要分享", "我有一个很赞的APP，听说大家都在用哦！赶快下载吧。     " + url_net, "");
                            }*/
                            shareMsg("我要分享", "我要分享", "我有一个很赞的APP，听说大家都在用哦！赶快下载吧。     " + url_net, "");
                        } else {
                            // 提示 暂无记录
                            showShortToast("网络异常，请重试");
                        }
                    }

                    @Override
                    public void onAfterFailure() {
                        dismissLoadingDialog();
                        showShortToast("网络异常，请重试");
                    }
                });
    }

    /**
     * 分享功能
     *
     * @param activityTitle
     *		  Activity的名字
     * @param msgTitle
     *		  消息标题
     * @param msgText
     *		  消息内容
     * @param imgPath
     *		  图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }
}

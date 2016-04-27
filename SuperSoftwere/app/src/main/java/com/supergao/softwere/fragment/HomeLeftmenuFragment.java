package com.supergao.softwere.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.supergao.softwere.entity.AppConfig;
import com.supergao.softwere.R;
import com.supergao.softwere.activity.SettingActivity;
import com.supergao.softwere.activity.user.IQRCodeActivity;
import com.supergao.softwere.activity.user.LoginActivity;
import com.supergao.softwere.onekeyshare.OnekeyShare;
import com.supergao.softwere.utils.AVService;
import com.supergao.softwere.utils.Log;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;

/**
 *左侧菜单展示Fragment
 *@author superGao
 *creat at 2015/11/6.
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
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "13718582913")));
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
        showLoadingDialog(R.string.dialog_share);

        GetCallback<AVObject> getCallback=new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                dismissLoadingDialog();
                if(e==null){
                    Log.d("versionInfo:",avObject.getInt("versionCode")+"");
                    url_net=avObject.getString("url");
                    //shareMsg("我要分享", "我要分享", "我有一个很赞的APP，听说大家都在用哦！赶快下载吧。     " + url_net, "");
                    showShare("我要分享",url_net,"我有一个很赞的社交APP，听说大家都在用哦！赶快下载吧。");
                }else {
                    Log.d("error:",e.getMessage()+"  errorCode: "+e.getCode());
                }
            }
        };
        AVService.loadVersion(getCallback);
    }

    private void showShare(String title,String url,String content) {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(url);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("SuperSoftwere");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        // 启动分享GUI
        oks.show(getActivity());
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

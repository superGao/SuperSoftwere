package com.supergao.softwere.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.supergao.softwere.R;
import com.supergao.softwere.activity.AboutUsActivity;
import com.supergao.softwere.activity.SetMessageActivity;
import com.supergao.softwere.activity.user.RevisePwdActivity;
import com.supergao.softwere.bean.VersionInfo;
import com.supergao.softwere.utils.AVService;
import com.supergao.softwere.utils.CheckVersionUtils;
import com.supergao.softwere.utils.Log;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import com.supergao.softwere.utils.ToastUtil;

/**
 * 系统设置fragment
 */
public class SettingFragment extends ContentFragment implements View.OnClickListener{
    private RelativeLayout rlSetPwd, rlSetMsg, rlCheckUpdateVersion, rlAboutUs;
    private TextView tvVersionProgress;
    private String  firToken;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.activity_system_setting, container, false);
        initView(contentView) ;
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        rlSetPwd = (RelativeLayout)view.findViewById(R.id.rl_set_pwd);
        rlSetMsg = (RelativeLayout)view.findViewById(R.id.rl_set_msg);
        rlCheckUpdateVersion = (RelativeLayout)view.findViewById(R.id.rl_check_update_version);
        tvVersionProgress = (TextView)view.findViewById(R.id.tv_version_upload_progress);
        rlAboutUs = (RelativeLayout)view.findViewById(R.id.rl_about_us);
        rlSetPwd.setOnClickListener(this);
        rlSetMsg.setOnClickListener(this);
        rlCheckUpdateVersion.setOnClickListener(this);
        rlAboutUs.setOnClickListener(this);
        /*String areaValue = getAreaValue();*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_set_pwd:    //密码设置
                startActivity(new Intent(getActivity(), RevisePwdActivity.class));
                break;
            case R.id.rl_set_msg:    //消息提醒设置
                startActivity(new Intent(getActivity(), SetMessageActivity.class));
                break;
            case R.id.rl_check_update_version: // 检测版本更新
                //rlCheckUpdateVersion.setClickable(false);
                firToken="0510bcad40586de5fb8808c0a67fb428";
                CheckVersionUtils checkVersionUtils=new CheckVersionUtils(getActivity(),rlCheckUpdateVersion,tvVersionProgress);
                checkVersionUtils.checkVersion(firToken);
                break;
            case R.id.rl_about_us:// 关于我们 介绍
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            default:
                break;
        }

    }



}

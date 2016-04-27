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
import com.supergao.softwere.R;
import com.supergao.softwere.activity.AboutUsActivity;
import com.supergao.softwere.activity.SetMessageActivity;
import com.supergao.softwere.activity.user.RevisePwdActivity;
import com.supergao.softwere.utils.AVService;
import com.supergao.softwere.utils.Log;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;

import lib.support.utils.ToastUtil;

/**
 * 系统设置fragment
 */
public class SettingFragment extends ContentFragment implements View.OnClickListener{
    private RelativeLayout rlSetPwd, rlSetMsg, rlCheckUpdateVersion, rlAboutUs;
    private boolean isFront;
    private String version_net;
    private String url_net;
    private String versionName;
    private TextView tvVersionProgress;
    // 下载线程
    private Thread downLoadThread;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_ERROR = 3;
    private int percent;//下载百分比
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
                rlCheckUpdateVersion.setClickable(false);
                version_checking_net(getVersionName());
                break;
            case R.id.rl_about_us:// 关于我们 介绍
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            default:
                break;
        }

    }

    /**
     * 获取应用程序版本名称信息
     */
    private String getVersionName() {
        PackageManager pm = getActivity().getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(
                    getActivity().getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 版本检测网络请求
     */
    public void version_checking_net(final String version) {
        showLoadingDialog(R.string.dialog_update);

        GetCallback<AVObject> getCallback = new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                dismissLoadingDialog();
                if (e == null) {
                    Log.d("versionInfo:", avObject.getInt("versionCode") + "oldVersion:"+version);
                    url_net = avObject.getString("url");
                    version_net=String.valueOf(avObject.getInt("versionCode"));
                    versionName=avObject.getString("appName");
                    if(version.equals(version_net)){
                        ToastUtil.showShort(getActivity(),"当前已经是最新版本");
                        return;
                    }
                    showDialog();

                } else {
                    Log.d("error:", e.getMessage() + "  errorCode: " + e.getCode());
                }
            }
        };
        AVService.loadVersion(getCallback);
    }
    /**
     * 显示对话框
     */
    private void showDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        builder.setMessage("是否立即下载最新版本?")
                // 不可取消（即返回键不能取消此对话框）
                .setCancelable(false)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                showUpdateDialog(url_net,version_net,versionName);
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                rlCheckUpdateVersion.setClickable(true);
                            }
                        });
        // 用对话框构造器创建对话框
        AlertDialog alert = builder.create();
        // 显示对话框
        alert.show();
    }
    /**
     * 下载最新版app
     */
    protected void showUpdateDialog(String apkurl, String vision_net,
                                    String version_name) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 说明sd卡状态正常，开始下载新apk
            // 下载时用到第三方开源框架 afinal jar包已经导入到libs目录下
            isFront = true;
            FinalHttp finalHttp = new FinalHttp();
            finalHttp.download(apkurl, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + version_name + vision_net + ".apk",
                    new AjaxCallBack<File>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            showLoadingDialog("正在下载新版本，请稍候.");
                        }

                        @Override
                        public void onLoading(long count, long current) {
                            super.onLoading(count, current);
                            // 计算当前下载的百分比
                            percent = (int) ((current) / (float) count * 100);
                            tvVersionProgress.setText("下载进度：" + percent + "%");
                            dismissLoadingDialog();
                        }

                        @Override
                        public void onSuccess(File t) {
                            super.onSuccess(t);
                            // 下载完成后，开始替换安装，↓调用自定义的安装方法
                            installAPK(t);
                            tvVersionProgress.setText(null);
                            rlCheckUpdateVersion.setClickable(true);
                            dismissLoadingDialog();
                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo,
                                              String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                            ToastUtil.showShort(getActivity(), "网络不给力，请稍后再试。");
                            tvVersionProgress.setText(null);
                            rlCheckUpdateVersion.setClickable(true);
                            dismissLoadingDialog();
                        }
                    });

        } else {
            Toast.makeText(getActivity(), "未检测到SD卡", Toast.LENGTH_SHORT)
                    .show();
            rlCheckUpdateVersion.setClickable(true);
            return;
        }
    }

    /**
     * ↓安装APK的方法,其实就是调用系统安装APK的工具， 发一个Intent就行
     *
     * @param t APK的路径,也就是APK文件
     */
    private void installAPK(File t) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
        if (isFront) {
            startActivity(intent);
        }
    }


    /*private String getAreaValue() {
        SharedPreferences sharePreference = null;
        if (sharePreference == null)
            sharePreference =getActivity().getSharedPreferences("config", MODE_PRIVATE);
        return sharePreference.getString("spSetAreaValue", "");
    }*/
}

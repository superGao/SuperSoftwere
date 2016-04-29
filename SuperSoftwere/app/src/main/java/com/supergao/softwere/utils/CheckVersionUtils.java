package com.supergao.softwere.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.supergao.softwere.R;
import com.supergao.softwere.bean.VersionInfo;
import com.supergao.softwere.popup.CustomProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * 检测版本更新Utils
 * author：superGao on 2016/4/29.
 */
public class CheckVersionUtils {
    private String version_net;
    private String url_net;
    private String appName;
    private Context context;
    private boolean isFront;
    /**
     * 加载对话框
     */
    private CustomProgressDialog customProgressDialog;
    private RelativeLayout rlCheckUpdateVersion;
    private int percent;//下载百分比
    private TextView tvVersionProgress;

    public CheckVersionUtils(Context context,RelativeLayout relativeLayout, TextView tvVersionProgress) {
        this.context=context;
        this.rlCheckUpdateVersion=relativeLayout;
        this.tvVersionProgress=tvVersionProgress;
        customProgressDialog=new CustomProgressDialog(context,"我正在玩儿命加载。。。",R.drawable.frame_haha);
    }

    /**
     * 检测 fir.im 的新版本
     * @param firToken
     */
    public void checkVersion(String firToken){
        FIR.checkForUpdateInFIR(firToken, new VersionCheckCallback() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.d("fir","check from fir.im success! " + "\n" + result);
                Gson gson = new Gson();
                VersionInfo versionInfo = gson.fromJson(result, new TypeToken<VersionInfo>() {
                }.getType());
                url_net = versionInfo.getInstallUrl();
                version_net=versionInfo.getVersion();
                appName=versionInfo.getName();
                if(getVersionName().equals(version_net)){
                    ToastUtil.showShort(context,"当前已经是最新版本");
                    return;
                }
                showDialog();
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                Log.d("fir", "check fir.im fail! " + "\n" + e.getMessage());
                customProgressDialog.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                customProgressDialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                customProgressDialog.dismiss();
            }
        });

    }


    /**
     * 获取应用程序版本名称信息
     */
    private String getVersionName() {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
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
        customProgressDialog.show();
        GetCallback<AVObject> getCallback = new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    Log.d("versionInfo:", avObject.getString("versionName") + "oldVersion:"+version);
                    url_net = avObject.getString("url");
                    version_net=avObject.getString("versionName");
                    appName=avObject.getString("appName");
                    if(version.equals(version_net)){
                        ToastUtil.showShort(context,"当前已经是最新版本");
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
                new AlertDialog.Builder(context);
        builder.setMessage("是否立即下载最新版本?")
                // 不可取消（即返回键不能取消此对话框）
                .setCancelable(false)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                showUpdateDialog(url_net,version_net,appName);
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
     * @param apkurl
     * @param vision_net
     * @param version_name
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
                            customProgressDialog.show();
                        }

                        @Override
                        public void onLoading(long count, long current) {
                            super.onLoading(count, current);
                            // 计算当前下载的百分比
                            percent = (int) ((current) / (float) count * 100);
                            tvVersionProgress.setText("下载进度：" + percent + "%");
                            customProgressDialog.dismiss();
                        }

                        @Override
                        public void onSuccess(File t) {
                            super.onSuccess(t);
                            // 下载完成后，开始替换安装，↓调用自定义的安装方法
                            installAPK(t);
                            tvVersionProgress.setText(null);
                            rlCheckUpdateVersion.setClickable(true);
                            customProgressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo,
                                              String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                            ToastUtil.showShort(context, "网络不给力，请稍后再试。");
                            tvVersionProgress.setText(null);
                            rlCheckUpdateVersion.setClickable(true);
                            customProgressDialog.dismiss();
                        }
                    });

        } else {
            Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_SHORT)
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
            context.startActivity(intent);
        }
    }

}

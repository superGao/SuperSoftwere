package com.supergao.softwere.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.supergao.softwere.R;
import com.supergao.softwere.activity.FeedBackActivity;

import java.util.ArrayList;
import java.util.List;

import lib.support.utils.ToastUtil;

/**
 * 关于我们fragment
 */
public class AboutUsFragment extends ContentFragment implements View.OnClickListener{
    private TextView tv_version_num;
    //private WebView webView;
    private RelativeLayout rlFeedback, rlScore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.acticity_about_us, container, false);
        initView(contentView) ;
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        tv_version_num=(TextView)view.findViewById(R.id.tv_version_num);
        rlFeedback = (RelativeLayout)view.findViewById(R.id.rl_feedback);
        rlScore = (RelativeLayout)view.findViewById(R.id.rl_score);
        rlFeedback.setOnClickListener(this);
        rlScore.setOnClickListener(this);
        //webView = (WebView) findViewById(R.id.wv_about_us);
        //loadUrl(AppConfig.userInfo.getId(), AppConfig.userInfo.getKey());

        tv_version_num.setText(getVersionName());
    }

    /**
     * 获取应用程序版本名称信息
     */
    private String getVersionName() {
        PackageManager pm = getActivity().getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getActivity().getPackageName(), 0);
            String s=info.versionName;
            return s;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_feedback:	//意见反馈
                startActivity(new Intent(getActivity(),FeedBackActivity.class));
                break;
            case R.id.rl_score:		// 给个好评 跳转到第三方应用市场 评分
                if(isAvilible(getActivity().getApplicationContext())){
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    ToastUtil.showShort(getActivity(),"本机尚未安装应用市场，请安装后再评分，谢谢合作");
                }

                break;
            default:
                break;
        }
    }
    /**
     * 判断手机中是否安装了指定的应用市场
     * 	com.qihoo.appstore  360手机助手
     com.taobao.appcenter    淘宝手机助手
     com.tencent.android.qqdownloader    应用宝
     com.hiapk.marketpho 安卓市场
     cn.goapk.market 安智市场
     * @param context
     * @return
     */
    public static boolean isAvilible(Context context) {
        String[] appstors={"com.qihoo.appstore","com.taobao.appcente","com.hiapk.marketpho","cn.goapk.market","com.xiaomi.market"};
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        if (pName.contains(appstors[0])||pName.contains(appstors[1])||pName.contains(appstors[2])||pName.contains(appstors[3])||pName.contains(appstors[4])){
            return true;
        }else{
            return false;
        }
    }
}

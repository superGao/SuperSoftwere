package com.supergao.software.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.supergao.software.R;
import com.supergao.software.api.Api;
import com.supergao.software.api.HttpEngine;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 服务协议 fragment
 * Created by YangJie on 2015/12/17.
 */
public class LicenseFragment extends ContentFragment {

    /**
     * 显示服务协议类型，服务条款 or 交易条款
     */
    public static final String TYPE = "type";
    @Bind(R.id.web_license)
    WebView licenseWeb;

    /**
     * 协议类型
     */
    private int type ;

    /**
     * 创建服务协议显示fragment
     * @param type 协议类型， 1：服务条款；2：交易条款
     * @return
     */
    public static LicenseFragment create(int type) {
        LicenseFragment fragment = new LicenseFragment() ;

        Bundle args = new Bundle() ;
        args.putInt(TYPE, type);
        fragment.setArguments(args);

        return fragment ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments() ;
        type = args.getInt(TYPE, 1) ;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_license, container, false);

        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        licenseWeb.getSettings().setJavaScriptEnabled(true);
        licenseWeb.setWebViewClient(new WebViewClient());
        licenseWeb.getSettings().setDefaultTextEncodingName("utf-8");

        if(type == 1) {
            licenseWeb.loadUrl(HttpEngine.getAbsoluteUrl(Api.Common.URL_LICENSE_RULE));
        } else if(type == 2) {
            licenseWeb.loadUrl(HttpEngine.getAbsoluteUrl(Api.Common.URL_LICENSE_ORDER));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

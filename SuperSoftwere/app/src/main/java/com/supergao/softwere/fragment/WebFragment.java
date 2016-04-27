package com.supergao.softwere.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.supergao.softwere.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：superGao on 2016/4/22.
 * note：借用请注明来源，侵权必究！
 */
public class WebFragment extends ContentFragment {
    @Bind(R.id.wv_game)
    public WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView=inflater.inflate(R.layout.game,container,false);
        ButterKnife.bind(this,contentView);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置支持JavaScript脚本
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setGeolocationEnabled(true);
        webView.loadUrl("http://g.alicdn.com/tmapp/hilodemos/3.0.8/flappy/index.html");
    }
}

package com.supergao.software.api.support;

import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

/**
 * JSON响应数据处理器
 * Created by YangJie on 2015/11/10.
 */
public class JSONHttpResponseHandler extends TextHttpResponseHandler {
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {

    }
}

package com.supergao.software.api;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.supergao.software.api.listener.ApiCallbackListener;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

import lib.support.utils.LogUtils;

/**
 * Http网络服务访问引擎
 * Created by YangJie on 2015/11/10.
 */
public class HttpEngine {
//    private final static String TAG = "LogUtils" ;
    /**
     * 服务器基本url路径
     */
    //private final static String SERVER_BASE_URL = "http://36.110.14.142:8016" ;
    private final static String SERVER_BASE_URL = "http://172.17.2.235:8016" ;
    /**
     * 默认编码
     */
    private final static String ENCODE_TYPE = "UTF-8";

    /**
     * 超时时间
     */
    private final static int TIME_OUT = 15000;

    /**
     * 公共http服务访问client
     */
    private static AsyncHttpClient sClient ;

    static {
        sClient = new AsyncHttpClient();
        sClient.setTimeout(60 * 1000); // 设置超时时间 60秒
    }

    public static AsyncHttpClient getHttpClient() {
        return sClient ;
    }

    /**
     * get 请求数据
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, Map<String, Object> params, AsyncHttpResponseHandler responseHandler) {
        LogUtils.d("GET: url[ "+url+" ], data[ " + JSON.toJSONString(params)+ " ]");
        sClient.get(getAbsoluteUrl(url), getRequestParams(params), responseHandler);
    }

    /**
     * get请求数据
     * @param url 接口相对url路径
     * @param params 参数
     * @param listener api请求回调接口
     * @param type 解析对象的type类型描述，例如类型为T,那么描述为 new TypeToken<ApiResponse<T>>(){}.getType()
     */
    public static <T> void get(String url, Map<String, Object> params, final ApiCallbackListener<T> listener, final Type type) {
        LogUtils.d("GET: url[ "+url+" ], data[ " + JSON.toJSONString(params)+ " ]");
        sClient.get(getAbsoluteUrl(url), getRequestParams(params), createHttpResponseHandler(listener, type));
    }

    /**
     * get请求数据
     * @param url 接口相对url路径
     * @param params 参数
     * @param listener api请求回调接口
     */
    public static <T> void get(String url, Map<String, Object> params, final ApiCallbackListener<T> listener) {
        get(url, params, listener, new TypeToken<ApiResponse<Void>>() {}.getType());
    }

    /**
     * post请求数据
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void post(String url, Map<String, Object> params, AsyncHttpResponseHandler responseHandler) {
        LogUtils.d("POST: url[ "+url+" ], data[ " + JSON.toJSONString(params)+ " ]");
        sClient.post(getAbsoluteUrl(url), getRequestParams(params), responseHandler);
    }

    /**
     * post请求数据
     * @param url 接口相对url路径
     * @param params 参数
     * @param listener api请求回调接口
     * @param type 解析对象的type类型描述，例如类型为T,那么描述为 new TypeToken<ApiResponse<T>>(){}.getType()
     */
    public static <T> void post(String url, Map<String, Object> params, final ApiCallbackListener<T> listener, final Type type) {
        LogUtils.d("POST: url[ "+url+" ], data[ " + JSON.toJSONString(params)+ " ]");
        sClient.post(getAbsoluteUrl(url), getRequestParams(params), createHttpResponseHandler(listener, type)) ;
    }

    /**
     * post请求数据
     * @param url 接口相对url路径
     * @param params 参数
     * @param listener api请求回调接口
     */
    public static <T> void post(String url, Map<String, Object> params, final ApiCallbackListener<T> listener) {
        post(url, params, listener, new TypeToken<ApiResponse<Void>>() {}.getType());
    }



    /**
     * 获取绝对访问路径
     * @param relativeUrl 相对路径
     * @return 绝对路径
     */
    public static String getAbsoluteUrl(String relativeUrl) {
        return SERVER_BASE_URL + relativeUrl;
    }

    /**
     * 根据map对象获取RequestParams对象
     * @param params map参数对象
     * @return
     */
    private static RequestParams getRequestParams(Map<String, Object> params) {
        RequestParams requestParams = new RequestParams() ;

        if (null != params && params.size()>0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() instanceof String) {
                    requestParams.put(entry.getKey(), (String) entry.getValue());
                } else if(entry.getValue() instanceof File) {
                    try {
                        requestParams.put(entry.getKey(), (File) entry.getValue(), "multipart/form-data");
                    } catch (FileNotFoundException e) {
                        LogUtils.e("", e);
                    }
                } else if (entry.getValue() instanceof InputStream) {
                    requestParams.put(entry.getKey(), (InputStream)entry.getValue(), "1.png", "multipart/form-data");
                }
            }
        }

        return requestParams ;
    }

    private static AsyncHttpResponseHandler createHttpResponseHandler(final ApiCallbackListener<?> listener, final Type type) {
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody) ;
                LogUtils.d("response: \n"+ jsonString);
                if (jsonString.startsWith("{")) {
                    LogUtils.d("response json: \n" + JSON.toJSONString(JSON.parseObject(jsonString), true));
                }

                Gson gson = new Gson() ;
                ApiResponse apiResponse = null ;
                try {
                    apiResponse = gson.fromJson(jsonString, type) ;
                } catch(Exception e) {
                    LogUtils.w("json string parse occur error. the json string is : " + jsonString, e);
                    listener.onResult(new ApiResponse(Api.PARSE_RESPONSE_DATA_ERROR_CODE, Api.PARSE_RESPONSE_DATA_ERROR_CODE_MSG));
                    return ;
                }

                listener.onResult(apiResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                LogUtils.w("statusCode: " + statusCode, error);
                if (null != responseBody) {
                    LogUtils.w("responseBody: " + new String(responseBody));
                }
                listener.onResult(new ApiResponse(Api.TIME_OUT_CODE, Api.TIME_OUT_CODE_MSG));
            }

            @Override
            public void onCancel() {
                super.onCancel();
                LogUtils.w(" the request cancel ... ");
            }
        } ;
    }

}

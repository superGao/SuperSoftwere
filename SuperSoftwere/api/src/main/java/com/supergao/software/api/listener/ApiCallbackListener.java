package com.supergao.software.api.listener;

import com.supergao.software.api.ApiResponse;

/**
 * Api端回调监听接口
 * @param <T> 实际返回对象类型
 * Created by YangJie on 2015/11/10.
 */
public interface ApiCallbackListener<T> {
    /**
     * api访问回调方法
     * @param obj 获取到的对象
     */
    public void onResult(ApiResponse<T> obj) ;
}

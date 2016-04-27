package com.supergao.softwere.core.listener;

/**
 * AppAction调用回调接口
 * @param <T> 返回数据
 * Created by YangJie on 2015/11/10.
 */
public interface ActionCallbackListener<T> {
    /**
     * 成功时调用
     *
     * @param data 返回的数据
     */
    public void onSuccess(T data);

    /**
     * 失败时调用
     *
     * @param code 错误码
     * @param message    错误信息
     */
    public void onFailure(String code, String message);
}
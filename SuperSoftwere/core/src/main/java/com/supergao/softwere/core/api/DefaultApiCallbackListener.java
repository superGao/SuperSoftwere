package com.supergao.softwere.core.api;

import com.supergao.softwere.api.ApiResponse;
import com.supergao.softwere.api.listener.AbstractApiCallbackListener;
import com.supergao.softwere.core.listener.ActionCallbackListener;

/**
 * ApiCallbackListener 默认实现类
 * Created by YangJie on 2015/11/10.
 */
public class DefaultApiCallbackListener<T> extends AbstractApiCallbackListener<T> {
    private ActionCallbackListener<T> callbackListener ;

    public DefaultApiCallbackListener(ActionCallbackListener<T> callbackListener) {
        this.callbackListener = callbackListener ;
    }



    @Override
    public void onResult(ApiResponse<T> obj) {
        if (obj.isSuccess()) {
            callbackListener.onSuccess(obj.getData());
        } else {
            callbackListener.onFailure(obj.getCode(), obj.getMsg());
        }
    }
}
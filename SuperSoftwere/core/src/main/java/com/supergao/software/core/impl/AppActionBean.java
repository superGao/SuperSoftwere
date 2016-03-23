package com.supergao.software.core.impl;

import android.text.TextUtils;

import com.supergao.software.api.Api;
import com.supergao.software.api.ApiResponse;
import com.supergao.software.api.HttpEngine;
import com.supergao.software.api.listener.ApiCallbackListener;
import com.supergao.software.bean.BaseUserInfo;
import com.supergao.software.bean.CaptchaType;
import com.supergao.software.bean.Common;
import com.supergao.software.bean.MessageEntity;
import com.supergao.software.bean.VersionInfo;
import com.supergao.software.bean.logistics.LogisticsInfo;
import com.supergao.software.core.AppAction;
import com.supergao.software.core.api.DefaultApiCallbackListener;
import com.supergao.software.core.listener.ActionCallbackListener;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.support.utils.LogUtils;
import lib.support.utils.StringUtil;

/**
 * 核心业务逻辑接口实现类
 * Created by YangJie on 2015/11/10.
 */
public class AppActionBean implements AppAction {
    @Override
    public void sendCode(String mobile, CaptchaType type, final ActionCallbackListener<Void> callbackListener) {

        if (TextUtils.isEmpty(mobile)) {
            callbackListener.onFailure("-100", "手机号码不能为空！");
            return ;
        } else if (!StringUtil.isMobileType(mobile)) {
            callbackListener.onFailure("-100", "手机号格式不正确！");
            return ;
        }

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("mobile", mobile);
        params.put("type", type.getValue());

        HttpEngine.get(Api.Common.SENDCODE, params, new DefaultApiCallbackListener<Void>(callbackListener));
    }

    /**
     * 用户头像上传
     * @param userId 用户id
     * @param key 授权访问key
     * @param inStream 输入流
     */
    @Override
    public void uploadHead(String userId, String key, InputStream inStream, ActionCallbackListener<Common> callbackListener) {
        Map<String, Object> params = new HashMap<String, Object>(3) ;
        params.put("id", userId) ;
        params.put("key", key) ;
        params.put("head", inStream) ;

        HttpEngine.post(Api.Common.UPLOADHEAD, params, new DefaultApiCallbackListener<Common>(callbackListener), new TypeToken<ApiResponse<Common>>() {
        }.getType());
    }

    @Override
    public void calculateInsuranceAmount(String goodsPrice, final ActionCallbackListener<Common> callbackListener) {
        Map<String, Object> params = new HashMap<String, Object>(1) ;
        params.put("goods_price", goodsPrice) ;

        HttpEngine.get(Api.Common.CALCULATE_INSURANCE_AMOUNT, params, new ApiCallbackListener<Common>() {
            @Override
            public void onResult(ApiResponse<Common> obj) {
                if (obj.isSuccess()) {
                    if (null != obj.getData()) {
                        obj.getData().attach();
                    }
                    callbackListener.onSuccess(obj.getData());
                } else {
                    callbackListener.onFailure(obj.getCode(), obj.getMsg());
                }
            }
        }, new TypeToken<ApiResponse<Common>>() {
        }.getType());
    }

    @Override
    public void getClerkList(String id, String key, ActionCallbackListener<List<BaseUserInfo>> callbackListener) {
        Map<String, Object> params = new HashMap<String, Object>(2) ;
        params.put("uid", id) ;
        params.put("key", key) ;

        HttpEngine.get(Api.User.GET_CLERK_LIST, params, new DefaultApiCallbackListener<List<BaseUserInfo>>(callbackListener),
                new TypeToken<ApiResponse<List<BaseUserInfo>>>() {
                }.getType());
    }

    @Override
    public void login(String mobile, String password, final ActionCallbackListener<BaseUserInfo> callbackListener) {
        // 验证数据的有效性
        if (TextUtils.isEmpty(mobile)) {
            callbackListener.onFailure("-2", "手机号码不能为空！");
            return ;
        } else if (!StringUtil.isMobileType(mobile)) {
            callbackListener.onFailure("-2", "手机号格式不正确！");
            return ;
        }
        // 验证密码
        if (TextUtils.isEmpty(password) || password.trim().length()<6) {
            callbackListener.onFailure("-2", "密码长度不能不少于6位字符！");
            return ;
        }

        // 登录逻辑
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("username", mobile);
        params.put("password", password);

        HttpEngine.post(Api.User.LOGIN, params, new ApiCallbackListener<BaseUserInfo>() {
                    @Override
                    public void onResult(ApiResponse<BaseUserInfo> obj) {
                        if (obj.isSuccess()) {
                            if (null != obj.getData()) {
                                obj.getData().attach();
                            }
                            callbackListener.onSuccess(obj.getData());
                        } else {
                            callbackListener.onFailure(obj.getCode(), obj.getMsg());
                        }
                    }
                },
                new TypeToken<ApiResponse<BaseUserInfo>>() {
                }.getType());
    }

    @Override
    public void flash(String id, String key, final ActionCallbackListener<BaseUserInfo> callbackListener) {
        // 登录逻辑
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("uid", id);
        params.put("key", key);

        HttpEngine.get(Api.User.FLASH, params, new ApiCallbackListener<BaseUserInfo>() {
                    @Override
                    public void onResult(ApiResponse<BaseUserInfo> obj) {
                        if (obj.isSuccess()) {
                            if (null != obj.getData()) {
                                obj.getData().attach();
                            }
                            callbackListener.onSuccess(obj.getData());
                        } else {
                            callbackListener.onFailure(obj.getCode(), obj.getMsg());
                        }
                    }
                },
                new TypeToken<ApiResponse<BaseUserInfo>>() {}.getType());
    }

    /**
     * 普通员工和司机获取二维码信息内容
     * @param id 访问用户id
     * @param key 授权访问key
     * @param callbackListener 回调对象
     */
    public void getIQRCodeContent(String id, String key, final ActionCallbackListener<String> callbackListener) {
        Map<String, Object> params = new HashMap<String, Object>(2) ;
        params.put("uid", id) ;
        params.put("key", key) ;

        /*HttpEngine.get(Api.User.GET_I_QRCODE_CONTENT, params, new DefaultApiCallbackListener<String>(callbackListener),
                new TypeToken<ApiResponse<String>>() {
                }.getType());*/
        HttpEngine.get(Api.User.GET_I_QRCODE_CONTENT, params, new ApiCallbackListener<String>() {
                    @Override
                    public void onResult(ApiResponse<String> obj) {
                        if(obj.isSuccess()){
                            if(null!=obj.getMsg()){
                                obj.getMsg();
                            }
                            callbackListener.onSuccess(obj.getData());
                        }else{
                            callbackListener.onFailure(obj.getCode(),obj.getMsg());
                        }
                    }

                },
                new TypeToken<ApiResponse<String>>(){}.getType());
    }

    /**
     * 网点管理员获取二维码信息
     * @param id 访问用户id
     * @param key 授权访问key
     * @param callbackListener 回调对象
     */
    public void getManagerIQRCodeContent(String id, String key, final ActionCallbackListener<String> callbackListener) {
        Map<String, Object> params = new HashMap<String, Object>(2) ;
        params.put("uid", id) ;
        params.put("key", key) ;

        /*HttpEngine.get(Api.User.GET_I_QRCODE_CONTENT_1, params, new DefaultApiCallbackListener<String>(callbackListener),
                new TypeToken<ApiResponse<String>>() {
                }.getType());*/
        HttpEngine.get(Api.User.GET_I_QRCODE_CONTENT_1, params, new ApiCallbackListener<String>() {
            @Override
            public void onResult(ApiResponse<String> obj) {
                if(obj.isSuccess()){
                    if(null!=obj.getMsg()){
                        obj.getMsg();
                    }
                    callbackListener.onSuccess(obj.getData());
                }else{
                    callbackListener.onFailure(obj.getCode(),obj.getMsg());
                }
            }

        },
        new TypeToken<ApiResponse<String>>(){}.getType());
    }

    @Override
    public void getUserInfo(String id, String key, final ActionCallbackListener<LogisticsInfo> callbackListener) {
        Map<String, Object> params = new HashMap<String, Object>(2) ;
        params.put("uid", id) ;
        params.put("key", key) ;

        HttpEngine.get(Api.User.GET_USER_INFO, params, new ApiCallbackListener<LogisticsInfo>(){
                    @Override
                    public void onResult(ApiResponse<LogisticsInfo> obj) {
                        if (obj.isSuccess()) {
                            if (null != obj.getData()) {
                                obj.getData().attach();
                            }
                            callbackListener.onSuccess(obj.getData());
                        } else {
                            callbackListener.onFailure(obj.getCode(), obj.getMsg());
                        }
                    }
                },
                new TypeToken<ApiResponse<LogisticsInfo>>() {}.getType());
    }

    @Override
    public void resetPwd(String mobilephone, String pwd, String code, CaptchaType type, ActionCallbackListener<Void> callbackListener) {
        if (TextUtils.isEmpty(code)) {
            callbackListener.onFailure("-2", "验证码不能为空！");
            return ;
        } else if (code.trim().length() != 6) {
            callbackListener.onFailure("-2", "验证码长度必须是6位！");
            return ;
        }

        // 验证数据的有效性
        if (TextUtils.isEmpty(mobilephone)) {
            callbackListener.onFailure("-2", "手机号码不能为空！");
            return ;
        } else if (!StringUtil.isMobileType(mobilephone)) {
            callbackListener.onFailure("-2", "手机号格式不正确！");
            return ;
        }
        // 验证密码
        if (TextUtils.isEmpty(pwd) || pwd.trim().length()<6) {
            callbackListener.onFailure("-2", "密码长度不能不少于6位字符！");
            return ;
        }

        Map<String, Object> params = new HashMap<String, Object>(3) ;
        params.put("username", mobilephone) ;
        params.put("password", pwd) ;
        params.put("code", code) ;
        params.put("type", type.getValue()) ; // 验证码类型

        HttpEngine.post(Api.User.RESET_PWD, params, new DefaultApiCallbackListener<Void>(callbackListener));
    }

    @Override
    public void getVersionCode(String version,final ActionCallbackListener<VersionInfo> callbackListener) {
        Map<String, Object> params = new HashMap<String, Object>(2) ;
        params.put("version", version) ;

        HttpEngine.get(Api.Common.CHECK_VERSION_CODE, params, new ApiCallbackListener<VersionInfo>(){
                    @Override
                    public void onResult(ApiResponse<VersionInfo> obj) {
                        if (obj.isSuccess()) {
                            if (null != obj.getData()) {
                                obj.getData();
                            }
                            callbackListener.onSuccess(obj.getData());
                        } else {
                            callbackListener.onFailure(obj.getCode(), obj.getMsg());
                        }
                    }
                },
                new TypeToken<ApiResponse<VersionInfo>>() {}.getType());
    }

    @Override
    public void uploadContent(String id, String key, String content, ActionCallbackListener<Void> callbackListener) {
        Map<String, Object> params = new HashMap<String, Object>(3) ;
        params.put("id", id) ;
        params.put("key", key) ;
        params.put("content", content) ;

        HttpEngine.post(Api.Common.FEEDBACK, params, new DefaultApiCallbackListener<Void>(callbackListener));
    }

    @Override
    public void loadMessage(String uid, String key, Integer condition, Integer p, final ActionCallbackListener<List<MessageEntity>> callbackListener) {
        Map<String, Object> params = new HashMap<String, Object>(4) ;
        params.put("uid", uid) ;
        params.put("key", key) ;
        params.put("conditon", condition.toString()) ;
        params.put("p", p.toString()) ;
        HttpEngine.get(Api.Common.LOADMESSAGE, params, new ApiCallbackListener<List<MessageEntity>>() {
            @Override
            public void onResult(ApiResponse<List<MessageEntity>> obj) {
                if (obj.isSuccess()) {
                    if (null != obj.getData()) {
                        for (MessageEntity oi : obj.getData()) {
                            oi.attach();
                        }
                    }
                    callbackListener.onSuccess(obj.getData());
                } else {
                    callbackListener.onFailure(obj.getCode(), obj.getMsg());
                }
            }
        }, new TypeToken<ApiResponse<List<MessageEntity>>>() {}.getType());

    }

    @Override
    public void readMessage(String uid, String key,String mid,String status, ActionCallbackListener<Void> callbackListener) {
        Map<String, Object> params = new HashMap<String, Object>(4) ;
        params.put("uid", uid) ;
        params.put("key", key) ;
        params.put("mid", mid) ;
        params.put("status", status) ;
        LogUtils.e("url==" + Api.Common.READMESSAGE + "===params===" + params.toString());
        HttpEngine.get(Api.Common.READMESSAGE, params, new DefaultApiCallbackListener<Void>(callbackListener));
    }
}

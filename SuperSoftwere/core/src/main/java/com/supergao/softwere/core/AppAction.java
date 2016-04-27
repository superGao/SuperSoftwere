package com.supergao.softwere.core;

import com.supergao.softwere.bean.BaseUserInfo;
import com.supergao.softwere.bean.CaptchaType;
import com.supergao.softwere.bean.Common;
import com.supergao.softwere.bean.MessageEntity;
import com.supergao.softwere.bean.VersionInfo;
import com.supergao.softwere.bean.logistics.LogisticsInfo;
import com.supergao.softwere.core.listener.ActionCallbackListener;

import java.io.InputStream;
import java.util.List;

/**
 * 核心业务逻辑接口
 * Created by YangJie on 2015/11/10.
 */
public interface AppAction {

    /**
     * 发送验证码
     * @param mobile 手机号码
     * @param type 验证码类型
     */
    void sendCode(String mobile, CaptchaType type, ActionCallbackListener<Void> callbackListener) ;

    /**
     * 用户头像上传
     * @param userId 用户id
     * @param key 授权访问key
     * @param inStream 输入流
     */
    void uploadHead(String userId, String key, InputStream inStream, ActionCallbackListener<Common> callbackListener) ;

    /**
     * 计算保费
     * @param goodsPrice 货物保价
     * @param callbackListener 回调对象
     */
    void calculateInsuranceAmount(String goodsPrice, ActionCallbackListener<Common> callbackListener) ;

    /**
     * 获取集货员列表
     * @param id 访问用户id
     * @param key 授权访问key
     * @param callbackListener 回调对象
     */
    void getClerkList(String id, String key, ActionCallbackListener<List<BaseUserInfo>> callbackListener) ;

    /**
     * 用户登录
     * @param mobile 电话号码
     * @param password 密码
     * @param callbackListener 登录结果回调函数
     */
    public void login(String mobile, String password, ActionCallbackListener<BaseUserInfo> callbackListener) ;

    /**
     * 用户登录数据刷新
     * @param id 访问用户id
     * @param key 授权访问key
     * @param callbackListener 回调对象
     */
    void flash(String id, String key, ActionCallbackListener<BaseUserInfo> callbackListener) ;

    /**
     * 获取二维码信息内容
     * @param id 访问用户id
     * @param key 授权访问key
     * @param callbackListener 回调对象
     */
    public void getIQRCodeContent(String id, String key, ActionCallbackListener<String> callbackListener) ;

    /**
     * 获取二维码信息内容
     * @param id 访问用户id
     * @param key 授权访问key
     * @param callbackListener 回调对象
     */
    public void getManagerIQRCodeContent(String id, String key, ActionCallbackListener<String> callbackListener) ;

    /**
     * 获取 用户信息
     * @param id 访问用户id
     * @param key 授权访问key
     * @param callbackListener 回调对象
     */
    public void getUserInfo(String id, String key, ActionCallbackListener<LogisticsInfo> callbackListener) ;

    /**
     * 重置密码
     * @param mobilephone 电话号码
     * @param pwd 密码
     * @param code 验证码
     * @param type 验证码类型
     * @param callbackListener 回调对象
     */
    void resetPwd(String mobilephone, String pwd, String code, CaptchaType type, ActionCallbackListener<Void> callbackListener) ;

    /**
     * 获取版本信息
     * @param version
     */
    void getVersionCode(String version,ActionCallbackListener<VersionInfo> callbackListener);

    /**
     * 意见反馈
     * @param id
     * @param key
     * @param content
     * @param callbackListener
     */
    void uploadContent(String id, String key,String content,ActionCallbackListener<Void> callbackListener);

    /**
     * 加载消息列表
     * @param uid
     * @param key
     * @param condition
     * @param p
     * @param callbackListener
     */
    void loadMessage(String uid,String key, Integer condition, Integer p,ActionCallbackListener<List<MessageEntity>> callbackListener);

    /**
     * 读取消息
     * @param uid
     * @param key
     * @param status
     * @param callbackListener
     */
    void readMessage(String uid,String key,String mid,String status,ActionCallbackListener<Void> callbackListener);
}

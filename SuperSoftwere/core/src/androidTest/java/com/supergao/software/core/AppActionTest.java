package com.supergao.software.core;

import android.test.InstrumentationTestCase;

import com.supergao.software.bean.BaseUserInfo;
import com.supergao.software.bean.CaptchaType;
import com.supergao.software.core.impl.AppActionBean;
import com.supergao.software.core.listener.ActionCallbackListener;

import org.junit.Test;

import lib.support.utils.LogUtils;

/**
 * 测试
 * Created by YangJie on 2015/11/10.
 */
public class AppActionTest extends InstrumentationTestCase {
    AppAction appAction = new AppActionBean() ;
    /**
     * 测试发送验证码
     */
    public void testSendCode() {
        appAction.sendCode("13911661401", CaptchaType.REGISTER, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                LogUtils.d("success");
            }

            @Override
            public void onFailure(String code, String message) {
                LogUtils.d("code:" + code + ", message:" + message);
            }
        });
    }

    @Test
    public void testLogin() {
        appAction.login("13911661401", "07224042", new ActionCallbackListener<BaseUserInfo>() {
            @Override
            public void onSuccess(BaseUserInfo data) {
                LogUtils.d("success");
            }

            @Override
            public void onFailure(String code, String message) {
                LogUtils.d("code:" + code + ", message:" + message);
            }
        });
    }

}

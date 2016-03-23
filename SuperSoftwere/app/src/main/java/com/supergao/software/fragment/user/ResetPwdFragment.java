package com.supergao.software.fragment.user;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.supergao.software.R;
import com.supergao.software.bean.CaptchaType;
import com.supergao.software.core.listener.DefaultActionCallbackListener;
import com.supergao.software.fragment.ContentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.support.utils.ToastUtil;

/**
 * 重置密码 fragment
 * Created by YangJie on 2015/12/2.
 */
public class ResetPwdFragment extends ContentFragment {
    @Bind(R.id.edt_mobile)
    EditText mobileTxt;
    @Bind(R.id.edt_captcha)
    EditText captchaTxt;
    @Bind(R.id.btn_send_captcha)
    Button sendCaptchaBtn;
    @Bind(R.id.edt_pwd)
    EditText pwdTxt;
    @Bind(R.id.btn_submit)
    Button submitBtn;
    @Bind(R.id.tv_resetpwd_captcha)
    TextView resetpwdCaptchaLabel;

    private Handler handler ;

    private boolean timeOut = false ;

    private int count = 60 ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_reset_pwd, container, false);
        ButterKnife.bind(this, contentView);

        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                count--;
                if (count == 1) {
                    sendCaptchaBtn.setClickable(true);
                    sendCaptchaBtn.setText("获取验证码");
                    timeOut = false;
                    count = 60;
                }
                if (timeOut) {
                    sendCaptchaBtn.setText("再次发送" + "(" + count + ")");
                    handler.sendEmptyMessageDelayed(0, 1000);
                }

            };
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeMessages(0);
        ButterKnife.unbind(this);

    }

    /**
     * 重置密码按钮点击响应
     */
    @OnClick(R.id.btn_submit)
    void onSubmit(View v) {
        // 重置密码
        resetPwd();
    }

    /**
     * 发送验证码点击响应
     */
    @OnClick(R.id.btn_send_captcha)
    void onSendCaptcha(View v) {
        // 发送验证码
        sendCaptcha();
    }

    /**
     * 发送验证码按钮点击事件
     */
    public void sendCaptcha() {
        //获取验证码
        sendCaptchaBtn.setClickable(false);
        timeOut = true;
        handler.sendEmptyMessageDelayed(0, 1000);

        String mobilephone = mobileTxt.getText().toString().trim();

        // 发送验证码
        getApp().getAppAction().sendCode(mobilephone, CaptchaType.UPDATE, new DefaultActionCallbackListener<Void>(getActivity()) {
            @Override
            public void onSuccess(Void data) {
//                ToastUtil.showShort(getActivity(), "发送验证码成功！");
            }

            @Override
            public void onFailure(String code, String message) {
                super.onFailure(code, message);
                if ("-100".equals(code)) {
                    handler.removeMessages(0);
                    sendCaptchaBtn.setText("获取验证码");
                    sendCaptchaBtn.setClickable(true);
                    timeOut = false ;
                    count = 60 ;
                }
            }
        });

    }

    /**
     * 重置密码
     */
    private void resetPwd() {
        String mobilephone = mobileTxt.getText().toString().trim() ;
        String pwd = pwdTxt.getText().toString().trim() ;
        String code = captchaTxt.getText().toString().trim() ;

        showLoadingDialog(R.string.dialog_reset, false);
        getApp().getAppAction().resetPwd(mobilephone, pwd, code, CaptchaType.UPDATE, new DefaultActionCallbackListener<Void>(getActivity()) {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showLong(getActivity(), R.string.toast_reset_pwd_success);
                dismissLoadingDialog();

                getActivity().finish();
            }

            @Override
            public void onAfterFailure() {
                dismissLoadingDialog();
            }
        });
    }

}

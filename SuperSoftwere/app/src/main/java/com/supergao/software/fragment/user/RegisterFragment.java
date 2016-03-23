package com.supergao.software.fragment.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestEmailVerifyCallback;
import com.avos.avoscloud.SignUpCallback;
import com.supergao.software.R;
import com.supergao.software.activity.LicenseActivity;
import com.supergao.software.activity.user.LoginActivity;
import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.utils.AVService;

import lib.support.utils.ToastUtil;


/**
 *
 *@author superGao
 *creat at 2016/3/14
 */
public class RegisterFragment extends ContentFragment implements View.OnClickListener {

    /**
     * 邮箱
     */
    private EditText edtEmail ;

    private EditText edt_username;

    /**
     * 密码框
     */
    private EditText pwdEdt ;

    /**
     * 注册按钮
     */
    private Button registerBtn ;

    /**
     * 服务条款
     */
    private TextView termsOfServiceTxt ;

    /**
     * 交易条款
     */
    private TextView termsOfTradeTxt ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_register, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        registerBtnClickListener() ;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView() {
        edtEmail = (EditText) getView().findViewById(R.id.edt_email) ;
        pwdEdt = (EditText) getView().findViewById(R.id.edt_pwd) ;
        edt_username=(EditText)getView().findViewById(R.id.edt_username);
        registerBtn = (Button) getView().findViewById(R.id.btn_register) ;

        termsOfServiceTxt = (TextView) getView().findViewById(R.id.tv_terms_of_service) ;
        termsOfTradeTxt = (TextView) getView().findViewById(R.id.tv_terms_of_trade) ;
    }

    private void registerBtnClickListener() {
        registerBtn.setOnClickListener(this);

        termsOfServiceTxt.setOnClickListener(this);
        termsOfTradeTxt.setOnClickListener(this);

    }



    /**
     * 注册按钮点击事件
     */
    private void onRegisterBtnClick() {
        String email = edtEmail.getText().toString().trim();
        String userName=edt_username.getText().toString().trim();
        String pwd = pwdEdt.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            ToastUtil.showShort(getActivity(),"请输入邮箱地址");
            return;
        }else if(TextUtils.isEmpty(userName)){
            ToastUtil.showShort(getActivity(),"请输入用户名");
            return;
        }else if(TextUtils.isEmpty(pwd)){
            ToastUtil.showShort(getActivity(),"请输入密码");
            return;
        }
        showLoadingDialog(R.string.dialog_registering, false);
        SignUpCallback signUpCallback = new SignUpCallback() {
            public void done(AVException e) {
                dismissLoadingDialog();
                if (e == null) {
                    gainEmail();
                } else {
                    Log.e("email", String.valueOf(e.getCode()));
                    dismissLoadingDialog();
                    switch (e.getCode()) {
                        case 202:
                            showError("用户名已被注册，请重新填写",getActivity());
                            break;
                        case 203:
                            showError("电子邮箱地址已经被占用",getActivity());
                            break;
                        default:
                            showError("网络错误，请重试",getActivity());
                            break;
                    }
                }
            }
        };
        AVService.signUp(userName, pwd, email, signUpCallback);
    }

    private void gainEmail(){
        String email = edtEmail.getText().toString().trim();
        RequestEmailVerifyCallback emailVerifyCallback=new RequestEmailVerifyCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    dismissLoadingDialog();
                    showRegisterSuccess();
                }else{
                    dismissLoadingDialog();
                    Log.e("email",String.valueOf(e.getCode()));
                    switch (e.getCode()){
                        case 125:
                            showError("电子邮箱地址无效",getActivity());
                            break;
                        case 205:
                            showError("找不到电子邮箱地址对应的用户",getActivity());
                            break;
                        default:
                            showError("网络错误，发送邮件失败",getActivity());
                            break;
                    }
                }
            }
        };
        AVService.gainEmail(email, emailVerifyCallback);
    }

    private void showRegisterSuccess() {
        new AlertDialog.Builder(getActivity())
                .setTitle("温馨提示")
                .setMessage("我们已经给您的邮箱发送了一封邮件，请接收邮件并点击链接完成注册。")
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                dialog.dismiss();
                            }
                        }).show();
    }

    public void showError(String errorMessage, Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("温馨提示")
                .setMessage(errorMessage)
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }


    /**
     * 启动 LicenseActivity
     * @param type 服务协议类型：1：服务条款；2：交易条款
     */
    private void startLicenseActivity(int type) {
        Intent intent = new Intent(getActivity(), LicenseActivity.class) ;
        intent.putExtra("type", type) ;
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register: {
                onRegisterBtnClick();
                break ;
            }
            case R.id.tv_terms_of_service: { // 服务条款
                startLicenseActivity(1);
                break ;
            }
            case R.id.tv_terms_of_trade: { // 交易条款
                startLicenseActivity(2);
                break ;
            }
        }
    }

}

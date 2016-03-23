package com.supergao.software.fragment.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.supergao.software.entity.AppConfig;
import com.supergao.software.R;
import com.supergao.software.entity.UserInfo;
import com.supergao.software.activity.MainActivity;
import com.supergao.software.activity.user.ResetPwdActivity;
import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.utils.AVService;
import com.supergao.software.utils.DoCacheUtil;

import lib.support.utils.ToastUtil;

/**
 *
 *@author superGao
 *creat at 2016/3/15
 */
public class LoginFragment extends ContentFragment implements View.OnClickListener {

    /**
     * 电话号码
     */
    private EditText edtUserName ;

    /**
     * 密码
     */
    private EditText pwdEdt ;

    /**
     * 忘记密码
     */
    private TextView forgetPwdTxt ;

    private DoCacheUtil doCacheUtil ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView() ;
        registerClickListener() ;

        // 缓存数据保存
        doCacheUtil = DoCacheUtil.get(getActivity()) ;
        if("isReLogin".equals(getActivity().getIntent().getStringExtra("isReLogin"))){
            Toast.makeText(getActivity(), "当前帐号已在其他设备登录,请您重新登录!", Toast.LENGTH_LONG).show();
        }
    }
    private void initView() {
        edtUserName = (EditText) getView().findViewById(R.id.edt_username) ;
        pwdEdt = (EditText) getView().findViewById(R.id.edt_pwd) ;
        forgetPwdTxt = (TextView) getView().findViewById(R.id.tv_forget_pwd) ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                String username = edtUserName.getText().toString().trim() ;
                String pwd = pwdEdt.getText().toString().trim() ;
                if(TextUtils.isEmpty(username)){
                    ToastUtil.showShort(getActivity(),"亲，没有输入用户名哦！");
                    return;
                }else if(TextUtils.isEmpty(pwd)){
                    ToastUtil.showShort(getActivity(),"你彪啊？密码都没输！");
                    return;
                }
                doLogin(username,pwd);
                break ;
            }
            case R.id.tv_forget_pwd: { // 忘记密码
                doForgetPwd() ;
                break ;
            }
        }
    }

    private void registerClickListener() {
        getView().findViewById(R.id.btn_login).setOnClickListener(this);

        forgetPwdTxt.setOnClickListener(this);
    }

    /**
     * 忘记密码
     */
    private void doForgetPwd() {
        Intent intent = new Intent(getActivity(), ResetPwdActivity.class) ;
        startActivity(intent);
    }

    /**
     * 登录
     */
    private void doLogin(String username,String pwd) {
        showLoadingDialog(R.string.dialog_logining, false);
        LogInCallback<AVUser> logInCallback=new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                //登录成功
                dismissLoadingDialog();
                if(e==null){
                    AppConfig.avUser=avUser;
                    AppConfig.userInfo= AVUser.cast(avUser, UserInfo.class);
                    AppConfig.userInfo.setHeader(avUser.getString("portrait"));
                    //将新加的字段加入缓存中
                    DoCacheUtil.get(getActivity()).put("header",AppConfig.userInfo.getHeader(),DoCacheUtil.TIME_DAY);
                    Log.d("user", doCacheUtil.getAsString("header"));
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }else{
                    Log.d("userinfo"," "+e.getCode());
                    switch (e.getCode()){
                        case 216:
                            showError("邮箱未验证,请查收邮件并完成验证。",getActivity());
                            break;
                        case 217:
                            showError("无效的密码，不允许空白密码",getActivity());
                            break;
                        case 126:
                            showError("该用户不存在，请注册。",getActivity());
                            break;
                        case 211:
                            showError("找不到该用户，请注册。",getActivity());
                            break;
                        default:
                            showError("网络错误，发送邮件失败",getActivity());
                            break;
                    }
                }


            }
        };
        AVService.login(username, pwd, logInCallback);
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

}

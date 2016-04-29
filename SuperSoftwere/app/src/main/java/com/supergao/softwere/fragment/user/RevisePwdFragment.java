package com.supergao.softwere.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.supergao.softwere.R;
import com.supergao.softwere.activity.user.LoginActivity;
import com.supergao.softwere.entity.AppConfig;
import com.supergao.softwere.fragment.ContentFragment;
import com.supergao.softwere.handler.MessageDefine;
import com.supergao.softwere.handler.MessageManager;
import com.supergao.softwere.utils.AVService;
import com.supergao.softwere.utils.Log;
import com.supergao.softwere.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *重置密码 fragment
 *@author superGao
 *creat at 2016/3/28
 */
public class RevisePwdFragment extends ContentFragment {
    @Bind(R.id.edt_oldPwd)
    EditText edtOldPwd;
    @Bind(R.id.edt_newPwd)
    EditText edtNewPwd;
    @Bind(R.id.edt_confirmPwd)
    EditText edtConfirmPwd;
    @Bind(R.id.btn_submit)
    Button submitBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_revise_pwd, container, false);
        ButterKnife.bind(this, contentView);

        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    /**
     * 修改密码按钮点击响应
     */
    @OnClick(R.id.btn_submit)
    void onSubmit(View v) {
        // 修改密码
        resetPwd();
    }

    /**
     * 修改密码
     */
    private void resetPwd() {
        final String oldPwd = edtOldPwd.getText().toString().trim() ;
        final String newPwd = edtNewPwd.getText().toString().trim() ;
        String confirPwd = edtConfirmPwd.getText().toString().trim() ;
        if(TextUtils.isEmpty(oldPwd)){
            ToastUtil.showShort(getActivity(),"旧密码不能为空，请重新输入");
            return;
        }
        if(TextUtils.isEmpty(newPwd)){
            ToastUtil.showShort(getActivity(),"新密码不能为空，请重新输入");
            return;
        }
        if(!newPwd.equals(confirPwd)){
            ToastUtil.showShort(getActivity(),"两次输入的密码不一致，请重新输入");
            return;
        }
        showLoadingDialog(R.string.dialog_revise, false);
        final UpdatePasswordCallback updatePasswordCallback=new UpdatePasswordCallback() {
            @Override
            public void done(AVException e) {
                dismissLoadingDialog();
                if(e==null){
                    ToastUtil.showShort(getActivity(),"密码修改成功，请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else{
                    switch (e.getCode()){
                        default:
                            ToastUtil.showShort(getActivity(),"网络连接失败，请重试");
                            break;
                    }
                    Log.d("resetError",e.getMessage()+"   code:"+e.getCode());
                }
            }
        };
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AVService.revisePassword(AppConfig.userInfo.getUsername(), oldPwd,newPwd,updatePasswordCallback);
                        } catch (AVException e) {
                            dismissLoadingDialog();
                            switch (e.getCode()){
                                case 210:
                                    MessageManager.sendMessage(handler, MessageDefine.PWDERROR, null);
                                    break;
                                default:
                                    MessageManager.sendMessage(handler, MessageDefine.NetworkError, null);
                                    break;
                            }
                            e.printStackTrace();
                        }
                    }
                }
        ).start();

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageDefine.PWDERROR:
                    ToastUtil.showShort(getActivity(),"旧密码错误，请重试");
                    break;
                case MessageDefine.NetworkError:
                    ToastUtil.showShort(getActivity(), "网络连接失败，请重试");
                    break;
            }
        }
    };

}

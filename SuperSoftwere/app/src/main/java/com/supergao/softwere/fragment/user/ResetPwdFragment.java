package com.supergao.softwere.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.supergao.softwere.R;
import com.supergao.softwere.activity.user.LoginActivity;
import com.supergao.softwere.fragment.ContentFragment;
import com.supergao.softwere.utils.AVService;
import com.supergao.softwere.utils.Log;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.supergao.softwere.utils.ToastUtil;

/**
 *重置密码 fragment
 *@author superGao
 *creat at 2016/3/28
 */
public class ResetPwdFragment extends ContentFragment {
    @Bind(R.id.edt_email)
    EditText edtEmail;
    @Bind(R.id.btn_submit)
    Button submitBtn;

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
    public void onDestroyView() {
        super.onDestroyView();
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
     * 重置密码
     */
    private void resetPwd() {
        String email = edtEmail.getText().toString().trim() ;
        if(TextUtils.isEmpty(email)){
            ToastUtil.showShort(getActivity(),"电子邮箱地址不能为空，请重新输入");
            return;
        }
        showLoadingDialog(R.string.dialog_reset, false);
        RequestPasswordResetCallback resetPassword=new RequestPasswordResetCallback() {
            @Override
            public void done(AVException e) {
                dismissLoadingDialog();
                if(e==null){
                    ToastUtil.showShort(getActivity(),"我们已向该邮箱发送了一封修改密码的邮件，请前往修改。");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else{
                    switch (e.getCode()){
                        case 205:
                            ToastUtil.showShort(getActivity(),"找不到电子邮箱地址对应的用户");
                            break;
                        case 125:
                            ToastUtil.showShort(getActivity(),"电子邮箱地址无效");
                            break;
                        default:
                            ToastUtil.showShort(getActivity(),"网络连接失败，请重试");
                            break;
                    }
                    Log.d("resetError",e.getMessage()+"   code:"+e.getCode());
                }
            }
        };
        AVService.resetPassword(email,resetPassword);

    }

}

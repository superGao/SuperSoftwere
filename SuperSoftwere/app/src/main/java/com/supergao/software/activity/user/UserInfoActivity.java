package com.supergao.software.activity.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.supergao.software.R;
import com.supergao.software.activity.BaseSingleFragmentActivity;
import com.supergao.software.entity.AppConfig;
import com.supergao.software.entity.UserInfo;
import com.supergao.software.fragment.ContentFragment;
import com.supergao.software.fragment.user.UserInfoFragment;
import com.supergao.software.utils.AVService;

import lib.support.utils.ToastUtil;

/**
 * 登录用户信息Activity
 * Created by YangJie on 2015/11/21.
 */
public class UserInfoActivity extends BaseSingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText(R.string.title_userinfo);
        setRightButtonVisibility(View.VISIBLE);
        setRightButton(R.string.btn_perfect_info, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserName();
            }
        });

    }

    /**
     * 修改用户名
     * 已登录用户第一次修改用户名成功，再次修改就报错206(未登录)，需要再次登录才能修改
     */
    private void updateUserName(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                String id=AppConfig.userInfo.getObjectId();
                Log.d("userID",id);
                final String userName=UserInfoFragment.realnameEdt.getText().toString().trim();
                SaveCallback saveCallback=new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            ToastUtil.showShort(UserInfoActivity.this,"更改用户名成功");
                            AppConfig.avUser.setUsername(userName);
                        } else {
                            switch (e.getCode()) {
                                case 202:
                                    showError("用户名已经被占用，请重新填写", UserInfoActivity.this);
                                    break;
                                default:
                                    showError(e.getMessage()+"  "+e.getCode(),UserInfoActivity.this);
                                    break;
                            }
                        }
                    }
                };
                try {
                    if(!TextUtils.isEmpty(userName)){
                        AVService.updateName(AppConfig.userInfo.getObjectId(),userName,saveCallback);
                    }else {
                        showError("用户名不能为空",UserInfoActivity.this);
                    }
                } catch (AVException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    @Override
    protected ContentFragment createContentFragment() {
        return new UserInfoFragment();
    }
}

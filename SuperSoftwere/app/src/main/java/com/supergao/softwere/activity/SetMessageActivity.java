package com.supergao.softwere.activity;

import android.os.Bundle;

import com.avoscloud.leanchatlib.activity.AVBaseActivity;
import com.supergao.softwere.R;
/**
 *
 *@author superGao
 *creat at 2016/3/14
 */
public class SetMessageActivity extends AVBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_setting_notify_layout);
        setTitle(R.string.profile_notifySetting);
    }

}





/*public class SetMessageActivity extends BaseSingleFragmentActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setTitleText("消息设置");
    }

    @Override
    protected ContentFragment createContentFragment() {
        return new SetMessageFragment();
    }
}*/

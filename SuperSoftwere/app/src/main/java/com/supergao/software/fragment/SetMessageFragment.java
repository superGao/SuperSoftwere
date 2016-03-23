package com.supergao.software.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.supergao.software.entity.AppConfig;
import com.supergao.software.R;
import com.tencent.android.tpush.XGBasicPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消息设置fragment
 */
public class SetMessageFragment extends ContentFragment implements View.OnClickListener{
    private RelativeLayout rlAllTimeReceiver;
    private ImageView allTimeReceiver;
    private RelativeLayout rlNoTimeReceiver;
    private ImageView noAllTimeReceiver;
    private RelativeLayout rlNoReceiver;
    private ImageView noReceiverMessage;
    private XGBasicPushNotificationBuilder build;
    private TextView rightText;
    private Button btn_save;
    public static final String IS_RECEIVER_MESSAGE = "isReceiverMessage";
    public static final String IS_VOICE = "isVoice";
    public static final String IS_BRATION = "isBration";

    private String receiver = "1";   // 1代表接收消息 	2 分时段接受  3 不接收消息
    private boolean isVoice = false;    //true 代表有声音  false代表 没有声音
    private boolean isVibration = false;    //震动
    private String spReceiverValue;
    private boolean spIsVoiceValue;
    private boolean spIsBrationValue;
    private int time;        //当前时间
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.activity_set_message, container, false);
        initView(contentView) ;
        initData();
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        rlAllTimeReceiver = (RelativeLayout)view.findViewById(R.id.rl_all_time_receiver);
        allTimeReceiver = (ImageView)view.findViewById(R.id.all_time_receiver);
        rlNoTimeReceiver = (RelativeLayout) view.findViewById(R.id.rl_no_time_receiver);
        noAllTimeReceiver = (ImageView) view.findViewById(R.id.no_all_time_receiver);
        rlNoReceiver = (RelativeLayout) view.findViewById(R.id.rl_no_receiver);
        noReceiverMessage = (ImageView) view.findViewById(R.id.no_receiver_message);
        btn_save=(Button)view.findViewById(R.id.btn_save);

        rlAllTimeReceiver.setOnClickListener(this);
        rlNoTimeReceiver.setOnClickListener(this);
        rlNoReceiver.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        build=new XGBasicPushNotificationBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String data = formatter.format(curDate);
        time = Integer.parseInt(data);
        SharedPreferences sharePreference = null;
        if (sharePreference == null)
            sharePreference = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        spReceiverValue = sharePreference.getString(IS_RECEIVER_MESSAGE, "1");
        spIsVoiceValue = sharePreference.getBoolean(IS_VOICE, true);
        spIsBrationValue = sharePreference.getBoolean(IS_BRATION, true);
        switch (Integer.parseInt(spReceiverValue)) {
            case 1://全时段接收推送
                allTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_after);
                noAllTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_befor);
                noReceiverMessage.setImageResource(R.drawable.icon_set_message_radio_befor);
                receiver = "1";
                initPush();
                break;
            case 2://8-22点接收推送
                allTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_befor);
                noAllTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_after);
                noReceiverMessage.setImageResource(R.drawable.icon_set_message_radio_befor);
                receiver = "2";
                if (time >= 8 && time <= 22) {
                    initPush();
                }
                break;
            case 3://不接收推送
                allTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_befor);
                noAllTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_befor);
                noReceiverMessage.setImageResource(R.drawable.icon_set_message_radio_after);
                receiver = "3";
                XGPushManager.unregisterPush(getActivity());            //反注册 调用该接口后 app将停止接受通知消息
                break;
            default:
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_all_time_receiver:        //全天接收消息
                allTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_after);
                noAllTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_befor);
                noReceiverMessage.setImageResource(R.drawable.icon_set_message_radio_befor);
                receiver = "1";
                initPush();
                break;
            case R.id.rl_no_time_receiver:        //8----22点 接收消息
                allTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_befor);
                noAllTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_after);
                noReceiverMessage.setImageResource(R.drawable.icon_set_message_radio_befor);
                receiver = "2";
                if (time >= 8 && time <= 22) {
                    initPush();
                }
                break;
            case R.id.rl_no_receiver:        //全天不接收消息
                allTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_befor);
                noAllTimeReceiver.setImageResource(R.drawable.icon_set_message_radio_befor);
                noReceiverMessage.setImageResource(R.drawable.icon_set_message_radio_after);
                receiver = "3";
                XGPushManager.unregisterPush(getActivity());            //反注册 调用该接口后 app将停止接受通知消息
                break;
            case R.id.btn_save:   //保存操作
                showLoadingDialog("正在保存设置");
                setMessageStatusValue(receiver, isVoice, isVibration);
                break;
            default:
                break;
        }
    }
    void setMessageStatusValue(String spValue, boolean voice, boolean vibration) {
        SharedPreferences sharePreference = null;
        if (sharePreference == null)
            sharePreference = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePreference.edit();
        editor.putString(IS_RECEIVER_MESSAGE, spValue);
        editor.putBoolean(IS_VOICE, voice);
        editor.putBoolean(IS_BRATION, vibration);
        editor.commit();
        dismissLoadingDialog();
        Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    void initPush() {

        // 开启logcat输出，方便debug，发布时请关闭
        // XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext

        Context context = getActivity().getApplicationContext();
        XGPushManager.registerPush(context, "xg_" + AppConfig.getUserId(), new XGIOperateCallback() {

            @Override
            public void onSuccess(Object arg0, int arg1) {
                Log.e("Login", "onSuccess->xg_" + AppConfig.getUserId() + "," + arg1 + "," + arg0);
            }

            @Override
            public void onFail(Object arg0, int arg1, String arg2) {
                // TODO Auto-generated method stub
                Log.e("Login", "onFail->xg_" + AppConfig.getUserId() + "," + arg1 + "," + arg2);
            }
        });

        // 2.36（不包括）之前的版本需要调用以下2行代码
        Intent service = new Intent(context, XGPushService.class);
        context.startService(service);


        // 其它常用的API：
        // 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
        // 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
        // 反注册（不再接收消息）：unregisterPush(context)
        // 设置标签：setTag(context, tagName)
        // 删除标签：deleteTag(context, tagName)
    }
}

package com.supergao.softwere.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.avoscloud.leanchatlib.utils.Constants;
import com.avoscloud.leanchatlib.utils.LogUtils;
import com.avoscloud.leanchatlib.utils.NotificationUtils;
import com.supergao.softwere.R;
import com.supergao.softwere.event.InvitationEvent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 *自定义推送逻辑
 *@author superGao
 *creat at 2016/4/15
 */
public class LeanchatReceiver extends BroadcastReceiver {

  public final static String AVOS_DATA = "com.avoscloud.Data";

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (!TextUtils.isEmpty(action)) {
      if (action.equals(context.getString(R.string.invitation_action))) {
        String avosData = intent.getStringExtra(AVOS_DATA);
        if (!TextUtils.isEmpty(avosData)) {
          try {
            //获取消息内容
            JSONObject json = new JSONObject(avosData);
            if (null != json) {
              String alertStr = json.getString(PushManager.AVOS_ALERT);

              Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
              notificationIntent.putExtra(Constants.NOTOFICATION_TAG, Constants.NOTIFICATION_SYSTEM);
              NotificationUtils.showNotification(context, "SuperSoftwere", alertStr, notificationIntent);
            }
          } catch (JSONException e) {
            LogUtils.logException(e);
          }
        }
      }
    }
    EventBus.getDefault().post(new InvitationEvent());
  }
}
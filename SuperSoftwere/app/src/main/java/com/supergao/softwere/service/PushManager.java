package com.supergao.softwere.service;

import android.content.Context;
import android.text.TextUtils;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.PushService;
import com.supergao.softwere.activity.WelcomeActivity;
import com.supergao.softwere.entity.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *@author superGao
 *creat at 2016/4/6
 */
public class PushManager {
  public final static String AVOS_ALERT = "alert";

  private final static String AVOS_PUSH_ACTION = "action";
  public static final String INSTALLATION_CHANNELS = "channels";
  private static PushManager pushManager;
  private Context context;

  public synchronized static PushManager getInstance() {
    if (pushManager == null) {
      pushManager = new PushManager();
    }
    return pushManager;
  }

  public void init(Context context) {
    this.context = context;
    PushService.setDefaultPushCallback(context, WelcomeActivity.class);
    subscribeCurrentUserChannel();
  }

  private void subscribeCurrentUserChannel() {
    String currentUserId = UserInfo.getCurrentUserId();
    if (!TextUtils.isEmpty(currentUserId)) {
      PushService.subscribe(context, currentUserId, WelcomeActivity.class);
    }
  }

  public void unsubscribeCurrentUserChannel() {
    String currentUserId = UserInfo.getCurrentUserId();
    if (!TextUtils.isEmpty(currentUserId)) {
      PushService.unsubscribe(context, currentUserId);
    }
  }

  public void pushMessage(String userId, String message, String action) {
    AVQuery query = AVInstallation.getQuery();
    query.whereContains(INSTALLATION_CHANNELS, userId);
    AVPush push = new AVPush();
    push.setQuery(query);

    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put(AVOS_ALERT, message);
    dataMap.put(AVOS_PUSH_ACTION, action);
    push.setData(dataMap);
    push.sendInBackground();
  }
}
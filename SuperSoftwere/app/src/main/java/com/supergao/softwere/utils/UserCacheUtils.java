package com.supergao.softwere.utils;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avoscloud.leanchatlib.utils.Constants;
import com.supergao.softwere.entity.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wli on 15/9/30.
 * TODO
 * 1、本地存储
 * 2、避免内存、外存占用过多
 */
public class UserCacheUtils {

  private static Map<String, UserInfo> userMap;

  static {
    userMap = new HashMap<String, UserInfo>();
  }

  public static UserInfo getCachedUser(String objectId) {
    return userMap.get(objectId);
  }

  public static boolean hasCachedUser(String objectId) {
    return userMap.containsKey(objectId);
  }

  public static void cacheUser(UserInfo user) {
    if (null != user && !TextUtils.isEmpty(user.getObjectId())) {
      userMap.put(user.getObjectId(), user);
    }
  }

  public static void cacheUsers(List<UserInfo> users) {
    if (null != users) {
      for (UserInfo user : users) {
        cacheUser(user);
      }
    }
  }

  public static void fetchUsers(List<String> ids) {
    fetchUsers(ids, null);
  }

  public static void fetchUsers(final List<String> ids, final CacheUserCallback cacheUserCallback) {
    Set<String> uncachedIds = new HashSet<String>();
    for (String id : ids) {
      if (!userMap.containsKey(id)) {
        uncachedIds.add(id);
      }
    }

    if (uncachedIds.isEmpty()) {
      if (null != cacheUserCallback) {
        cacheUserCallback.done(getUsersFromCache(ids), null);
        return;
      }
    }

    AVQuery<UserInfo> q = UserInfo.getQuery(UserInfo.class);
    q.whereContainedIn(Constants.OBJECT_ID, uncachedIds);
    q.setLimit(1000);
    q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    q.findInBackground(new FindCallback<UserInfo>() {
      @Override
      public void done(List<UserInfo> list, AVException e) {
        if (null == e) {
          for (UserInfo user : list) {
            userMap.put(user.getObjectId(), user);
          }
        }
        if (null != cacheUserCallback) {
          cacheUserCallback.done(getUsersFromCache(ids), e);
        }
      }
    });
  }

  public static List<UserInfo> getUsersFromCache(List<String> ids) {
    List<UserInfo> userList = new ArrayList<UserInfo>();
    for (String id : ids) {
      if (userMap.containsKey(id)) {
        userList.add(userMap.get(id));
      }
    }
    return userList;
  }

  public static abstract class CacheUserCallback {
    public abstract void done(List<UserInfo> userList, Exception e);
  }
}

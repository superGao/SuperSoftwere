package com.supergao.softwere.friends;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.supergao.softwere.entity.UserInfo;
import com.supergao.softwere.utils.UserCacheUtils;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *@author superGao
 *creat at 2016/3/29
 */
public class FriendsManager {

  private static volatile List<String> friendIds = new ArrayList<String>();


  public static List<String> getFriendIds() {
    return friendIds;
  }

  public static void setFriendIds(List<String> friendList) {
    friendIds.clear();
    if (friendList != null) {
      friendIds.addAll(friendList);
    }
  }

  public static void fetchFriends(boolean isForce, final FindCallback<UserInfo> findCallback) {
    AVQuery.CachePolicy policy =
      (isForce ? AVQuery.CachePolicy.NETWORK_ELSE_CACHE : AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
    UserInfo.getCurrentUser().findFriendsWithCachePolicy(policy, new FindCallback<UserInfo>() {
      @Override
      public void done(List<UserInfo> list, AVException e) {
        if (null != e) {
          findCallback.done(null, e);
        } else {
          final List<String> userIds = new ArrayList<String>();
          for (UserInfo user : list) {
            userIds.add(user.getObjectId());
          }
          UserCacheUtils.fetchUsers(userIds, new UserCacheUtils.CacheUserCallback() {
            @Override
            public void done(List<UserInfo> list1, Exception e) {
              setFriendIds(userIds);
              findCallback.done(list1, null);
            }
          });

        }
      }
    });
  }
}

package com.supergao.softwere.friends;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.supergao.softwere.entity.UserInfo;

/**
 * Created by lzw on 14-9-27.
 */
@AVClassName("AddRequest")
public class AddRequest extends AVObject {
  public static final int STATUS_WAIT = 0;
  public static final int STATUS_DONE = 1;

  public static final String FROM_USER = "fromUser";
  public static final String TO_USER = "toUser";
  public static final String STATUS = "status";

  /**
   * 标记接收方是否已读该消息
   */
  public static final String IS_READ = "isRead";

  public AddRequest() {
  }

  public UserInfo getFromUser() {
    return getAVUser(FROM_USER, UserInfo.class);
  }

  public void setFromUser(UserInfo fromUser) {
    put(FROM_USER, fromUser);
  }

  public UserInfo getToUser() {
    return getAVUser(TO_USER, UserInfo.class);
  }

  public void setToUser(UserInfo toUser) {
    put(TO_USER, toUser);
  }

  public int getStatus() {
    return getInt(STATUS);
  }

  public void setStatus(int status) {
    put(STATUS, status);
  }

  public boolean isRead() {
    return getBoolean(IS_READ);
  }

  public void setIsRead(boolean isRead) {
    put(IS_READ, isRead);
  }
}

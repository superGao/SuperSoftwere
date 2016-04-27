package com.supergao.softwere.event;


import com.supergao.softwere.friends.AddRequest;

/**
 *
 *@author superGao
 *creat at 2016/4/8
 */
public class NewFriendItemClickEvent {
  public AddRequest addRequest;
  public boolean isLongClick;
  public NewFriendItemClickEvent(AddRequest request, boolean isLongClick) {
    addRequest = request;
    this.isLongClick = isLongClick;
  }
}

package com.supergao.software.utils;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.RequestEmailVerifyCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.supergao.software.activity.user.LoginActivity;
import com.supergao.software.entity.UserInfo;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *@author superGao
 *creat at 2016/3/15
 */
public class AVService {
  public static void countDoing(String doingObjectId, CountCallback countCallback) {
    AVQuery<AVObject> query = new AVQuery<AVObject>("DoingList");
    query.whereEqualTo("doingListChildObjectId", doingObjectId);
    Calendar c = Calendar.getInstance();
    c.add(Calendar.MINUTE, -10);
    // query.whereNotEqualTo("userObjectId", userId);
    query.whereGreaterThan("createdAt", c.getTime());
    query.countInBackground(countCallback);
  }

  //Use callFunctionMethod
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static void getAchievement(String userObjectId) {
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("userObjectId", userObjectId);
    AVCloud.callFunctionInBackground("hello", parameters,
            new FunctionCallback() {
              @Override
              public void done(Object object, AVException e) {
                if (e == null) {
                  Log.e("at", object.toString());// processResponse(object);
                } else {
                  // handleError();
                }
              }
            });
  }

  public static void createDoing(String userId, String doingObjectId) {
    AVObject doing = new AVObject("DoingList");
    doing.put("userObjectId", userId);
    doing.put("doingListChildObjectId", doingObjectId);
    doing.saveInBackground();
  }

  public static void requestPasswordReset(String email, RequestPasswordResetCallback callback) {
    AVUser.requestPasswordResetInBackground(email, callback);
  }

  public static void findDoingListGroup(FindCallback<AVObject> findCallback) {
    AVQuery<AVObject> query = new AVQuery<AVObject>("DoingListGroup");
    query.orderByAscending("Index");
    query.findInBackground(findCallback);
  }

  public static void findChildrenList(String groupObjectId, FindCallback<AVObject> findCallback) {
    AVQuery<AVObject> query = new AVQuery<AVObject>("DoingListChild");
    query.orderByAscending("Index");
    query.whereEqualTo("parentObjectId", groupObjectId);
    query.findInBackground(findCallback);
  }

  public static void initPushService(Context ctx) {
    PushService.setDefaultPushCallback(ctx, LoginActivity.class);
    PushService.subscribe(ctx, "public", LoginActivity.class);
    AVInstallation.getCurrentInstallation().saveInBackground();
  }

  /**
   * 发送邮件
   * @param email
   * @param emailVerifyCallback
   */
  public static void gainEmail(String email, RequestEmailVerifyCallback emailVerifyCallback){
    AVUser.requestEmailVerfiyInBackground(email, emailVerifyCallback);
  }

  /**
   * 用户注册
   * @param username
   * @param password
   * @param email
   * @param signUpCallback
   */
  public static void signUp(String username, String password, String email, SignUpCallback signUpCallback) {
    UserInfo user = new UserInfo();
    user.setUsername(username);
    user.setPassword(password);
    user.setEmail(email);
    //user.setPortrait("888888"); //向数据库中自定义字段上传数据
    user.signUpInBackground(signUpCallback);
  }

  /**
   * 用户登录
   * @param username
   * @param password
   * @param logInCallback
   */
  public static void login(String username,String password,LogInCallback<AVUser> logInCallback){
    AVUser.logInInBackground(username, password, logInCallback);

  }

  public static void logout() {
    AVUser.logOut();
  }

  public static void createAdvice(String userId, String advice, SaveCallback saveCallback) {
    AVObject doing = new AVObject("SuggestionByUser");
    doing.put("UserObjectId", userId);
    doing.put("UserSuggestion", advice);
    doing.saveInBackground(saveCallback);
  }

  /**
   * 加载广告图
   * @param findCallback
   */
  public static void loadPicture(FindCallback<AVObject> findCallback){
    AVQuery<AVObject> query=new AVQuery<AVObject>("_File");
    query.whereContains("name", "png");//查询字段name中所有包含png的所有数据
    //query.getInBackground("56ea0c48b01460002933987f",getCallback);//查询指定objectId的值
    query.findInBackground(findCallback);
  }

  public static void loadPicture(CloudQueryCallback<AVCloudQueryResult> cloudQueryCallback){
    String sql="select url from _File";
    AVQuery.doCloudQueryInBackground(sql, cloudQueryCallback);
  }

  /**
   * 修改用户名
   * @param userId
   * @param userName
   * @param saveCallback
   * @throws AVException
   */
  public static void updateName(String userId,String userName,SaveCallback saveCallback) throws AVException {
    String tableName = "_User";
    AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
    AVObject post = new AVObject(tableName);
    post = query.get(userId);
    post.put("username", userName);
    post.saveInBackground(saveCallback);
  }
}

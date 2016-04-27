package com.supergao.softwere.utils;
import android.content.Context;
import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.RequestEmailVerifyCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.supergao.softwere.activity.user.LoginActivity;
import com.supergao.softwere.entity.UserInfo;

import java.io.IOException;
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
    // query.whereNotEqualTo("userObjectId", userId);//881386398557315438
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
    String sql="select url from _File where mime_type is not exists";//is exists
    AVQuery.doCloudQueryInBackground(sql, cloudQueryCallback);
  }

  /**
   * 修改用户名
   * 也可使用sql语句进行操作eg：update GameScore set score=90 where objectId='558e20cbe4b060308e3eb36c'
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

  /**
   * 上传用户头像
   * @param fileName
   * @param bytes
   * @param saveCallback
   */
  public static void updateHeader(String fileName,byte [] bytes,String userId,SaveCallback saveCallback,ProgressCallback progressCallback) throws IOException {
    /*AVFile avFile = new AVFile(fileName, bytes);//第一个参数文件名，第二个参数文件byte数组
    avFile.saveInBackground(saveCallback,progressCallback);
    AVObject avObject = new AVObject("_File");
    avObject.put("attached", avFile);
    avObject.saveInBackground();*/

    AVFile avFile = new AVFile(fileName,bytes);
    avFile.saveInBackground(saveCallback, progressCallback);
    AVObject avObject = new AVObject("Picture");
    avObject.setFetchWhenSave(true);//获取更新字段在服务器上的最新结果
    avObject.put("content", avFile);
    avObject.put("userId",userId);
    //avObject.put("objectId", AVObject.createWithoutData("Picture", userId));
    avObject.saveInBackground();
  }

  /**
   * 加载用户头像
   * @param userId
   * @param getDataCallback
   */
  public static void loadHeader(String userId,GetDataCallback getDataCallback) throws AVException {
    AVQuery<AVObject> query = new AVQuery<AVObject>("Picture");
    query.whereEqualTo("userId", userId);//获取指定用户数据
    AVObject avObject = new AVObject("Picture");
    avObject=query.getFirst();  //获取该用户最新的一条数据
    AVFile avFile;
    if(null!=avObject){
      avFile= avObject.getAVFile("content");
    }else{
      query.whereEqualTo("userId", "defaultAvatar");//获取默认图片
      avObject = new AVObject("Picture");
      avObject=query.getFirst();
      avFile= avObject.getAVFile("content");
    }
    avFile.getDataInBackground(getDataCallback);
  }

  /**
   * 获取app版本信息
   * @param findCallback
   */
  public static void loadVersion(GetCallback<AVObject> findCallback){
    AVQuery<AVObject> query = new AVQuery<AVObject>("AppVersion");
    query.getFirstInBackground(findCallback);
  }

  /**
   * 忘记密码后重置密码
   * @param email
   * @param requestPasswordResetCallback
   */
  public static void resetPassword(String email,RequestPasswordResetCallback requestPasswordResetCallback){
    AVUser.requestPasswordResetInBackground(email, requestPasswordResetCallback);
  }

  /**
   * 用户登录状态修改密码
   * @param userName
   * @param oldPwd
   * @param newPwd
   * @param updatePasswordCallback
   * @throws AVException
   */
  public static void revisePassword(String userName,String oldPwd,String newPwd ,UpdatePasswordCallback updatePasswordCallback) throws AVException {
    AVUser userA = AVUser.logIn(userName, oldPwd);//请确保用户当前的有效登录状态
    userA.updatePasswordInBackground(oldPwd,newPwd,updatePasswordCallback);
  }

}

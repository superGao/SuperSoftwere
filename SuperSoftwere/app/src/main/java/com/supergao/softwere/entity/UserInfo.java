package com.supergao.softwere.entity;

import android.content.Context;
import android.graphics.Bitmap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.supergao.softwere.utils.ToastUtil;

/**
 * 自定义的 AVUser
 * author：superGao on 2016/3/15.
 * note：借用请注明来源，侵权必究！
 */
public class UserInfo extends AVUser {

    private String header;

    private Bitmap portraitBit;

    public static final String USERNAME = "username";
    public static final String AVATAR = "avatar";
    public static final String LOCATION = "location";
    public static final String INSTALLATION = "installation";


    public static String getCurrentUserId () {
        UserInfo currentUser = getCurrentUser(UserInfo.class);
        return (null != currentUser ? currentUser.getObjectId() : null);
    }


    public String getAvatarUrl() {
        AVFile avatar = getAVFile(AVATAR);
        if (avatar != null) {
            return avatar.getUrl();
        } else {
            return null;
        }
    }


    public void saveAvatar(final Context context,String path, final SaveCallback saveCallback) {
        final AVFile file;
        try {
            file = AVFile.withAbsoluteLocalPath(getUsername(), path);
            put(AVATAR, file);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (null == e) {
                        saveInBackground(saveCallback);
                        ToastUtil.showShort(context,"头像上传成功");
                    } else {
                        if (null != saveCallback) {
                            saveCallback.done(e);
                        }
                    }
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer percentDone) {
                    //打印进度
                    ToastUtil.showShort(context,"上传进度:   "+percentDone);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserInfo getCurrentUser() {
        return getCurrentUser(UserInfo.class);
    }

    public void updateUserInfo() {
        AVInstallation installation = AVInstallation.getCurrentInstallation();
        if (installation != null) {
            put(INSTALLATION, installation);
            saveInBackground();
        }
    }

    public AVGeoPoint getGeoPoint() {
        return getAVGeoPoint(LOCATION);
    }

    public void setGeoPoint(AVGeoPoint point) {
        put(LOCATION, point);
    }

    public static void signUpByNameAndPwd(String name, String password, SignUpCallback callback) {
        AVUser user = new AVUser();
        user.setUsername(name);
        user.setPassword(password);
        user.signUpInBackground(callback);
    }

    public void removeFriend(String friendId, final SaveCallback saveCallback) {
        unfollowInBackground(friendId, new FollowCallback() {
            @Override
            public void done(AVObject object, AVException e) {
                if (saveCallback != null) {
                    saveCallback.done(e);
                }
            }
        });
    }

    public void findFriendsWithCachePolicy(AVQuery.CachePolicy cachePolicy, FindCallback<UserInfo>
            findCallback) {
        AVQuery<UserInfo> q = null;
        try {
            q = followeeQuery(UserInfo.class);
        } catch (Exception e) {
        }
        q.setCachePolicy(cachePolicy);
        q.setMaxCacheAge(TimeUnit.MINUTES.toMillis(1));
        q.findInBackground(findCallback);
    }

    public Bitmap getPortraitBit() {
        return portraitBit;
    }

    public void setPortraitBit(Bitmap portraitBit) {
        this.portraitBit = portraitBit;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPortrait() {
        return this.getString("portrait");
    }

    public void setPortrait(String portrait) {
        this.put("portrait",portrait);
    }
}

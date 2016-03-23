package com.supergao.software.handler;


import android.util.Log;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.supergao.software.api.Api;
import com.supergao.software.api.HttpEngine;
import com.supergao.software.bean.ImageInfo;
import com.supergao.software.utils.AVService;

import java.util.ArrayList;
import java.util.List;

public class JsonBean {

    /**
     * хпои
     */

    public static ResultObject requestHomePageImage() {
        final String res = HttpUtil.httpGet(HttpEngine.getAbsoluteUrl(Api.Common.GET_ROLL_IMAGE));
        //loadPic();
        final List<ImageInfo> list=new ArrayList<ImageInfo>();
        CloudQueryCallback<AVCloudQueryResult> cloudQueryCallback=new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if(e==null){

                    for(int i=0;i<avCloudQueryResult.getResults().size();i++){
                        ImageInfo imageInfo=new ImageInfo();
                        imageInfo.setUrl(avCloudQueryResult.getResults().get(i).getString("url"));
                        list.add(imageInfo);
                    }
                    Log.d("resStr",list.get(0).getUrl());
                }else{
                    Log.d("res",e.getMessage()+"  code="+e.getCode());
                }
            }
        };
        AVService.loadPicture(cloudQueryCallback);

        return JsonParse.parseHomePageImage(res);
    }

    private static void loadPic(){
        FindCallback<AVObject> findCallback=new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e!=null){
                    Log.d("res1",e.getMessage()+"  错误码："+e.getCode());
                    return;
                }
                Log.d("res1",list.get(0).getString("url"));
            }
        };
        AVService.loadPicture(findCallback);
    }
}

package com.supergao.softwere.utils;

import android.content.Context;
import android.content.Intent;

import com.supergao.softwere.entity.AppConfig;
import com.supergao.softwere.activity.MainActivity;
import com.supergao.softwere.bean.BaseUserInfo;
import com.supergao.softwere.bean.RegisterType;
import com.supergao.softwere.bean.Role;
import com.supergao.softwere.service.LocationUploadService;
import com.tencent.android.tpush.XGPushManager;

public class UnRegisterUserUtil {
	
	 public  static void  unRegisterUserTodo(Context context){
		 // 注销的用户是司机，那么停止上传位置信息的service
		 if (Role.DRIVER.equals(AppConfig.sUserInfo.getRoleObj())) { // 司机
			 context.stopService(new Intent(context, LocationUploadService.class));
		 }

		 AppConfig.sUserInfo = null ;
		 AppConfig.sRegisterType = null ;
		 DoCacheUtil.get(context).remove(RegisterType.class.getSimpleName()) ;
		 DoCacheUtil.get(context).remove(BaseUserInfo.class.getSimpleName()) ;

		 // 取消推送注册
		 XGPushManager.unregisterPush(context.getApplicationContext());

		 //加提醒toast 或者dialog
		 context.startActivity(new Intent(context, MainActivity.class).putExtra("reLogin", "reLogin"));
	 }
	
}

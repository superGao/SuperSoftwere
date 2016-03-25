package com.supergao.software.bean;

import android.os.Environment;

/**
 * 定义常量
 */
public class Constant {

	public static final String ROLLIMAGE_KEY = "rollimage";//轮播图


	public static class ROLLIMAGE{
		public static final String IMAGE_URL  = "imageUrl";  //轮播图地址
		public static final String AD_URL  = "adUrl";   //广告地址
		public static final String IMAGEJSON  = "json";  //轮播图JSON地址
	}
	
	public static class ErrorMessage{
		public static final String GETDATA_FAIL_ERROR = "获取数据失败!";
		public static final String DATAREQUEST_FAIL_ERROR = "网络数据请求失败!";
		public static final String DATAPARSE_FAIL_ERROR =  "数据解析失败!";
		public static final String DATA_EXCEPTION_ERROR = "服务器异常，正在努力抢修中，请稍后再试!";
		public static final String NETWORK_EXCEPTION_ERROR = "网络异常!";
	}

	public static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

	public static final String APPS = "/com.d3rich.trafficbiz/";

	public static final String PATH = ROOT + APPS;
	
	/** 拍照头像的文件名 */
	public static String avatarFileName;

}

package com.supergao.software.bean;

import android.os.Environment;

/**
 * 定义常量
 */
public class Constant {

	public static final String ROLLIMAGE_KEY = "rollimage";
	
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
	
	public static class Validate_State{
		public static final int VALIDATE_REGIST = 0;	//注册验证码类型
		public static final int VALIDATE_PASSWORD = 0;	//修改密码验证码类型
		public static final int VALIDATE_PHONE = 0;		//修改手机验证码类型
	}
	
	public static class Address_State{
		public static final String ADDRESS_FEIGHT = "freight";	//运费查询
		public static final String ADDRESS_CENTER = "center";	//个人中心常用地址
		public static final String ADDRESS_ADDRESS = "address";	//常用地址
		public static final String ADDRESS_CONTACT = "contact";	//常用联系人
	}
	
	public static class Order_State{
		public static final String SEND = "send";	//发件
		public static final String RECEIVE = "receive";	//收件
	}
	
	
	public static final String ADD_ADDRESS_IS_NULL = "add_address_is_null";

	
	public static final String MY_FOCUS_SERVICE_LID = "my_focus_service_lid";
	

	
	
	public static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

	public static final String APPS = "/com.d3rich.trafficbiz/";

	public static final String PATH = ROOT + APPS;
	
	/** 拍照头像的文件名 */
	public static String avatarFileName;
	
	//司机运输计划列表跳转到详情的id值
	public static final String TRANSPORT_DETILS_ID = "transport_detils_id";
	
	//物流公司我的订单列表跳转到详情的id值
	public static final String LOGISTIC_ORDER_ID = "logistic_order_id";
	
		//运费查询 
	
	public static final String FREIGHT_SEND = "freight_send";
	
	public static final String FREIGHT_RECEIVE = "freight_receive";

	public static final String CHANGER_MOBILE = "freight_receive";
	
	
	public static final String DRV_ABOUT_COMPANY_INTENT_KEY = "drv_about_company_intent_key";
	

	public static final String WORKER_DETAILS_INTENT_KEY = "worker_details_intent_key";
	public static final String DRIVER_DETAILS_INTENT_KEY = "driver_details_intent_key";
	
	
}

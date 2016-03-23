package com.supergao.software.api;

/**
 * 远程服务器api接口
 * Created by YangJie on 2015/11/10.
 */
public interface Api {
    String APP_KEY = "ANDROID_KCOUPON";
    String TIME_OUT_CODE = "-1";
    String TIME_OUT_CODE_MSG = "连接服务器失败";
    /**
     * JSON 字符串解析错误
     */
    String PARSE_RESPONSE_DATA_ERROR_CODE = "-11" ;
    String PARSE_RESPONSE_DATA_ERROR_CODE_MSG = "解析服务器响应数据失败" ;

    /**
     * 公共接口
     */
    interface Common {
        // 发送验证码
        String SENDCODE = "/user/sendcode" ;
        // 头像上传接口
        String UPLOADHEAD = "/user/headupload" ;
        // 图片轮换地址
        String GET_ROLL_IMAGE = "/clientorder/appadv" ;
        // 计算保费
        String CALCULATE_INSURANCE_AMOUNT = "/clientorder/calculate" ;

        String URL_LICENSE_RULE = "/website/rule" ;
        String URL_LICENSE_ORDER = "/website/rule/order" ;
        String CHECK_VERSION_CODE = "/user/versionandroid" ;    //检查最新版本
        String FEEDBACK = "/user/opinion";                    //  意见反馈
        String LOADMESSAGE = "/user/messagelist";               // 加载消息列表
        String READMESSAGE = "/user/readmessage";               // 读取消息
    }

    /**
     * 用户接口
     */
    interface User {
        // 用户登录
        String LOGIN = "/user/login" ;
        // 用户数据刷新
        String FLASH = "/user/flash" ;
        // 获取集货员列表
        String GET_CLERK_LIST = "/order/selectclerk" ;
        // 获取我的二维码内容信息
        String GET_I_QRCODE_CONTENT = "/order/qrcodedata" ;
        // 获取我的二维码内容信息 网点管理员
        String GET_I_QRCODE_CONTENT_1 = "/order/qrcodedata1" ;
        // 获取用户信息
        String GET_USER_INFO = "/order/userinfo" ;
        // 重置密码
        String RESET_PWD = "/user/resetpass" ;
    }

    /**
     * 物流公司接口
     */
    interface Logistics {
        // 注册
        String REGISTER = "/user/register";
        // 完善信息
        String PERFECTINFO = "/logistics/addlogistics" ;
        // 获取物流公司信息
        String GET_LOGISTICS_INFO = "/order/lname";
        // 获取物流公司列表
        String GET_LOGISTICS_LIST = "/order/logisticslist" ;
        // 提交中转信息
        String SUBMIT_FORWARD_ORDERS = "/order/transfer";
    }

    /**
     * 司机接口
     */
    interface Driver {
        // 注册
        String REGISTER = "/user/register";
        // 完善信息
        String PERFECTINFO = "/driver/completeInfo" ;
        // 获取司机列表
        String GET_DRIVER_LIST = "/order/servicedriver" ;
        // 上传位置信息
        String UPLOAD_LOCATION_INFO = "/logistics/addtranslocation" ;
    }

    /**
     * 订单接口
     */
    interface Order {
        // 获取订单列表
        String GET_ORDER_LIST = "/order/orderlist" ;
        // 获取订单详情
        String GET_ORDER_INFO = "/order/orderInfo" ;
        // 根据扫描的二维码信息获取订单详情
        String GET_ORDER_INFO_BY_QRCODE_INFO = "/order/list" ;
        // 获取拒单理由列表
        String GET_REFUSAL_REASON_LIST = "/order/showreason" ;
        // 拒绝订单
        String REFUSE = "/order/refuseorder" ;
        // 为订单指派集货员
        String ASSIGN_CLERK = "/order/assignclerk" ;
        // 为订单指派运输司机
        String ASSIGN_DRIVER = "/order/assigndriver" ;
        // 获取司机关联订单数量
        String GET_ORDER_COUNT_FOR_DRIVER = "/order/selorder" ;
        // 集货员接单
        String RECEIVE_ORDER = "/order/orderreceive" ;
        // 集货员接单(从上一物流公司 网点管理员)
        String RECEIVE_FORWARD_ORDER = "/order/transferreceive" ;
        // 计算预估运费
        String CALCULATE_DELIVER_FEE = "/order/calprice" ;
        // 工作人员接单 from（集货员 司机）
        String RECEIVE_ORDER_FROM_EMPLOYEE = "/order/dirverreceive" ;
        // 获取司机确认收货列表
        String GET_CONFIRM_FINISHED_ORDERS = "/order/confirmlist" ;
        // 获取网点员工确认收货列表
        String GET_CONFIRM_CLERK_FINISHED_ORDERS = "/order/confirmclerklist" ;
        // 末配 确认收货
        String CONFIRM_FINISHED_ORDER = "/order/confirmorder" ;
        // 网点管理员接单
        String ORDER_RECEIVE = "/order/receive" ;
        // 集货员接单
        String ORDER_ACCEPT = "/clerk/clerkconfirm" ;
        // 集货员拒单
        String ORDER_REFUSE = "/clerk/clerkrefuse" ;
        // 司机接单
        String ORDER_DRIVER_ACCEPT = "/driver/driverconfirm" ;
        // 司机拒单
        String ORDER_DRIVER_REFUSE = "/driver/driverrefuse" ;
    }
}

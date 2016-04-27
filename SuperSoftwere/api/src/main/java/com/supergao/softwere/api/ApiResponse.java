package com.supergao.softwere.api;

/**
 * 网络返回数据类定义
 * @param <T> 泛型，返回data对应的json数据的Java实体类描述（:bean模块中定义）
 * Created by YangJie on 2015/11/10.
 */
public class ApiResponse<T> {
    /**
     * 返回状态码，0：表示成功，99：表示失败
     */
    private String code ;

    /**
     * 返回状态信息 文字描述
     */
    private String msg ;

    /**
     * 返回单个对象
     */
    private T data ;

    /**
     * 返回多个对象
     */
    private T dataList ;

    public ApiResponse(String code, String msg) {
        this.code = code ;
        this.msg = msg ;
    }

    public boolean isSuccess() {
        return "0".equals(code) ;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getDataList() {
        return dataList;
    }

    public void setDataList(T dataList) {
        this.dataList = dataList;
    }
}

package com.supergao.softwere.handler;

/**
 * 结果类
 */
public class ResultObject {
	
	private Boolean success;
    private String message;
    private String code;
    private Object object;
    
    public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
        this.message = message;
    }
    public Object getObject() {
        return object;
    }
    public void setObject(Object object) {
        this.object = object;
    }
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}


}

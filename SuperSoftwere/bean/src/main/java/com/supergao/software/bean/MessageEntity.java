package com.supergao.software.bean;

import java.io.Serializable;

/**
 * 
 * @author zxw
 * 
 */
public class MessageEntity implements Serializable {

	private String id ;
	private String uid;
	private String content;
	private String ceate_time;
	private String type;
	private String status;
	private String title;
	private String otid;
	private String roletype;
	private String otidtype;

	private String mid ;
	private String uId;
	private String mcontent;
	private String mcreateTime;
	private String mtype;
	private String mstatus;
	private String mtitle;
	private String motid;
	private String mroletype;
	private String motidtype;


	public void attach() {
		this.mid=id;
		this.uId=uid;
		this.mcontent=content;
		this.mcreateTime=ceate_time;
		this.mtype=type;
		this.mstatus=status;
		this.mtitle=title;
		this.motid=otid;
		this.mroletype=roletype;
		this.motidtype=otidtype;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getMcontent() {
		return mcontent;
	}

	public void setMcontent(String mcontent) {
		this.mcontent = mcontent;
	}

	public String getMcreateTime() {
		return mcreateTime;
	}

	public void setMcreateTime(String mcreateTime) {
		this.mcreateTime = mcreateTime;
	}

	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}

	public String getMstatus() {
		return mstatus;
	}

	public void setMstatus(String mstatus) {
		this.mstatus = mstatus;
	}

	public String getMtitle() {
		return mtitle;
	}

	public void setMtitle(String mtitle) {
		this.mtitle = mtitle;
	}

	public String getMotid() {
		return motid;
	}

	public void setMotid(String motid) {
		this.motid = motid;
	}

	public String getMroletype() {
		return mroletype;
	}

	public void setMroletype(String mroletype) {
		this.mroletype = mroletype;
	}

	public String getMotidtype() {
		return motidtype;
	}

	public void setMotidtype(String motidtype) {
		this.motidtype = motidtype;
	}
}

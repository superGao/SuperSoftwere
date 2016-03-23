package com.supergao.software.bean;

/**
 * 轮播图
 */
public class ImageInfo  {
	
	private static final long serialVersionUID = 1L;
	
	String title;
	String url;
	String sImageUrl;
	String bImageUrl;
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getsImageUrl() {
		return sImageUrl;
	}
	public void setsImageUrl(String sImageUrl) {
		this.sImageUrl = sImageUrl;
	}
	public String getbImageUrl() {
		return bImageUrl;
	}
	public void setbImageUrl(String bImageUrl) {
		this.bImageUrl = bImageUrl;
	}
}

package com.supergao.softwere.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.supergao.softwere.bean.Constant;
import com.supergao.softwere.bean.ImageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 解析JSON
 */
public class JsonParse {

	/**
	 * 加载轮番图接口
	 * @param str
	 * @return
	 */
	public static ResultObject parseHomePageImage(String str) {

		ResultObject ro = new ResultObject();
		try {
			if (str == null)
				throw new Exception();
			Map<String, Object> json = JSON.parseObject(str);
			String res = String.valueOf(json.get("code"));
			if (res.equals("0")) {
				JSONArray dataJsonArray = (JSONArray) json.get("data");
				List<ImageInfo> ll = null;
				if (dataJsonArray != null) {
					ll = new ArrayList<ImageInfo>();
					for (Object imegeJsonObject : dataJsonArray) {
						Map<String, Object> imegeJsonMap = (Map<String, Object>) imegeJsonObject;

						ImageInfo imageInfo = new ImageInfo();
						imageInfo.setTitle((String) imegeJsonMap.get("name"));
						imageInfo.setUrl((String) "http://"+ imegeJsonMap.get("link"));
						imageInfo.setsImageUrl((String) imegeJsonMap
								.get("320_pic")+imegeJsonMap.get("img"));
						imageInfo.setbImageUrl((String)imegeJsonMap
								.get("640_pic")+imegeJsonMap.get("img"));

						ll.add(imageInfo);
					}
					ro.setSuccess(true);
					ro.setObject(ll);
				} else {
					ro.setSuccess(false);
					ro.setMessage(Constant.ErrorMessage.DATA_EXCEPTION_ERROR);
				}
			} else {
				ro.setSuccess(false);
				ro.setMessage((String) json.get("message"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			ro.setSuccess(false);
			ro.setMessage(Constant.ErrorMessage.DATA_EXCEPTION_ERROR);
		}
		return ro;
	}
}

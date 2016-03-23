package com.supergao.software.handler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 联网
 *
 */
public class HttpUtil {
	
	public static String httpGet(String url)
	{
		// 创建请求HttpClient客户  
        HttpClient httpClient = new DefaultHttpClient();
        try {
            // 创建请求
            HttpGet get = new HttpGet(new URI(url));
            // 发get请求
            HttpResponse httpResponse = httpClient.execute(get);
             // 如果服务成功返回响应
             if (httpResponse.getStatusLine().getStatusCode() == 200) {   
                 HttpEntity entity = httpResponse.getEntity();
                 if (entity != null) {   
                     // 获取服务器响应的json字符
                     String json = EntityUtils.toString(entity);
                     return json;
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();   
         }
         return null; 
	}
	
	public static String httpPost(String url, Map<String, Object> params)
	{
		// 创建请求HttpClient客户  
		HttpClient httpClient = new DefaultHttpClient();
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		JSONObject jsonObj = new JSONObject();
		//获得请求参数
		if(params!=null && !params.isEmpty()) {
		   for(Entry<String, Object> entry : params.entrySet()){
		       pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
		   }
		   //pairs.add(new BasicNameValuePair("json", jsonObj.toJSONString(params)));
		}
		try {   
			HttpPost post = new HttpPost(new URI(url));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
			//entity.setContentType("application/json");//此类型后台用servlet的get方法取不到，只能用流方式读取
			post.setEntity(entity);
			
			// 发post请求   
			HttpResponse httpResponse = httpClient.execute(post);
			// 如果服务成功返回响应   
			if (httpResponse.getStatusLine().getStatusCode() == 200) {   
				HttpEntity msg = httpResponse.getEntity();
				if (entity != null) {   
					// 获取服务器响应的json字符  
					String json = EntityUtils.toString(msg);
					return json;
				}   
			}   
		} catch (Exception e) {
			e.printStackTrace();   
		}   
		return null; 
	}
	
	public static String httpUpload(String URL, byte[] bytes){
		String text = null;
		try {
			java.net.URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Content-Type", "text/html");
			conn.setRequestProperty("Content-Type", "image/jpeg");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setConnectTimeout(10000);
			conn.connect();
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (text != null) {
					text += line;
				} else {
					text = line;
				}
			}
			conn.disconnect();
		} catch (Exception e) {
			System.out.println("发送文件出现异常！" + e);
			e.printStackTrace();
		}
		return text;
	}
	
	/**
	 * 上传图片到服务器
	 * @param URL
	 * @param bytes
	 * @param filename
	 * @return
	 */
	public static String httpUploadTo(String URL, byte[] bytes,String filename){
		String text = null;
		try {
			java.net.URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			// 发送POST请求必须设置如下两行 ,请求头
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=---------------------------------0xKhTmLbOuNdArY");
			conn.setConnectTimeout(10000);
			conn.connect();
			OutputStream out = conn.getOutputStream();
			
			//请求体 Postbody
			StringBuilder sb = new StringBuilder();
			sb.append("-----------------------------------0xKhTmLbOuNdArY\r\n");
			sb.append("Content-Disposition: form-data; name=\"iosImage\"; filename=\""
					+ filename + "\"\r\n");
                sb.append("Content-Type: image/jpeg\r\n\r\n");
			out.write(sb.toString().getBytes("utf-8"));
			out.write(bytes);
			out.write("\r\n-----------------------------------0xKhTmLbOuNdArY--\r\n".getBytes("utf-8"));
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (text != null) {
					text += line;
				} else {
					text = line;
				}
			}
			conn.disconnect();
		} catch (Exception e) {
			System.out.println("发送文件出现异常！" + e);
			e.printStackTrace();
		}
		return text;
	}
	/**
	 * 是否有网络
	 *
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		if (context == null)
			return false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null || !ni.isAvailable()) {
			return false;
		} else {
			return true;
		}

	}
}

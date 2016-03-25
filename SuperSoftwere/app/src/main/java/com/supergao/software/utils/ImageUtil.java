package com.supergao.software.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.supergao.software.utils.Log;
import android.widget.ImageView;

import com.supergao.software.handler.MessageDefine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class ImageUtil {
	
	private Context context;
	private String packageName = "Ziruke";
	public static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();  //内存图片软引用缓冲
	private HashMap<String, Bitmap> imageMap = new HashMap<String, Bitmap>();
	private boolean lock = false;
	
	public ImageUtil(){
		//imageCache = new HashMap<String, SoftReference<Bitmap>>();
		createImageDir();
	}
	
	public ImageUtil(Context context){
		this.context = context;
		//imageCache = new HashMap<String, SoftReference<Bitmap>>();
		
		createImageDir();
	}
	
	public boolean isSDCard(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public String getSDCardPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
	public String getPackageName(){
		if(context == null) return packageName;
		return context.getPackageName();
	}
	
	public boolean createDir(String path){
		File file = new File(path);
    	if (!file.exists()) return file.mkdirs();
    	return true;
	}
	
	public String getImageDir(){
		return getSDCardPath()+"/"+packageName+"/images/";
	}
	
	public boolean createImageDir(){
		return createDir(getImageDir());
	}
	
	public String getImageName(String imageURL){
		
		int index = imageURL.lastIndexOf("/");
		if(index > 0)
			return imageURL.substring(imageURL.lastIndexOf("/")+1); 
		else
			return imageURL;
	}
	
	public String getImagePath(String imageURL){
		if(isSDCard())
			return getImageDir()+getImageName(imageURL);
		else
			return getImageName(imageURL);
	}

	public Bitmap getImageBitmap(String imageURL){
		
		Bitmap bmp = null;
		try{
			if(imageURL == null || imageURL.equals("")) 
				return null;
			
			//bmp = getCacheBitmap(imageURL);
			//if(bmp != null) return bmp;
			
			bmp = getLocalBitmap(getImagePath(imageURL));
			if(bmp == null){
				bmp = getRemoteBitmap(imageURL);
			}
			//imageCache.put(imageURL, new SoftReference<Bitmap>(bmp));
			return bmp;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Bitmap getImageBitmap2(final String imageURL){
		
		if(imageURL == null || imageURL.equals("")) 
			return null;
		
		Thread thread = new Thread(){
			@Override
	        public void run() {
				//while(lock){}
				//lock = true;
				Bitmap bmp = getImageBitmap(imageURL);
				imageMap.put(imageURL, bmp);
			}
		};
		thread.start();
		try {
			thread.join();
			//lock = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageMap.get(imageURL);
	}
	
	public void getImageBitmap(final String imageURL, final Handler handler){
		new Thread(){
			@Override
	        public void run() {
				try{
					Bitmap bitmap = getImageBitmap(imageURL);
					if(bitmap != null){
						Message msg = new Message();
						msg.what = MessageDefine.LOADIMAGE;
						msg.obj = bitmap;
						handler.sendMessage(msg);
					}else{
						Message msg = new Message();
						msg.what = MessageDefine.ERROR;
						msg.obj = null;
						handler.sendMessage(msg);
					}
				}catch(Exception e){
					e.printStackTrace();
					Message msg = new Message();
					msg.what = MessageDefine.ERROR;
					msg.obj = null;
					handler.sendMessage(msg);
				}
			}
		}.start();
	}
	
	//加锁线程
	public void getSynImageBitmap(final ImageView imageView, final String imageURL, final ImageCallBack imageCallBack){
		Thread thread=new Thread(){
			@Override
	        public void run() {
				try{
					Bitmap bitmap = getImageBitmap(imageURL);
					if(bitmap != null){
						imageCallBack.loadImage(imageView, bitmap);
					}else{
						imageCallBack.loadDefaultImage(imageView);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		thread.start();
		try {
			thread.join();
			//lock = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getImageBitmap(final ImageView imageView, final String imageURL, final ImageCallBack imageCallBack){
		new Thread(){
			@Override
			public void run() {
				try{
					Bitmap bitmap = getImageBitmap(imageURL);
					if(bitmap != null){
						imageCallBack.loadImage(imageView, bitmap);
					}else{
						imageCallBack.loadDefaultImage(imageView);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public Bitmap getCacheBitmap(String imageURL){
		Bitmap bmp = null;
		
		if(imageCache.containsKey(imageURL))
			bmp = imageCache.get(imageURL).get(); 
		
		return bmp;
	}
	
	public Bitmap getLocalBitmap(String imagePath){
		
		File file = new File(imagePath);
		if(!file.exists()) return null;
		return BitmapFactory.decodeFile(imagePath);
	}
	
	public Bitmap getRemoteBitmap(String imageURL){
		
		String imagePath = getImagePath(imageURL);
		return downloadFile(imageURL, imagePath);
	}
	
	public Bitmap downloadFile(String imageURL, String imagePath){

		File file = new File(imagePath);
		if(file.exists())
		    file.delete();
				
		byte[] buffer = getImageBytes(imageURL);
		if(buffer == null) 
			return null;
		
		Bitmap bmp = null;
		try{
			bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
		}catch(Exception e){
			return null;
		}
		
		if(isSDCard())
			saveImageFile(buffer, imagePath);
		
		return bmp;
	}
	
	public void saveImageFile(byte[] bs, String imagePath){
		try {
			if(bs != null){
				OutputStream os = new FileOutputStream(imagePath);
				os.write(bs, 0, bs.length);   
				os.close();
			}
		}catch (Exception e) {
	        e.printStackTrace();
		}
	}
	
	public void saveImageFile(Bitmap bm, String imagePath, int quantity){
		if(bm == null) return ;
		
		File file = new File(imagePath);
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, quantity, outStream);
			outStream.flush();
			outStream.close();

		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] getImageBytes(String imageURL){
		
		byte[] data = null;
		
		if (imageURL == null && "".equals(imageURL))
			return null;
		
		try {
	         // 构造URL   
	         URL url = new URL(imageURL);
	         // 打开连接   
	         URLConnection con = url.openConnection();
	         //获得文件的长度
	         int contentLength = con.getContentLength();
	         // 输入流   
	         InputStream is = con.getInputStream();
	         // 1K的数据缓冲   
	         byte[] bs = new byte[1024];   
	         // 读取到的数据长度   
	         int len;   
	         // 输出的文件流   
	         ByteArrayOutputStream baos = new ByteArrayOutputStream();
	         // 开始读取   
	         //方法1
	         /*data = new byte[contentLength];
	         int destPos = 0;
	         while ((len = is.read(bs)) != -1) {     
	        	 System.arraycopy(bs, 0, data, destPos, len);
	         } */
	         //方法2
	         while ((len = is.read(bs)) != -1) {     
	        	 baos.write(bs, 0, len);  
	        	 baos.flush();
	         }  
	         data = baos.toByteArray();
	         // 完毕，关闭所有链接   
	         baos.close();
	         is.close();
		            
		} catch (Exception e) {
	        e.printStackTrace();
		}
		
		return data;
	}
	
	//第一种方法  
    public Bitmap getHttpBitmap(String data)
    {  
        Bitmap bitmap = null;
        try  
        {  
            //初始化一个URL对象  
            URL url = new URL(data);
            //获得HTTPConnection网络连接对象  
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5*1000);  
            connection.setDoInput(true);  
            connection.connect();  
            //得到输入流  
            InputStream is = connection.getInputStream();
            Log.i("TAG", "*********inputstream**" + is);
            bitmap = BitmapFactory.decodeStream(is);
            Log.i("TAG", "*********bitmap****" + bitmap);
            //关闭输入流  
            is.close();  
            //关闭连接  
            connection.disconnect();  
        } catch (Exception e)
        {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
          
        return bitmap;  
    }  
      
    //第二种方法  
    public Bitmap getBitmap(String s)
    {  
        Bitmap bitmap = null;
        try  
        {  
            URL url = new URL(s);
            bitmap = BitmapFactory.decodeStream(url.openStream());
        } catch (Exception e)
        {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return bitmap;  
    }  
	
	public static Bitmap compressImage(Bitmap image, int size) {
		ByteArrayInputStream isBm = compressByteArrayInputStream(image);
		Options op = new Options();
		op.inSampleSize = size;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, op);// 把ByteArrayInputStream数据生成图片
	
		return bitmap;
	}
	
	public static byte[] compressImage(String filename,int size)
	{
		byte[] bitmapdata= null;
		File f = new File(BitmapUtil.DIR, filename);
		if(f.exists())
		{
			Bitmap bitmap=new BitmapFactory().decodeFile(f.getAbsolutePath());
			bitmap=ImageUtil.compressImage(bitmap,size);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		    bitmapdata = stream.toByteArray();	
		}
		
		return bitmapdata;
	}
	
	
	public static byte[] getZoomBitmap(String imagePath) {
		 byte[] bitmapdata= null;
		 File f = new File(imagePath);
			if(f.exists())
			{
		// 解决图片内存溢出问题
		Options options = new Options();
		options.inJustDecodeBounds = true;//这样就只返回图片参数
		
		// 获取这个图片的宽和高
		Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath(), options); // 此时返回bm为空
		options.inJustDecodeBounds = false;//上面操作完后,要设回来,不然下面同样获取不到实际图片
		// 计算缩放比
		int be = (int) (options.outHeight / (float) 200);
		//上面算完后一下如果比200大,那就be就大于1,那么就压缩be,如果比200小,那图片肯定很小了,直接按原图比例显示就行
		if (be <= 0){be = 1;}
		options.inSampleSize = be;//be=2.表示压缩为原来的1/2,以此类推
		// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false,不然返回的还是一个空bitmap
		bm = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	    bitmapdata = stream.toByteArray();	
			}
		return bitmapdata;
	}
	
	public static ByteArrayInputStream compressByteArrayInputStream(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos); // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			if(options < 0) {
				break;
			}
			baos.reset(); // 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10; // 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		return isBm;
	}
	
	public static byte[] compressByte(Bitmap image) {
		byte[] bytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos); // 质量压缩50%
		bytes = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			Log.v("图片压缩", e.getMessage());
		}
		return bytes;
	}
	
	public static byte[] compressByte(Bitmap image, int s) {
		byte[] bytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, s, baos); // 质量压缩50%
		bytes = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			Log.v("图片压缩", e.getMessage());
		}
		return bytes;
	}
	
	public static byte[] compressNoByte(Bitmap image) {
		byte[] bytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos); // 质量压缩50%
		bytes = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			Log.v("图片压缩", e.getMessage());
		}
		return bytes;
	}
	
	//获取资源图片
	public Bitmap getRes(String name) {
		ApplicationInfo appInfo = context.getApplicationInfo();
		int resID = context.getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return BitmapFactory.decodeResource(context.getResources(), resID);
	}

	
	/** 
     * 回调接口 
     * @author onerain 
     * 
     */  
    public interface ImageCallBack
    {  
        public void loadImage(ImageView imageView, Bitmap bitmap);
        public void  loadDefaultImage(ImageView imageView);
    }
}

package com.supergao.softwere.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
/**
 *
 *@author superGao
 *creat at 2016/3/25
 */
public class BitmapUtil {
	/**
	 * 读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/***
	 * 根据资源文件获取Bitmap
	 * 
	 * @param context
	 * @param drawableId
	 * @return
	 */
	public static Bitmap readBitmapById(Context context, int drawableId,
			int screenWidth, int screenHight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		options.inInputShareable = true;
		options.inPurgeable = true;
		InputStream stream = context.getResources().openRawResource(drawableId);
		Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
		return getBitmap(bitmap, screenWidth, screenHight);
	}
	
	/**
	 * 获取文件二进制
	 * @param filename
	 * @return
	 */
	public static byte[] getBitmapByte(String filename){
		byte b[] = null;
		
		File f = new File(BitmapUtil.DIR, filename);
		if(f.exists()){
			try {
				InputStream in = new FileInputStream(f);
				b = new byte[(int)f.length()];     //创建合适文件大小的数组
				in.read(b);    //读取文件中的内容到b[]数组
				in.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return b;
	}

	/***
	 * 等比例压缩图片
	 * 
	 * @param bitmap
	 * @param screenWidth
	 * @param screenHight
	 * @return
	 */
	public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
			int screenHight) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Log.e("jj", "图片宽度" + w + ",screenWidth=" + screenWidth);
		Matrix matrix = new Matrix();
		float scale = (float) screenWidth / w;
		float scale2 = (float) screenHight / h;

		// scale = scale < scale2 ? scale : scale2;

		// 保证图片不变形.
		matrix.postScale(scale, scale2);
		// w,h是原图的属性.
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}

	/***
	 * 保存图片至SD卡
	 * 
	 * @param bm
	 * @param filename
	 * @param quantity
	 */
	private static int FREE_SD_SPACE_NEEDED_TO_CACHE = 1;
	private static int MB = 1024 * 1024;
	public final static String DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+"/images";

	public static void saveBitmap(Bitmap bm, String filename, int quantity) {
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > getFreeSize()) {
			return;
		}
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
			return;

		// 目录不存在就创建
		File dirPath = new File(DIR);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		File file = new File(DIR + "/" + filename);
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

	/***
	 * 获取SD卡图片
	 * 
	 * @param filename
	 * @param quantity
	 * @return
	 */
	public static Bitmap getBitmap(String filename, int quantity) {
		InputStream inputStream = null;
		Bitmap map = null;
		URL url_Image = null;
		String LOCALURL = "";
		if (filename == null)
			return null;

		LOCALURL = URLEncoder.encode(filename);
		try {
		if (exist(LOCALURL)) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;//这样就只返回图片参数
				// 获取这个图片的宽和高
				map = BitmapFactory.decodeFile(DIR + "/" + LOCALURL, options); // 此时返回bm为空
				options.inJustDecodeBounds = false;//上面操作完后,要设回来,不然下面同样获取不到实际图片
				// 计算缩放比
				int be = (int) (options.outHeight / (float) 200);
				//上面算完后一下如果比200大,那就be就大于1,那么就压缩be,如果比200小,那图片肯定很小了,直接按原图比例显示就行
				if (be <= 0){be = 1;}
				options.inSampleSize = be;//be=2.表示压缩为原来的1/2,以此类推
				// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false,不然返回的还是一个空bitmap
				map = BitmapFactory.decodeFile(DIR + "/" + LOCALURL, options);
				
			} else {
				url_Image = new URL(filename);
				inputStream = url_Image.openStream();
				map = BitmapFactory.decodeStream(inputStream);
				// url = URLEncoder.encode(url, "UTF-8");
				if (map != null) {
					saveBitmap(map, LOCALURL, quantity);
				}
				inputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	/** 
	 * compute Sample Size 
	 *  
	 * @param options 
	 * @param minSideLength 
	 * @param maxNumOfPixels 
	 * @return 
	 */  
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {  
	    int initialSize = computeInitialSampleSize(options, minSideLength,  
	            maxNumOfPixels);  
	  
	    int roundedSize;  
	    if (initialSize <= 8) {  
	        roundedSize = 1;  
	        while (roundedSize < initialSize) {  
	            roundedSize <<= 1;  
	        }  
	    } else {  
	        roundedSize = (initialSize + 7) / 8 * 8;  
	    }  
	  
	    return roundedSize;  
	}


	/** 
	 * compute Initial Sample Size 
	 *  
	 * @param options 
	 * @param minSideLength 
	 * @param maxNumOfPixels 
	 * @return 
	 */  
	private static int computeInitialSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {  
	    double w = options.outWidth;  
	    double h = options.outHeight;  
	  
	    // 上下限范围  
	    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
	  
	    if (upperBound < lowerBound) {  
	        // return the larger one when there is no overlapping zone.  
	        return lowerBound;  
	    }  
	  
	    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
	        return 1;  
	    } else if (minSideLength == -1) {  
	        return lowerBound;  
	    } else {  
	        return upperBound;  
	    }  
	}  
	
	/** 
	 * get Bitmap 
	 *  
	 * @param imgFile 
	 * @param minSideLength 
	 * @param maxNumOfPixels 
	 * @return 
	 */  
	public static Bitmap tryGetBitmap(String imgFile, int minSideLength,
	        int maxNumOfPixels) {  
	    if (imgFile == null || imgFile.length() == 0)  
	        return null;  
	  
	    try {  
	        FileDescriptor fd = new FileInputStream(DIR + "/" + imgFile).getFD();
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;  
	        // BitmapFactory.decodeFile(imgFile, options);  
	        BitmapFactory.decodeFileDescriptor(fd, null, options);
	  
	        options.inSampleSize = computeSampleSize(options, minSideLength,  
	                maxNumOfPixels);  
	        try {  
	            // 这里一定要将其设置回false，因为之前我们将其设置成了true  
	            // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，即，BitmapFactory解码出来的Bitmap为Null,但可计算出原始图片的长度和宽度  
	            options.inJustDecodeBounds = false;  
	  
	            Bitmap bmp = BitmapFactory.decodeFile(DIR + "/" + imgFile, options);
	            return bmp == null ? null : bmp;  
	        } catch (OutOfMemoryError err) {
	            return null;  
	        }  
	    } catch (Exception e) {
	        return null;  
	    }  
	}  

	/***
	 * 判断图片是存在
	 * 
	 * @param filename
	 * @return
	 */
	public static boolean exist(String filename) {
		File file = new File(DIR + "/" + filename);
		return file.exists();
	}

	/** * 计算sdcard上的剩余空间 * @return */
	private static int getFreeSize() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;

		return (int) sdFreeMB;
	}

}

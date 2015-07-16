package com.school.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	public static String url;
	public static  DecimalFormat df2 = new DecimalFormat("####.00");
	
	public static String getFormat(double price){
		return df2.format(price);
	}
	
	public static String getStringTimeLong(String strLongTime) {
		long lTime = Long.parseLong(strLongTime)*1000;
		return getStringTimeLong(new Date(lTime));
	}
	public static String getStringTimeLong(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}
	
	
	public static boolean isStringBlank(String str){
		if(null == str || "".equals(str.trim())){
			return true;
		}
		return false;
	}
	
    
  //dip转换为px
	public static int dip2px(Context context, float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue*scale+0.5f);
	}
	
	//px转换为dip
	public static int px2dip(Context context ,float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue/scale+0.5f);
	}
	/** 
     * 将px值转换为sp值，保证文字大小不变 
     *  
     * @param pxValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }  
  
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }  
  
    
    /**
     * 计算宽高比例
     * img_size ＝ 500-300
     * @param img_size
     * @return
     */
    public static float getImageWidthHeightScale(String img_size){
		float iScale = 1;
		if(!StringUtils.isBlank(img_size)){
			try{
				String []sizes = img_size.split("-");
				if(sizes.length > 1){
					float size1 = Integer.parseInt(sizes[0]);
					float size2 = Integer.parseInt(sizes[1]);
					iScale = size1 /size2;
				}
			}catch(Exception e){
				
			}
			
		}
		
		return iScale;
	}
	
	/**
	 * 计算目标高度
	 * @param width 目标宽度
	 * @param widthHeightScale
	 * @return
	 */
    public static int getHeight(int width,float widthHeightScale){
    	return (int) (width / widthHeightScale);
    }
    
	public static int getHeight(int width,String img_size){
		return getHeight(width,getImageWidthHeightScale(img_size));
	}
}

package com.school.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.FloatMath;

/**
 * 通用方法
 * 
 * @author SPM
 * 
 */
public class BaseUtils {
	/**
	 * 内容格式化的通用方法
	 * 
	 * @author SPM
	 * 
	 */
	public static class Format {
		/**
		 * 通过dip获取相应的px值
		 * 
		 * @param context
		 * @param dip
		 * @return
		 */
		public static int formatDipToPx(Context context, int dip) {
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			return (int) FloatMath.ceil(dip * dm.density);
		}

		/**
		 * 获取格式化播放时长字符串[HH:mm:ss]
		 * 
		 * @param mCurSec
		 *            时长(单位：秒)
		 * @return HH:mm:ss
		 */
		public static String getPlayTimeFormat(int mCurSec) {
			if (mCurSec <= 0) {
				return "00:00:00";
			}
			StringBuffer formattedTime = new StringBuffer();
			int hour = mCurSec / 60 / 60;
			int leftSec = mCurSec % (60 * 60);
			int min = leftSec / 60;
			int sec = leftSec % 60;

			formattedTime.append(hour < 10 ? "0" : "").append(hour).append(":");
			formattedTime.append(min < 10 ? "0" : "").append(min).append(":");
			formattedTime.append(sec < 10 ? "0" : "").append(sec);
			return formattedTime.toString();
		}
	}

	/**
	 * 线程相关的通用方法
	 * 
	 * @author SPM
	 * 
	 */
	public static class TreadTask {
		/**
		 * 停止并释放一个timer
		 * 
		 * @param timer
		 */
		public static void stopTimer(Timer timer) {
			if (timer != null) {
				timer.cancel();
				timer.purge();
				timer = null;
			}
		}
	}

	/**
	 * <p>
	 * 通过反射实现toString方法
	 * <p>
	 * 
	 * @param obj
	 * @return
	 */
	public static final <T> String toStringByReflection(T obj) {
		String toString = "";
		StringBuilder sb = new StringBuilder();
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
		sb.append(clazz.getName()).append("@")
				.append(Integer.toHexString(obj.hashCode())).append("[")
				.append("\n\r");
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Object value = null;
			try {
				value = field.get(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			sb.append(field.getName()).append("=").append(value).append(",")
					.append("\n\r");
		}
		int lastIndex = sb.length() - 3;
		toString = sb.substring(0, lastIndex);
		toString = toString.concat("]");
		return toString;
	}

/*	*//**
	 * 显示toast
	 * 
	 * @param msg
	 *//*
	public static void showToast(String msg, int type) {
		if (type == 1)
			Toast.makeText(AppApplication.context, msg, Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(AppApplication.context, msg, Toast.LENGTH_LONG).show();

	}
*/
	/**
	 * 发送短信
	 * 
	 * @param activity
	 * @param str
	 */
	public static void sendMessage(Activity activity, String phone, String str) {
		Uri uri = Uri.parse("smsto:" + phone);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.putExtra("sms_body", str);
		activity.startActivity(it);
	}

	

	static SharedPreferences sp;
	public final static String cookieXml = "qicai";

	public static String getCookie(Context c, String name) {
		String value = "";
		try {
			if (sp == null)
				sp = c.getSharedPreferences(cookieXml, Activity.MODE_PRIVATE);
			value = sp.getString(name, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public static void setCookie(Context c, String name, String value) {
		try {
			if (sp == null)
				sp = c.getSharedPreferences(cookieXml, Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(name, value);
			editor.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 *
	 */
	public static String getStringDate(Long date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 *//*
	public static int dip2px(float dpValue) {
		final float scale = AppApplication.context.getResources()
				.getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	*//**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 *//*
	public static int px2dip(float pxValue) {
		final float scale = AppApplication.context.getResources()
				.getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
*/
	// 时间转化
	public static String getDate(String time) {
		long t = Long.parseLong(time);
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String date = sdf.format(new Date(t*1000));
		return date.substring(11, date.length()-3);
	}
    // 获取IMEI值
    public static String getIMEI(final Context context) {
           String mIMEI = null;
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                mIMEI = tm.getDeviceId();
            }
            if (null == mIMEI) {
                mIMEI = "000000000000000";
            }
 
        return mIMEI;
    }
	/*public static List<AppInfo> getShareAppList() {  
	    List<AppInfo> shareAppInfos = new ArrayList<AppInfo>();  
	    PackageManager packageManager = AppApplication.context.getPackageManager();  
	    List<ResolveInfo> resolveInfos = getShareApps(AppApplication.context);  
	    if (null == resolveInfos) {  
	        return null;  
	    }  
	    else {  
	        for (ResolveInfo resolveInfo : resolveInfos) {  
	            AppInfo appInfo = new AppInfo();  
	            appInfo.setAppPkgName(resolveInfo.activityInfo.packageName);  
	            appInfo.setAppLauncherClassName(resolveInfo.activityInfo.name);  
	            appInfo.setAppName(resolveInfo.loadLabel(packageManager).toString());  
	            appInfo.setAppIcon(resolveInfo.loadIcon(packageManager));  
	            shareAppInfos.add(appInfo);  
	        }  
	    }  
	    return shareAppInfos;  
	}*/
	public static List<ResolveInfo> getShareApps(Context context) {  
	    List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();  
	    Intent intent = new Intent(Intent.ACTION_SEND, null);  
	    intent.addCategory(Intent.CATEGORY_DEFAULT);  
	    intent.setType("image/*");  
	    PackageManager pManager = context.getPackageManager();  
	    mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);  
	    return mApps;  
	}
}

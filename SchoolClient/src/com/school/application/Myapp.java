package com.school.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.school.bean.CacheManager;
import com.school.bean.UserBean;
import com.school.bean.VersionInfo;

public class Myapp extends Application
{
	public static Myapp instanse;
    
	public static int  width;
	public static int  height;
	public static int  columnWidth;
    public static String user_id ;
    public static int px2spmyscroll(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }
	@Override
	public void onCreate() {
		super.onCreate();
		instanse = this;
		CacheManager.getInstance().setApplication(this);
		UserBean user = getUserBean();
		user_id = user.fdUserId == null || user.fdUserId.equals("")?"":user.fdUserId;
		getChannel();
	} 
    
	public UserBean getUserBean(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		UserBean user = new UserBean();
		user.fdUserId = preferences.getString("user_id", "");
		return user;
	}
	
	public void saveUserBean(UserBean user){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		editor.putString("user_id", user.fdUserId);
		editor.commit();
	}
	
	public long getUpdateTime(String key){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		long time = preferences.getLong("updatetime"+key, System.currentTimeMillis());
		return time;
	}
	
	public void saveUpdateTime(String key,long time){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		editor.putLong("updatetime"+key, time);
		editor.commit();
	}
	
	public static boolean isNetworkAvailable(Context context) {  
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo[] info = mgr.getAllNetworkInfo();  
        if (info != null) {  
            for (int i = 0; i < info.length; i++) {  
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {  
                    return true;  
                }  
            }  
        }  
        return false;  
    }  
	
	public VersionInfo getServerVersion(){
		VersionInfo info = new VersionInfo();
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		info.setVersion(preferences.getString("ServerVersion", ""));
		info.setDisplayMessage(preferences.getString("ServerMessage", ""));
		info.setDownloadURL(preferences.getString("ServerUrl", ""));
		return info;
	}
	
	public void saveServerVersion(VersionInfo a_serverVersion){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		editor.putString("ServerVersion", a_serverVersion.getVersion());
		editor.putString("ServerUrl", a_serverVersion.getDownloadURL());
		editor.putString("ServerMessage", a_serverVersion.getDisplayMessage());
		editor.commit();
	}
	
	public String getChannel(){
		String strChannel = "";
		ApplicationInfo appInfo = null;
		try {
			appInfo = getPackageManager().getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA);
			strChannel=appInfo.metaData.getString("UMENG_CHANNEL");
			System.out.println("UMENG_CHANNEL:"+strChannel);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return strChannel;
	}
	
}
package com.school.bean;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.school.application.Myapp;

public class CacheManager {
	private static CacheManager instance = new CacheManager();
	private Myapp app;
	
	private CacheManager(){
		
	}
	
	public void setApplication(Myapp app){
		this.app = app;
	}
	
	public static CacheManager getInstance(){
		return instance;
	}
	
	public UserBean getLoginUser(){
		return app.getUserBean();
	} 
	
	public void saveUserBean(UserBean user){
		app.saveUserBean(user);
	}
	
	public long getUpdateTime(String key){
		return app.getUpdateTime(key);
	}
	
	public void saveUpdateTime(String key,long time){
		app.saveUpdateTime(key,time);
	}
}

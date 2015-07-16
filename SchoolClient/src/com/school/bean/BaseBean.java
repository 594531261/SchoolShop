package com.school.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.school.utils.Utils;

public abstract class BaseBean implements Serializable{
	
	public abstract void parse(JSONObject oj);
	public static String getStringFromJson(JSONObject oj, String key){
		String str = "";
		try {
			str = oj.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
			str = "";
		}
		return str;
	}
	
	public static int getIntFromJson(JSONObject oj, String key){
		int intStr = -1;
		try {
			String str = getStringFromJson(oj,key);
			if(!Utils.isStringBlank(str)){
				intStr = Integer.parseInt(str);
			}else{
				intStr = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			intStr = -1;
		}
		return intStr;
	}
	public static double getDoubleFromJson(JSONObject oj, String key){
		double intStr = -1;
		try {
			String str = getStringFromJson(oj,key);
			if(!Utils.isStringBlank(str)){
				intStr = Double.parseDouble(str);
			}else{
				intStr = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			intStr = -1;
		}
		return intStr;
	}

}

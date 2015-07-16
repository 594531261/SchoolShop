package com.school.bean;

import org.json.JSONException;
import org.json.JSONObject;



public class Category implements Comparable<String>{
//	{"category_id":"1","title":"首页","seq_num":"1"}
	public String fdTitle;
	public String fdId;
	public String fdSeqNum;
	
	@Override
	public int compareTo(String another) {
		return fdSeqNum.compareTo(another);
	}
	
	public void parse(JSONObject oj) throws JSONException{
		this.fdTitle = oj.getString("title");
		this.fdId = oj.getString("category_id");
		this.fdSeqNum = oj.getString("seq_num");
	}
}

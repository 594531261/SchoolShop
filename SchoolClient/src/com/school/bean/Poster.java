package com.school.bean;

import java.util.List;

import org.json.JSONObject;

import com.school.net.NetInfo;
import com.school.utils.Utils;

public class Poster extends BaseBean{
	private String poster_id  ;
	private String poster_title ;
	private String poster_img ;
	private String is_show ;
	private String product_id ;
	private String status ;
	private String addtime ;
	private String seq_num ;
	private String starttime ;
	private String endtime ;
	private String main_img_size;
	
	
	public long getEndtime() {
		long endTime = Long.parseLong(this.endtime);
		
		return endTime*1000;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	
	@Override
	public void parse(JSONObject oj) {
		 this.setPoster_id(getStringFromJson(oj, "poster_id"));
         this.setProduct_id(getStringFromJson(oj, "product_id"));
         this.setIs_show(getStringFromJson(oj, "is_show"));
         this.setPoster_img(getStringFromJson(oj, "poster_img"));
         this.main_img_size = (getStringFromJson(oj, "main_img_size"));
         
	}
	public String getPoster_id() {
		return poster_id;
	}
	public void setPoster_id(String poster_id) {
		this.poster_id = poster_id;
	}
	public String getPoster_title() {
		return poster_title;
	}
	public void setPoster_title(String poster_title) {
		this.poster_title = poster_title;
	}
	public String getPoster_img() {
		return poster_img;
	}
	public void setPoster_img(String poster_img) {
		this.poster_img = poster_img;
	}
	public String getIs_show() {
		return is_show;
	}
	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getSeq_num() {
		return seq_num;
	}
	public void setSeq_num(String seq_num) {
		this.seq_num = seq_num;
	}
	public String getMain_img_size() {
		return main_img_size;
	}
	
}

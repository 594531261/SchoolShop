package com.school.bean;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import android.R.integer;

import com.school.net.NetInfo;
import com.school.utils.Utils;

public class Produce extends BaseBean{
	private int product_id  ;
	private double original_price  ;
	private String current_price  ;
	private String product_count ;
	private String product_num ;
	private String endtime ;
	private String starttime ;
	private int stock  ;
	private List lsSize;
	private List lsColor;
	private List lsCategory;
	
	public String size;
	public String color;
	
	private String title;
	private String brand;
	private String businessmam;
	private String activity_begin_time;
	private String activity_end_time;
	private String activity_info;
	private String main_img_url;
	
	private String local_main_img_url;//临时图片（全路径）
	private String main_img_size;//临时图片（全路径）
	
	
	private String introduction_img_url;
	private String introduction_img_size;
	
	
	
	
	public String getMain_img_size() {
		return main_img_size;
	}
	public String getIntroduction_img_size() {
		return introduction_img_size;
	}
	public String getLocal_main_img_url() {
		return local_main_img_url;
	}
	public void setLocal_main_img_url(String local_main_img_url) {
		this.local_main_img_url = local_main_img_url;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public double getOriginal_price() {
		return original_price;
	}
	public String getStrOriginal_price() {
		return "￥" + original_price;
	}
	public void setOriginal_price(double original_price) {
		this.original_price = original_price;
	}
	
	public double getDoubleCurrentPrice(){
		double price = 0.0;
		if(!hasCurrentPrice()){
			price = getOriginal_price();
		}else{
			price = Double.parseDouble(this.current_price);
		}
		
		return price;
	}
	
	public boolean hasCurrentPrice(){
		if(Utils.isStringBlank(this.current_price)){
			return false;
		}else{
			double d = Double.parseDouble(this.current_price);
			if(d == 0){
				return false;
			}
		}
		return true;
	}
	
	public String getCurrent_price() {
		if("".equals(current_price))
			current_price = "0";
		return current_price;
//		return current_price==""?"0":this.current_price;
	}
	public String getStrCurrent_price() {
		return "￥" + getCurrent_price();
//		return current_price==""?"0":this.current_price;
	}
	
	
	public void setCurrent_price(String current_price) {
		this.current_price = current_price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public List getSize() {
		return lsSize;
	}
	public void setSize(List size) {
		this.lsSize = size;
	}
	public List getColor() {
		return lsColor;
	}
	public void setColor(List color) {
		this.lsColor = color;
	}
	public List getCategory() {
		return lsCategory;
	}
	public void setCategory(List category) {
		this.lsCategory = category;
	}
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getBusinessmam() {
		return businessmam;
	}
	public void setBusinessmam(String businessmam) {
		this.businessmam = businessmam;
	}
	public String getActivity_begin_time() {
		return activity_begin_time;
	}
	public void setActivity_begin_time(String activity_begin_time) {
		this.activity_begin_time = activity_begin_time;
	}
	public String getActivity_end_time() {
		return activity_end_time;
	}
	public void setActivity_end_time(String activity_end_time) {
		this.activity_end_time = activity_end_time;
	}
	public String getActivity_info() {
		return activity_info;
	}
	public void setActivity_info(String activity_info) {
		this.activity_info = activity_info;
	}
	public String getMain_img_url() {
		return NetInfo.GETPNG + main_img_url;
	}
	public void setMain_img_url(String main_img_url) {
		this.main_img_url = main_img_url;
	}
	public String getIntroduction_img_url() {
		return introduction_img_url;
	}
	public void setIntroduction_img_url(String introduction_img_url) {
		this.introduction_img_url = introduction_img_url;
	}
	
	
	
	public String getTotal_sales() {
		
		if("".equals(product_count)){
			
			product_count = "0";
		}
		return product_count;
	}
	public void setTotal_sales(String total_sales) {
		this.product_count = total_sales;
	}
	
	public long getEndtime() {
		long endTime=0; ;
		if(!"".equals(this.endtime)){
			endTime = Long.parseLong(this.endtime);
		}else{
			endTime=0;
		}
		
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
		 this.setProduct_id(getIntFromJson(oj, "product_id"));
         this.setTitle(getStringFromJson(oj, "title"));
         this.setBrand(getStringFromJson(oj, "brand"));
         this.setBusinessmam(getStringFromJson(oj, "businessmam"));
         this.setOriginal_price(getDoubleFromJson(oj,"original_price"));
         this.setCurrent_price(getStringFromJson(oj, "current_price"));
         this.setMain_img_url(getStringFromJson(oj, "main_img_url"));
//         this.setTotal_sales(getStringFromJson(oj, "total_sales"));		
         this.size = getStringFromJson(oj, "size");
         this.color = getStringFromJson(oj, "color");
         this.stock = getIntFromJson(oj, "stock");
         this.introduction_img_url = getStringFromJson(oj, "introduction_img_url");
         this.activity_info = getStringFromJson(oj, "activity_info");
         this.activity_end_time = getStringFromJson(oj, "activity_end_time");
         this.activity_begin_time = getStringFromJson(oj, "activity_begin_time");
         this.product_count = getStringFromJson(oj, "product_count");
         this.product_num = getStringFromJson(oj, "product_num");
         this.endtime = getStringFromJson(oj, "endtime");
         this.starttime = getStringFromJson(oj, "starttime");
         this.introduction_img_size = getStringFromJson(oj, "introduction_img_size");
         this.main_img_size = getStringFromJson(oj, "main_img_size");
         
	}
	public String getProduct_num() {
		return product_num;
	}
	public void setProduct_num(String product_num) {
		this.product_num = product_num;
	}

	public double getZhekou(){
		double zhe = 0.0f;

		String currr = getCurrent_price();
		if ("".equals(currr))
			currr = "0";
		double cur = Double.parseDouble(currr);
		double ore = getOriginal_price();

		if(ore != 0){
			zhe = cur / ore;
		}else{
			zhe = 0;
		}
		return zhe*10;
	}
	
}

package com.school.bean;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.school.utils.Utils;

public class CartBean extends BaseBean {
	// cart_id int ID
	// product_id int 商品ID
	// product_num string 数量
	// product_size string 尺寸
	// product_color string 颜色
	// addtime int 加入时间
	// product_object array 商品对象

	public String cart_id;
	public String product_id;
	private String product_num;
	public String product_size;
	public String product_color;
	public String addtime;
	public String seltag = "0";
	public Produce product_object = new Produce();

	
	public String getProduct_num() {
		
		return StringUtils.isBlank(product_num)?"0":product_num;
	}

	public void setProduct_num(String product_num) {
		this.product_num = product_num;
	}

	public String getTitle() {
		return product_object.getTitle();
	}

	public String getImgUrl() {
		return Utils.isStringBlank(product_object.getLocal_main_img_url()) ? product_object
				.getMain_img_url() : product_object.getLocal_main_img_url();
	}
	
	public double getPrice(){
		return product_object.getDoubleCurrentPrice();
	}

	@Override
	public void parse(JSONObject oj) {
		HashMap<String, String> map = new HashMap<String, String>();

		this.cart_id = getStringFromJson(oj, "cart_id");
		this.product_id = getStringFromJson(oj, "product_id");
		this.product_color = getStringFromJson(oj, "product_color");
		this.product_size = getStringFromJson(oj, "product_size");
		this.product_num = getStringFromJson(oj, "product_num");
		this.addtime = getStringFromJson(oj, "addtime");

		String da;
		try {
			da = oj.getString("product_object");
			JSONObject ojProduct = new JSONObject(da);
			product_object.parse(ojProduct);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}

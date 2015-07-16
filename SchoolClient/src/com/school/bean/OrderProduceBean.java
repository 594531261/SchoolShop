package com.school.bean;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import com.school.schoolclient.ConfirmOrder;

public class OrderProduceBean extends BaseBean{
//	  [product_id] => 47   商品id
//              [product_num] => 1  商品数量
//              [product_size] => M   商品号码
//              [product_color] => 白色   商品颜色
//              [product_money] => 此商品下单时的单价
	
	public String product_id;
	public String product_num;
	public String product_size;
	public String product_color;
	public String product_money;
	
	public Produce product_object;
	
	@Override
	public void parse(JSONObject oj) {
		product_id = getStringFromJson(oj, "product_id");
		product_num = getStringFromJson(oj, "product_num");
		product_size = getStringFromJson(oj, "product_size");
		product_color = getStringFromJson(oj, "product_color");
		product_money = getStringFromJson(oj, "product_money");
		
		try {
			JSONObject json_product_object = oj.getJSONObject("product_object");
			product_object = new Produce();
			product_object.parse(json_product_object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public CartBean getCartBean(){
		CartBean cBean = new CartBean();
		cBean.product_id = String.valueOf(product_id);
		cBean.setProduct_num(product_num);
		cBean.product_color = product_color;
		cBean.product_size = product_size;

		cBean.product_object = new Produce();
		cBean.product_object.setTitle(product_object.getTitle());
		cBean.product_object.setLocal_main_img_url(product_object.getMain_img_url());
		cBean.product_object.setCurrent_price(product_money);
		return cBean;
	}

}

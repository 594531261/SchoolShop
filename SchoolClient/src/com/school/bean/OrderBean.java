package com.school.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.media.JetPlayer.OnJetEventListener;

public class OrderBean extends BaseBean{
//	 [order_id] => 4
//	            [order_num] => 140826060600    订单编号
//	            [address_id] => 2             地址id
//	            [order_status] => 0            订单状态 0待审核 1已审核 2已发货 3已完成 4已取消 6已删除
//	            [order_note] => 备注情况
//	            [order_debug] =>             管理员备注
//	            [addtime] => 1409047560       添加时间
//	            [shentime] =>                 审核时间
//	            [fatime] =>                     
//	            [wantime] =>
//	            [order_money] =>
	
	public String order_id;
	public String order_num;
	public String address_id;
	public String order_status;
	public String order_note;
	public String order_debug;
	public String addtime;
	public String shentime;
	public String fatime;
	public String wantime;
	public String order_money;
	public ArrayList<OrderProduceBean> mOrderProduceBeans = new ArrayList<OrderProduceBean>();
	
	public double getPrice(){
		double dbPrice = 0.0;
		try {
			dbPrice = Double.parseDouble(order_money);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbPrice;
	}
	
	public boolean isCanCanCel(){
		return "0".equals(order_status);
	}
	
	public String getStatus(){
//		状态0待审核,1 已审核待发货2已发货，3完成 4取消 5 订单有误 6订单删除
		int status = Integer.parseInt(order_status);
		String strStatus = "待审核";
		switch (status) {
		case 0:
			strStatus = "待审核";
			break;
		case 1:
			strStatus = "已审核待发货";
			break;
		case 2:
			strStatus = "已发货";
			break;
		case 3:
			strStatus = "完成";
			break;
		case 4:
			strStatus = "取消";
			break;
		case 5:
			strStatus = "订单有误";
			break;
		case 6:
			strStatus = "订单删除";
			break;

		default:
			break;
		}
		return strStatus;
	}
	
	public void parse(JSONObject oj){
		this.order_id = getStringFromJson(oj, "order_id");
		this.order_num = getStringFromJson(oj, "order_num");
		this.address_id = getStringFromJson(oj, "address_id");
		this.order_status = getStringFromJson(oj, "order_status");
		this.order_note = getStringFromJson(oj, "order_note");
		this.order_debug = getStringFromJson(oj, "order_debug");
		this.addtime = getStringFromJson(oj, "addtime");
		this.shentime = getStringFromJson(oj, "shentime");
		this.fatime = getStringFromJson(oj, "fatime");
		this.wantime = getStringFromJson(oj, "wantime");
		this.order_money = getStringFromJson(oj, "order_money");
		try {
			JSONArray ja = oj.getJSONArray("product_list");
			for(int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				OrderProduceBean op = new OrderProduceBean();
				op.parse(jo);
				mOrderProduceBeans.add(op);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}

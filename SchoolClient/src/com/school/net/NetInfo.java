package com.school.net;

import org.json.JSONException;
import org.json.JSONObject;

public class NetInfo {
	public final static String SMS = "http://182.92.186.192/api.php/user/sendsms/";
	public final static String CATEGORY = "http://182.92.186.192/api.php/version/getCategoryList/";
	public final static String VERIFY = "http://182.92.186.192/api.php/user/phoneverify/";
	public final static String PRODUCELIST = "http://182.92.186.192/api.php/product/getproductlist/";
	public final static String UPDATECHECK = "http://182.92.186.192/api.php/version/updatecheck/";
	public final static String SCHOOLLIST = "http://182.92.186.192/api.php/version/getSchoolList/";
	public final static String PRODUCE = "http://182.92.186.192/api.php/product/getproduct/";
	public final static String ADDCART = "http://182.92.186.192/api.php/cart/addcart/";
	public final static String DELCART = "http://182.92.186.192/api.php/cart/delcart/";
	public final static String CARTLIST = "http://182.92.186.192/api.php/cart/getcartlist/";
	public final static String ADDORDER = "http://182.92.186.192/api.php/order/addorder/";
	public final static String DELORDER = "http://182.92.186.192/api.php/order/delorder/";
	public final static String ORDERLIST = "http://182.92.186.192/api.php/order/getorderlist/";
	public final static String ADDRESS = "http://182.92.186.192/api.php/address/getaddress/";
	public final static String UPDATEADDRESS = "http://182.92.186.192/api.php/address/updateaddress/";
	public final static String GETPNG = "http://182.92.186.192/upload/";
	public final static String PRODUCELISTA = "http://182.92.186.192/api.php/product/getProductLista/";
	
	
	public final static String RTN_CODE = "code";
	public final static String FLAG_SUCCEED = "1";
	public final static String RTN_DATA = "data";
	
	public static boolean isSucceed(JSONObject ite){
		String code = "";
		try {
			code = new String(ite.getString(RTN_CODE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "1".equals(code);
	}
}

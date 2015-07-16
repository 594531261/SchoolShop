package com.school.schoolclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyFinalHttp;
import com.school.application.Myapp;
import com.school.bean.AddressBean;
import com.school.bean.CartBean;
import com.school.net.NetInfo;
import com.school.net.SignUtil;
import com.school.utils.Utils;
import com.school.viewswitcher.ShowAllListView;

public class ConfirmOrder extends Activity {
	private static final int REQUESTCODE_ADDRESS = 1100;
	private LinearLayout confirm_song, shop_six, confirm_fu;
	private List<CartBean> list = null;
	private ShowAllListView listview;
	private MyAdapter adapter;
//	private String product_id;
//	private String chicun;
//	private String color;
//	private String count;
//	private String title;
//	private String bean_price;
//	private String bean_imgurl;
	private String address_id;
	private String tags;
	private FinalBitmap finalBitmap;
	private String product_list;
	private TextView ar_detail, ar_person, ar_xian;
	private TextView txt_total_num, txt_total_price;
	
	boolean isCanCancel = false;
	boolean isCancelOrderl = false;
	String order_id ="";
	
	private AddressBean mAddressBean = new AddressBean();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shopthree);
		

		LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);
		ll_right.setVisibility(View.GONE);
		
		finalBitmap = FinalBitmap.create(this);
		finalBitmap.setExitTasksEarly(false);
		finalBitmap.configBitmapLoadThreadSize(5);
		listview = (ShowAllListView) findViewById(R.id.listView_order);
		txt_total_num = (TextView) findViewById(R.id.txt_total_num);
		txt_total_price = (TextView) findViewById(R.id.txt_total_price);
		
		list = (List<CartBean>) getIntent().getSerializableExtra("list");
		
		order_id = getIntent().getStringExtra("order_id");
		isCanCancel = getIntent().getBooleanExtra("isCanCancel", false);
		isCancelOrderl = getIntent().getBooleanExtra("isCancelOrderl", false);
		
//		product_id = this.getIntent().getStringExtra("product_id");
//		chicun = this.getIntent().getStringExtra("chicun");
//		color = this.getIntent().getStringExtra("color");
//		count = this.getIntent().getStringExtra("count");
//
//		title = this.getIntent().getStringExtra("title");
//		bean_price = this.getIntent().getStringExtra("bean_price");
//		bean_imgurl = this.getIntent().getStringExtra("bean_imgurl");
		tags = this.getIntent().getStringExtra("tags");
//		list = new ArrayList<HashMap<String, String>>();
//		if ("1".equals(tags)) {
//			initData();
//		} else {
//			product_list = this.getIntent().getStringExtra("product_list");
//		}

		initview();
		adapter = new MyAdapter();
		listview.setAdapter(adapter);
		
		initTotalInfo();

	}

	public void backfinish(View view) {

		this.finish();
	}

	public void submit(View view) {
		if ("1".equals(tags)) {
			Message msg = handler.obtainMessage();
			msg.what = 2;
			handler.sendMessage(msg);
		} else {
			if(!"".equals(mAddressBean.address_id)){
				
				Message msg = handler.obtainMessage();
				msg.what = 3;
				handler.sendMessage(msg);
			}else{
				Toast.makeText(this, "地址不能为空", Toast.LENGTH_LONG).show();
			}
		}

		// startActivity(new Intent(this,ShopSet.class));
	}

	public void goaddress(View view) {
		if(!isCancelOrderl){
			Intent intent = new Intent(this, ShopAdress.class);
			intent.putExtra("mAddressBean", mAddressBean);
			
			startActivityForResult(intent, REQUESTCODE_ADDRESS);
		}
	}

	private void initview() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		int height = (mScreenHeigh / 3 * 2) / 5 - 10;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 100);
		confirm_song = (LinearLayout) findViewById(R.id.confirm_song);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 80);
		confirm_song.setLayoutParams(params);
		shop_six = (LinearLayout) findViewById(R.id.shop_six);
		shop_six.setLayoutParams(params);
		confirm_fu = (LinearLayout) findViewById(R.id.confirm_fu);
		confirm_fu.setLayoutParams(params);
		ar_xian = (TextView) findViewById(R.id.ar_xian);
		ar_detail = (TextView) findViewById(R.id.ar_detail);
		ar_person = (TextView) findViewById(R.id.ar_person);
		Message msg = handler.obtainMessage();
		msg.what = 1;
		handler.sendMessage(msg);
		
		TextView txt_title = (TextView) findViewById(R.id.txt_view_title);
	
		LinearLayout	confirm_subit = (LinearLayout) findViewById(R.id.confirm_subit);
		TextView txt_confirm_subit = (TextView) findViewById(R.id.txt_confirm_subit);
		if(isCancelOrderl){
			findViewById(R.id.iv_more).setVisibility(View.GONE);
			if(!isCanCancel){
				confirm_subit.setVisibility(View.GONE);
			}
			txt_confirm_subit.setText(getString(R.string.cancelorder));
			txt_title.setText(getString(R.string.cancelorder));
		}else{
			txt_title.setText(getString(R.string.confirmorder));
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(Myapp.isNetworkAvailable(ConfirmOrder.this)){
					loadAddress();
				}else{
					String wifigps = ConfirmOrder.this.getString(R.string.wifigps);
					Toast.makeText(ConfirmOrder.this, wifigps,Toast.LENGTH_LONG).show();
				}
				break;
			case 2:
//				loadAddOrder();
//				break;
			case 3:
				if(Myapp.isNetworkAvailable(ConfirmOrder.this)){
					if(isCancelOrderl){
						loadCancelCartOrder();
					}else{
						loadAddCartOrder();
					}
				}else{
					String wifigps = ConfirmOrder.this.getString(R.string.wifigps);
					Toast.makeText(ConfirmOrder.this, wifigps,Toast.LENGTH_LONG).show();
				}
				break;

			}
		}
	};
	private FinalHttp finalHttp;

	private void loadAddress() {
		finalHttp = new MyFinalHttp(this);
		List<String> srcList = new ArrayList<String>();
		srcList.add("user_id");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user_id", Myapp.user_id);

		String sign = SignUtil.generateSignature(srcList, map);

		AjaxParams params = new AjaxParams();
		params.put("user_id", Myapp.user_id);
		params.put("sign", sign.substring(5, sign.length() - 5));
		finalHttp.post(NetInfo.ADDRESS, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {

				super.onSuccess(t);
				JSONObject ite;
				try {
					ite = new JSONObject(t);
					String code = new String(ite.getString("code"));
					String getmsg = new String(ite.getString("msg"));
					if ("1".equals(code)) {
						// String msgs = new String(ite.getString("msg"));
						String data = new String(ite.getString("data"));
						JSONObject da = new JSONObject(data);
						mAddressBean.parse(da);
						refreshAddress();
					
					}else{
						Toast.makeText(ConfirmOrder.this, getmsg, Toast.LENGTH_LONG).show();
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {

				super.onFailure(t, errorNo, strMsg);
			}
		});
	}
	
	
	private void refreshAddress(){
		ar_xian.setText(mAddressBean.school_name);
		ar_detail.setText(mAddressBean.address_detail);
		ar_person.setText(mAddressBean.address_uname + "  " + mAddressBean.address_uphone);
		address_id = mAddressBean.address_id;
	}


	private void loadAddCartOrder() {
		finalHttp = new MyFinalHttp(this);
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("user_id", Myapp.user_id);
		map.put("product_list", getJsonStr());
		map.put("order_note", "备注情况");
		map.put("address_id", mAddressBean.address_id);
		AjaxParams params = SignUtil.getSignatureAjaxParams(map);
		finalHttp.post(NetInfo.ADDORDER, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {

				super.onSuccess(t);
				JSONObject ite;
				try {
					ite = new JSONObject(t);
					String code = new String(ite.getString("code"));
					String msgs = new String(ite.getString("msg"));
					if ("1".equals(code)) {
						// Message msg = handler.obtainMessage();
						// msg.what=3;
						// Bundle bundle = new Bundle();
						// bundle.putString("msg", msgs);
						// msg.setData(bundle);
						// handler.sendMessage(msg);
						ConfirmOrder.this.finish();
					}else{
						Toast.makeText(ConfirmOrder.this, msgs, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {

				super.onFailure(t, errorNo, strMsg);
			}
		});
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Object getItem(int position) {

			return list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public boolean isEnabled(int position) {

			return super.isEnabled(position);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder;
			HashMap<String, String> map = new HashMap<String, String>();
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_item, null);
				holder = new ViewHolder();
				holder.confirm_img = (ImageView) convertView.findViewById(R.id.confirm_img);
				holder.confirm_title = (TextView) convertView.findViewById(R.id.confirm_title);
				holder.confirm_cur = (TextView) convertView.findViewById(R.id.confirm_cur);
				holder.confirm_count = (TextView) convertView.findViewById(R.id.confirm_count);
				holder.confirm_color = (TextView) convertView.findViewById(R.id.confirm_color);
				holder.confirm_chima = (TextView) convertView.findViewById(R.id.confirm_chima);
				holder.confirm_all = (TextView) convertView.findViewById(R.id.confirm_all);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			
			CartBean cBean = list.get(position);
			if (cBean.getImgUrl() != null){
				finalBitmap.display(holder.confirm_img,cBean.getImgUrl());
			}
			holder.confirm_title.setText((CharSequence) cBean.getTitle());
			holder.confirm_cur.setText("￥"  + cBean.getPrice());
			holder.confirm_count.setText((CharSequence) cBean.getProduct_num());
			holder.confirm_color.setText((CharSequence) cBean.product_color);
			holder.confirm_chima.setText((CharSequence) cBean.product_size);
			holder.confirm_all.setText("￥" + (cBean.getPrice()*Integer.parseInt(cBean.getProduct_num())));
			return convertView;
		}

	}

	class ViewHolder {
		ImageView confirm_img;
		TextView confirm_title;
		TextView confirm_cur;
		TextView confirm_count;
		TextView confirm_color;
		TextView confirm_chima;
		TextView confirm_all;
	}

	
	private String getJsonStr(){
		JSONObject obj = new JSONObject();
		JSONArray jason = new JSONArray();
		try {
			for (int i = 0; i < list.size(); i++) {
				JSONObject objtemp = new JSONObject();
				objtemp.put("product_id", list.get(i).product_id);
				objtemp.put("product_size", list.get(i).product_size);
				objtemp.put("product_num", list.get(i).getProduct_num());
				objtemp.put("product_color",list.get(i).product_color);
				jason.put(objtemp);
			}
			obj.put("product_list", jason);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	
	private void initTotalInfo(){
		double totalPrice = 0.0;
		int totalNum = 0;
		for(int i=0;i<list.size();i++){
			CartBean cBean = list.get(i);
			int intNum =Integer.parseInt(cBean.getProduct_num());
			totalPrice += (cBean.product_object.getDoubleCurrentPrice() * intNum);
			totalNum += intNum;
		}
		
		txt_total_price.setText("￥" + Utils.getFormat(totalPrice));
		txt_total_num.setText("" + totalNum);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUESTCODE_ADDRESS){
			if(resultCode == RESULT_OK){
				mAddressBean = (AddressBean) data.getSerializableExtra("mAddressBean");
				refreshAddress();
			}
		}
	}
	

	private void loadCancelCartOrder() {
		finalHttp = new MyFinalHttp(this);
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("user_id", Myapp.user_id);
		map.put("order_id", order_id);
		AjaxParams params = SignUtil.getSignatureAjaxParams(map);
		finalHttp.post(NetInfo.DELORDER, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {

				super.onSuccess(t);
				JSONObject ite;
				try {
					ite = new JSONObject(t);
					String code = new String(ite.getString("code"));
					String msgs = new String(ite.getString("msg"));
					if ("1".equals(code)) {
						Toast.makeText(ConfirmOrder.this, msgs, Toast.LENGTH_LONG).show();
						ConfirmOrder.this.finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {

				super.onFailure(t, errorNo, strMsg);
			}
		});
	}
	
}

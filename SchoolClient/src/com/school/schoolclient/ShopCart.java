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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.MyFinalHttp;
import com.school.application.Myapp;
import com.school.bean.CartBean;
import com.school.net.NetInfo;
import com.school.net.SignUtil;
import com.school.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class ShopCart extends Activity {
	private List<CartBean> list = null;
	private ArrayList<CartBean> selected_listkey = null;
	private ListView listview;
	private MyAdapter adapter;
	private FinalHttp finalHttp;
	private FinalBitmap finalBitmap;
	private TextView txt_total_price;
	private ImageView cart_person;
	ImageView cart_all;
	LinearLayout ll_right;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shopcart);
		
		TextView txt_title = (TextView) findViewById(R.id.txt_view_title);
		txt_title.setText(getString(R.string.cars));
		ll_right = (LinearLayout) findViewById(R.id.ll_right);
		ll_right.setVisibility(View.VISIBLE);
		edit_car = (TextView) findViewById(R.id.edit_car);
		edit_car.setText(getResources().getString(R.string.carsupdate));
		
		
		txt_total_price = (TextView) findViewById(R.id.txt_total_price);
		finalBitmap = FinalBitmap.create(this);
		finalBitmap.setExitTasksEarly(false);
		finalBitmap.configBitmapLoadThreadSize(10);
		listview = (ListView) findViewById(R.id.listView_cart);
		list = new ArrayList<CartBean>();
		selected_listkey = new ArrayList<CartBean>();
		initview();
		// initData();
		adapter = new MyAdapter();
		listview.setAdapter(adapter);
	}

	public void set(View view) {
		Intent intent = new Intent(this, ConfirmOrder.class);
		intent.putExtra("list", selected_listkey);
//		intent.putExtra("product_list", obj.toString());
//		intent.putExtra("tags", "2");

		startActivity(intent);
	}

	 public void del(View view){
    	 Message msg = handler.obtainMessage();
     	msg.what=3;
     	handler.sendMessage(msg);
    }
	
	boolean isSel = true;

	public void allsel(View view) {
		selected_listkey.clear();
		if (isSel) {
			cart_all.setBackgroundResource(R.drawable.selected_cirle);
			for (int i = 0; i < list.size(); i++) {
				selected_listkey.add(list.get(i));
				list.get(i).seltag = "1";
				isSel = false;
			}
		} else {
			selected_listkey.clear();
			cart_all.setBackgroundResource(R.drawable.select_cirle);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).seltag = "0";
				isSel = true;
			}
		}

		adapter.notifyDataSetChanged();
	}

	private LinearLayout cart_oneg;
	private TextView edit_car;

	private void initview() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		int height = (mScreenHeigh / 3 * 2) / 5 - 10;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, Utils.dip2px(getApplication(),60));
		cart_oneg = (LinearLayout) findViewById(R.id.cart_oneg);
		cart_oneg.setLayoutParams(params);
		LinearLayout.LayoutParams editcar = new LinearLayout.LayoutParams(Utils.dip2px(getApplication(),103),Utils.dip2px(getApplication(),51));
		editcar.gravity = Gravity.CENTER_VERTICAL;
		
		final	TextView txt_confrim = (TextView) findViewById(R.id.txt_confrim);
		cart_all = (ImageView) findViewById(R.id.cart_all);
		ll_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(edit_car.getText().equals(getString(R.string.carsupdate))){
					edit_car.setText(getString(R.string.wanc));
					txt_confrim.setText(getString(R.string.del));
				}else{
					edit_car.setText(getString(R.string.carsupdate));
					txt_confrim.setText(getString(R.string.gojie));
				}
			}
		});
		
	}

	public void cartconfim(View v){
		if(edit_car.getText().equals(getString(R.string.carsupdate))){
			set(v);
		}else{
			del(v);
		}
	}
	
	private class MyAdapter extends BaseAdapter {
		
		public void notifyDataSetChanged(){
			super.notifyDataSetChanged();
			refreshTotalPrice();
		}

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
			View view = convertView;
			HashMap<String, String> map = new HashMap<String, String>();
			final CartBean cBean = list.get(position);
			
			final String key = cBean.seltag;
			view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.cart_item, null);
			final ImageView cart_img = (ImageView) view.findViewById(R.id.cart_img);
			final ImageView cart_person = (ImageView) view.findViewById(R.id.cart_person);

			// finalBitmap.configLoadingImage(R.drawable.kuzi);
			finalBitmap.display(cart_person, cBean.product_object.getMain_img_url());

			String product_id = cBean.product_id;

			final TextView cart_txt = (TextView) view.findViewById(R.id.cart_txt);
			cart_txt.setText(cBean.product_object.getTitle());

			final TextView cart_price = (TextView) view.findViewById(R.id.cart_price);
			cart_price.setText(cBean.product_object.getDoubleCurrentPrice()+"");

			final TextView cart_count = (TextView) view.findViewById(R.id.cart_count);
			cart_count.setText(cBean.getProduct_num());

			final TextView cart_color = (TextView) view.findViewById(R.id.cart_color);
			cart_color.setText(cBean.product_color);

			final TextView cart_size = (TextView) view.findViewById(R.id.cart_size);
			cart_size.setText(cBean.product_size);

			if ("0".equals(key)) {
				cart_img.setBackgroundResource(R.drawable.select_cirle);
			} else {
				cart_img.setBackgroundResource(R.drawable.selected_cirle);
			}

			cart_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String key = cBean.seltag;
					if ("0".equals(key)) {
						selected_listkey.add(cBean);
						cart_img.setBackgroundResource(R.drawable.selected_cirle);
						cBean.seltag = "1";
					} else {
						selected_listkey.remove(cBean);
						cart_img.setBackgroundResource(R.drawable.select_cirle);
						cBean.seltag = "0";
					}
					if(selected_listkey.size() == list.size()){
						cart_all.setBackgroundResource(R.drawable.selected_cirle);
						isSel = true;
					}else{
						cart_all.setBackgroundResource(R.drawable.select_cirle);
						isSel = false;
					}
					refreshTotalPrice();
				}
			});

			return view;
		}

	}

	private void loadGetCart() {
		finalHttp = new MyFinalHttp(this);
		List<String> srcList = new ArrayList<String>();
		srcList.add("user_id");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user_id", Myapp.user_id);

		String sign = SignUtil.generateSignature(srcList, map);

		AjaxParams params = new AjaxParams();
		params.put("user_id", Myapp.user_id);

		params.put("sign", sign.substring(5, sign.length() - 5));
		finalHttp.post(NetInfo.CARTLIST, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				JSONObject ite;
				try {
					ite = new JSONObject(t);
					String code = new String(ite.getString("code"));
					String getmsg = new String(ite.getString("msg"));
				
					if ("1".equals(code)) {
						
						isSel = false;
						cart_all.setBackgroundResource(R.drawable.select_cirle);
						list.clear();
						selected_listkey.clear();
						
						JSONArray object = ite.getJSONArray("data");
						for (int i = 0; i < object.length(); i++) {
							CartBean cBean = new CartBean();
							cBean.parse(object.getJSONObject(i));
							list.add(cBean);

						}
						Message msg = handler.obtainMessage();
						msg.what = 2;
						handler.sendMessage(msg);
					}else{
						Toast.makeText(ShopCart.this, getmsg, Toast.LENGTH_LONG).show();
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

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(Myapp.isNetworkAvailable(ShopCart.this)){
					loadGetCart();
				}else{
					String wifigps = ShopCart.this.getString(R.string.wifigps);
					Toast.makeText(ShopCart.this, wifigps,Toast.LENGTH_LONG).show();
				}
				break;
			case 2:
				adapter.notifyDataSetChanged();
				break;
			case 3:
				if(Myapp.isNetworkAvailable(ShopCart.this)){
					delCart();
				}else{
					String wifigps = ShopCart.this.getString(R.string.wifigps);
					Toast.makeText(ShopCart.this, wifigps,Toast.LENGTH_LONG).show();
				}
				break;

			}
		}
	};
	
	  private void delCart(){
	    	for(int i=0;i<selected_listkey.size();i++){
	    		String cart_id = selected_listkey.get(i).cart_id;
	    		finalHttp = new MyFinalHttp(this);
	    		TreeMap<String , String> map = new TreeMap<String , String>();
	    		map.put("user_id", Myapp.user_id);
	    		map.put("cart_id", cart_id);
	    		
	    		
	    		AjaxParams params = SignUtil.getSignatureAjaxParams(map);
	    		finalHttp.post(NetInfo.DELCART, params, new AjaxCallBack<String>() {
	    			
	    			@Override
	    			public void onSuccess(String t) {
	    				super.onSuccess(t);
	    				JSONObject ite;
	    				try {
	    					ite = new JSONObject(t);
	    					String code = new String(ite.getString("code"));
	    					String getmsg = new String(ite.getString("msg"));
	    					Toast.makeText(ShopCart.this, getmsg, Toast.LENGTH_LONG).show();
	    					if("1".equals(code)){
	    						 Message msg = handler.obtainMessage();
	    					    	msg.what=1;
	    					    	handler.sendMessage(msg);
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

	public void backfinish(View view) {

		this.finish();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		Message msg = handler.obtainMessage();
		msg.what = 1;
		handler.sendMessage(msg);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	public void refreshTotalPrice(){
		double totalPrice = 0.0;
		for (int i = 0; i < selected_listkey.size(); i++) {
			CartBean cBean = selected_listkey.get(i);
			totalPrice += (cBean.product_object.getDoubleCurrentPrice()*Integer.parseInt(cBean.getProduct_num()));
		}
		txt_total_price.setText("￥" + Utils.getFormat(totalPrice));
	}
}

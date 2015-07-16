package com.school.schoolclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.MyFinalHttp;
import com.school.application.Myapp;
import com.school.bean.CartBean;
import com.school.bean.OrderBean;
import com.school.bean.Produce;
import com.school.net.NetInfo;
import com.school.net.SignUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单列表
 * @author wayne
 *
 */
public class ShopOrder extends Activity {
	Handler mHandler = new Handler();
	private List<OrderBean> list = new ArrayList<OrderBean>();
	private List<String> groupkey = new ArrayList<String>();
	private List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> bList = new ArrayList<HashMap<String, String>>();
	private ListView listview;
	private FinalBitmap finalBitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shoporder);

		LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);
		ll_right.setVisibility(View.GONE);
		TextView txt_title = (TextView) findViewById(R.id.txt_view_title);
		txt_title.setText(getString(R.string.allorders));
		
		finalBitmap = FinalBitmap.create(this);
		finalBitmap.setExitTasksEarly(false);
		finalBitmap.configBitmapLoadThreadSize(10);
		
		listview = (ListView) findViewById(R.id.listView_list);
		MyAdapter adapter = new MyAdapter();
		listview.setAdapter(adapter);
	}

	public void cart(View view) {

		startActivity(new Intent(this, ShopSet.class));
	}
	private void loadData() {
		FinalHttp finalHttp =  new MyFinalHttp(this);
		List<String> srcList = new ArrayList<String>();
		srcList.add("user_id");
		
		HashMap<String , String> map = new HashMap<String , String>();
		map.put("user_id", Myapp.user_id);
		
		
		String sign = SignUtil.generateSignature(srcList,map);
		
		AjaxParams params = new AjaxParams();
		params.put("user_id", Myapp.user_id);
		params.put("sign", sign.substring(5,sign.length()-5));
		finalHttp.post(NetInfo.ORDERLIST, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				JSONObject ite;
					try {
						ite = new JSONObject(t);
						String code = new String(ite.getString("code"));
						String getmsg = new String(ite.getString("msg"));
						if("1".equals(code)){
							JSONArray object = ite.getJSONArray("data");
							final List<OrderBean> tList = new ArrayList<OrderBean>();
							for(int i = 0; i < object.length(); i++) {//遍历JSONArray  
				                JSONObject oj = object.getJSONObject(i);
				                OrderBean orderBean = new OrderBean();
				                orderBean.parse(oj);
				                tList.add(orderBean);
							}
							mHandler.post(new Runnable() {
								
								@Override
								public void run() {
									list.clear();
									list.addAll(tList);
									((BaseAdapter)(listview.getAdapter())).notifyDataSetChanged();
								}
							});
						}else{
							Toast.makeText(ShopOrder.this, getmsg, Toast.LENGTH_LONG).show();
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
			if (groupkey.contains(getItem(position))) {
				return false;
			}
			return super.isEnabled(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_item, null);

			final OrderBean tOrderBean = list.get(position);
			LinearLayout shop_one = (LinearLayout) view.findViewById(R.id.shop_one);
			shop_one.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					gotoEditOrder(tOrderBean);
				}
			});

			TextView order_no = (TextView) view.findViewById(R.id.order_no);
			order_no.setText(tOrderBean.order_num);

			TextView order_price = (TextView) view.findViewById(R.id.order_price);
			order_price.setText("￥" +tOrderBean.getPrice());

			TextView order_date = (TextView) view.findViewById(R.id.order_date);
			order_date.setText(com.school.utils.Utils.getStringTimeLong(tOrderBean.addtime));

			TextView order_status = (TextView) view.findViewById(R.id.order_status);
			order_status.setText(tOrderBean.getStatus());
			
			ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
			finalBitmap.display(iv_img, tOrderBean.mOrderProduceBeans.get(0).product_object.getMain_img_url());			
			
			TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
			
			txt_title.setText(tOrderBean.mOrderProduceBeans.get(0).product_object.getTitle());
			
			return view;
		}

	}

	public void backfinish(View view) {

		this.finish();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if(Myapp.isNetworkAvailable(ShopOrder.this)){
			loadData();
		}else{
			String wifigps = ShopOrder.this.getString(R.string.wifigps);
			Toast.makeText(ShopOrder.this, wifigps,Toast.LENGTH_LONG).show();
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	
	public void gotoEditOrder(OrderBean tOrderBean){
		Intent intent = new Intent(this, ConfirmOrder.class);
		int size = tOrderBean.mOrderProduceBeans == null?0: tOrderBean.mOrderProduceBeans.size();
		ArrayList ls = new ArrayList<CartBean>();
		for(int i=0; i<size;i++){
			ls.add(tOrderBean.mOrderProduceBeans.get(i).getCartBean());
		}
		intent.putExtra("list", ls);
		intent.putExtra("order_id", tOrderBean.order_id);
		intent.putExtra("isCanCancel", tOrderBean.isCanCanCel());
		intent.putExtra("isCancelOrderl", true);
		startActivity(intent);
	}
}

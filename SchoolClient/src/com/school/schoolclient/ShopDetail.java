package com.school.schoolclient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TreeMap;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyFinalHttp;
import com.school.application.Myapp;
import com.school.bean.Produce;
import com.school.net.NetInfo;
import com.school.net.SignUtil;
import com.school.utils.CustomDigitalClock;
import com.school.utils.Utils;
import com.school.utils.CustomDigitalClock.ClockListener;
import com.umeng.analytics.MobclickAgent;

public class ShopDetail extends Activity {
	private ImageView img_main;
	private TextView yj;
	private View shop_five_line,shop_four_line;
	private ImageView next;
	private LinearLayout shop_one, shop_two, shop_three, shop_four,shop_four1 , shop_five,
			shop_six1, shop_six2, biaoqian, des_titile, shop_eight, shop_seven;
	private TextView detail_title, detail_zhe, detail_ori, detail_cur,
			detail_count, detail_favorable;
	private CustomDigitalClock detail_date;
	int product_id;
	
	private Produce mProduce = new Produce();
	private MyReceiver broadservice;
	private String main_img_size;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shopone);
		finalHttp =  new MyFinalHttp(this);
		finalBitmap = FinalBitmap.create(this);
		finalBitmap.setExitTasksEarly(false);
		finalBitmap.configBitmapLoadThreadSize(5);

		product_id = this.getIntent().getIntExtra("product_id", -1);
		main_img_size = getIntent().getStringExtra("main_img_size");
		
		next = (ImageView) findViewById(R.id.next);
		img_main = (ImageView) findViewById(R.id.img);
		img_main.setScaleType(ScaleType.CENTER_INSIDE);
		img_main.setBackgroundResource(R.drawable.default_img);
//		LinearLayout.LayoutParams llps = new LayoutParams(width, Utils.getHeight(width, img_size))
		
		img_main.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopDetail.this, DetailPng.class);
				if(mProduce == null 
						|| mProduce.getIntroduction_img_url() == null 
						|| mProduce.getIntroduction_img_url().equals("")){
					return;
				}
				intent.putExtra("intro_url", mProduce.getIntroduction_img_url());
				intent.putExtra("intro_size", mProduce.getIntroduction_img_size());
				startActivity(intent);
				
			}
		});
		

		detail_title = (TextView) findViewById(R.id.detail_title);
		detail_zhe = (TextView) findViewById(R.id.detail_zhe);
		detail_ori = (TextView) findViewById(R.id.detail_ori);
		detail_cur = (TextView) findViewById(R.id.detail_cur);
		detail_count = (TextView) findViewById(R.id.detail_count);
		detail_date = (CustomDigitalClock) findViewById(R.id.detail_date);
		
		detail_date.setClockListener(new ClockListener() {
			
			@Override
			public void timeEnd() {
				shop_four.setVisibility(View.GONE);
				detail_ori.setVisibility(View.GONE);
				detail_cur.setText(mProduce.getStrOriginal_price()+"");
			}
			
			@Override
			public void remainFiveMinutes() {
				
			}
		});
		detail_favorable = (TextView) findViewById(R.id.detail_favorable);
		shop_five_line = (View) findViewById(R.id.shop_five_line);
		shop_four_line = (View) findViewById(R.id.shop_four_line);
		initview();
		broadservice = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.islogin.one");
		this.registerReceiver(broadservice, filter);
	}

	private FinalHttp finalHttp;
	private FinalBitmap finalBitmap;

	private void loadData() {
		String local_time = String.valueOf(System.currentTimeMillis());
		String pid = String.valueOf(product_id).trim();

		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("user_id", local_time);
		map.put("product_id", pid);

		finalHttp = new MyFinalHttp(this);
		AjaxParams params = SignUtil.getSignatureAjaxParams(map);
		finalHttp.post(NetInfo.PRODUCE, params, new AjaxCallBack<String>() {

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
						String data = new String(ite.getString("data"));
						JSONObject da = new JSONObject(data);
						mProduce = new Produce();
						mProduce.parse(da);
						
//						bean_des = da.getString("title");
//						bean_price = "￥" + da.getString("current_price");
//						bean_chicun = da.getString("size");
//						bean_color = da.getString("color");
//						intro_url = da.getString("introduction_img_url");
						long endTime =mProduce.getEndtime();
//						detail_date.reStart(System.currentTimeMillis()+20*60*60*1000);
						detail_date.reStart(endTime);
						detail_title.setText(mProduce.getTitle());

						detail_ori.setText( mProduce.getStrOriginal_price());
//						String sales = mProduce.getTotal_sales();
/*
						if (sales != null && !"".equals(sales)) {
							int sa = Integer.parseInt(sales);
							if (sa >= 10000) {
								sales = "1W";
							}
						} else {
							sales = "0";
						}*/
						
						String product_count = mProduce.getTotal_sales();
						
						if(Integer.parseInt(product_count)>10000){
							double prc = 0.0f;
							String currr = product_count;
							double cur = Double.parseDouble(currr);
							double ore = 10000;
							prc = cur / ore;
							String pc = String.valueOf(prc).trim();
							if(pc.length()>4){
								product_count = pc.substring(0, 4)+"w";
							}else{
								product_count=pc+"w";
							}
						}
						
						detail_count.setText(product_count);
						
						
						
						
						if(mProduce.hasCurrentPrice()){
							detail_cur.setText(mProduce.getStrCurrent_price());
						}else{
							detail_ori.setVisibility(View.GONE);
							detail_cur.setText(mProduce.getStrOriginal_price());
						}

						detail_favorable.setText(mProduce.getActivity_info());

					String	bean_imgurl =  mProduce.getMain_img_url();
						// Bitmap bitmap = getBitmapFromNetWork(url);
						// img_main.setImageBitmap(bitmap);

						// finalBitmap.configLoadingImage(R.drawable.kuzi);
						finalBitmap.display(img_main, bean_imgurl);

						double zhe = mProduce.getZhekou();
						String zhe1 = String.valueOf(zhe);
						String zhes;
						if (zhe1.length() > 3) {
							zhes = zhe1.substring(0, 3);
						} else {
							zhes = zhe1;
						}

						detail_zhe.setText(zhes + "折");
						if (zhe == 0.0) {
							biaoqian.setVisibility(View.GONE);

						}
						if(endTime==0){
							biaoqian.setVisibility(View.GONE);
							shop_four1.setVisibility(View.GONE);
							shop_four_line.setVisibility(View.GONE);
						}
						
						// JSONArray object = da.getJSONArray("product_object");
						// for(int i = 0; i < object.length(); i++)
						// {//遍历JSONArray
						// Produce bean = new Produce();
						// JSONObject oj = object.getJSONObject(i);
						// bean.setProduct_id(oj.getInt("product_id"));
						// bean.setTitle(oj.getString("title"));
						// bean.setBrand(oj.getString("brand"));
						// bean.setBusinessmam(oj.getString("businessmam"));
						// bean.setOriginal_price(oj.getInt("original_price"));
						// bean.setCurrent_price(oj.getString("current_price"));
						// bean.setMain_img_url(oj.getString("main_img_url"));
						//
						//
						//
						//
						//
						// }
					}else{
						Toast.makeText(ShopDetail.this, getmsg, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}

	private void initview() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		int mScreenWright = dm.widthPixels;
		// yj = (TextView)findViewById(R.id.yj);
		detail_ori.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				Myapp.width, Utils.getHeight(Myapp.width,main_img_size));
		shop_one = (LinearLayout) findViewById(R.id.shop_one);
		shop_one.setLayoutParams(params);
		finalBitmap.configBitmapMaxWidth(mScreenWright);
		finalBitmap.configBitmapMaxHeight(Utils.getHeight(mScreenWright, main_img_size));

		img_main.setLayoutParams(params);
		// LinearLayout.LayoutParams param = new
		// LinearLayout.LayoutParams(mScreenWright,133);
		shop_two = (LinearLayout) findViewById(R.id.shop_two);
		// shop_two.setLayoutParams(param);
		//
		// LinearLayout.LayoutParams paramd = new
		// LinearLayout.LayoutParams(mScreenWright/10*8,133);
		// des_titile = (LinearLayout)findViewById(R.id.des_titile);
		// des_titile.setLayoutParams(paramd);
		//
		// LinearLayout.LayoutParams paramq = new
		// LinearLayout.LayoutParams(mScreenWright/10*2,133);
		// paramq.rightMargin=5;
		biaoqian = (LinearLayout) findViewById(R.id.biaoqian);
		// qiangou.setLayoutParams(paramq);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dip2px(getApplication(),50));
		shop_three = (LinearLayout) findViewById(R.id.shop_three);
		shop_three.setLayoutParams(param);
		param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dip2px(getApplication(),50));
		shop_four = (LinearLayout) findViewById(R.id.shop_four);
		shop_four1 = (LinearLayout) findViewById(R.id.shop_four1);
		shop_four1.setLayoutParams(param);
		param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dip2px(getApplication(),50));
		shop_five = (LinearLayout) findViewById(R.id.shop_five);
		shop_five.setLayoutParams(param);
		param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dip2px(getApplication(),50));
		shop_eight = (LinearLayout) findViewById(R.id.shop_eight);
		shop_eight.setLayoutParams(param);

		shop_seven = (LinearLayout) findViewById(R.id.shop_seven);

		param = new LinearLayout.LayoutParams(mScreenWright / 2 - 50, Utils.dip2px(getApplication(),60));
		shop_six1 = (LinearLayout) findViewById(R.id.shop_six1);
		shop_six1.setLayoutParams(param);
		shop_six2 = (LinearLayout) findViewById(R.id.shop_six2);
		shop_six2.setLayoutParams(param);

		shop_six1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					if ("".equals(Myapp.user_id)) {
						startActivity(new Intent(ShopDetail.this,PopLogin.class));
					} else {
						Intent intent = new Intent(ShopDetail.this,PopConfirm.class);
						intent.putExtra("product_id", product_id);
						intent.putExtra("bean_imgurl", mProduce.getMain_img_url());
						intent.putExtra("bean_des", mProduce.getTitle());
						intent.putExtra("bean_price", mProduce.getDoubleCurrentPrice() + "");
						intent.putExtra("bean_chicun", mProduce.size);
						intent.putExtra("bean_color", mProduce.color);
						intent.putExtra("tag", 1);
						startActivity(intent);
					}
			}
		});
		shop_six2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					if ("".equals(Myapp.user_id)) {
						startActivity(new Intent(ShopDetail.this,PopLogin.class));
					} else {
						Intent intent = new Intent(ShopDetail.this,PopConfirm.class);
						intent.putExtra("product_id", product_id);
						intent.putExtra("bean_imgurl", mProduce.getMain_img_url());
						intent.putExtra("bean_des", mProduce.getTitle());
						intent.putExtra("bean_price", mProduce.getDoubleCurrentPrice() + "");
						intent.putExtra("bean_chicun", mProduce.size);
						intent.putExtra("bean_color", mProduce.color);
						intent.putExtra("tag", 2);
						startActivity(intent);

						/*
						 * Message msg = handler.obtainMessage(); msg.what=2;
						 * handler.sendMessage(msg);
						 */
						/*
						 * Intent intent = new
						 * Intent(ShopDetail.this,PopConfirm.class);
						 * intent.putExtra("product_id", product_id);
						 * intent.putExtra("bean_imgurl", bean_imgurl);
						 * intent.putExtra("bean_des", bean_des);
						 * intent.putExtra("bean_price", bean_price);
						 * intent.putExtra("bean_chicun", bean_chicun);
						 * intent.putExtra("bean_color", bean_color);
						 * intent.putExtra("tag", 2); startActivity(intent);
						 */
					}

				// startActivity(new Intent(ShopDetail.this,ShopCart.class));
			}
		});

	}

	private void loadAddCart() {
		finalHttp =  new MyFinalHttp(this);
		String local_time = String.valueOf(System.currentTimeMillis());
//		List<String> srcList = new ArrayList<String>();
//		srcList.add("user_id");
//		srcList.add("product_id");
//		srcList.add("product_size");
//		srcList.add("product_num");
//		srcList.add("product_color");
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("user_id", Myapp.user_id);
		map.put("product_id", String.valueOf(product_id));
		map.put("product_size", "");
		map.put("product_num", "1");
		map.put("product_color", "");

		AjaxParams params = SignUtil.getSignatureAjaxParams(map);
		finalHttp.post(NetInfo.ADDCART, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				JSONObject ite;
				try {
					ite = new JSONObject(t);
					String code = new String(ite.getString("code"));
					String getmsg = new String(ite.getString("msg"));
					if ("1".equals(code)) {
						String msgs = new String(ite.getString("msg"));
						Message msg = handler.obtainMessage();
						msg.what = 3;
						Bundle bundle = new Bundle();
						bundle.putString("msg", msgs);
						msg.setData(bundle);
						handler.sendMessage(msg);
					}else{
						Toast.makeText(ShopDetail.this, getmsg, Toast.LENGTH_LONG).show();
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
				break;
			case 2:
				if(Myapp.isNetworkAvailable(ShopDetail.this)){
					loadAddCart();
				}else{
					String wifigps = ShopDetail.this.getString(R.string.wifigps);
					Toast.makeText(ShopDetail.this, wifigps,Toast.LENGTH_LONG).show();
				}
				break;
			case 3:
				String msgs = msg.getData().getString("msg");
				Toast.makeText(ShopDetail.this, msgs, Toast.LENGTH_LONG).show();
				break;

			}
		}
	};
	boolean is = true;

	public void show(View v) {
		if (mProduce.getActivity_info() != null && !"".equals(mProduce.getActivity_info())) {
			if (is) {
				shop_seven.setVisibility(View.VISIBLE);
				next.setImageResource(R.drawable.down_arrow);
				is = false;
			} else {
				shop_seven.setVisibility(View.GONE);
				next.setImageResource(R.drawable.next_arrow);
				is = true;
			}
		}
	}

	public void onResume() {
		super.onResume();
		if(Myapp.isNetworkAvailable(ShopDetail.this)){
			loadData();
		}else{
			String wifigps = ShopDetail.this.getString(R.string.wifigps);
			Toast.makeText(ShopDetail.this, wifigps,Toast.LENGTH_LONG).show();
		}
		if (mProduce.getActivity_info() == null || "".equals(mProduce.getActivity_info())) {
			shop_five.setVisibility(View.GONE);
			shop_five_line.setVisibility(View.GONE);

		}
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadservice);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void close(View v) {
		this.finish();
	}

	public void introduce(View v) {
		Intent intent = new Intent(this, DetailPng.class);
		intent.putExtra("intro_url", mProduce.getIntroduction_img_url());
		intent.putExtra("intro_size", mProduce.getIntroduction_img_size());
		startActivity(intent);
	}

	private Bitmap getBitmapFromNetWork(String imageUrl) {
		Bitmap bitmap = null;
		InputStream inputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(imageUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5 * 1000);
			if (conn.getResponseCode() == 200) { // 如果成功返回
				inputStream = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);
			} else {
				System.out.println("图片请求失败");
			}
		} catch (Exception e) {
			System.out.println("e=" + e.toString());
		} finally {
			try {
				if (byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (Exception e) {
				System.out.println("e=" + e.toString());
			}
		}

		return bitmap;
	}

	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("android.islogin.one".equals(action)) {
//				isLogin = true;
			}
		}

	}
}

package com.school.schoolclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyFinalHttp;
import com.school.application.Myapp;
import com.school.bean.CartBean;
import com.school.bean.Produce;
import com.school.net.NetInfo;
import com.school.net.SignUtil;
import com.school.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class PopConfirm extends Activity /* implements OnClickListener */{

	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private LinearLayout id1, id2, id3, id4, pop_layout;
	private MyAdapterO adapter0;
	List<Map<String, Object>> list0;
	private MyAdapter1 adapter1;
	List<Map<String, Object>> list1;
	private EditText count;
	private LinearLayout add, lose;
	private ImageView con_img;
	private int incount = 1;
	private FinalHttp finalHttp;
	int product_id;
	String bean_imgurl;
	String bean_des;
	String bean_price;
	String bean_chicun;
	String bean_color;
	private TextView con_price, con_title, con_title_color, con_title_chicun;
	private FinalBitmap finalBitmap;
	private int mScreenWidth;

	public Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap bitmap1 = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);

		return bitmap1;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirm_dialog);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		Myapp.width = mScreenWidth;
		Myapp.height = dm.heightPixels;
		
		finalBitmap = FinalBitmap.create(this);
		finalBitmap.setExitTasksEarly(false);
		finalBitmap.configBitmapLoadThreadSize(2);
		id3 = (LinearLayout) this.findViewById(R.id.id3);
		count = (EditText) this.findViewById(R.id.count);
		add = (LinearLayout) this.findViewById(R.id.add);
		lose = (LinearLayout) this.findViewById(R.id.lose);
		con_img = (ImageView) this.findViewById(R.id.con_img);
		con_price = (TextView) this.findViewById(R.id.con_price);
		con_title = (TextView) this.findViewById(R.id.con_title);
		con_title_color = (TextView) this.findViewById(R.id.con_title_color);
		con_title_chicun = (TextView) this.findViewById(R.id.con_title_chicun);

		Message msg = new Message();
		msg.what = 1;
		handler.sendMessage(msg);

		product_id = this.getIntent().getIntExtra("product_id", 0);
		bean_imgurl = this.getIntent().getStringExtra("bean_imgurl");
		bean_des = this.getIntent().getStringExtra("bean_des");
		bean_price = this.getIntent().getStringExtra("bean_price");
		bean_chicun = this.getIntent().getStringExtra("bean_chicun");
		bean_color = this.getIntent().getStringExtra("bean_color");

		con_title.setText(bean_des);
		con_price.setText(bean_price);

		RelativeLayout pop_layout = (RelativeLayout) this
				.findViewById(R.id.pop_layout);
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenWright = dm.widthPixels;
		FrameLayout.LayoutParams paramss = new FrameLayout.LayoutParams(
				mScreenWright, LayoutParams.WRAP_CONTENT);
		pop_layout.setLayoutParams(paramss);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.dip2px(getApplication(),347),Utils.dip2px(getApplication(),50));
		id3.setLayoutParams(params);
		LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(Utils.dip2px(getApplication(),147), Utils.dip2px(getApplication(),50));
		para.leftMargin = 2;
		count.setLayoutParams(para);
		// Bitmap lo= BitmapFactory.decodeResource(getResources(),
		// R.drawable.lose_black);
		// Bitmap lobit = zoomImage(lo,50,50);
		// lose.setImageBitmap(lo);
		//
		// Bitmap ad= BitmapFactory.decodeResource(getResources(),
		// R.drawable.add_black);
		// Bitmap adbit = zoomImage(ad,50,50);
		// add.setImageBitmap(ad);

		// LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(100,71);
		// p.leftMargin=2;

		// lose.setLayoutParams(p);
		// add.setLayoutParams(p);
		count.setText("" + incount);
		
		lose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				incount--;
				if (incount == 0)
					incount = 1;
				count.setText(incount + "");
				try {
					double totalPrice = Double.parseDouble(bean_price);
					con_price.setText(totalPrice*incount + "");
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				incount++;
				count.setText(incount + "");
				try {
					double totalPrice = Double.parseDouble(bean_price);
					con_price.setText(totalPrice*incount + "");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		list0 = new ArrayList<Map<String, Object>>();
		if (bean_chicun == null)
			bean_chicun = "";
		String[] chicun = bean_chicun.split(",");
		if ("".equals(bean_chicun)) {
			con_title_chicun.setVisibility(View.GONE);
		}
		if (chicun.length > 0) {
			for (int i = 0; i < chicun.length; i++) {
				if (!"".equals(chicun[i])) {

					Map<String, Object> item = new HashMap<String, Object>();
					item.put("textItem", chicun[i]);
					list0.add(item);
				}
			}
		}
		GridView gridview_cun = (GridView) findViewById(R.id.gridview_cun);
		adapter0 = new MyAdapterO(list0);
		int size0 = list0.size();
		if (size0 == 0) {
			gridview_cun.setVisibility(View.GONE);
		}
//		gridview_cun.setNumColumns(size0);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dip2px(getApplication(),(size0/3 + 1)*40));
		gridview_cun.setLayoutParams(param);

		gridview_cun.setAdapter(adapter0);
		gridview_cun.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				adapter0.setSeleitem(arg2);
				adapter0.notifyDataSetChanged();
			}
		});

		list1 = new ArrayList<Map<String, Object>>();
		if (bean_color == null)
			bean_color = "";
		String[] color = bean_color.split(",");
		if ("".equals(bean_color)) {
			con_title_color.setVisibility(View.GONE);
		}
		if (color.length > 0) {
			for (int i = 0; i < color.length; i++) {
				if (!"".equals(color[i])) {
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("textItem", color[i]);
					list1.add(item);
				}
			}
		}
		GridView gridview_color = (GridView) findViewById(R.id.gridview_color);
		adapter1 = new MyAdapter1(list1);
		int size1 = list1.size();
		if (size1 == 0) {
			gridview_color.setVisibility(View.GONE);
		}
//		gridview_color.setNumColumns(size1);
//		LinearLayout.LayoutParams parax = new LinearLayout.LayoutParams(Utils.dip2px(getApplication(),95
//				* size1 + 12 * (size1 - 1)), Utils.dip2px(getApplication(),72));
//		gridview_color.setLayoutParams(parax);
		LinearLayout.LayoutParams parax = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dip2px(getApplication(),(size1/3 + 1)*40));
		gridview_color.setLayoutParams(parax);

		gridview_color.setAdapter(adapter1);
		gridview_color.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				adapter1.setSeleitem(arg2);
				adapter1.notifyDataSetChanged();
			}
		});

	}

	String chicun = "";
	String color = "";

	public void gocar(View v) {
		int tag = this.getIntent().getIntExtra("tag", 0);

		if (!"".equals(bean_chicun) && StringUtils.isBlank(chicun)) {
			Toast.makeText(PopConfirm.this, "请选择尺寸!", Toast.LENGTH_LONG).show();
		} else if (!"".equals(bean_color) && StringUtils.isBlank(color)) {
			Toast.makeText(PopConfirm.this, "请选择颜色!", Toast.LENGTH_LONG).show();

		} else {

			if (tag == 1) {
				Intent intent = new Intent(this, ConfirmOrder.class);
				CartBean cBean = new CartBean();
				cBean.product_id = String.valueOf(product_id);
				cBean.setProduct_num(count.getText().toString()) ;
				cBean.product_color = color;
				cBean.product_size = chicun;

				cBean.product_object = new Produce();
				cBean.product_object.setTitle(bean_des);
				cBean.product_object.setLocal_main_img_url(bean_imgurl);
				cBean.product_object.setCurrent_price(bean_price);
				ArrayList ls = new ArrayList<CartBean>();
				ls.add(cBean);
				intent.putExtra("list", ls);
				// intent.putExtra("product_id", String.valueOf(product_id));
				// intent.putExtra("chicun",chicun);
				// intent.putExtra("color", color);
				// intent.putExtra("count", count.getText().toString());
				// intent.putExtra("title", bean_des);
				// intent.putExtra("bean_price", bean_price);
				// intent.putExtra("bean_imgurl", bean_imgurl);
				// intent.putExtra("tags", "1");
				startActivity(intent);
				finish();
			}
			if (tag == 2) {
				Message msg = handler.obtainMessage();
				msg.what = 2;
				handler.sendMessage(msg);
				// startActivity(new Intent(this,ShopCart.class));
			}
		}

	}

	private class MyAdapterO extends BaseAdapter {
		private List<Map<String, Object>> list;

		public MyAdapterO(List<Map<String, Object>> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return super.isEnabled(position);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.chicun_item, null);
			final TextView text_item = (TextView) view
					.findViewById(R.id.text_item);
			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
					mScreenWidth/3-20, LayoutParams.WRAP_CONTENT);
			text_item.setLayoutParams(param);

			product_size = list.get(position).get("textItem").toString();
			text_item.setText(product_size);

			if (getSeleitem() == position) {
				chicun = text_item.getText().toString();
				System.out.println("hicun0------------------------->" + chicun);
				text_item.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.circle_white_selected_bg));
			} else {
				text_item.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.circle_white_bg));
			}
			return view;
		}

		public int seleitem = -1;

		public int getSeleitem() {
			return seleitem;
		}

		public void setSeleitem(int seleitem) {
			this.seleitem = seleitem;
		}

	}

	private class MyAdapter1 extends BaseAdapter {
		private List<Map<String, Object>> list;

		public MyAdapter1(List<Map<String, Object>> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return super.isEnabled(position);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.color_item, null);
			final TextView text_item = (TextView) view
					.findViewById(R.id.text_item);
			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
					mScreenWidth/3-20, LayoutParams.WRAP_CONTENT);
			text_item.setLayoutParams(param);
			product_color = list.get(position).get("textItem").toString();
			text_item.setText(product_color);
			if (getSeleitem() == position) {
				color = text_item.getText().toString();
				System.out.println("hicun0------------------------->" + chicun);
				text_item.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.circle_white_selected_bg));
			} else {
				text_item.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.circle_white_bg));
			}

			// if(getSeleitem() == position){
			//
			// System.out.println("color------------------------->"+color);
			// text_item.setBackgroundColor(getResources().getColor(R.color.red));
			// }else{
			// text_item.setBackgroundColor(getResources().getColor(R.color.white));
			// }
			return view;
		}

		public int seleitem = -1;

		public int getSeleitem() {
			return seleitem;
		}

		public void setSeleitem(int seleitem) {
			this.seleitem = seleitem;
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// finalBitmap.configLoadingImage(R.drawable.kuzi);
				finalBitmap.display(con_img, bean_imgurl);
				break;
			case 2:
				if(Myapp.isNetworkAvailable(PopConfirm.this)){
					loadAddCart();
				}else{
					String wifigps = PopConfirm.this.getString(R.string.wifigps);
					Toast.makeText(PopConfirm.this, wifigps,Toast.LENGTH_LONG).show();
				}
				break;
			case 3:
				String msgs = msg.getData().getString("msg");
				String code = msg.getData().getString("code");
				if ("1".equals(code)) {
					sendBroadcast(new Intent("android.islogin.one"));
					PopConfirm.this.finish();
				}else{
					Toast.makeText(PopConfirm.this, msgs, Toast.LENGTH_LONG).show();
				}
				break;

			}
		}
	};
	String product_size = "";
	String product_num = "";
	String product_color = "";

	private void loadAddCart() {
		product_num = count.getText().toString();
		finalHttp = new MyFinalHttp(this);
		// List<String> srcList = new ArrayList<String>();
		// srcList.add("user_id");
		// srcList.add("product_id");
		// srcList.add("product_size");
		// srcList.add("product_num");
		// srcList.add("product_color");
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("user_id", Myapp.user_id);
		map.put("product_id", String.valueOf(product_id));
		map.put("product_size", chicun);
		map.put("product_num", product_num);
		map.put("product_color", color);

		AjaxParams params = SignUtil.getSignatureAjaxParams(map);
		// params.put("user_id", Myapp.user_id);
		// params.put("product_id", String.valueOf(product_id));
		// params.put("product_size", product_size);
		// params.put("product_num", product_num);
		// params.put("product_color", product_color);
		//
		// params.put("sign", sign.substring(5,sign.length()-5));
		finalHttp.post(NetInfo.ADDCART, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				JSONObject ite;
				try {
					ite = new JSONObject(t);
					String code = new String(ite.getString("code"));
					// if("1".equals(code)){
					String msgs = new String(ite.getString("msg"));
					Message msg = handler.obtainMessage();
					msg.what = 3;
					Bundle bundle = new Bundle();
					bundle.putString("msg", msgs);
					bundle.putString("code", code);
					msg.setData(bundle);
					handler.sendMessage(msg);
					// }
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

	public void backfinish(View view) {
		this.sendBroadcast(new Intent("android.islogin.one"));
		this.finish();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}

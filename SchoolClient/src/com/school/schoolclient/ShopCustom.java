package com.school.schoolclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class ShopCustom extends Activity {
	private TextView yj;
	private LinearLayout shop_one, shop_two, shop_three, shop_four, shop_five,
			shop_six;
	private List<HashMap<String, String>> list = null;
	private List<String> groupkey = new ArrayList<String>();
	private List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> bList = new ArrayList<HashMap<String, String>>();
	private ListView listview;
	private View line;
	private ImageView next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shopcustom);

		LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);
		ll_right.setVisibility(View.GONE);
		TextView txt_title = (TextView) findViewById(R.id.txt_view_title);
		txt_title.setText(getString(R.string.custom));

		initview();
		initData();
		listview = (ListView) findViewById(R.id.listView_answer);
		line = (View) findViewById(R.id.listView_line);
		next = (ImageView) findViewById(R.id.next);
		MyAdapter adapter = new MyAdapter();
		listview.setAdapter(adapter);

	}

	public void login(View view) {
		startActivity(new Intent(this, ShopLogin.class));
	}

	public void backfinish(View view) {

		this.finish();
	}
	
	public void onCallPhone(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("联系客服");
		builder.setMessage("确认拨打客服电话吗？");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 直接连接打电话
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:03128402014"));
				startActivity(intent);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
//		builder.create().show();
		AlertDialog mAlertDialog = builder.create();
		mAlertDialog.setCancelable(false);
		mAlertDialog.show();
	}

	boolean is = true;

	public void goanswer(View view) {
		if (is) {

			listview.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
			next.setImageResource(R.drawable.down_arrow);
			is = false;
		} else {

			listview.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			next.setImageResource(R.drawable.next_arrow);
			is = true;
		}

	}

	private void initview() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		int height = (mScreenHeigh / 3 * 2) / 5 - 10;

	}

	private class MyAdapter extends BaseAdapter {

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
			if (groupkey.contains(getItem(position))) {
				return false;
			}
			return super.isEnabled(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			HashMap<String, String> map = new HashMap<String, String>();
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.answer_item, null);
			TextView order_no = (TextView) view.findViewById(R.id.que);
			order_no.setText((CharSequence) list.get(position).get("que"));

			TextView order_price = (TextView) view.findViewById(R.id.ans);
			order_price.setText((CharSequence) list.get(position).get("ans"));

			return view;
		}

	}

	public void initData() {
		list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("que", "1、订单修改/取消订单");
		map.put("ans", "在下单时间后一个小时内可以在个人中心->全部订单中选择相应的订单取消。如果订单信息填写错了，重新下单即可。");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("que", "2、下单后看不到订单");
		map.put("ans", "因为网络问题信息更新有所延误，导致无法显示。您可以稍后刷新页面，或联系客服咨询订单是否创建成功。");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("que", "3、为什么提交订单后是待审核状态");
		map.put("ans", "订单提交成功后为待审核状态是正常的，我们会在您下单后一小时内审核您的订单。");
		list.add(map);
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

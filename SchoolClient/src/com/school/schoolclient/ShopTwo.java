package com.school.schoolclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShopTwo extends Activity {
	private LinearLayout shop_one, shop_g1, shop_zero, shop_g2, shop_six;
	private MyAdapterO adapter0;
	private MyAdapter adapter;
	List<Map<String, Object>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shoptwo);
		initview();

		// 准备要添加的数据条目
		// 准备要添加的数据条目
		list = new ArrayList<Map<String, Object>>();

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("textItem", "34");
		list.add(item);

		item = new HashMap<String, Object>();
		item.put("textItem", "35");
		list.add(item);

		item = new HashMap<String, Object>();
		item.put("textItem", "36");
		list.add(item);

		item = new HashMap<String, Object>();
		item.put("textItem", "37");
		list.add(item);

		item = new HashMap<String, Object>();
		item.put("textItem", "38");
		list.add(item);

		item = new HashMap<String, Object>();
		item.put("textItem", "39");
		list.add(item);

		item = new HashMap<String, Object>();
		item.put("textItem", "40");
		list.add(item);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		adapter0 = new MyAdapterO(list);
		gridview.setAdapter(adapter0);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				adapter0.setSeleitem(arg2);
				adapter0.notifyDataSetChanged();
			}
		});

		list = new ArrayList<Map<String, Object>>();

		item = new HashMap<String, Object>();
		item.put("textItem", "ASD98739285,黑色");
		list.add(item);
		item = new HashMap<String, Object>();
		item.put("textItem", "ASD98739285,红色");
		list.add(item);
		item = new HashMap<String, Object>();
		item.put("textItem", "ASD98739285,紫色");
		list.add(item);

		GridView listview = (GridView) findViewById(R.id.mygridview);
		adapter = new MyAdapter(list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				adapter.setSeleitem(arg2);
				adapter.notifyDataSetChanged();
			}
		});

	}

	public void onfirom(View view) {
		startActivity(new Intent(this, ConfirmOrder.class));
	}

	private void initview() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, mScreenHeigh / 4);
		shop_one = (LinearLayout) findViewById(R.id.shop_one);
		shop_one.setLayoutParams(params);
		//
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 100);
		shop_zero = (LinearLayout) findViewById(R.id.shop_zero);
		shop_zero.setLayoutParams(param);
		shop_six = (LinearLayout) findViewById(R.id.shop_six);
		shop_six.setLayoutParams(param);

		LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (mScreenHeigh / 4 * 3 - 350) / 2);
		shop_g2 = (LinearLayout) findViewById(R.id.shop_g2);
		shop_g2.setLayoutParams(paramss);
		shop_g1 = (LinearLayout) findViewById(R.id.shop_g1);
		shop_g1.setLayoutParams(paramss);

		// LinearLayout.LayoutParams paramline = new
		// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,mScreenHeigh/4*3-200);
		// shopline = (LinearLayout)findViewById(R.id.shopline);
		// shopline.setLayoutParams(paramline);
		// shop_three = (LinearLayout)findViewById(R.id.shop_three);
		// shop_three.setLayoutParams(param);
		// shop_four = (LinearLayout)findViewById(R.id.shop_four);
		// shop_four.setLayoutParams(param);
		// shop_five = (LinearLayout)findViewById(R.id.shop_five);
		// shop_five.setLayoutParams(param);
		// shop_six = (LinearLayout)findViewById(R.id.shop_six);
		// shop_six.setLayoutParams(param);
		//
		// Button button_firm = (Button)findViewById(R.id.button_firm);
		// button_firm.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// }
		// });

	}

	private class MyAdapter extends BaseAdapter {
		private List<Map<String, Object>> list;

		public MyAdapter(List<Map<String, Object>> list) {
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
			HashMap<String, String> map = new HashMap<String, String>();
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.grid_item, null);
			final TextView text_item = (TextView) view
					.findViewById(R.id.text_item);

			text_item
					.setText((CharSequence) list.get(position).get("textItem"));
			if (getSeleitem() == position) {
				text_item.setBackgroundColor(getResources().getColor(
						R.color.red));
			} else {
				text_item.setBackgroundColor(getResources().getColor(
						R.color.selcolor));
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
					R.layout.gridc_item, null);
			final TextView text_item = (TextView) view
					.findViewById(R.id.text_item);

			text_item
					.setText((CharSequence) list.get(position).get("textItem"));
			if (getSeleitem() == position) {
				text_item.setBackgroundColor(getResources().getColor(
						R.color.red));
			} else {
				text_item.setBackgroundColor(getResources().getColor(
						R.color.selcolor));
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

}

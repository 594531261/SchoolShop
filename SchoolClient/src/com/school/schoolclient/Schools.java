package com.school.schoolclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.MyFinalHttp;
import com.school.application.Myapp;
import com.school.bean.AddressBean;
import com.school.bean.SchoolsBean;
import com.school.net.NetInfo;
import com.school.net.SignUtil;

public class Schools extends Activity {
	private FinalHttp finalHttp;
	private ListView listview;
	MyAdapter adapter;
	ArrayList<SchoolsBean> schools = new ArrayList<SchoolsBean>();
	AddressBean mAddressBean;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.schools);
		mAddressBean = (AddressBean) getIntent().getSerializableExtra("mAddressBean");
		
		
		listview = (ListView) findViewById(R.id.listView_list);
		if(Myapp.isNetworkAvailable(Schools.this)){
			loadData();
		}else{
			String wifigps = Schools.this.getString(R.string.wifigps);
			Toast.makeText(Schools.this, wifigps,Toast.LENGTH_LONG).show();
		}
		adapter = new MyAdapter();
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("school", schools.get(position));
				setResult(RESULT_OK, intent);
				Schools.this.finish();
			}
		});
	}

	private void loadData() {
		finalHttp = new MyFinalHttp(this);
		String local_time = String.valueOf(System.currentTimeMillis());

		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("local_time", local_time);

		AjaxParams params = SignUtil.getSignatureAjaxParams(map);
		finalHttp.post(NetInfo.SCHOOLLIST, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				JSONObject ite;
				try {
					ite = new JSONObject(t);
					String code = new String(ite.getString("code"));
					String getmsg = new String(ite.getString("msg"));
					if ("1".equals(code)) {
						JSONArray object = ite.getJSONArray("data");
						schools = new ArrayList<SchoolsBean>();
						for (int i = 0; i < object.length(); i++) {// 遍历JSONArray
							JSONObject oj = object.getJSONObject(i);
							SchoolsBean school = new SchoolsBean();
							school.parse(oj);
							schools.add(school);
						}
						Message msg = handler.obtainMessage();
						handler.sendMessage(msg);
					}else{
						Toast.makeText(Schools.this, getmsg, Toast.LENGTH_LONG).show();
						
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

	public void close(View v) {
		this.finish();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();

		}
	};

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return schools.size();
		}

		@Override
		public Object getItem(int position) {
			return schools.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			HashMap<String, String> map = new HashMap<String, String>();
			view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.school_item, null);

			TextView name = (TextView) view.findViewById(R.id.name);
			name.setText((CharSequence) (schools.get(position).name));

			return view;
		}

	}
}

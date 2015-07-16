package com.school.schoolclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyFinalHttp;
import com.school.application.Myapp;
import com.school.bean.AddressBean;
import com.school.bean.SchoolsBean;
import com.school.net.NetInfo;
import com.school.net.SignUtil;

public class ShopAdress extends Activity {
	private static final int REQUESTCODE_SELECTED_SCHOOL = 100;
	private TextView yj;
	private LinearLayout shop_two, shop_three, shop_four, shop_five, shop_six;
	private FinalHttp finalHttp;
	private EditText phone, person, ress;
	private TextView school;

	AddressBean mAddressBean = new AddressBean();
	SchoolsBean mSchoolsBean = new SchoolsBean();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shopadressfour);

		

		LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);
		ll_right.setVisibility(View.GONE);
		TextView txt_title = (TextView) findViewById(R.id.txt_view_title);
		txt_title.setText(getString(R.string.addaddress));
		
		mAddressBean = (AddressBean) getIntent().getSerializableExtra(
				"mAddressBean");
		initview();
//		loadAddress();

		init();
	}

	private void init() {
		school.setText(mAddressBean.school_name);
		phone.setText(mAddressBean.address_uphone);
		ress.setText(mAddressBean.address_detail);
		person.setText(mAddressBean.address_uname);
	}

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
				// TODO Auto-generated method stub
				super.onSuccess(t);
				JSONObject ite;
				try {
					ite = new JSONObject(t);
					String code = new String(ite.getString("code"));
					String getmsg = new String(ite.getString("msg"));
					/*if ("1".equals(code)) {
						String msgs = new String(ite.getString("msg"));
					}*/
//						Toast.makeText(ShopAdress.this, getmsg, Toast.LENGTH_LONG).show();
					
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
	
	private boolean validate(){
		boolean isSucceed = false;
		if (StringUtils.isBlank(mAddressBean.school_id)) {
			Toast.makeText(ShopAdress.this, getString(R.string.curschool) + "不能为空！", Toast.LENGTH_SHORT).show();
		} else if ( StringUtils.isBlank(ress.getText().toString())) {
			Toast.makeText(ShopAdress.this, getString(R.string.detailaddress) + "不能为空！", Toast.LENGTH_SHORT).show();
		} else if ( StringUtils.isBlank(phone.getText().toString())) {
			Toast.makeText(ShopAdress.this, getString(R.string.getperson) + "不能为空！", Toast.LENGTH_SHORT).show();
		} else if ( StringUtils.isBlank(phone.getText().toString())) {
			Toast.makeText(ShopAdress.this, getString(R.string.userphone) + "不能为空！", Toast.LENGTH_SHORT).show();
		} else{
			isSucceed = true;
		}
		return isSucceed;
	}

	private void saveAddress() {
		if(!validate()){
			return;
		}
		finalHttp = new MyFinalHttp(this);
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("user_id", Myapp.user_id);
		map.put("address_uname", person.getText().toString());
		map.put("address_uphone", phone.getText().toString());
		map.put("address_detail", ress.getText().toString());
		map.put("school_id", mAddressBean.school_id);
		AjaxParams params = SignUtil.getSignatureAjaxParams(map);
		finalHttp.post(NetInfo.UPDATEADDRESS, params,
				new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						JSONObject ite;
						try {
							ite = new JSONObject(t);
							String msgs = new String(ite.getString("msg"));
							String code = new String(ite.getString("code"));
							Toast.makeText(ShopAdress.this, msgs, Toast.LENGTH_LONG).show();
							if ("1".equals(code)) {
								String data = new String(ite.getString("data"));
								JSONObject da = new JSONObject(data);
								mAddressBean.parse(da);
								Intent intent = new Intent();
								intent.putExtra("mAddressBean", mAddressBean);
								ShopAdress.this.setResult(RESULT_OK, intent);
								ShopAdress.this.finish();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
					}
				});
	}

	public void backfinish(View view) {

		this.finish();
	}

	public void save(View view) {
		if(Myapp.isNetworkAvailable(ShopAdress.this)){
			saveAddress();
		}else{
			String wifigps = ShopAdress.this.getString(R.string.wifigps);
			Toast.makeText(ShopAdress.this, wifigps,Toast.LENGTH_LONG).show();
		}
	}

	private void initview() {
		school = (TextView) findViewById(R.id.school);
		phone = (EditText) findViewById(R.id.phone);
		ress = (EditText) findViewById(R.id.ress);
		person = (EditText) findViewById(R.id.person);

	}

	public void getShools(View view) {
		Intent intent = new Intent(this, Schools.class);
		intent.putExtra("mAddressBean", mAddressBean);
		startActivityForResult(intent, REQUESTCODE_SELECTED_SCHOOL);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUESTCODE_SELECTED_SCHOOL) {
			if (resultCode == RESULT_OK) {
				mSchoolsBean = (SchoolsBean) data
						.getSerializableExtra("school");
				mAddressBean.school_name = mSchoolsBean.name;
				mAddressBean.school_id = mSchoolsBean.school_id;
				init();
			}
		}
	}

}

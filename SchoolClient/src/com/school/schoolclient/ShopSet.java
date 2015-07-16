package com.school.schoolclient;

import java.util.TreeMap;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyFinalHttp;
import com.school.application.Myapp;
import com.school.bean.VersionInfo;
import com.school.net.NetInfo;
import com.school.net.SignUtil;
import com.school.utils.UpdateManager;
import com.school.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class ShopSet extends Activity{
	private TextView vercodename,vercodename_new;
	private View linegin;
	private LinearLayout set_two,set_three,set_four,set_login;
	String versionName="1.0";
	Myapp app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shopset);
		app = ((Myapp) getApplication());
		
		linegin = (View)findViewById(R.id.linegin);
		vercodename = (TextView)findViewById(R.id.vercodename);
		vercodename_new = (TextView)findViewById(R.id.vercodename_new);
		
		LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);
		ll_right.setVisibility(View.GONE);
		TextView txt_title = (TextView) findViewById(R.id.txt_view_title);
		txt_title.setText(getString(R.string.settitle));

		initview();
		
		if(!"".equals(Myapp.user_id)){
			set_four.setVisibility(View.GONE);
		}
		
		//当前版本号
		PackageManager pm = this.getPackageManager();//context为当前Activity上下文 
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(this.getPackageName(), 0);
			versionName = pi.versionName;
//			System.out.println("versionCode------------->"+versionCode);
//			System.out.println("versionName------------->"+versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		vercodename.setText(versionName+"");
		
		refreshVersion();
		
	}
	
	private void refreshVersion(){
		VersionInfo serverVersion = ((Myapp) getApplication()).getServerVersion();
		if(versionName.compareTo(serverVersion.getVersion())<0){
			vercodename_new.setVisibility(View.VISIBLE);
			vercodename.setVisibility(View.GONE);
		}else{
			vercodename.setVisibility(View.VISIBLE);
			vercodename_new.setVisibility(View.GONE);
		}
	}
	
	public void allorder(View view){
		if(StringUtils.isBlank(Myapp.user_id)){
			startActivity(new Intent(this,PopLogin.class));
		}else{
			startActivity(new Intent(this,ShopOrder.class));
		}
	}
	@SuppressWarnings("unused")
	public void verifyVersion(View view){
		VersionInfo serverVersion = ((Myapp) getApplication()).getServerVersion();
		if(versionName.compareTo(serverVersion.getVersion())<0){
			UpdateManager um = new UpdateManager(this,serverVersion,false);
			um.checkUpdate();
		}else{
			Toast.makeText(this, "当前为最新版本", Toast.LENGTH_SHORT).show();
		}
//		if(Myapp.isNetworkAvailable(ShopSet.this)){
//			loadUpdate() ;
//		}else{
//			String wifigps = ShopSet.this.getString(R.string.wifigps);
//			Toast.makeText(ShopSet.this, wifigps,Toast.LENGTH_LONG).show();
//		}
//		startActivity(new Intent(this,ShopOrder.class));
	}
	
	private void loadUpdate() {
		FinalHttp finalHttp =  new MyFinalHttp(this);
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("version_code", versionName);


		AjaxParams params = SignUtil.getSignatureAjaxParams(map);
		finalHttp.post(NetInfo.UPDATECHECK, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				JSONObject ite;
				try {
					ite = new JSONObject(t);
					String code = new String(ite.getString("code"));
					String getmsg = new String(ite.getString("msg"));
					if ("1".equals(code)) {
						String data = ite.getString("data");
						JSONObject da = new JSONObject(data);
						String url = da.getString("img_url");
						String version_code = da.getString("version_code");
						String message= da.getString("update_content");
						VersionInfo info = new VersionInfo();
						info.setDownloadURL(url);
						info.setDisplayMessage(message);
						info.setVersion(version_code);
						app.saveServerVersion(info);
						refreshVersion();
						if(versionName.compareTo(version_code) < 0){
							UpdateManager um = new UpdateManager(ShopSet.this,info);
							um.checkUpdate();
						}else{
							Toast.makeText(ShopSet.this, "当前为最新版本",Toast.LENGTH_LONG).show();
						}
					}else{
						Toast.makeText(ShopSet.this, getmsg, Toast.LENGTH_LONG).show();
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
	
	public void custom(View view){
		 startActivity(new Intent(this,ShopCustom.class));
	}
	private void initview(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,Utils.dip2px(getApplication(),100));
		
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,Utils.dip2px(getApplication(),200));
		set_two = (LinearLayout)findViewById(R.id.set_two);
		set_two.setLayoutParams(param);
		
		set_four = (LinearLayout)findViewById(R.id.set_four);
		
		LinearLayout.LayoutParams paramconfim  = new LinearLayout.LayoutParams(Utils.dip2px(getApplication(),100),Utils.dip2px(getApplication(),40));
		paramconfim.rightMargin=50;
		paramconfim.gravity=Gravity.CENTER_VERTICAL;
		set_login = (LinearLayout)findViewById(R.id.set_login);
		
		set_login.setLayoutParams(paramconfim);
		set_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopSet.this,PopLogin.class);
				intent.putExtra("camehere", 2);
				startActivityForResult(intent, 500);
			}
		});
		
		LinearLayout.LayoutParams parim  = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,mScreenHeigh-Utils.dip2px(getApplication(),170));
		set_three = (LinearLayout)findViewById(R.id.set_three);
		set_three.setLayoutParams(parim);
//		
//		Button button_firm = (Button)findViewById(R.id.button_firm);
//		button_firm.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			}
//		});
		
	}
	public void backfinish(View view){

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

	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 500){
			if(resultCode == RESULT_OK){
				linegin.setVisibility(View.GONE);
				set_four.setVisibility(View.GONE);
			}
		}
		
		
	}
	
	public void goAboutUs(View v){
		Intent intent = new Intent(ShopSet.this, AboutUs.class);
		startActivity(intent);
	}
}


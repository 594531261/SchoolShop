package com.school.schoolclient;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.MyFinalHttp;
import com.google.gson.Gson;
import com.school.application.Myapp;
import com.school.bean.VersionInfo;
import com.school.net.NetInfo;
import com.school.net.SignUtil;
import com.school.utils.BaseUtils;
import com.school.utils.UpdateManager;

public class Loading extends Activity {
	Handler handler;
	private Activity activity;
	private Intent intent;
	private Gson gson;
	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;

	// 底部小点图片
	private ImageView[] dots;

	// 记录当前选中位置
	private int currentIndex;
	Myapp app;
	String versionName="1.0.0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		app = ((Myapp) getApplication());
		
		String flag = BaseUtils.getCookie(activity, "isfinish1");
		if (flag != null && flag.length() > 0
				&& BaseUtils.getCookie(activity, "isfinish1").equals("finish")) {
			initView();
		} else {
			BaseUtils.setCookie(activity, "isfinish1", "finish");
			guideView();
		}
	}
	private void initView() {
		setContentView(R.layout.activity_loading);
		
		handler = new Handler();
		verifyVersion();
		
//		getDeviceInfo();
	}


	private void guideView() {
		setContentView(R.layout.activity_guide);
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		// 初始化引导图片列表
		views.add(inflater.inflate(R.layout.view1, null));
		views.add(inflater.inflate(R.layout.view2, null));
		views.add(inflater.inflate(R.layout.view3, null));
		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views, activity);

		vp = (ViewPager) findViewById(R.id.viewpager);
		initDots();
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setCurrentDot(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[views.size()];

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			if (i == 0)
				dots[i].setImageResource(R.drawable.welcome_dot_active);
			else
				dots[i].setImageResource(R.drawable.welcome_dot);
		}

		currentIndex = 0;
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		for (int i = 0; i < views.size(); i++) {
			if (position == i) {
				dots[i].setImageResource(R.drawable.welcome_dot_active);

			} else {
				dots[i].setImageResource(R.drawable.welcome_dot);
			}
		}

		currentIndex = position;
	}

	/**
	 * 跳转到
	 */
	private void goHome() {
		intent = new Intent(activity, MainActivity.class);
		startActivity(intent);
		activity.finish();
	}
	
	public void verifyVersion(){
		
		//当前版本号
		PackageManager pm = this.getPackageManager();//context为当前Activity上下文 
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(this.getPackageName(), 0);
			versionName = pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		
		if(Myapp.isNetworkAvailable(Loading.this)){
			loadUpdate() ;
		}else{
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					goHome();
				}
			}, 3000);
		}
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
//					Toast.makeText(Loading.this, getmsg, Toast.LENGTH_LONG).show();
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
						if(versionName.compareTo(version_code) < 0){
							UpdateManager um = new UpdateManager(Loading.this,info);
							um.checkUpdate();
						}else{
							goHome();
							//Toast.makeText(Loading.this, "当前为最新版本",Toast.LENGTH_LONG).show();
						}
					}else{
						goHome();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					goHome();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				goHome();
			}
		});
	}
}

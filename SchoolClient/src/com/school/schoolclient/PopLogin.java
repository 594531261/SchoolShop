package com.school.schoolclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyFinalHttp;
import com.school.application.Myapp;
import com.school.bean.CacheManager;
import com.school.bean.UserBean;
import com.school.net.NetInfo;
import com.school.net.SignUtil;
import com.umeng.analytics.MobclickAgent;

public class PopLogin extends Activity /* implements OnClickListener */{

	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private LinearLayout id1, id2, id3, id4, pop_layout;
	private EditText phoneext, phoneverify;
	SharedPreferences preferences;
	TextView getVerif;
	int camehere;
	Myapp myapp;
	TimeCount time = new TimeCount(60000, 1000);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_dialog);
		myapp = (Myapp) getApplication();
		camehere = this.getIntent().getIntExtra("camehere", 0);
		phoneext = (EditText) this.findViewById(R.id.phoneext);
		phoneverify = (EditText) this.findViewById(R.id.phoneverify);
		// phoneext.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		// phoneverify.setInputType(EditorInfo.TYPE_CLASS_PHONE);

		id1 = (LinearLayout) this.findViewById(R.id.id1);
		id2 = (LinearLayout) this.findViewById(R.id.id2);
		id3 = (LinearLayout) this.findViewById(R.id.id3);
		id4 = (LinearLayout) this.findViewById(R.id.id4);
		RelativeLayout pop_layout = (RelativeLayout) this
				.findViewById(R.id.pop_layout);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		int mScreenWright = dm.widthPixels;
		FrameLayout.LayoutParams paramss = new FrameLayout.LayoutParams(
				mScreenWright, LayoutParams.WRAP_CONTENT);
		pop_layout.setLayoutParams(paramss);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				mScreenWright, 100);
		id1.setLayoutParams(params);
		id2.setLayoutParams(params);
		id3.setLayoutParams(params);
		params = new LinearLayout.LayoutParams(
				mScreenWright, 80);
		params.topMargin=20;
		params.bottomMargin=20;
		id4.setLayoutParams(params);
		getVerif = (TextView) findViewById(R.id.getVerif);
		
		//构造CountDownTimer对象
		getVerif.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(Myapp.isNetworkAvailable(PopLogin.this)){
					etx = phoneext.getText().toString();
					if (etx == null || "".equals(etx)) {
						Toast.makeText(PopLogin.this, "手机号不能为空", Toast.LENGTH_LONG).show();
					}else{
						if (isMobileNO(etx)) {
							time.start();//开始计时
							loadGetVerify();
							
						}else{
							time.onFinish();
							time.cancel();
							Toast.makeText(PopLogin.this, "手机号码格式不对", Toast.LENGTH_LONG).show();
						}
					}
				}else{
					String wifigps = PopLogin.this.getString(R.string.wifigps);
					Toast.makeText(PopLogin.this, wifigps,Toast.LENGTH_LONG).show();
				}
				
			}
		});

	}

	String etx, veri;

	public void login(View v) {

		
		if(Myapp.isNetworkAvailable(PopLogin.this)){
			loadLogin();
		}else{
			String wifigps = PopLogin.this.getString(R.string.wifigps);
			Toast.makeText(PopLogin.this, wifigps,Toast.LENGTH_LONG).show();
		}

	}

		  
	public boolean isMobileNO(String mobiles) {

		Pattern p = Pattern
				.compile("^((1))\\d{10}$");
//				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

//		System.out.println(m.matches() + "---");

		return m.matches();

	}
	
	// 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity

	/*
	 * public boolean onTouchEvent(MotionEvent event){ // finish(); return true;
	 * }
	 */
	public void backfinish(View view) {
		this.sendBroadcast(new Intent("android.islogin.one"));
		this.finish();
	}

	private FinalHttp finalHttp;

	private void loadLogin() {
		etx = phoneext.getText().toString();
		veri = phoneverify.getText().toString();

		if (etx == null || "".equals(etx)) {
			Toast.makeText(PopLogin.this, "手机号不能为空", Toast.LENGTH_LONG).show();
		}
		if (veri == null || "".equals(veri)) {
			Toast.makeText(PopLogin.this, "验证码不能为空", Toast.LENGTH_LONG).show();
		}
		if (!"".equals(etx) && !"".equals(veri)) {
			
			if (isMobileNO(etx)) {

				finalHttp = new MyFinalHttp(this);

				TreeMap<String, String> map = new TreeMap<String, String>();
				map.put("phone", etx);

				map.put("source", myapp.getChannel());
//				map.put("source", "A07");
//				map.put("source", "A08");
//				map.put("source", "A09");
				map.put("verify", veri);


				finalHttp = new MyFinalHttp(this);
				AjaxParams params = SignUtil.getSignatureAjaxParams(map);
				finalHttp.post(NetInfo.VERIFY, params,
						new AjaxCallBack<String>() {

							@Override
							public void onSuccess(String t) {
								// TODO Auto-generated method stub
								super.onSuccess(t);
								JSONObject ite = null;
								try {
									ite = new JSONObject(t);
									String code = new String(ite
											.getString("code"));
									String data = new String(ite
											.getString("data"));
									String getmas = new String(ite
											.getString("msg"));
									Toast.makeText(PopLogin.this, getmas,
											Toast.LENGTH_LONG).show();
									if ("1".equals(code)) {
										JSONObject da = new JSONObject(data);
										UserBean user = new UserBean();
										user.fdUserId = da.getString("user_id");
										CacheManager.getInstance()
												.saveUserBean(user);
										Myapp.user_id = user.fdUserId;

										if (camehere == 2) {
											Intent intent = new Intent();
											setResult(RESULT_OK, intent);
										}

										PopLogin.this.finish();
										/*
										 * if (camehere == 2) {
										 * PopLogin.this.finish(); } else {
										 * PopLogin.this.finish();
										 * startActivity(new
										 * Intent(PopLogin.this,
										 * PopConfirm.class)); }
										 */
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								// TODO Auto-generated method stub
								super.onFailure(t, errorNo, strMsg);
							}
						});
			}else{
				Toast.makeText(PopLogin.this, "手机号码格式不对", Toast.LENGTH_LONG).show();
			}
		}

	}

	private void loadGetVerify() {
		etx = phoneext.getText().toString();
		finalHttp = new MyFinalHttp(this);
		List<String> srcList = new ArrayList<String>();
		srcList.add("phone");

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("phone", etx);

		String sign = SignUtil.generateSignature(srcList, map);

		finalHttp = new MyFinalHttp(this);
		AjaxParams params = new AjaxParams();
		params.put("phone", etx);
		params.put("sign", sign.substring(5, sign.length() - 5));
		finalHttp.post(NetInfo.SMS, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				JSONObject ite;
				try {
					ite = new JSONObject(t);
					String code = new String(ite.getString("code"));
					String getmsg = new String(ite.getString("msg"));
//					if (!"1".equals(code)) {
						Toast.makeText(PopLogin.this, getmsg, Toast.LENGTH_LONG)
								.show();
//					}
						if(!"1".equals(code)){
							time.onFinish();
							time.cancel();
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

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			getVerif.setText("重新获取验证码");
			getVerif.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_ans_bg));
			getVerif.setClickable(true);
		}

		
		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			getVerif.setClickable(false);
			getVerif.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_ans_disable_bg));
			getVerif.setText(millisUntilFinished / 1000 + "秒后重新获取");
		}
	}
}

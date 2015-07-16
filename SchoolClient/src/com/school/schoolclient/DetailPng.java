package com.school.schoolclient;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.school.application.Myapp;
import com.school.net.NetInfo;
import com.school.utils.Utils;
import com.umeng.analytics.MobclickAgent;
public class DetailPng extends Activity {
	private FinalBitmap finalBitmap;
	private String intro_url ;
	private String intro_size ;
	private LinearLayout line;
	private LinearLayout shop_six1,shop_six2;
	int mScreenHeigh;
	int mScreenWright;
	LinearLayout.LayoutParams params;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.introduction);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenHeigh = dm.heightPixels;
		mScreenWright = dm.widthPixels;
//		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(mScreenHeigh/5)*3);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		
		finalBitmap = FinalBitmap.create(this);
		finalBitmap.setExitTasksEarly(false);
		finalBitmap.configBitmapLoadThreadSize(10);

//		finalBitmap.configBitmapMaxWidth(mScreenWright);
//		finalBitmap.configBitmapMaxHeight((mScreenHeigh/5)*3);
		
		//		finalBitmap.configLoadingImage(R.drawable.kuzi);
		line = (LinearLayout)findViewById(R.id.line);
		intro_url = this.getIntent().getStringExtra("intro_url");
		intro_size = this.getIntent().getStringExtra("intro_size");
		
		
		
		/*LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Myapp.width/2-50,Utils.dip2px(getApplication(),80));
		shop_six1 = (LinearLayout)findViewById(R.id.shop_six1);
		shop_six1.setLayoutParams(param);
		shop_six2 = (LinearLayout)findViewById(R.id.shop_six2);
		shop_six2.setLayoutParams(param);*/
		
		Message msg = new Message();
		msg.what=1;
		handler.sendMessage(msg);
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				
				if(intro_url == null 
				|| intro_url.equals("")){
					return;
				}
				String[] intr = intro_url.split(",");	
				String [] imgsSizes = intro_size.split(",");
				for(int i=0;i<intr.length;i++){
					ImageView img = new ImageView(DetailPng.this);
//					img.setBackgroundResource(R.drawable.default_img);
//					img.setImageDrawable(getResources().getDrawable(R.drawable.default_img));
					params = new LinearLayout.LayoutParams(Myapp.width,Utils.getHeight(Myapp.width, imgsSizes[i]));
					params.bottomMargin=0;
					params.topMargin =0;
					line.addView(img,params);
					finalBitmap.display(img, NetInfo.GETPNG+intr[i]);
				}
				break;
			case 2:
				break;

			}
		}
	};
	
	public void close(View v){
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

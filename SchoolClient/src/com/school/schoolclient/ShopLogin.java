package com.school.schoolclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShopLogin extends Activity{
	private TextView yj;
	private LinearLayout shop_one,shop_two,shop_three,shop_four,shop_five,shop_six;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shoplogin);
		initview();
		
		
	}
	
	public void login(View view){
		 startActivity(new Intent(this,MainActivity.class));
	}
	private void initview(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		int height = (mScreenHeigh/3*2)/5-10;
		
		TextView save = (TextView)findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 startActivity(new Intent(ShopLogin.this,ShopOrder.class));
			}
		});
//		yj = (TextView)findViewById(R.id.yj);
//		yj.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG ); 
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,60);
//		shop_one = (LinearLayout)findViewById(R.id.shop_one);
//		shop_one.setLayoutParams(params);
//		
//		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,height);
//		shop_two = (LinearLayout)findViewById(R.id.shop_two);
//		shop_two.setLayoutParams(param);
//		shop_three = (LinearLayout)findViewById(R.id.shop_three);
//		shop_three.setLayoutParams(param);
//		shop_four = (LinearLayout)findViewById(R.id.shop_four);
//		shop_four.setLayoutParams(param);
//		shop_five = (LinearLayout)findViewById(R.id.shop_five);
//		shop_five.setLayoutParams(param);
//		shop_six = (LinearLayout)findViewById(R.id.shop_six);
//		shop_six.setLayoutParams(param);
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

}


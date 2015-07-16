package com.school.schoolclient;

import com.school.application.Myapp;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.TextView;

public class AboutUs extends Activity{
	Myapp app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_aboutus);
		app = (Myapp) getApplication();
		initView();
	}

	private void initView(){
		TextView txt_view_title = (TextView) findViewById(R.id.txt_view_title);
		txt_view_title.setText("关于我们");
		final TextView txt_appversion = (TextView) findViewById(R.id.txt_appversion);
		
		//当前版本号
		PackageManager pm = this.getPackageManager();//context为当前Activity上下文 
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(this.getPackageName(), 0);
			txt_appversion.setText(pi.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		txt_appversion.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				txt_appversion.setText(app.getChannel());
				return false;
			}
		});
		
	}
	
	public void backfinish(View view){
		finish();
	}
	
}

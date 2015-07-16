package com;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.school.application.Myapp;

public class MyFinalHttp extends FinalHttp{
	private ProgressDialog tProgressDialog;

	private Context context;
	public MyFinalHttp(Context context){
		this.context = context;
	}
	
	public void post(boolean isShowDlg,String url, AjaxParams params,
			final	AjaxCallBack callBack) {
		if(isShowDlg){
			showDlg();
		}
		AjaxCallBack myCallBack = new AjaxCallBack<String>() {
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				callBack.onSuccess(t);
				closeDlg();
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				callBack.onFailure(t, errorNo, strMsg);
				closeDlg();
				Toast.makeText(Myapp.instanse, "网络错误，请重试！", Toast.LENGTH_SHORT).show();
			}
		};
		super.post(url, params, myCallBack);
	}
	@Override
	public void post(String url, AjaxParams params,
		final	AjaxCallBack callBack) {
		this.post(true, url, params, callBack);
	}

	
	private void showDlg(){
		if (tProgressDialog == null) {
			ProgressDialog pDlg;
			pDlg = new ProgressDialog(context);
			pDlg.setCancelable(true);
//			pDlg.setOnCancelListener(this);
			// pDlg.setTitle("提示");
			pDlg.setMessage("加载中...");
			pDlg.setIndeterminate(true);
			tProgressDialog = pDlg;
			tProgressDialog.show();
		}
	}
	
	private void closeDlg(){
		if (tProgressDialog != null) {
			tProgressDialog.dismiss();
			tProgressDialog = null;
			
		}
	}
}

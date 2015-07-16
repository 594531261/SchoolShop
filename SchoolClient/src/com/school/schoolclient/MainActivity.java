package com.school.schoolclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.MyFinalHttp;
import com.school.application.Myapp;
import com.school.bean.Category;
import com.school.fragment.BaseFragment;
import com.school.net.NetInfo;
import com.school.net.SignUtil;
import com.school.utils.Utils;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("ResourceAsColor")
public class MainActivity extends FragmentActivity implements OnClickListener {

	private HorizontalScrollView mHorizontalScrollView;
	private LinearLayout mLinearLayout;
	private ViewPager pager;
	private ImageView mImageView;
	private int mScreenWidth;
	private int colum = 6;
	private int item_width;
	private FinalHttp finalHttp;
	private int endPosition;
	private int beginPosition;
	private int currentFragmentIndex;
	private boolean isEnd;
	private ArrayList<Fragment> fragments;
	SharedPreferences preferences;
	Editor editor;

	ArrayList<Category> mCategorys = new ArrayList<Category>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);


		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		Myapp.width = mScreenWidth;
		Myapp.height = dm.heightPixels;

		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_view);
		mLinearLayout = (LinearLayout) findViewById(R.id.hsv_content);
		mImageView = (ImageView) findViewById(R.id.img1);
		item_width = (int) ((mScreenWidth / colum + 0.5f));
		mImageView.getLayoutParams().width = item_width;
		pager = (ViewPager) findViewById(R.id.pager);

		Message msg = handler.obtainMessage();
		msg.what = 1;
		handler.sendMessage(msg);
		// initViewPager();
	}

	private void initViewPager() {
		fragments = new ArrayList<Fragment>();
//		BaseFragment base = new BaseFragment();
//		if (mCategorys.size() > 0) {
//			base.setCategoryId(mCategorys.get(0).fdId);
//		}
//		fragments.add(base);
		for (int i = 0; i < mCategorys.size(); i++) {
			BaseFragment women = new BaseFragment();
			women.setIsIndex(i == 0);
			women.setCategoryId(mCategorys.get(i).fdId);
			fragments.add(women);
		}

		MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragments);
		pager.setAdapter(fragmentPagerAdapter);
		// fragmentPagerAdapter.setFragments(fragments);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
		pager.setCurrentItem(0);
	}

	private void loadData() {
		finalHttp = new MyFinalHttp(this);
		String local_time = String.valueOf(System.currentTimeMillis());
		List<String> srcList = new ArrayList<String>();
		srcList.add("local_time");

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("local_time", local_time);

		String sign = SignUtil.generateSignature(srcList, map);

		AjaxParams params = new AjaxParams();
		params.put("local_time", local_time);
		params.put("sign", sign.substring(5, sign.length() - 5));
		finalHttp.post(NetInfo.CATEGORY, params, new AjaxCallBack<String>() {

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
						mCategorys = new ArrayList<Category>();
						for (int i = 0; i < object.length(); i++) {// 遍历JSONArray
							JSONObject oj = object.getJSONObject(i);
							Category tCategory = new Category();
							tCategory.parse(oj);
							mCategorys.add(tCategory);
						}
						Message msg = handler.obtainMessage();
						msg.what = 2;
						handler.sendMessage(msg);
					}else{
						Toast.makeText(MainActivity.this, getmsg, Toast.LENGTH_LONG).show();
						
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> fragments;
		private FragmentManager fm;

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
		}

		public MyFragmentPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> fragments) {
			super(fm);
			this.fm = fm;
			this.fragments = fragments;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			Object obj = super.instantiateItem(container, position);
			return obj;
		}

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(final int position) {
			Animation animation = new TranslateAnimation(endPosition, position
					* item_width, 0, 0);

			beginPosition = position * item_width;

			currentFragmentIndex = position;
			if (animation != null) {
				animation.setFillAfter(true);
				animation.setDuration(0);
				mImageView.startAnimation(animation);
				mHorizontalScrollView.smoothScrollTo((currentFragmentIndex - 1)
						* item_width, 0);
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			if (!isEnd) {
				if (currentFragmentIndex == position) {
					endPosition = item_width * currentFragmentIndex
							+ (int) (item_width * positionOffset);
				}
				if (currentFragmentIndex == position + 1) {
					endPosition = item_width * currentFragmentIndex
							- (int) (item_width * (1 - positionOffset));
				}

				Animation mAnimation = new TranslateAnimation(beginPosition,
						endPosition, 0, 0);
				mAnimation.setFillAfter(true);
				mAnimation.setDuration(0);
				mImageView.startAnimation(mAnimation);
				mHorizontalScrollView.invalidate();
				beginPosition = endPosition;
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_DRAGGING) {
				isEnd = false;
			} else if (state == ViewPager.SCROLL_STATE_SETTLING) {
				isEnd = true;
				beginPosition = currentFragmentIndex * item_width;
				if (pager.getCurrentItem() == currentFragmentIndex) {
					mImageView.clearAnimation();
					Animation animation = null;
					animation = new TranslateAnimation(endPosition,
							currentFragmentIndex * item_width, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(1);
					mImageView.startAnimation(animation);
					mHorizontalScrollView.invalidate();
					endPosition = currentFragmentIndex * item_width;
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		pager.setCurrentItem((Integer) v.getTag());
	}

	public void goset(View v) {
		startActivity(new Intent(this, ShopSet.class));
	}

	public void goCart(View v) {
		if(StringUtils.isBlank(Myapp.user_id)){
			startActivity(new Intent(this,PopLogin.class));
		}else{
			startActivity(new Intent(this, ShopCart.class));
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if(Myapp.isNetworkAvailable(MainActivity.this)){
					loadData();
				}else{
					String wifigps = MainActivity.this.getString(R.string.wifigps);
					Toast.makeText(MainActivity.this, wifigps,Toast.LENGTH_LONG).show();
				}
				break;
			case 2:
				for (int i = 0; i < mCategorys.size(); i++) {
					RelativeLayout layout = new RelativeLayout(
							MainActivity.this);
					TextView view = new TextView(MainActivity.this);
					view.setText(mCategorys.get(i).fdTitle);
//					view.setTextSize((int)getResources().getDimension(R.dimen.index_text_size_28px));
					view.setTextColor(R.color.txt);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.CENTER_IN_PARENT);
					layout.addView(view, params);
					mLinearLayout.addView(layout,(int) (mScreenWidth / colum + 0.5f),Utils.dip2px(MainActivity.this, 80));
					layout.setOnClickListener(MainActivity.this);
					layout.setTag(i);
				}
				initViewPager();
				break;
			}

		}
	};
}

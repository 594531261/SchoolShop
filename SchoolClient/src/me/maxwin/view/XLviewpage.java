package me.maxwin.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.MyFinalHttp;
import com.school.application.Myapp;
import com.school.bean.Poster;
import com.school.bean.Produce;
import com.school.net.NetInfo;
import com.school.net.SignUtil;
import com.school.schoolclient.R;
import com.school.schoolclient.ShopDetail;

public class XLviewpage extends LinearLayout {
	private LinearLayout mContainer;
	FinalBitmap finalBitmap;
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合
	private List<ImageView> dots; // 图片标题正文的那些点
	private int currentItem = 0; // 当前图片的索引号
	private LinearLayout dot,gbsize;
	private FinalHttp finalHttps;
	Context mContext;
	LinearLayout.LayoutParams pp;
	MyAdapter adapter;
	List<Poster> tList = new ArrayList<Poster>();
	public XLviewpage(Context context) {
		super(context);
		mContext = context;
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XLviewpage(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		this.mContext = context;
		finalBitmap = FinalBitmap.create(context);
		finalBitmap.setExitTasksEarly(false);
		finalBitmap.configBitmapLoadThreadSize(10);
		// 初始情况，设置下拉刷新view高度为0
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, (int)getResources().getDimension(R.dimen.focus_height));
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.viewge, null);
		addView(mContainer, lp);
//		setGravity(Gravity.BOTTOM);
		mContainer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});

		if(Myapp.isNetworkAvailable(mContext)){
			loadData();
		}else{
			String wifigps = mContext.getString(R.string.wifigps);
			Toast.makeText(mContext, wifigps,Toast.LENGTH_LONG).show();
		}
		imageViews = new ArrayList<ImageView>();
		viewPager = (ViewPager) findViewById(R.id.vp);
		gbsize = (LinearLayout) findViewById(R.id.gbsize);
		LinearLayout.LayoutParams ppgbsize = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int)getResources().getDimension(R.dimen.focus_height));
		gbsize.setLayoutParams(ppgbsize);
		dot = (LinearLayout) findViewById(R.id.dot);
		 dots = new ArrayList<ImageView>();
		  pp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		 pp.gravity = Gravity.CENTER_VERTICAL;
		 pp.weight=1;
		 adapter = new MyAdapter();
		viewPager.setAdapter(adapter);// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		viewPager.setCurrentItem(0);
		
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				viewPager.setCurrentItem(currentItem);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				int size = tList.size();
				for (int i = 0; i < size; i++) {
					Poster bean = tList.get(i);

					ImageView imageView = new ImageView(mContext);
					finalBitmap.display(imageView,
							NetInfo.GETPNG + bean.getPoster_img());

					imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
					LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					imageView.setLayoutParams(mParams);

					imageViews.add(imageView);

					ImageView subdot = new ImageView(mContext);
					if (i == 0) {
						subdot.setImageResource(R.drawable.dot_selected);
					} else {
						subdot.setImageResource(R.drawable.dot_unselected);
					}
					subdot.setLayoutParams(pp);
					dot.addView(subdot);
					dots.add(subdot);
				}

				dot = (LinearLayout) findViewById(R.id.dot);
				FrameLayout.LayoutParams ppgbsize = new FrameLayout.LayoutParams(
						(int) getResources().getDimension(R.dimen.dot_height)
								* size, (int) getResources().getDimension(
								R.dimen.dot_height));
				ppgbsize.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
				ppgbsize.bottomMargin = (int) getResources().getDimension(
						R.dimen.dot_bottom);
				dot.setLayoutParams(ppgbsize);

				adapter.notifyDataSetChanged();
				startAutoPlay();

				break;

			default:
				break;
			}

		}
	};
	int itemsCount =0;
	public void startAutoPlay() {
		itemsCount = dots.size();
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (currentItem == itemsCount - 1) {
					currentItem = 0;
				} else {
					currentItem++;
				}
				Message msg = handler.obtainMessage();
				msg.what=1;
				handler.sendMessage(msg);
			}
		}, 4000, 4000);
	}
	
	private List<Produce> loadData() {
		String local_time = String.valueOf(System.currentTimeMillis());
		List<String> srcList = new ArrayList<String>();
		srcList.add("user_id");
		srcList.add("category");
		srcList.add("product_id");
		// srcList.add("page");

		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("user_id", local_time);
		map.put("category", "-1");
		map.put("product_id", "");
		// map.put("page", (currentPage + 1)+"");

		// String sign = SignUtil.generateSignature(srcList,map);

		finalHttps = new MyFinalHttp(mContext);
		AjaxParams params = SignUtil.getSignatureAjaxParams(map);

		final List<Produce> duitangs = new ArrayList<Produce>();
		finalHttps.post(NetInfo.PRODUCELIST, params,
				new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						JSONObject ite;
						try {
							ite = new JSONObject(t);
							String code = new String(ite.getString("code"));
							String getmsg = new String(ite.getString("msg"));
							
							if ("1".equals(code)) {
								String data = new String(ite
										.getString("data"));
								JSONObject da = new JSONObject(data);
								JSONArray object = da
										.getJSONArray("poster_object");
								int length = object.length();
								
								for (int i = 0; i < length ; i++) {// 遍历JSONArray
									Poster bean = new Poster();
									JSONObject oj = object.getJSONObject(i);
									bean.parse(oj);
									tList.add(bean);
									
								}
								Message msg = handler.obtainMessage();
								msg.what=2;
								handler.sendMessage(msg);
							}else{
								Toast.makeText(mContext, getmsg, Toast.LENGTH_LONG).show();
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
		return duitangs;
	}



	 /**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
//			System.out.println("position---------->"+position);
			for(int i=0;i<dots.size();i++){
				if(i==position){
					dots.get(i).setImageResource(R.drawable.dot_selected);
				}else{
					dots.get(i).setImageResource(R.drawable.dot_unselected);
				}
			}
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, final int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			imageViews.get(arg1).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, ShopDetail.class);
					intent.putExtra("product_id", Integer.parseInt(tList.get(arg1).getProduct_id()));
					intent.putExtra("main_img_size",(tList.get(arg1).getMain_img_size()));
					mContext.startActivity(intent);
				}
			});
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
	private String getMonOfDate()
	{
        Calendar c = Calendar.getInstance(); 
    	c.setTimeZone(TimeZone.getDefault());
        int  mMonth,mDay;
        String mon,day;
        mMonth = c.get(Calendar.MONTH)+1;//获取当前月份 
        if(mMonth<10){
        	mon = "0"+mMonth;
        }else{
        	mon = ""+mMonth;
        }
        mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码 
        if(mDay<10){
        	day = "0"+mDay;
        }else{
        	day = ""+mDay;
        }
        return day+"/"+mon;
	}
	private  String getWeekOfDate() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}
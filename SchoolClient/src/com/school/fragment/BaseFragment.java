package com.school.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeMap;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import me.maxwin.view.XLviewpage;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyFinalHttp;
import com.dodowaterfall.widget.ScaleImageView;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.school.application.Myapp;
import com.school.bean.BaseBean;
import com.school.bean.CacheManager;
import com.school.bean.Produce;
import com.school.net.NetInfo;
import com.school.net.SignUtil;
import com.school.schoolclient.R;
import com.school.schoolclient.ShopDetail;
import com.school.utils.Utils;

@SuppressLint("NewApi")
public class BaseFragment extends Fragment implements IXListViewListener {
	private Activity activity;
	View view;
	private boolean isIndex = false;

	private String mCategoryId = "1";
	public void setCategoryId(String id) {
		this.mCategoryId = id;
	}
	
	/**
	 * 是否是首页
	 * @param isIndex
	 */
	public void setIsIndex(boolean isIndex){
		this.isIndex = isIndex;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = new DisplayMetrics();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (view == null) {
			view = inflater.inflate(R.layout.layout_2, null);
			activity = this.getActivity();

			mAdapterView = (XListView) view.findViewById(R.id.list);
			
			if(isIndex){
				XLviewpage mViewPage = new XLviewpage(activity);
				mAdapterView.addHeaderView(mViewPage);
			}

			mAdapterView.setPullLoadEnable(true);
			mAdapterView.setXListViewListener(this);

			mAdapter = new StaggeredAdapter(activity, mAdapterView);
			mAdapterView.setAdapter(mAdapter);
			mImageFetcher = new ImageFetcher(activity, 240);
			mImageFetcher.setImageCache(new ImageCache(activity,"/.school/"));
			mAdapterView.startRefresh();
//			AddItemToContainer(currentPage, 2);
		} else {
			((ViewGroup) view.getParent()).removeAllViews();
		}

		return view;
	}

	private ImageFetcher mImageFetcher;
	private XListView mAdapterView = null;
	private StaggeredAdapter mAdapter = null;
	private int currentPage = 1;
	private int is_next = 1;
	private String indexPage="0";
	ContentTask task = new ContentTask(activity, 1);

	private class ContentTask{

		private int mType = 1;
		Status status = Status.FINISHED;

		public ContentTask(Context context, int type) {
			super();
			mType = type;
		}
		
		public synchronized void setType(int type){
			this.mType = type;
		}
		
		public Status getStatus(){
			return status;
		}
		public synchronized void execute(String param){
			loadData(param);
		}

		private List<Produce> loadData(String currentPages) {
			status = Status.RUNNING;
			
			String local_time = String.valueOf(System.currentTimeMillis());
			TreeMap<String, String> map = new TreeMap<String, String>();
			map.put("user_id", local_time);
			map.put("category", mCategoryId);
			map.put("page", currentPages);

			finalHttps = new MyFinalHttp(getActivity());
			AjaxParams params = SignUtil.getSignatureAjaxParams(map);

			final List<Produce> duitangs = new ArrayList<Produce>();
			finalHttps.post(false,NetInfo.PRODUCELISTA, params,
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
									
									CacheManager.getInstance().saveUpdateTime(mCategoryId, System.currentTimeMillis());
									is_next = BaseBean.getIntFromJson(ite, "is_next");
									
									String data = new String(ite.getString("data"));
									JSONObject da = new JSONObject(data);
									JSONArray object = da.getJSONArray("product_object");
//									 System.out.println("object.length()------------>"+object.length());
									int length = object.length();
									if(length==0){
//										mAdapterView.stopLoadMore();
//										Toast.makeText(activity, "加载完成", Toast.LENGTH_LONG).show();
									}else{
//										Toast.makeText(activity, getmsg, Toast.LENGTH_LONG).show();
										for (int i = 0; i < length; i++) {// 遍历JSONArray
											Produce bean = new Produce();
											JSONObject oj = object.getJSONObject(i);
											bean.parse(oj);
											// System.out.println("title------------>"+oj.getString("title"));
											// System.out.println("brand------------>"+oj.getString("brand"));
											duitangs.add(bean);

//											currentPage = String.valueOf(bean.getProduct_id());
//											System.out.println("currentPage-2----->"+currentPage);
										}
										
										
										
										if(is_next == 0){
											mAdapterView.setPullLoadEnable(false);
										}
										
										currentPage++;
										if (mType == 1) {
//											mAdapter.addItemTop(duitangs);
											mAdapter.refreshItems(duitangs);
											mAdapter.notifyDataSetChanged();
											mAdapterView.stopRefresh();

										} else if (mType == 2) {
											mAdapterView.stopLoadMore();
											mAdapter.addItemLast(duitangs);
											mAdapter.notifyDataSetChanged();
										}
										/*if(length<20){
											Toast.makeText(activity, "加载完成", Toast.LENGTH_LONG).show();
											mAdapterView.stopLoadMore();
										}*/
									}
									
									
									
								}else{
									Toast.makeText(activity, getmsg, Toast.LENGTH_LONG).show();
								}
									
							} catch (JSONException e) {
								e.printStackTrace();
							}
							 status = Status.FINISHED;
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							 status = Status.FINISHED;
						}
					});
			return duitangs;
		}
	}

	private MyFinalHttp finalHttps;

	/**
	 * 添加内容
	 * 
	 * @param pageindex
	 * @param type
	 *            1为下拉刷新 2为加载更多
	 */
	private synchronized void AddItemToContainer(int pageindex, int type) {
		if(Myapp.isNetworkAvailable(activity)){
			if (task.getStatus() != Status.RUNNING /*&& !indexPage.equals(pageindex)*/) {
				task.setType(type);// = new ContentTask(activity, type);
				task.execute(String.valueOf(pageindex));
			}
		}
		
		else{
			String wifigps = activity.getString(R.string.wifigps);
			Toast.makeText(activity, wifigps,Toast.LENGTH_LONG).show();
		}
	}

	public class StaggeredAdapter extends BaseAdapter {
		private LinkedList<Produce> mInfos;

		public StaggeredAdapter(Context context, XListView xListView) {
			mInfos = new LinkedList<Produce>();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());

			if (position == 0) {
				convertView = layoutInflator.inflate(R.layout.ample, null);
				convertView.setBackgroundResource(R.drawable.week_circle);
				TextView clocktxt = (TextView) convertView.findViewById(R.id.clocktxt);
				TextView first_date = (TextView) convertView.findViewById(R.id.first_date);
				TextView first_week = (TextView) convertView.findViewById(R.id.first_week);
				first_date.setText(getDate());
				first_week.setText(getWeekOfDate());
				clocktxt.setText(getDateTime());
				return convertView;
			} else {
				final Produce tProduce = mInfos.get(position-1);
				if (tProduce == null) {
					System.out.println("aaa");
					return null;
				}
				// if(position == 1 || convertView == null){
				convertView = layoutInflator.inflate(R.layout.infos_list, null);
				holder = new ViewHolder();
				holder.convertView = convertView;
				holder.imageView = (ScaleImageView) convertView.findViewById(R.id.news_pic);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.num = (TextView) convertView.findViewById(R.id.num);
				holder.detail_zhe = (TextView) convertView.findViewById(R.id.detail_zhe);
				holder.biaoqian = (LinearLayout) convertView.findViewById(R.id.biaoqian);
				
				FrameLayout.LayoutParams pas = new FrameLayout.LayoutParams(
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT
						,Utils.getHeight(Myapp.width/2, tProduce.getMain_img_size()));
				pas.topMargin =0;
				pas.leftMargin = 0;
				pas.gravity=Gravity.TOP;
				holder.imageView.setLayoutParams(pas);
				convertView.setTag(holder);
				// }else{
				// holder = (ViewHolder) convertView.getTag();
				// }

				// holder.imageView.setImageWidth(Myapp.columnWidth);
				// holder.imageView.setImageHeight(400);
				holder.title.setText(tProduce.getTitle());
				holder.price.setText(tProduce.getDoubleCurrentPrice()+"");
				
				/*int count = Integer.parseInt(tProduce.getTotal_sales());
				String total =null;
				if(count>=10000){
					total = "1W";
				}else{
					total =count+"";
				}*/
				
				String product_count = tProduce.getTotal_sales();
				
				if(Integer.parseInt(product_count)>10000){
					double prc = 0.0f;
					String currr = product_count;
					double cur = Double.parseDouble(currr);
					double ore = 10000;
					prc = cur / ore;
					String pc = String.valueOf(prc).trim();
					if(pc.length()>4){
						product_count = pc.substring(0, 4)+"w";
					}else{
						product_count=pc+"w";
					}
				}
//				System.out.println("getCurrent_price:"+tProduce.getCurrent_price()+"---------"+"product_count:"+product_count);
				holder.num.setText(product_count);

				double zhe = tProduce.getZhekou();

				String zhe1 = String.valueOf(zhe);
				String zhes;
				if (zhe1.length() > 4) {
					zhes = zhe1.substring(0, 4);
				} else {
					zhes = zhe1;
				}
				if ("0.0".equals(zhes)) {
					holder.biaoqian.setVisibility(View.GONE);
				}

				holder.detail_zhe.setText(zhes + "折");
				mImageFetcher.loadImage(tProduce.getMain_img_url(),holder.imageView);

				holder.convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity, ShopDetail.class);
						intent.putExtra("product_id", tProduce.getProduct_id());
						intent.putExtra("main_img_size", tProduce.getMain_img_size());
						activity.startActivity(intent);
					}
				});

				return convertView;
			}

		}

		class ViewHolder {
			View convertView;
			ScaleImageView imageView;
			TextView title;
			TextView num;
			TextView price;
			TextView detail_zhe;
			LinearLayout biaoqian;
//			LinearLayout updatecheck;
		}

		@Override
		public int getCount() {
			return mInfos.size()+1;
		}

		@Override
		public Object getItem(int arg0) {
			return mInfos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		public void addItemLast(List<Produce> datas) {
			mInfos.addAll(datas);
		}

		public void addItemTop(List<Produce> datas) {
			for (Produce info : datas) {
				mInfos.addFirst(info);
			}
		}
		public void refreshItems(List<Produce> datas) {
			mInfos.clear();
			mInfos.addAll(datas);
		}
	}

	@Override
	public void onRefresh() {
//		System.out.println("currentPage-7---->"+currentPage);
		currentPage = 1;
		is_next = 1;
		mAdapterView.setPullLoadEnable(true);
		AddItemToContainer(currentPage, 1);

	}

	@Override
	public void onLoadMore() {
//		System.out.println("currentPage-3---->"+currentPage);
		AddItemToContainer(currentPage, 2);

	}

	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		// mAdapterView.setAdapter(mAdapter);
//		MobclickAgent.onPageStart("MainScreen"); // 统计页面
	}

	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd("MainScreen");
	}
	private String getDateTime()
	{
				
        Calendar c = Calendar.getInstance(); 
//        TimeZone zone = TimeZone.getTimeZone("GMT+8");
    	c.setTimeZone(TimeZone.getDefault());
    	c.setTimeInMillis(CacheManager.getInstance().getUpdateTime(mCategoryId));
        int  mYear,mMonth,mDay,mHour,mMinute;
        String mon,day,hour,min;
        mYear = c.get(Calendar.YEAR); //获取当前年份 
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
        
        mHour = c.get(Calendar.DECEMBER);//获取当前的小时数 
        if(mHour<10){
        	hour = "0"+mHour;
        }else{
        	hour = ""+mHour;
        }
        mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
        if(mMinute<10){
        	min = "0"+mMinute;
        }else{
        	min = ""+mMinute;
        }
//        date = mYear+"年"+mon+"月"+day+"日"+" "+getWeekOfDate(new Date());
//        CacheManager.getInstance().saveUpdateTime(hour+":"+min);
        return  hour+":"+min;
	}
	
	public String getDate(){
		Calendar c = Calendar.getInstance(); 
//      TimeZone zone = TimeZone.getTimeZone("GMT+8");
  	c.setTimeZone(TimeZone.getDefault());
  	c.setTimeInMillis(CacheManager.getInstance().getUpdateTime(mCategoryId));
      int  mYear,mMonth,mDay,mHour,mMinute;
      String mon,day,hour,min;
      mYear = c.get(Calendar.YEAR); //获取当前年份 
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
      return day + "/" + mon;
	}
	
	/**
     * 获取当前日期是星期几<br>
     * 
     * @param dt
     * @return 当前日期是星期几
     */
    public String getWeekOfDate() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(CacheManager.getInstance().getUpdateTime(mCategoryId));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}

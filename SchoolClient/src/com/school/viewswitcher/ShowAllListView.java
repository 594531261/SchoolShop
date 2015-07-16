package com.school.viewswitcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author dwy
 * 全部展开的listView，高度为包裹内容，设置为不滚动，全部显示
 *
 */
public class ShowAllListView extends ListView {

	public ShowAllListView(Context context) {
		super(context);
	}
	
	public ShowAllListView(Context context,AttributeSet attrs) {
		super(context,attrs);
	}

//	/**
//	 * 设置不滚动
//	 */
//	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
//	{
//		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//				MeasureSpec.AT_MOST);
//		super.onMeasure(widthMeasureSpec, expandSpec);
//
//	}
	
	public void setListViewHeightBasedOnChildren( ) {
		ListView listView = this;
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		
		if(listAdapter.getCount()>3){
			totalHeight += (listAdapter.getCount())/3 * 30;
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1)) 
				+ listView.getPaddingBottom()
				+ listView.getPaddingTop()
				+getVerticalFadingEdgeLength() * 2 + 30;
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		setListViewHeightBasedOnChildren();
	}
	
}

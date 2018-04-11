package com.msg.android.lib.widget.viewpager.adapter;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.msg.android.lib.widget.viewpager.CommonBannerViewPager;

import java.util.List;


public class CommonBannerViewPagerAdapter extends CommonBannerViewPager.AbsMyViewPagerAdapter {

	private List<View> mListViews;
	private Activity context;
	public CommonBannerViewPagerAdapter(Activity context,List<View> mListViews) {
		this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		this.context = context;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(getRealCount() <= 1){
			return getRealCount();
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
		CommonBannerViewPager myViewPager = (CommonBannerViewPager) container;
		int index = position % mListViews.size();
		//System.out.println("index ======= " + index);
		try {
			((ViewPager)container).addView(mListViews.get(index), 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		return mListViews.get(index);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;// 官方提示这样写
	}

	@Override
	public int getRealCount() {
		// TODO Auto-generated method stub
		if(mListViews!=null && mListViews.size() > 0){
			return mListViews.size();
		}
		return 0;
	}

}

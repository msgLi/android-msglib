package com.msg.android.lib.widget.viewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;

/**
 * 1.自定义viewpager实现广告界面,支持手势滑动和点击事�?不支持无限向�?��方向循环 2.播放到两边之后会循环来回播放
 * 
 */
public class CommonBannerViewPager extends ChildViewPager {

	private Context context;
	private LayoutInflater inflater;

	private long fir_time, last_time;

	private CommonBannerViewPagerItemClick click;

	private boolean isClick = true;

	public boolean isPlay = true;

	public boolean hasStart = false;

	private int start = 100;

	private boolean isStartRight = true;

	private float MoveX, MoveY;

	private float mDownX,mDownY;

	private int time;// 切换间隔时间

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == start && isPlay) {
				if(((AbsMyViewPagerAdapter)getAdapter()).getRealCount() == 1){
					//sendMess();
					//isPlay = false;
				}
				if (getCurrentItem() + 1 == getAdapter().getCount()) {
					setCurrentItem(0);
				} else {
					setCurrentItem(getCurrentItem() + 1);
				}
			}
			sendMess();

		}

	};

	public static abstract class AbsMyViewPagerAdapter extends PagerAdapter{
		public abstract int getRealCount();
	}

	
	public CommonBannerViewPager(Context context) {
		super(context);
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public CommonBannerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		inflater = LayoutInflater.from(context);
	}


	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		Log.i("ljs", "this is a touch");
		
		isPlay = false;

		float X = arg0.getX();
		float Y = arg0.getY();

		switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			fir_time = System.nanoTime();
			MoveX = X;
			MoveY = Y;
			isClick = true;
			isPlay = false;
			break;
		case MotionEvent.ACTION_UP:
			last_time = System.nanoTime();
			if ((last_time - fir_time) / 1000000l > 1000
					|| last_time - fir_time <= 0 || !isClick) {
				Log.i("ljs", "this is a click cancel");
			} else {
				Log.i("ljs", "this is a click ");
				if(null!=click){
					click.OnClick(getCurrentItem());
				}
			}
			isPlay = true;
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) (MoveX - X);
			int moveY = (int) (MoveY - Y);
			if (moveX * moveX + moveY * moveY > 640) {
				isClick = false;
				isPlay = true;
			}else{
				isClick = true;
				isPlay = false;
			}

			break;
		}
		return super.onTouchEvent(arg0);
	}
		
	/**
	 * 自定义点击事件接�?
	 * 
	 * @param click
	 */
	public void SetMysetOnClickListener(CommonBannerViewPagerItemClick click) {
		this.click = click;
	}

	/**
	 * 通过此方法启动hanlder实现无限循环来播放view,time 为每次播放间隔时�?
	 */
	public void start(int time) {
		hasStart = true;
		this.time = time;
		sendMess();
	}

	public void pausePlay(){
		isPlay = false;
	}

	public void continuePlay(){
		isPlay = true;
	}

	private void sendMess() {
		Message message = new Message();
		message.what = start;
		handler.sendMessageDelayed(message, time);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getX();
			mDownY = ev.getY();
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			if(Math.abs(ev.getX()-mDownX) > Math.abs(ev.getY()-mDownY)){
				getParent().requestDisallowInterceptTouchEvent(true);
			}else{
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		}
		boolean ret = super.dispatchTouchEvent(ev);
		if (ret) {
			requestDisallowInterceptTouchEvent(true);
		}
		return ret;
	}

}

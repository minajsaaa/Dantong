package com.goodmorningrainbow.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

public class CustomViewPager extends ViewPager {

	private boolean enabled;
	
	public CustomViewPager(Context context) {
		super(context);
		postInitViewPager();
	}
	
	public CustomViewPager(Context context, AttributeSet attr) {
		super(context, attr);
		postInitViewPager();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		try {
			if(this.enabled) {
				return super.onTouchEvent(event);
			} 
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			Log.e("INFO", exceptionAsString);
		}
		
		return false;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		
		if(this.enabled) {
			return super.onInterceptTouchEvent(event);
		}
		
		return false;
	}
	
	public void setPagingEnabled() {
		this.enabled = true;
	}
	
	public void setPagingDisabled() {
		this.enabled = false;
	}
	
	private FixedSpeedScroller mScroller = null;
	
	private void postInitViewPager() {
		try {
			Field scroller = ViewPager.class.getDeclaredField("mScroller");
			scroller.setAccessible(true);
			Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
			interpolator.setAccessible(true);
			
			mScroller = new FixedSpeedScroller(getContext(), (Interpolator)interpolator.get(null));
			scroller.set(this, mScroller);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setScrollDurationFactor(double scrollFactor) {
		mScroller.setScrollDurationFactor(scrollFactor);
	}
}

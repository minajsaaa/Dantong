package com.goodmorningrainbow.dantongapp;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GCMAlertService extends Service {
	
	private final String CHECKED_ACTIVITY = "PushAlertActivity";

	private PowerManager pm;
	
	private String notiMessage = "";
	private String link = "";
	private Display display = null;
	
	private LinearLayout backgroundView = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		
		if(intent != null && intent.getExtras() != null) {
			Bundle b = intent.getExtras();
			if (b != null) {
				if (b.getString("msg") != null) {
					notiMessage = b.getString("msg");					
				}
				if (b.getString("link") != null) {
					link = b.getString("link");
				}
			} else {
				stopSelf();
			}
			Log.e("", "=== start alert service");
			if(!isScreenOn()) {
				initScreenOff();
			} else {
				if(!CheckedTopActivity()) initScreenOn();
				else initScreenOff();
			}
		}
	}
	
	private void fadeOutFinish(View v, int dur){

        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(dur);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        v.startAnimation(animation);
        
        animation.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
		    	stopSelf();
			}
		});
    }
	
	private boolean isScreenOn() {
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

	private void initScreenOn() {
		
		LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		backgroundView = (LinearLayout) vi.inflate(R.layout.a_gcm, null);

		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,					
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);			
		
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);	
		
		wm.addView(backgroundView, params);
		
		final RelativeLayout layout = (RelativeLayout) backgroundView.findViewById(R.id.gcm_layout);
		TextView tv_title = (TextView) backgroundView.findViewById(R.id.gcm_title);
		TextView tv_message = (TextView) backgroundView.findViewById(R.id.gcm_message);
		
		tv_title.setText(getString(R.string.app_name));
		tv_message.setText(notiMessage);
		
		
		tv_title.setBackgroundColor(Color.TRANSPARENT);
		tv_message.setBackgroundColor(Color.TRANSPARENT);
		
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				fadeOutFinish(layout, 2000);
			}
		}, 3000);
		
		int disWidth = display.getWidth() / 45;
		int disHeight = display.getHeight() / 45;
		int pointSize = (disWidth > disHeight) ? disHeight : disWidth;
		
		if (display.getWidth() < 500) pointSize = 14;
		else pointSize = 16;
		
		tv_title.setTextSize(pointSize);
		tv_message.setTextSize(pointSize);
	}
	
	private void initScreenOff() {
		Intent i = new Intent(getApplicationContext(), PushAlertActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("msg", notiMessage);
		i.putExtra("link", link);
		startActivity(i);
	}
	
	private boolean CheckedTopActivity() {
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> info;
		info = activityManager.getRunningTasks(1);
		for(Iterator iterator = info.iterator(); iterator.hasNext();) {
			RunningTaskInfo runningTaskInfo = (RunningTaskInfo) iterator.next();
			if(!runningTaskInfo.topActivity.getClassName().contains(CHECKED_ACTIVITY)) {
				return false;
			}
		}
		return true;
	}
}

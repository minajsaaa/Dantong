package com.goodmorningrainbow.dantongapp;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDialog extends Dialog implements OnClickListener {

    private Activity activity;
	
	private TextView TITLE;
	private TextView MESSAGE;
	private ImageView ICON;
	private String link = "";
	
	public CustomDialog(Activity activity, String link) {
		super(activity);
		
        this.activity = activity;
        this.link = link;
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		setContentView(R.layout.activity_dialog);
		
		ICON = (ImageView) findViewById(R.id.dialog_icon);
		TITLE = (TextView) findViewById(R.id.dialog_title);
		MESSAGE = (TextView) findViewById(R.id.dialog_msg);
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_open:
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			
			RecentTaskInfo recentTaskInfo = getRecentTaskInfo(activity.getPackageName());
			Intent i = null;
			
			if(link.length() == 0) {
				i = new Intent(activity.getApplicationContext(), MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			} else {
				i = new Intent(Intent.ACTION_VIEW, Uri.parse(link)); 
			}
			/*if(recentTaskInfo != null) {
				if(recentTaskInfo.baseIntent.toString().contains("PushAlertActivity")) {
					i = new Intent(activity.getApplicationContext(), TitleActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				} else {
					i = recentTaskInfo.baseIntent;
				}
			}*/
			activity.startActivity(i);
			
			dismiss();
			activity.finish();			
			break;
			
		case R.id.btn_close:
			dismiss();
			activity.finish();
			break;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Button BtnOpen = (Button) findViewById(R.id.btn_open);
		Button BtnClose = (Button) findViewById(R.id.btn_close);
		
		BtnOpen.setOnClickListener(this);
		BtnClose.setOnClickListener(this);
	}

	@Override
	public void setTitle(CharSequence title) {
		TITLE.setText(title);
	}
	
	public void setMsg(String msg) {
		MESSAGE.setText(msg);
	}
	
	public void setIcon(Drawable drawable) {
		ICON.setImageDrawable(drawable);
	}
	
	private RecentTaskInfo getRecentTaskInfo(final String packageName) {
	    final ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
	    try {
	        final List<RecentTaskInfo> infoList = manager.getRecentTasks(10, 0); 
	        for (RecentTaskInfo info : infoList) {
	            if (info.baseIntent.getComponent().getPackageName().equals(packageName)) {
	                return info;
	            }
	        }
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	    }
	    return null;
	 }
}

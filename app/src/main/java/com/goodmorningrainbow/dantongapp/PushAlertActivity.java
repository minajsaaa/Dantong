package com.goodmorningrainbow.dantongapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class PushAlertActivity extends Activity {
	
	private String msg = "";
	private String link = "";
	private static CustomDialog custom;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setBackgroundColor(Color.BLACK);
		setContentView(layout);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		onNewIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		if(intent.getExtras() != null){
			Bundle b = intent.getExtras();
			if(b != null) {
				msg = b.getString("msg");
				link = b.getString("link");				
			}
			
			System.out.println("link : " + link);
			
			if(msg != null) {
				Log.e("", "Screen off");
				CheckedAlert();
				
				custom = new CustomDialog(this, link);
				custom.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
				custom.setTitle(getString(R.string.app_name));
				custom.setCancelable(false);
				custom.setCanceledOnTouchOutside(false);
				
				custom.setMsg(msg);
				custom.show();
			}
		}
	}
	
	public boolean CheckedAlert() {
		if(custom == null) return false;		
		
		custom.dismiss();
		custom = null;
		
		return true;
	}
	
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		if(custom != null) {
			custom.dismiss();
			custom = null;
			finish();
		}
	}
}

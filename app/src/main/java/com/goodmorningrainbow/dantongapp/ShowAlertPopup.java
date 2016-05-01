package com.goodmorningrainbow.dantongapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodmorningrainbow.common.Const;

public class ShowAlertPopup extends Activity {
	
	private RelativeLayout layout;
	private TextView costTxt;
	private TextView monthlyTxt;
	
	private int pType = 0;
	private int pMonthly = 0;
	private String mCost = "";	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.show_popup_layout);
		
		pType = getIntent().getExtras().getInt(Const.POPUP_TYPE);
		mCost = getIntent().getExtras().getString(Const.SHOW_COST);
		pMonthly = getIntent().getExtras().getInt(Const.SHOW_MONTHLY);
		
		layout = (RelativeLayout)findViewById(R.id.showPopupLayout);
		costTxt = (TextView)findViewById(R.id.showPopupCostTxt);
		monthlyTxt = (TextView)findViewById(R.id.showPopupMonthlyTxt);
		
		switch (pType) {
		case 0:
			layout.setBackgroundResource(R.drawable.popup_monthly);			
			monthlyTxt.setText(String.format(getString(R.string.monthly), pMonthly));
			break;

		case 1:
			layout.setBackgroundResource(R.drawable.popup_dc);
			if(pMonthly == 36) pMonthly = 30;
			monthlyTxt.setText(String.format(getString(R.string.monthly), pMonthly));
			break;

		case 2:
			layout.setBackgroundResource(R.drawable.popup_tax);
			monthlyTxt.setText(String.format(getString(R.string.monthly), pMonthly));
			break;
		}
		
		costTxt.setText(mCost);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
        switch (event.getAction()) {
    	
		case MotionEvent.ACTION_UP:
			finish();
			break;		
		}
    	
    	return true;
	}
}

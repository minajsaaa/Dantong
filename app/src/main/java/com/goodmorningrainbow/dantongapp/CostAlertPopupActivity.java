package com.goodmorningrainbow.dantongapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

public class CostAlertPopupActivity extends Activity{
	
	private ImageView alertImg;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.cost_alert_popup_layout);
    	
    	int id = getIntent().getExtras().getInt("id");
    	
    	alertImg = (ImageView)findViewById(R.id.alertPopupImg);
    	
    	if(id == 89) {
    		//alertImg.setBackgroundResource(R.drawable.popup_clubt_standard);
    		alertImg.setImageResource(R.drawable.popup_clubt_standard);
    	} else {
    		//alertImg.setBackgroundResource(R.drawable.popup_clubt_premium);
    		alertImg.setImageResource(R.drawable.popup_clubt_premium);
    	}
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

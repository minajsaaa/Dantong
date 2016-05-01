package com.goodmorningrainbow.dantongapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class SelectManuPopupActivity extends Activity {
	
	private Button pSmBtn;
	private Button pLgBtn;
	private Button pPeBtn;
	private Button pEtBtn;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	setContentView(R.layout.sel_manuf_pop_layout);
    	
    	pSmBtn = (Button)findViewById(R.id.mSSBtn);
    	pLgBtn = (Button)findViewById(R.id.mLGBtn);
    	pPeBtn = (Button)findViewById(R.id.mPEBtn);
    	pEtBtn = (Button)findViewById(R.id.mETBtn);
    	
    	//삼성
    	pSmBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Const.SELECT_MANUF = "1";
				Intent intent = getIntent();
				intent.putExtra("manuf", "1");
				setResult(RESULT_OK, intent);
				finish();
			}
		});
    	
    	//LG
    	pLgBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Const.SELECT_MANUF = "2";
				Intent intent = getIntent();
				intent.putExtra("manuf", "2");
				setResult(RESULT_OK, intent);
				finish();
			}
		});
    	
    	//펜택
    	pPeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Const.SELECT_MANUF = "3";
				Intent intent = getIntent();
				intent.putExtra("manuf", "3");
				setResult(RESULT_OK, intent);
				finish();
			}
		});
    	
    	//기타 통신사
    	pEtBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Const.SELECT_MANUF = "4";
				Intent intent = getIntent();
				intent.putExtra("manuf", "4");
				setResult(RESULT_OK, intent);
				finish();
			}
		});
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

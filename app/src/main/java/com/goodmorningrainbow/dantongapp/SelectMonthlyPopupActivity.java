package com.goodmorningrainbow.dantongapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectMonthlyPopupActivity extends Activity {
	
	private Button mon24Btn;
	private Button mon30Btn;
	private Button mon36Btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sel_monthly_pop_layout);
		
		mon24Btn = (Button)findViewById(R.id.selMon24Btn);
		mon30Btn = (Button)findViewById(R.id.selMon30Btn);
		mon36Btn = (Button)findViewById(R.id.selMon36Btn);
		
		mon24Btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.putExtra("month", 24);				
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		mon30Btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.putExtra("month", 30);				
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		mon36Btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.putExtra("month", 36);				
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

}

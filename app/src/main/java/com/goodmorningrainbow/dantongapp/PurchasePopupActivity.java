package com.goodmorningrainbow.dantongapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PurchasePopupActivity extends Activity implements View.OnClickListener {
	
	private Button adviceButton;
	private Button requestButton;

	//	========================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchase_popup_layout);

		adviceButton = (Button) findViewById(R.id.adviceButton);
		requestButton = (Button) findViewById(R.id.requestButton);

		adviceButton.setOnClickListener(this);
		requestButton.setOnClickListener(this);
	}

	//	========================================================================================

	@Override
	public void onClick(View v) {
		Intent intent = getIntent();
		int value = v.getId() == R.id.adviceButton ? 0 : 1;
		intent.putExtra("select", value);
		setResult(RESULT_OK, intent);
		finish();

	}



}

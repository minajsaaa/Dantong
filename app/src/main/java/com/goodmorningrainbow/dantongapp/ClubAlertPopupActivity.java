package com.goodmorningrainbow.dantongapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.goodmorningrainbow.common.Const;

public class ClubAlertPopupActivity extends Activity {

	private ImageView clubImage;
	private Button okBtn;
	private Button cancleBtn;
	private Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.club_alert_popup_layout);
		
		activity = this;
		
		clubImage = (ImageView)findViewById(R.id.clubPopupImg);
		okBtn = (Button)findViewById(R.id.clubOkBtn);
		cancleBtn = (Button)findViewById(R.id.clubCancleBtn);
		
		final int price = getIntent().getExtras().getInt("price");
		
		if(Const.SELECT_AGENCY == Const.TELECOM_SK) {
			if(price == 340000) {
				//clubImage.setImageResource(R.drawable.popup_free_club34);
			} else {
				//clubImage.setImageResource(R.drawable.popup_free_club35);
			}
		} else if(Const.SELECT_AGENCY == Const.TELECOM_KT) {
            if(price == 340000) {
            	//clubImage.setImageResource(R.drawable.popup_zero_plan34);
			} else {
				//clubImage.setImageResource(R.drawable.popup_zero_plan38);
			}
		} else {
            if(price == 300000) {
            	//clubImage.setImageResource(R.drawable.popup_zero_club30);
            } else if(price == 320000) {
            	//clubImage.setImageResource(R.drawable.popup_zero_club32);
			} else { 
				//clubImage.setImageResource(R.drawable.popup_zero_club34);
			}
		}
		
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("returnPrice", price);
                setResult(RESULT_OK, intent);
                finish();
			}
		});
		
		cancleBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("price", 0);
                setResult(RESULT_OK, intent);
                finish();				
			}
		});
	}
}

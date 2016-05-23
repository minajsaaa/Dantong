package com.goodmorningrainbow.dantongapp;

import java.util.ArrayList;

import kr.co.fingerpush.android.GCMFingerPushManager;
import kr.co.fingerpush.android.NetworkUtility.NetworkDataListener;
import nobug.webview.Browser;
import nobug.webview.BrowserActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.goodmorningrainbow.common.Const;
import com.goodmorningrainbow.common.CustomViewPager;
import com.goodmorningrainbow.common.PagerFragmentAdapter;
import com.goodmorningrainbow.constant.UrlDefinition;
import com.goodmorningrainbow.fragment.AdviceDetailFragment;
import com.goodmorningrainbow.fragment.MainLeftFragment;
import com.goodmorningrainbow.model.PhoneListDT;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends FragmentActivity implements OnClickListener {
	
	private CustomViewPager viewPager;
	private PagerFragmentAdapter mAdapter;
	
	private BackPressCloseHandler backPressCloseHandler;
	private MainActivity activity;
	
	private int current_page = Const.PAGE_LEFT;
	private Dialog mMainDialog;
	
	private GCMFingerPushManager manager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity = this;

		manager = GCMFingerPushManager.getInstance(this);
        NotificationManager notificationManager;
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        
        manager.setDevice(new NetworkDataListener() {
			@Override
			public void onError(String code, String errorMessage) {
				Log.e("", "errorMessage ::: " + errorMessage);
			}

			@Override
			public void onComplete(String code, String resultMessage, ArrayList<?> DataList, Integer TotalArticleCount, Integer CurrentPageNo) {
				Log.e("", "result code ::: " + code);
				Log.e("", "result Message ::: " + resultMessage);
				Toast.makeText(MainActivity.this, resultMessage, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCancel() {
			}
		});
		
		Tracker t = ((RainbowAnalytics) getApplication()).getTracker(RainbowAnalytics.TrackerName.APP_TRACKER);
		t.setScreenName("MainActivity");
		t.send(new HitBuilders.AppViewBuilder().build());

		backPressCloseHandler = new BackPressCloseHandler(this);
		
		mAdapter = new PagerFragmentAdapter(getSupportFragmentManager(), this);
		
		viewPager = (CustomViewPager)findViewById(R.id.mainViewPager);
		viewPager.setPagingDisabled();
		viewPager.setAdapter(mAdapter);
		viewPager.setOffscreenPageLimit(4);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK) {			
			if(requestCode == Const.SELECT_MANUF_RESULT_CODE) {
				Const.SELECT_MANUF = data.getStringExtra("manuf");
				MainLeftFragment.getInstance().setPhoneListFromServer();

			} else if(requestCode == Const.SELECT_PHONE_LIST_RESULT_CODE) {
				Const.PHONE_POSITION = data.getIntExtra("position", 0);
				MainLeftFragment.getInstance().setResultFromPhonList();								
			} else if(requestCode == Const.CLUB_SELECT_PRICE_RESULT_CODE) {				
				MainLeftFragment.getInstance().setResultFromClubPriceList(data);
				Log.e("rrobbie", "CLUB_SELECT_PRICE_RESULT_CODE : " + data);
			} else if(requestCode == Const.SELECT_PRICE_LIST_RESULT_CODE) {				
				MainLeftFragment.getInstance().setResultFromPriceList();						
			} else if(requestCode == Const.SELECT_MONTHLY_LIST_RESULT_CODE){				
				MainLeftFragment.getInstance().setResultFromMonthlyList(data);
			} else if(requestCode == Const.SELECT_ADVICE_RESULT_CODE) {
				int value = data.getIntExtra("select", 0);
				if( value == 0 ) {
					moveToPage(Const.PAGE_ADVICE);
				} else {
					Log.e("rrobbie", Const.PHONE_ID + " / " + Const.SELECT_AGENCY);
					String url = UrlDefinition.PhonePlus + "?key=" + Const.PHONE_ID + "&net=" + Const.SELECT_AGENCY;
					Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
					intent.putExtra("url", url);
					startActivity(intent);
				}
			}
		}
	}
	
	public void moveToPage(int position) {
		Log.d("rrobbie", "move to page : " + position );
		current_page = position;
		
		if( position == Const.PAGE_ADVICE_DETAIL ) {
			try {
				AdviceDetailFragment.getInstance().setProperties();	
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		viewPager.setCurrentItem(position, true);
	}
	
	@Override
	public void onBackPressed() {
		if(current_page == Const.PAGE_LEFT) {
			if(MainLeftFragment.getInstance().isShowEdit) {
				MainLeftFragment.getInstance().closeEditor();
			} else {
				mMainDialog = createDialog();
				mMainDialog.show();
			}
		} else {
			moveToPage(Const.PAGE_LEFT);
		}
	}
	
	private AlertDialog createDialog() {
    	final View innerView = getLayoutInflater().inflate(R.layout.finish_dialog_layout, null);
    	AlertDialog.Builder ab = new AlertDialog.Builder(this);
    	ab.setView(innerView);    	

    	ImageView end1_img = (ImageView)innerView.findViewById(R.id.end1_img);
    	ImageView end2_img = (ImageView)innerView.findViewById(R.id.end2_img);
    	ImageView end3_img = (ImageView)innerView.findViewById(R.id.end3_img);
    	ImageView advice_img = (ImageView)innerView.findViewById(R.id.advice_img);
    	
    	Button okButton = (Button) innerView.findViewById(R.id.okButton);
    	Button cancelButton = (Button) innerView.findViewById(R.id.cancelButton);
    	Button adviceButton = (Button) innerView.findViewById(R.id.adviceButton);
    	
    	end1_img.setOnClickListener(this);
    	end2_img.setOnClickListener(this);
    	end3_img.setOnClickListener(this);
    	advice_img.setOnClickListener(this);
    	
    	okButton.setOnClickListener(this);
    	cancelButton.setOnClickListener(this);
    	adviceButton.setOnClickListener(this);   	
    	return ab.create();
    }
    
    private void setDismiss(Dialog dialog) {
    	if(dialog != null && dialog.isShowing()) {
    		dialog.dismiss();
    	}
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.end1_img:
			Uri uri1 = Uri.parse("http://dantongmall.com");
			Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
			startActivity(intent1);
			break;
		case R.id.end2_img:
			Uri uri2 = Uri.parse("http://dantongmall.com");
			Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
			startActivity(intent2);
			break;
		case R.id.end3_img:
			Uri uri3 = Uri.parse("http://dantongmall.com/gbs/list.php?boardid=16");
			Intent intent3 = new Intent(Intent.ACTION_VIEW, uri3);
			startActivity(intent3);
			break;			
		case R.id.advice_img:
			setDismiss(mMainDialog);	    	
			moveToPage(Const.PAGE_ADVICE_DETAIL);
			break;	

		case R.id.okButton:
			activity.finish();
			break;				
			
		case R.id.cancelButton:
			setDismiss(mMainDialog);
			break;			
			
		case R.id.adviceButton:
			setDismiss(mMainDialog);
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-4523-9114"));
		    startActivity(intent);
			
			break;						
		default:
			break;
		}
	}
}

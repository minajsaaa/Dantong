package com.goodmorningrainbow.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.goodmorningrainbow.common.Const;
import com.goodmorningrainbow.common.IOnHandlerMessage;
import com.goodmorningrainbow.dantongapp.MainActivity;
import com.goodmorningrainbow.dantongapp.R;

public class AdviceFragment extends Fragment implements IOnHandlerMessage, OnClickListener{

	private static volatile AdviceFragment instance = null;
	
	private MainActivity mActivity = null;
	private View mView = null;
	
	private Button adviceButton;
	private Button cancelButton;
	
	public static AdviceFragment getInstance() {
		if(instance == null) {
			synchronized (AdviceFragment.class) {
				if(instance == null) {
					instance = new AdviceFragment();
				}
			}
		}
		
		Log.d("rrobbie", "AdviceFragment");
		
		return instance;
	}
	
	public void setActivity(MainActivity activity) {
		this.mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_advice_layout, container, false);
		adviceButton = (Button) mView.findViewById(R.id.adviceButton);
		cancelButton = (Button) mView.findViewById(R.id.cancelButton);
		
		adviceButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		
		return mView;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.adviceButton:
			mActivity.moveToPage(Const.PAGE_ADVICE_DETAIL);
			break;
			
		case R.id.cancelButton:
			mActivity.moveToPage(Const.PAGE_LEFT);
			break;
		default:
			break;
		}
		
	}
}


package com.goodmorningrainbow.dantongapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.goodmorningrainbow.lib.MonthlyListArrayAdapter;

public class MonthlyPopupActivity extends Activity {
	
	private ArrayList<String> mArray;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.sel_monthly_list_pop_layout);
		
		mArray = new ArrayList<String>();
		
		for(int i=1; i<25; i++) {
			mArray.add(i+"개월");
		}
		
		listView = (ListView)findViewById(R.id.monthlyListView);
		
		listView.setAdapter(new MonthlyListArrayAdapter(getApplicationContext(), R.layout.monthly_listview_rows, mArray));
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("POS", position);
				setResult(RESULT_OK, intent);
				finish();				
			}
		});
	}
}

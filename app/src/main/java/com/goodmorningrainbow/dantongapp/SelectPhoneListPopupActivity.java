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

import com.goodmorningrainbow.common.Const;
import com.goodmorningrainbow.lib.PhoneListArrayAdapter;
import com.goodmorningrainbow.model.PhoneListDT;

public class SelectPhoneListPopupActivity extends Activity {
	
	private ListView listView;
	private ArrayList<PhoneListDT> listArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		listArray = (ArrayList<PhoneListDT>)getIntent().getExtras().get("arraylist");
		
		setContentView(R.layout.sel_phone_list_pop_layout);
		
		listView = (ListView)findViewById(R.id.listView);
		
		listView.setAdapter(new PhoneListArrayAdapter(this, R.layout.phone_listview_rows, listArray));
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(Const.VERBOSE)System.out.println("position : " + position);
				Intent intent = getIntent();
				intent.putExtra("position", position);				
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}

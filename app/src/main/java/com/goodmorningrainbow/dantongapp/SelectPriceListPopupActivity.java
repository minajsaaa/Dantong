package com.goodmorningrainbow.dantongapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.goodmorningrainbow.common.Const;
import com.goodmorningrainbow.lib.PriceListArrayAdapter;

public class SelectPriceListPopupActivity extends Activity {

	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.sel_price_list_pop_layout);
		
		listView = (ListView)findViewById(R.id.priceListView);
		
		if(Const.PRICE_LIST_ARRAY != null) {
			listView.setAdapter(new PriceListArrayAdapter(this, R.layout.price_listview_rows, Const.PRICE_LIST_ARRAY));
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Const.PRICE_POSITION = position;
				setResult(RESULT_OK);
				finish();
			}
		});
	}
}

package com.goodmorningrainbow.lib;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.goodmorningrainbow.dantongapp.R;

public class MonthlyListArrayAdapter extends ArrayAdapter<String> {

private ArrayList<String> items;
	
	public MonthlyListArrayAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
		super(context, textViewResourceId, items);
		
		this.items = items;
	}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	View view = convertView;
    	
    	if(view == null) {
    		LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		view = vi.inflate(R.layout.monthly_listview_rows, null);
    	} 
    	
    	TextView name = (TextView)view.findViewById(R.id.monthlyNm);
		
		name.setText(items.get(position));
    	
    	return view;
    }
}

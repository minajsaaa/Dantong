package com.goodmorningrainbow.lib;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.goodmorningrainbow.dantongapp.R;
import com.goodmorningrainbow.model.PriceListDT;

public class PriceListArrayAdapter extends ArrayAdapter<PriceListDT> {

private ArrayList<PriceListDT> items;
	
	public PriceListArrayAdapter(Context context, int textViewResourceId, ArrayList<PriceListDT> items) {
		super(context, textViewResourceId, items);
		
		this.items = items;
	}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	View view = convertView;
    	
    	if(view == null) {
    		LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		view = vi.inflate(R.layout.price_listview_rows, null);
    	} 
    	
    	PriceListDT listDT = items.get(position);
    	
    	if(listDT != null) {
    		TextView name = (TextView)view.findViewById(R.id.priceName);
    		
    		name.setText(listDT.getName());
    	}
    	
    	return view;
    }
}

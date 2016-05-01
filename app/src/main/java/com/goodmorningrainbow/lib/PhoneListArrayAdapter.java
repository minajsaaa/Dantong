package com.goodmorningrainbow.lib;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.goodmorningrainbow.dantongapp.R;
import com.goodmorningrainbow.model.PhoneListDT;

public class PhoneListArrayAdapter extends ArrayAdapter<PhoneListDT> {
	
	private ArrayList<PhoneListDT> items;
	
	public PhoneListArrayAdapter(Context context, int textViewResourceId, ArrayList<PhoneListDT> items) {
		super(context, textViewResourceId, items);
		
		this.items = items;
	}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	View view = convertView;
    	
    	if(view == null) {
    		LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		view = vi.inflate(R.layout.phone_listview_rows, null);
    	} 
    	
    	PhoneListDT listDT = items.get(position);
    	
    	if(listDT != null) {
    		TextView nameEng = (TextView)view.findViewById(R.id.pNameEng);
    		TextView nameKor = (TextView)view.findViewById(R.id.pNameKor);
    		
    		nameEng.setText(listDT.getName());
    		nameKor.setText(listDT.getNameKor());
    	}
    	
    	return view;
    }
}

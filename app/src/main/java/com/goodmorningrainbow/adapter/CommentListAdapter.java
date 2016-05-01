package com.goodmorningrainbow.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.goodmorningrainbow.dantongapp.R;
import com.goodmorningrainbow.model.CommentDT;

public class CommentListAdapter extends ArrayAdapter<CommentDT> {
	
	private ArrayList<CommentDT> items;	
	private ViewHolder holder;
	
	public CommentListAdapter(Context context, int resourceId, ArrayList<CommentDT> items) {
		super(context, resourceId, items);
		
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		
		if(view == null) {
			
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.comment_list_item, null);
			
			holder = new ViewHolder();
			
			holder.hNickName = (TextView)view.findViewById(R.id.nickNameTxt);
			holder.hTime = (TextView)view.findViewById(R.id.timeTxt);
			holder.hComment = (TextView)view.findViewById(R.id.commentTxt);
			holder.hRatingBar = (RatingBar)view.findViewById(R.id.ratingBar);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder)view.getTag();
		}
		
		final CommentDT commDt = items.get(position);
		
		if(commDt != null) {
			holder.hNickName.setText(commDt.getNickName());
			holder.hTime.setText(commDt.getTime());
			holder.hComment.setText(commDt.getComment());
			holder.hRatingBar.setRating((float)commDt.getRating());
		}
		
		return view;
	}
	
	static class ViewHolder {
		TextView hNickName;
		TextView hTime;
		TextView hComment;
		RatingBar hRatingBar;		
	}
}

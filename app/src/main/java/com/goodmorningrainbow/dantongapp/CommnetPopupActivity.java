package com.goodmorningrainbow.dantongapp;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class CommnetPopupActivity extends Activity {
	
	private Button okBtn;
	private Button cancelBtn;
	private EditText nickEditor;
	private EditText commEditor;
	private RatingBar ratingBar;
	
	private int rating = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.comment_popup_layout);
		
		nickEditor = (EditText)findViewById(R.id.p_edit_nicknm);
		ratingBar = (RatingBar)findViewById(R.id.p_edit_ratingBar);
		commEditor = (EditText)findViewById(R.id.p_edit_comment);
		okBtn = (Button)findViewById(R.id.p_edit_ok_btn);
		cancelBtn = (Button)findViewById(R.id.p_edit_cancel_btn);
		
//		commEditor.addTextChangedListener(watcher);
				
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String nickNmTemp = nickEditor.getText().toString();
				String commentTemp = commEditor.getText().toString();
				
				String nickNm = nickNmTemp.replace("'", "");
				String comment = commentTemp.replace("'", "");
				
				Log.d("rrobbie", "comment : " + comment );
				
				if(nickNm.length() == 0) {
					Toast.makeText(getApplicationContext(), R.string.no_input_nick, Toast.LENGTH_SHORT).show();
				} else if(comment.length() == 0) {
					Toast.makeText(getApplicationContext(), R.string.no_input_comment, Toast.LENGTH_SHORT).show();
				} else if(rating == 0) {
					Toast.makeText(getApplicationContext(), R.string.no_input_rating, Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent();
					intent.putExtra("nick", nickNm);
					intent.putExtra("comment", comment);
					intent.putExtra("rating", rating);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		
		ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				CommnetPopupActivity.this.rating = (int)rating;
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	TextWatcher watcher = new TextWatcher() {
		String text;
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
            int length = s.toString().length();
            if( length > 0 ){
                Pattern ps = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐]+$");//영문, 숫자, 한글만 허용
                if(!ps.matcher(s).matches()){
//                	commEditor.setText(text);
//                	commEditor.setSelection(commEditor.length());
                }
            }
		}
	};	
}

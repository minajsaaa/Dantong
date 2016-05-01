package com.goodmorningrainbow.dantongapp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.goodmorningrainbow.common.Const;
import com.goodmorningrainbow.model.PriceListDT;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

public class SelectPaySystemPopupActivity extends Activity {
	
	private TextView voiceTv;
	private TextView letterTv;
	private TextView dataTv;
	private TextView basePriceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	setContentView(R.layout.sel_pay_system_popup_layout);
    	
    	voiceTv = (TextView)findViewById(R.id.voiceTxt);
    	letterTv = (TextView)findViewById(R.id.letterTxt);
    	dataTv = (TextView)findViewById(R.id.dataTxt);
    	basePriceTv = (TextView)findViewById(R.id.basePriceTxt);
    	
    	PriceListDT priceDT = Const.PRICE_LIST_ARRAY.get(Const.PRICE_POSITION);
    	
    	if(priceDT.getLetter().equals("999")) {
    		letterTv.setText("무제한");
    	} else {
    		letterTv.setText(priceDT.getLetter() + "건");
    	}
    	
    	//무제한일 경우 구분
    	if(priceDT.getVoice().equals("999")) {
    		voiceTv.setText("무제한");
    	} else {
    		voiceTv.setText(priceDT.getVoice() + "분");
    	}
    	
    	if(priceDT.getData_unit().equals("무제한")) {
    		dataTv.setText("무제한");
    	} else {
    		dataTv.setText(priceDT.getData() + priceDT.getData_unit());
    	}
    	
    	basePriceTv.setText("기본료 " + makeMoneyType(priceDT.getDefcost()) + "원");
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	switch (event.getAction()) {
    	
		case MotionEvent.ACTION_UP:
			setResult(RESULT_OK);
			finish();
			break;		
		}
    	
    	return true;
    }
    
	// 셋째 자리수 마다 콤마를 찍음.
	public static String makeMoneyType(String dblMoneyString) {		

		String format = "#,##0";
		DecimalFormat df = new DecimalFormat(format);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();

		dfs.setGroupingSeparator(',');// 구분자를 ,로
		df.setGroupingSize(3);// 3자리 단위마다 구분자처리 한다.
		df.setDecimalFormatSymbols(dfs);

		return (df.format(Double.parseDouble(dblMoneyString))).toString();
	}
}

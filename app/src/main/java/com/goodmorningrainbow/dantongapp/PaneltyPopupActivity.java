package com.goodmorningrainbow.dantongapp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

import com.goodmorningrainbow.common.Const;

public class PaneltyPopupActivity extends Activity {
	
	private int monthlyPay; //월 청구 금액
	private int monthlyInstallmentPrincipal; //구매 할부 원금
	private int public_cost; //공시 지원금	
	private int support_cost; //추기 지원금
    private int monthly; //개월수
	
	private int remainInstallmentPay; //잔여할부 금액
	private int cancelPay; //해지 예상금액
	private int keepPay; //유지비용 예상금액
    private int publicPaneltyPay; //공시지원금 위약금
    private int supportPaneltyPay; //추가지원금 위약금
	
    private TextView mTextView00; //월 청구금액
    private TextView mTextView01; //잔여할부 금액
    private TextView mTextView02; //공시지원금 위약금
    private TextView mTextView03; //추가지원금 위약금
    private TextView mTextView04; //해지 예상금액
    private TextView mTextView05; //유지비용 예상금액

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	setContentView(R.layout.panelty_popup_layout);
    	
    	//개월수
    	monthly = getIntent().getExtras().getInt("MONTH");
    	//월 청구 금액
    	monthlyPay = getIntent().getExtras().getInt("COST_DATA01");
    	//구매 할부 원금
    	monthlyInstallmentPrincipal = getIntent().getExtras().getInt("COST_DATA02");
    	//공시 지원금
    	public_cost = getIntent().getExtras().getInt("COST_DATA03");
    	//추가 지원금
    	support_cost = getIntent().getExtras().getInt("COST_DATA04");
    	
    	mTextView00 = (TextView)findViewById(R.id.pTextView00);
    	mTextView01 = (TextView)findViewById(R.id.pTextView01);
    	mTextView02 = (TextView)findViewById(R.id.pTextView02);
    	mTextView03= (TextView)findViewById(R.id.pTextView03);
    	mTextView04 = (TextView)findViewById(R.id.pTextView04);
    	mTextView05 = (TextView)findViewById(R.id.pTextView05);
    	
    	calculation();
    	
    	setValue();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	switch (event.getAction()) {
    	
		case MotionEvent.ACTION_UP:
			finish();
			break;		
		}
    	
    	return true;
    }
    
    private void calculation() {
    	//잔여할부 금액
    	remainInstallmentPay = monthlyInstallmentPrincipal - (monthlyInstallmentPrincipal/24 * monthly);
    	//공시지원금 위약금
    	switch (Const.SELECT_AGENCY) {
		case Const.TELECOM_SK:
		case Const.TELECOM_LG:			
			if(monthly <= 6) {
				publicPaneltyPay = public_cost;
			} else {				
				publicPaneltyPay = (int)(public_cost * ((24.0-monthly)/18.0));
			}
				
			break;

		case Const.TELECOM_KT:
			publicPaneltyPay = public_cost - (int)(public_cost/24.0 * monthly);
			break;
		}
    	
    	//추가지원금 위약금
    	supportPaneltyPay = support_cost - (int)(support_cost/24.0 * monthly);
    	
    	//해지 예산금액
    	cancelPay = remainInstallmentPay + publicPaneltyPay + supportPaneltyPay;
    	//유지비용 예상금액
    	//keepPay = publicPaneltyPay + cancelPay;
    	keepPay = (monthlyPay * monthly) + cancelPay;
    }
    
    private void setValue() {
    	mTextView00.setText(makeMoneyType(getFloorHundred(monthlyPay)));
    	mTextView01.setText(makeMoneyType(getFloorHundred(remainInstallmentPay)));
    	mTextView02.setText(makeMoneyType(getFloorHundred(publicPaneltyPay)));
    	mTextView03.setText(makeMoneyType(getFloorHundred(supportPaneltyPay)));
    	mTextView04.setText(makeMoneyType(getFloorHundred(cancelPay)));
    	mTextView05.setText(makeMoneyType(getFloorHundred(keepPay)));
    }
    
    // 셋째 자리수 마다 콤마를 찍음.
	public String makeMoneyType(int dblMoneyString) {
		String moneyString = new Integer(dblMoneyString).toString();

		String format = "#,##0";
		DecimalFormat df = new DecimalFormat(format);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();

		dfs.setGroupingSeparator(',');// 구분자를 ,로
		df.setGroupingSize(3);// 3자리 단위마다 구분자처리 한다.
		df.setDecimalFormatSymbols(dfs);

		return (df.format(Double.parseDouble(moneyString))).toString() + "원";
	}
    
    //백원 단위 절삭
  	public int getFloorHundred(int cost) {
  		double tempCost = cost * 0.01;
  		
  		return (int)(Math.floor(tempCost) * 100); 
  	}
}

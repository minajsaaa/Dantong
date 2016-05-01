package com.goodmorningrainbow.fragment;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Toast;

import com.goodmorningrainbow.common.Const;
import com.goodmorningrainbow.common.IOnHandlerMessage;
import com.goodmorningrainbow.dantongapp.MainActivity;
import com.goodmorningrainbow.dantongapp.R;

public class AdviceDetailFragment extends Fragment 
implements IOnHandlerMessage, OnClickListener, OnCheckedChangeListener, OnGlobalLayoutListener {

	private static volatile AdviceDetailFragment instance = null;
	
	private MainActivity mActivity = null;
	private View mView = null;
	
	private ImageView detailImg;
	private CheckBox checkbox;
	private Button agreeButton;
	private Button smsButton;
	private Button mailButton;
	private Button cancelButton;
	
	private EditText smsTextContext;
	private EditText smsNumber;	
	private EditText name;
	
	private RadioGroup radioGroupTelecom;
	private RadioGroup radioGroupAsk;
	
	private ScrollView scrollView1;
	
	public static AdviceDetailFragment getInstance() {
		if(instance == null) {
			synchronized (AdviceDetailFragment.class) {
				if(instance == null) {
					instance = new AdviceDetailFragment();
				}
			}
		}		
		return instance;
	}
	
	public void setActivity(MainActivity activity) {
		this.mActivity = activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		mView = inflater.inflate(R.layout.fragment_advice_detail_layout, container, false);
		
		scrollView1 = (ScrollView) mView.findViewById(R.id.scrollView1);
		
		radioGroupTelecom = (RadioGroup) mView.findViewById(R.id.radioGroupTelecom);
		radioGroupAsk = (RadioGroup) mView.findViewById(R.id.radioGroupAsk);
		
		detailImg = (ImageView) mView.findViewById(R.id.detailImg);
		checkbox = (CheckBox) mView.findViewById(R.id.checkbox);
		agreeButton = (Button) mView.findViewById(R.id.agreeButton);
		smsButton = (Button) mView.findViewById(R.id.smsButton);
		mailButton = (Button) mView.findViewById(R.id.mailButton);
		cancelButton = (Button) mView.findViewById(R.id.cancelButton);
		
		name = (EditText) mView.findViewById(R.id.name);
		
		smsNumber = (EditText) mView.findViewById(R.id.smsNumber);
		smsTextContext = (EditText) mView.findViewById(R.id.smsTextContext);		
		
		radioGroupTelecom.setOnCheckedChangeListener(this);
		radioGroupAsk.setOnCheckedChangeListener(this);
		
		smsButton.setOnClickListener(this);
		mailButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);						
		agreeButton.setOnClickListener(this);
		
		if( MainLeftFragment.getInstance().phoneDetailImg != null ) {
			detailImg.setImageBitmap(MainLeftFragment.getInstance().phoneDetailImg);			
		}

		final View activityRootView = mView.findViewById(R.id.scrollView1);		
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				Rect r = new Rect();
				ScrollView contentView = (ScrollView) mView.findViewById(R.id.scrollView1);
			    contentView.getWindowVisibleDisplayFrame(r);
			    int screenHeight = contentView.getRootView().getHeight();
			    int keypadHeight = screenHeight - r.bottom;
			    
			    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
			    	Resources resource = getActivity().getResources();
			    	int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, resource.getDisplayMetrics());
			    	contentView.setY(-px);
			    }
			    else {
			    	contentView.setY(0);
			    }

			}
		});

		return mView;
	}
	
	public void setDetailImg(Bitmap img) {
		detailImg.setImageBitmap(img);
	}
	
	public void setProperties() {
		try {			
			checkbox.setChecked(false);
			smsNumber.setText("");	
			smsTextContext.setText("");
			name.setText("");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getMessage() {		
	    String smsNum = smsNumber.getText().toString();
	    String smsText = smsTextContext.getText().toString();
	    String smsAskType = getRadioType( radioGroupAsk.getCheckedRadioButtonId() ); 
	    String smsTelecom = getRadioType( radioGroupTelecom.getCheckedRadioButtonId() );
	    String smsName = name.getText().toString();
	    String temp = smsTelecom + "/" + smsAskType + "/" + smsName + "/" + smsNum + "/" + smsText;
	    return temp;
	}
	
	private void sendSMS(){
	    String smsNum = smsNumber.getText().toString();
	    String smsText = smsTextContext.getText().toString();
	    	 
	    if (smsNum.length()>0 && smsText.length()>0){	    	
	    	sendSMS(smsNum, getMessage());
	    }else{
	        Toast.makeText(getActivity(), "모두 입력해 주세요", Toast.LENGTH_SHORT).show();
	    }
	}	

    private void sendSMS(String phoneNumber, String message){
    	String SENT = "SMS_SENT";
    	String DELIVERED = "SMS_DELIVERED";
     	
    	PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SENT), 0);
    	PendingIntent deliveredPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(DELIVERED), 0);

    	getActivity().registerReceiver(new BroadcastReceiver() {		
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getActivity(), "SMS를 전송하였습니다.", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getActivity(), "Generic failure", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getActivity(), "No service", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getActivity(), "Null PDU", Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getActivity(), "Radio off", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

    	try {
        	SmsManager sms = SmsManager.getDefault();        	
        	sms.sendTextMessage("01045239114", null, message, sentPI, deliveredPI);			
		} catch (Exception e) {
			e.printStackTrace();
		}    	
    }
    
    private String getRadioType(int id) {
    	if( id == R.id.radioAsk0 )	return "번호이동";
    	if( id == R.id.radioAsk1 )	return "신규가입";
    	if( id == R.id.radioAsk2 )	return "기기변경";
    	if( id == R.id.radioTelecom0 )	return "SKT";
    	if( id == R.id.radioTelecom1 )	return "KT";
    	if( id == R.id.radioTelecom2 )	return "LGT";
    	return "알뜰폰";
    }
    
    private void sendMail() {
    	Intent it = new Intent(Intent.ACTION_SEND);
    	String[] mailaddr = {"livedantong@naver.com"};

	    String smsNum = smsNumber.getText().toString();
	    String smsText = smsTextContext.getText().toString();
	    String smsAskType = getRadioType( radioGroupAsk.getCheckedRadioButtonId() ); 
	    String smsTelecom = getRadioType( radioGroupTelecom.getCheckedRadioButtonId() );
	    String smsName = name.getText().toString();	    
	    
	    Log.d("rrobbie", "smsAskType : " + smsAskType + " / " + smsTelecom );
	    
	    String temp = "희망 통신사 : " + smsTelecom + "\n"
	    			+ "문의 유형 : " + smsAskType + "\n"
	    			+ "신청자 : " + smsName + "\n"
	    			+ "연락처 : " + smsNum + "\n"
	    			+ "문의 사항 : " + smsText;
	    
	    if (smsText.length()>0){
	    	it.setType("plaine/text");
	    	it.putExtra(Intent.EXTRA_EMAIL, mailaddr); // 받는사람
	    	it.putExtra(Intent.EXTRA_SUBJECT, "단통법 문의사항"); // 제목
	    	it.putExtra(Intent.EXTRA_TEXT, "\n" + temp); // 첨부내용

	    	startActivity(it);	    	
	    } else{
	        Toast.makeText(getActivity(), "모두 입력해 주세요", Toast.LENGTH_SHORT).show();
	    }
    }       
	
//	===============================================================================================================	
    
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.smsButton:
			
			if( checkbox.isChecked() ) {
				sendSMS();				
			} else {
				Toast.makeText(getActivity(), "개인정보수집 및 활용에 동의해야합니다.", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.mailButton:
			if( checkbox.isChecked() ) {
				sendMail();				
			} else {
				Toast.makeText(getActivity(), "개인정보수집 및 활용에 동의해야합니다.", Toast.LENGTH_SHORT).show();
			}
			break;			

		case R.id.cancelButton:
//			detailImg.setImageBitmap(null);			
			mActivity.moveToPage(Const.PAGE_LEFT);
			break;
			
		case R.id.agreeButton:
			Uri uri2 = Uri.parse("http://dantongmall.com/custom/per.php");
			Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
			startActivity(intent2);
			break;
			
		default:
			break;
		}
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		
	}

}

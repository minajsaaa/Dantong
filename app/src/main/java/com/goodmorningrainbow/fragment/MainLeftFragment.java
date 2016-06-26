package com.goodmorningrainbow.fragment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.goodmorningrainbow.common.Const;
import com.goodmorningrainbow.common.IOnHandlerMessage;
import com.goodmorningrainbow.common.WeakRefHandler;
import com.goodmorningrainbow.dantongapp.ClubAlertPopupActivity;
import com.goodmorningrainbow.dantongapp.MainActivity;
import com.goodmorningrainbow.dantongapp.PaneltyPopupActivity;
import com.goodmorningrainbow.dantongapp.PurchasePopupActivity;
import com.goodmorningrainbow.dantongapp.R;
import com.goodmorningrainbow.dantongapp.SelectManuPopupActivity;
import com.goodmorningrainbow.dantongapp.SelectMonthlyPopupActivity;
import com.goodmorningrainbow.dantongapp.SelectPaySystemPopupActivity;
import com.goodmorningrainbow.dantongapp.SelectPhoneListPopupActivity;
import com.goodmorningrainbow.dantongapp.SelectPriceListPopupActivity;
import com.goodmorningrainbow.dantongapp.ShowAlertPopup;
import com.goodmorningrainbow.model.CommentDT;
import com.goodmorningrainbow.model.CostDT;
import com.goodmorningrainbow.model.PhoneListDT;
import com.goodmorningrainbow.model.PriceListDT;

public class MainLeftFragment extends Fragment implements IOnHandlerMessage{

	private static volatile MainLeftFragment instance = null;
	
	private MainActivity mActivity = null;
	private View mView = null;
	
	private ImageView pImgView; //선택된 폰 이미지
	private Button mSkBtn; //SK버튼
	private Button mKtBtn; //KT버튼
	private Button mLgBtn; //LG버튼
	private Button mSeBtn; //세븐모방일 버튼
	private Button mCjBtn; //CJ버튼
	private Button mUmBtn; //UMobile버튼
	
	private Button nBtn00;
	private Button nBtn01;
	private Button nBtn02;
	private Button nBtn03;
	private Button nBtn04;
	private Button nBtn05;
	
	private EditText editText00;
	private EditText editText01;
	private EditText editText02;
	
	private Button pSelBtn; //휴대폰 선택 버튼
	private Button cSelBtn; //요금제 선택 버튼
	//private Button mSelBtn; //위약금/유지비 버튼
	private Button deviceModeBtn; //단말기 지원 버튼
	private Button contrctModeBtn; //선택 약정
	
	private Button releTxt; //출고가
	private Button pSupportTxt; //공시지원금
	private Button aSupportTxt; //추가지원금
	private TextView cPlanTxt; //요금제
	private TextView cSaveTxt; //요금할인
	private TextView purchTxt; //총 할인후 금액
	private TextView tStanDayTxt; //오늘 기준일
	
	public  ArrayList<PhoneListDT> phoneArray;
	public  ArrayList<PriceListDT> priceArray;
	
	private ProgressDialog pd;
	private CostDT costDT;
	
	private RelativeLayout mTable04; //구매할부원금 layout
	private RelativeLayout mTable05; //요금할인 layout
	private RelativeLayout mTable06; //총할인후 금액 layout
	
	private RelativeLayout mAccessoryLayout;
	private ImageView mAccessoryClose;
	
	int phone_cost = 0; //출고가
	int public_cost = 0; //공시지원금
	int support_cost = 0; //추가지원금
	int h_support_cost = 0; //추가지원금
	int contrct_cost = 0; 
	int mip_cost_addsuport = 0; //할부수수료
	int monthlyInstallmentPrincipal = 0; //구매할부원금
	int monthlyPayMoney = 0;//월 납부 금액
	
	int realCostAddSupport = 0; //실구매가 추가지원금이 포함된 가격
	
	boolean isShowSupportCost = false;
	public boolean isShowEdit = false;
	private boolean isCloseAcc = false;
	
	private int add_scale_cost = 0; //CJ에서 52000원 요금제 이상인 경우 1개월 5,000 24개월 120,000할인 가격
	
	private Bitmap phoneImg = null;
	public Bitmap phoneDetailImg = null;
	private WeakRefHandler handler;
	private String imgUrl = null;
	
	private int monthly = 24; //기준개월수
	private int clubMnt = 0;
	
	private Handler mHandler;
	
	private boolean isShowMonthlyPP; //구매할부 원금 24개월 나누는 기준인지 아닌지
	private boolean isShowMonthlyPrice; //요금할인 24개월 나누는 기준인지 아닌지                
	private boolean isShowMonthlyTotal; //총할인후 금액 24개월 나누는 기준인지 아닌(월 납부 금액)
	
	private boolean isTouchPenalty; //위약금 클릭여부
	
	private boolean isSetPhoneImg;
	
	private LinearLayout ratingLayout;
	private RatingBar ratingBar;
	private Button ratingCnt;
	
	private Animation leftAni;
	private Animation rightAni;
	
	private double[] interestRate = {0.059, 0.0027, 0.059, 0.0027, 0.059, 0.059};
	
	private CountDownTimer timer = null;
	
	private int DEVICE_MODE = 0;
	private int CONTRACT_MODE = 1;
	
	private int showMode; 
	
	public static MainLeftFragment getInstance() {
		if(instance == null) {
			synchronized (MainLeftFragment.class) {
				if(instance == null) {
					instance = new MainLeftFragment();
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
		
		isSetPhoneImg = false;
		
		leftAni  = AnimationUtils.loadAnimation(getActivity(), R.anim.left_in);
		rightAni = AnimationUtils.loadAnimation(getActivity(), R.anim.right_out);
		
		showMode = DEVICE_MODE;
	}
	
	@Override
	public void handleMessage(Message msg) {
		
		if(msg.what == Const.H_LEFT_IMG) {
			if(phoneImg != null) {
				pImgView.setImageBitmap(phoneImg);
				isSetPhoneImg = true;
				
				Bundle bundle = msg.getData();
				
				float ave = bundle.getFloat("ave");
						
				ratingCnt.setText(Integer.toString(bundle.getInt("cnt")));
				ratingBar.setRating(ave);
				ratingLayout.setVisibility(View.VISIBLE);
			}	
		} else if(msg.what == Const.H_RIGHT_IMG) {
			if(phoneDetailImg != null) {
				MainRightFragment.getInstance().setDetailImg(phoneDetailImg);	
			}			
		} else if(msg.what == Const.LEFT_ANI) {
			
		} else if(msg.what == Const.RIGHT_ANI) {
			
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_main_left_layout, container, false);
		
        mHandler = new Handler();
		
		pImgView = (ImageView)mView.findViewById(R.id.phone_img);		
		
		mSkBtn = (Button)mView.findViewById(R.id.skTelBtn);
		mKtBtn = (Button)mView.findViewById(R.id.ktTelBtn);
		mLgBtn = (Button)mView.findViewById(R.id.lgTelBtn);
		mSeBtn = (Button)mView.findViewById(R.id.seTelBtn);
		mCjBtn = (Button)mView.findViewById(R.id.cjTelBtn);
		mUmBtn = (Button)mView.findViewById(R.id.umTelBtn);
		
		nBtn00 = (Button)mView.findViewById(R.id.normalBtn0);
		nBtn01 = (Button)mView.findViewById(R.id.normalBtn1);
		nBtn02 = (Button)mView.findViewById(R.id.normalBtn2);
		nBtn03 = (Button)mView.findViewById(R.id.normalBtn3);
		nBtn04 = (Button)mView.findViewById(R.id.normalBtn4);
		nBtn05 = (Button)mView.findViewById(R.id.normalBtn5);		
		
		editText00 = (EditText)mView.findViewById(R.id.releasePayEditText);
		editText01 = (EditText)mView.findViewById(R.id.publicSupportEditText);
		editText02 = (EditText)mView.findViewById(R.id.addSupportEditText);
		
		pSelBtn = (Button)mView.findViewById(R.id.phone_select_btn);
		cSelBtn = (Button)mView.findViewById(R.id.phone_callplan_btn);
		//mSelBtn = (Button)mView.findViewById(R.id.phone_money_btn);
		deviceModeBtn = (Button)mView.findViewById(R.id.price_mode_01_btn);
		contrctModeBtn = (Button)mView.findViewById(R.id.price_mode_02_btn);
		
		releTxt  = (Button)mView.findViewById(R.id.releasePayTxt);
		pSupportTxt = (Button)mView.findViewById(R.id.publicSupportPayTxt);
		aSupportTxt = (Button)mView.findViewById(R.id.addSupportPayTxt);
		cPlanTxt = (TextView)mView.findViewById(R.id.callingPlanTxt);
		cSaveTxt = (TextView)mView.findViewById(R.id.callingSaveTxt);
		purchTxt = (TextView)mView.findViewById(R.id.purchaseTxt);
		tStanDayTxt = (TextView)mView.findViewById(R.id.todayTxt);
		
		mTable04 = (RelativeLayout)mView.findViewById(R.id.mainTable04);
		mTable05 = (RelativeLayout)mView.findViewById(R.id.mainTable05);
		mTable06 = (RelativeLayout)mView.findViewById(R.id.mainTable06);
		ratingLayout = (LinearLayout)mView.findViewById(R.id.ratingLayout);
		ratingBar = (RatingBar)mView.findViewById(R.id.rating);
		ratingCnt = (Button)mView.findViewById(R.id.rateAveCnt);
	/*
		mAccessoryLayout = (RelativeLayout)mView.findViewById(R.id.accessory_layout);
		mAccessoryClose = (ImageView)mView.findViewById(R.id.accessory_close);
		*/
		init();
		
		return mView;
	}
	
	// 초기화 함수 - 처음 진입시 한번만 실행
	@SuppressLint("SimpleDateFormat")
	private void init() {

		handler = new WeakRefHandler(this);

		pImgView.setImageResource(R.drawable.main_modelview);

		pSelBtn.setEnabled(false);
		cSelBtn.setEnabled(false);

		pd = new ProgressDialog(mActivity);

		isShowSupportCost = false;
		support_cost = 0;
		h_support_cost = 0;

		// 오늘날짜 셋팅
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		tStanDayTxt.setText(Html.fromHtml("<u>기준일 " + sdf.format(now) + "</u>"));

		Const.isClub = false;
		monthly = 24;
		clubMnt = 0;

		detailTxtInit();
		
		deviceModeBtn.setBackgroundResource(R.drawable.main_money_select1);
		contrctModeBtn.setBackgroundResource(R.drawable.main_money_select2);
		deviceModeBtn.setEnabled(false);
		contrctModeBtn.setEnabled(false);
		showMode = DEVICE_MODE;
	}
	
	// 하단 상세정보 초기화
	private void detailTxtInit() {
		
		Log.d("rrobbie", "detailTxtInit");
		
		isSetPhoneImg = false;
		ratingLayout.setVisibility(View.INVISIBLE);

		add_scale_cost = 0;

		isShowSupportCost = false;

		releTxt.setText("");
		pSupportTxt.setText("");
		aSupportTxt.setText("");
		cPlanTxt.setText("");
		cSaveTxt.setText("");
		purchTxt.setText("");

		nBtn00.setEnabled(false);
		nBtn01.setEnabled(false);
		nBtn02.setEnabled(false);
		nBtn03.setEnabled(false);
		nBtn04.setEnabled(false);
		nBtn05.setEnabled(false);

		releTxt.setEnabled(false);
		aSupportTxt.setEnabled(false);
		pSupportTxt.setEnabled(false);

		releTxt.setVisibility(View.VISIBLE);
		aSupportTxt.setVisibility(View.VISIBLE);
		pSupportTxt.setVisibility(View.VISIBLE);

		editText00.setVisibility(View.GONE);
		editText01.setVisibility(View.GONE);
		editText02.setVisibility(View.GONE);

		mTable04.setBackgroundResource(R.drawable.main_table_04);
		mTable05.setBackgroundResource(R.drawable.main_table_05);
		mTable06.setBackgroundResource(R.drawable.main_table_06);

		isShowMonthlyPP = false;
		isShowMonthlyPrice = false;
		isShowMonthlyTotal = false;
		isTouchPenalty = false;

		//mSelBtn.setText("");

		// SK버튼
		mSkBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Const.SELECT_AGENCY = Const.TELECOM_SK;

				mSkBtn.setBackgroundResource(R.drawable.main_menu1_btn01_on);
				mKtBtn.setBackgroundResource(R.drawable.main_menu1_btn03);
				mLgBtn.setBackgroundResource(R.drawable.main_menu1_btn05);
				mSeBtn.setBackgroundResource(R.drawable.main_menu1_btn02);
				mCjBtn.setBackgroundResource(R.drawable.main_menu1_btn04);
				mUmBtn.setBackgroundResource(R.drawable.main_menu1_btn06);

				// 다시 다른 통신사로 선택시 기존의 값들을 초기화 하기
				upperBtnsInit();
			}
		});

		// KT버튼
		mKtBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Const.SELECT_AGENCY = Const.TELECOM_KT;

				mSkBtn.setBackgroundResource(R.drawable.main_menu1_btn01);
				mKtBtn.setBackgroundResource(R.drawable.main_menu1_btn03_on);
				mLgBtn.setBackgroundResource(R.drawable.main_menu1_btn05);
				mSeBtn.setBackgroundResource(R.drawable.main_menu1_btn02);
				mCjBtn.setBackgroundResource(R.drawable.main_menu1_btn04);
				mUmBtn.setBackgroundResource(R.drawable.main_menu1_btn06);

				upperBtnsInit();
			}
		});

		// LG번튼
		mLgBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Const.SELECT_AGENCY = Const.TELECOM_LG;

				mSkBtn.setBackgroundResource(R.drawable.main_menu1_btn01);
				mKtBtn.setBackgroundResource(R.drawable.main_menu1_btn03);
				mLgBtn.setBackgroundResource(R.drawable.main_menu1_btn05_on);
				mSeBtn.setBackgroundResource(R.drawable.main_menu1_btn02);
				mCjBtn.setBackgroundResource(R.drawable.main_menu1_btn04);
				mUmBtn.setBackgroundResource(R.drawable.main_menu1_btn06);

				upperBtnsInit();
			}
		});

		// 세븐모바일 버튼
		mSeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Const.SELECT_AGENCY = Const.TELECOM_SV;

				mSkBtn.setBackgroundResource(R.drawable.main_menu1_btn01);
				mKtBtn.setBackgroundResource(R.drawable.main_menu1_btn03);
				mLgBtn.setBackgroundResource(R.drawable.main_menu1_btn05);
				mSeBtn.setBackgroundResource(R.drawable.main_menu1_btn02_on);
				mCjBtn.setBackgroundResource(R.drawable.main_menu1_btn04);
				mUmBtn.setBackgroundResource(R.drawable.main_menu1_btn06);

				upperBtnsInit();
			}
		});

		// CJ모바일 버튼
		mCjBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Const.SELECT_AGENCY = Const.TELECOM_CJ;

				mSkBtn.setBackgroundResource(R.drawable.main_menu1_btn01);
				mKtBtn.setBackgroundResource(R.drawable.main_menu1_btn03);
				mLgBtn.setBackgroundResource(R.drawable.main_menu1_btn05);
				mSeBtn.setBackgroundResource(R.drawable.main_menu1_btn02);
				mCjBtn.setBackgroundResource(R.drawable.main_menu1_btn04_on);
				mUmBtn.setBackgroundResource(R.drawable.main_menu1_btn06);

				upperBtnsInit();
			}
		});

		// UMobile 버튼
		mUmBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Const.SELECT_AGENCY = Const.TELECOM_UM;

				mSkBtn.setBackgroundResource(R.drawable.main_menu1_btn01);
				mKtBtn.setBackgroundResource(R.drawable.main_menu1_btn03);
				mLgBtn.setBackgroundResource(R.drawable.main_menu1_btn05);
				mSeBtn.setBackgroundResource(R.drawable.main_menu1_btn02);
				mCjBtn.setBackgroundResource(R.drawable.main_menu1_btn04);
				mUmBtn.setBackgroundResource(R.drawable.main_menu1_btn06_on);

				upperBtnsInit();
			}
		});

		// 휴대폰 리스트 버튼
		pSelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showManufPopupView();
			}
		});

		// 요금제 선택 버튼
		cSelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// new GetPriceListDataTask().execute();
				showPriceListPopup();
				Log.e("rrobbie", "요금제 선택");
			}
		});

		// 위약금/유지비
		/*mSelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isTouchPenalty) {
					if (Const.SELECT_AGENCY == Const.TELECOM_SK
							|| Const.SELECT_AGENCY == Const.TELECOM_KT
							|| Const.SELECT_AGENCY == Const.TELECOM_LG) {
						Intent intent = new Intent(mActivity, MonthlyPopupActivity.class);
						mActivity.startActivityForResult(intent, Const.SELECT_MONTHLY_LIST_RESULT_CODE);
					} else {
						Toast.makeText(mActivity, "적용되지 않는 통신사 입니다.", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(mActivity, "통신사 및 요금제를 선택해 주세요.", Toast.LENGTH_SHORT).show();
				}

			}
		});*/

		// 좌측버튼(출고가)
		nBtn00.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				inputReleCost();
			}
		});

		// 좌측버튼(공시지원금)
		nBtn01.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				inputPSupportCost();
			}
		});

		// 좌측버튼(추가지원금)
		nBtn02.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(isShowSupportCost);
				if (isShowSupportCost) {
					support_cost = 0;
					aSupportTxt.setText("0원");
					isShowSupportCost = false;

				} else {
					support_cost = h_support_cost;
					aSupportTxt.setText(makeMoneyType(support_cost));
					isShowSupportCost = true;
				}

				refresh();
			}
		});

		// 좌측버튼(구매할부원금)
		nBtn03.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShowMonthlyPP) {
					isShowMonthlyPP = false;
					mTable04.setBackgroundResource(R.drawable.main_table_04);
					refresh();

				} else {
					isShowMonthlyPP = true;
					mTable04.setBackgroundResource(R.drawable.main_table_04_1);

					refresh();

					Intent intent = new Intent(mActivity, ShowAlertPopup.class);
					intent.putExtra(Const.POPUP_TYPE, 0);
					intent.putExtra(Const.SHOW_COST, cPlanTxt.getText().toString());
					intent.putExtra(Const.SHOW_MONTHLY, monthly);
					startActivity(intent);
				}
			}
		});

		// 좌측버튼(요금할인)
		nBtn04.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShowMonthlyPrice) {
					isShowMonthlyPrice = false;
					mTable05.setBackgroundResource(R.drawable.main_table_05);

					refresh();
				} else {
					isShowMonthlyPrice = true;
					mTable05.setBackgroundResource(R.drawable.main_table_05_1);

					refresh();

					Intent intent = new Intent(mActivity, ShowAlertPopup.class);
					intent.putExtra(Const.POPUP_TYPE, 1);
					intent.putExtra(Const.SHOW_COST, cSaveTxt.getText().toString());
					intent.putExtra(Const.SHOW_MONTHLY, monthly);
					startActivity(intent);
				}
			}
		});

		// 좌측버튼(총할인후 금액)
		nBtn05.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShowMonthlyTotal) {
					isShowMonthlyTotal = false;
					mTable06.setBackgroundResource(R.drawable.main_table_06);

					refresh();
				} else {
					isShowMonthlyTotal = true;
					mTable06.setBackgroundResource(R.drawable.main_table_06_1);

					refresh();

					Intent intent = new Intent(mActivity, ShowAlertPopup.class);
					intent.putExtra(Const.POPUP_TYPE, 2);
					intent.putExtra(Const.SHOW_COST, purchTxt.getText().toString());
					intent.putExtra(Const.SHOW_MONTHLY, monthly);
					startActivity(intent);
				}
			}
		});

		// 출고가 버튼
		releTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				inputReleCost();
			}
		});

		// 공시지원금 버튼
		pSupportTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				inputPSupportCost();
			}
		});

		// 추가지원금 버튼
		aSupportTxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isShowEdit = true;
				aSupportTxt.setVisibility(View.GONE);
				editText02.setVisibility(View.VISIBLE);

				String temp = aSupportTxt.getText().toString().replace("원", "");
				editText02.setText(temp.replace(",", ""));
				editText02.requestFocus();
				editText02.setSelection(editText02.length());

				mHandler.postDelayed(new Runnable() {
					public void run() {
						InputMethodManager mgr = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.showSoftInput(editText02, InputMethodManager.SHOW_FORCED);
					}
				}, 500);
			}
		});

		// 출고가 EditText
		editText00.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					if (editText00.getText().length() == 0) {
						Toast.makeText(mActivity, getString(R.string.no_number), Toast.LENGTH_SHORT).show();
					} else {
						isShowEdit = false;
						releTxt.setVisibility(View.VISIBLE);
						editText00.setVisibility(View.GONE);

						phone_cost = Integer.parseInt(editText00.getText().toString());
						releTxt.setText(makeMoneyType(phone_cost));

						refresh();
					}
				}

				return false;
			}
		});

		// 공시지원금 EditText
		editText01.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {

					if (editText01.getText().length() == 0) {
						Toast.makeText(mActivity, getString(R.string.no_number), Toast.LENGTH_SHORT).show();
					} else {
						isShowEdit = false;
						pSupportTxt.setVisibility(View.VISIBLE);
						editText01.setVisibility(View.GONE);

						public_cost = Integer.parseInt(editText01.getText().toString());
						pSupportTxt.setText(makeMoneyType(public_cost));

						refresh();
					}
				}

				return false;
			}
		});

		// 추가지원금 EditText
		editText02.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {

					if (editText02.getText().length() == 0) {
						Toast.makeText(mActivity, getString(R.string.no_number), Toast.LENGTH_SHORT).show();
					} else {
						isShowEdit = false;
						aSupportTxt.setVisibility(View.VISIBLE);
						editText02.setVisibility(View.GONE);

						isShowSupportCost = true;
						support_cost = h_support_cost = Integer.parseInt(editText02.getText().toString());
						aSupportTxt.setText(makeMoneyType(support_cost));

						refresh();
					}
				}

				return false;
			}
		});
		
		//핸드폰 이미지 클릭시
		pImgView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isSetPhoneImg) {
					mActivity.moveToPage(Const.PAGE_RIGHT);	
				} else {
					Toast.makeText(mActivity, mActivity.getString(R.string.select_phone), Toast.LENGTH_SHORT).show();
				}
			}
		});
		/*
		//악세사리 광고 닫기
		mAccessoryClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(timer != null) {
					timer.cancel();
				}
				closeAccImg();
			}
		});

		//광고 이미지 클릭
		mAccessoryLayout.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(imgUrl != null) {
					mActivity.moveToPage(Const.PAGE_ADVICE);
				}				
			}
		});*/
		
		//단말기 지원 클릭
		deviceModeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(showMode == CONTRACT_MODE) {
					deviceModeBtn.setBackgroundResource(R.drawable.main_money_select1_push);
					contrctModeBtn.setBackgroundResource(R.drawable.main_money_select2);
					showMode = DEVICE_MODE;
					aSupportTxt.setText(makeMoneyType(support_cost));
					refresh();
				}
			}
		});
		
		//선택약정 클릭
		contrctModeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(showMode == DEVICE_MODE && 
					(Const.SELECT_AGENCY == Const.TELECOM_SK || Const.SELECT_AGENCY == Const.TELECOM_KT || Const.SELECT_AGENCY == Const.TELECOM_LG)) {
					deviceModeBtn.setBackgroundResource(R.drawable.main_money_select1);
					contrctModeBtn.setBackgroundResource(R.drawable.main_money_select2_push);
					showMode = CONTRACT_MODE;
					aSupportTxt.setText("0원");
					refresh();
				}
			}
		});
	}
	
	private void closeAccImg() {
		isCloseAcc = true;
		rightAni.setRepeatCount(0);
		rightAni.setRepeatMode(Animation.ABSOLUTE);
		
		rightAni.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				//			mAccessoryLayout.setEnabled(false);
				//			mAccessoryClose.setEnabled(false);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//			mAccessoryLayout.setVisibility(View.GONE);
				//			mAccessoryClose.setVisibility(View.GONE);
			}
		});
		
	//	mAccessoryLayout.startAnimation(rightAni);
	}
	
	// 상단 초기화 및 통신사 선텍시 초기화
	private void upperBtnsInit() {
		pSelBtn.setEnabled(true);
		cSelBtn.setEnabled(false);

		pSelBtn.setText("");
		cSelBtn.setText("");

		Const.PHONE_POSITION = 0;
		Const.PRICE_POSITION = 0;

		Log.d("rrobbie", "upperBtnsInit");
		
		detailTxtInit();
	}
	
	// 하단 6개 버튼 활성화
	private void ableLowsBtns() {
		nBtn00.setEnabled(true);
		nBtn01.setEnabled(true);
		nBtn02.setEnabled(true);
		nBtn03.setEnabled(true);
		nBtn04.setEnabled(true);
		nBtn05.setEnabled(true);

		releTxt.setEnabled(true);
		aSupportTxt.setEnabled(true);
		pSupportTxt.setEnabled(true);
	}
	
	// 셋째 자리수 마다 콤마를 찍음.
	public static String makeMoneyType(int dblMoneyString) {
		String moneyString = new Integer(dblMoneyString).toString();

		String format = "#,##0";
		DecimalFormat df = new DecimalFormat(format);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();

		dfs.setGroupingSeparator(',');// 구분자를 ,로
		df.setGroupingSize(3);// 3자리 단위마다 구분자처리 한다.
		df.setDecimalFormatSymbols(dfs);

		return (df.format(Double.parseDouble(moneyString))).toString() + "원";
	}
	
	// 제조사 팝업 창 뜨우기
	private void showManufPopupView() {
		Intent intent = new Intent(mActivity, SelectManuPopupActivity.class);
		mActivity.startActivityForResult(intent, Const.SELECT_MANUF_RESULT_CODE);
	}
	
	// 가격리스트 팝업 Activity
	private void showPriceListPopup() {
		Intent intent = new Intent(mActivity, SelectPriceListPopupActivity.class);
		mActivity.startActivityForResult(intent, Const.SELECT_PRICE_LIST_RESULT_CODE);
	}

	// 가격리스트 팝업 Activity
	private void showPurchasePopup() {
		Intent intent = new Intent(mActivity, PurchasePopupActivity.class);
		mActivity.startActivityForResult(intent, Const.SELECT_ADVICE_RESULT_CODE);
	}
	//	========================================================================================


	private void inputReleCost() {
		isShowEdit = true;
		releTxt.setVisibility(View.GONE);
		editText00.setVisibility(View.VISIBLE);		
		
		editText00.setText(Integer.toString(phone_cost));
		editText00.requestFocus();
		editText00.setSelection(editText00.length());
		
		mHandler.postDelayed(new Runnable() {
		    public void run() {
		        InputMethodManager mgr = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		        mgr.showSoftInput(editText00, InputMethodManager.SHOW_FORCED);
		    }
		}, 500);
	}
	
	private void inputPSupportCost() {
		isShowEdit = true;
		pSupportTxt.setVisibility(View.GONE);
		editText01.setVisibility(View.VISIBLE);
		
		editText01.setText(Integer.toString(public_cost));
		editText01.requestFocus();
		editText01.setSelection(editText01.length());
		
		mHandler.postDelayed(new Runnable() {
		    public void run() {
		        InputMethodManager mgr = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		        mgr.showSoftInput(editText01, InputMethodManager.SHOW_FORCED);
		    }
		}, 500);
	}
	
	// 백원 단위 절삭
	public static int getFloorHundred(int cost) {
		double tempCost = cost * 0.01;

		return (int) (Math.floor(tempCost) * 100);
	}
	
	// 십원 단위 반올림
	public static int getFloorTen(int cost) {
		double tempCost = cost * 0.1;

		return (int) (Math.round(tempCost) * 10);
	}
	
	private void refresh() {

		isTouchPenalty = true;

		PriceListDT priceDT = priceArray.get(Const.PRICE_POSITION);

		//contrct_cost = Integer.parseInt(priceDT.getContract()) * monthly; // 요금제 할인
		if(monthly == 24) {
			contrct_cost = Integer.parseInt(priceDT.getContract()) * 24;
		} else {
			contrct_cost = Integer.parseInt(priceDT.getContract()) * 30;
		}
		/*if(showMode == DEVICE_MODE) {
			if(monthly == 24) {
				contrct_cost = Integer.parseInt(priceDT.getContract()) * 24;
			} else {
				contrct_cost = Integer.parseInt(priceDT.getContract()) * 30;
			}
		} else {
			contrct_cost = Integer.parseInt(priceDT.getContract()) * monthly;
		}*/

		mip_cost_addsuport = 0; // 할부 이자율 금액((출고가 - 공시지원금 - 추가할인) * 통신사별 이자율

		if (Const.SELECT_AGENCY == Const.TELECOM_SK || Const.SELECT_AGENCY == Const.TELECOM_SV || 
		    Const.SELECT_AGENCY == Const.TELECOM_LG || Const.SELECT_AGENCY == Const.TELECOM_UM) {

			// 월이자율 : 할부이자율/12
			//double monthlyRate = 0.059 / 12.0;
			double monthlyRate = interestRate[Const.SELECT_AGENCY] / 12;

			// 단말기할부원금
			int buyPrincipal = 0;

			/*if (Const.SELECT_AGENCY == Const.TELECOM_SK && (priceDT.getId().equals("89") || priceDT.getId().equals("90"))) { // 클럽T
				buyPrincipal = phone_cost - support_cost;
			} else {
				buyPrincipal = phone_cost - public_cost - support_cost;
			}*/
			
			if(showMode == DEVICE_MODE) {
				buyPrincipal = phone_cost - public_cost - support_cost;	
			} else {
				//buyPrincipal = phone_cost - support_cost;
				buyPrincipal = phone_cost;
			}
			// 할부금분자 : 단말기할부원금*월이자율*((월이자율+1)^할부개월수)
			double numerator = buyPrincipal * monthlyRate * (Math.pow(monthlyRate + 1, monthly));

			// 할부금분모 : ((1+월이자율)^할부개월수)-1
			double denominator = Math.pow(1 + monthlyRate, monthly) - 1;

			mip_cost_addsuport = (int) Math.round(numerator / denominator) * monthly - buyPrincipal;
			mip_cost_addsuport = mip_cost_addsuport / 10 * 10;

		} else {

			//int total = (int) ((phone_cost - public_cost - h_support_cost) * (monthly + 1) * 0.0027);
			int total = 0;
			
			if(showMode == DEVICE_MODE) {
				total = (int) ((phone_cost - public_cost - h_support_cost) * (monthly + 1) * interestRate[Const.SELECT_AGENCY]);
			} else {
				total = (int) ((phone_cost - h_support_cost) * (monthly + 1) * interestRate[Const.SELECT_AGENCY]);
			}

			mip_cost_addsuport = getFloorHundred(total);

			/*
			 * int total02 = (int)((phone_cost - public_cost) * monthly *
			 * 0.0027);
			 * 
			 * mip_cost_addsuport = getFloorHundred(total02);
			 */
		}

		if (Const.VERBOSE) System.out.println("mip_cost_addsuport : " + mip_cost_addsuport);

		if(showMode == DEVICE_MODE) {
			realCostAddSupport = getFloorTen(phone_cost - public_cost - support_cost - contrct_cost + mip_cost_addsuport - add_scale_cost);	
		} else {
			realCostAddSupport = getFloorTen(phone_cost - support_cost + mip_cost_addsuport - add_scale_cost - (contrct_cost + (priceDT.getDisCost() * monthly)));
		}

		/*if (Const.SELECT_AGENCY == Const.TELECOM_SK && (priceDT.getId().equals("89") || priceDT.getId().equals("90"))) { // 클럽T
			pSupportTxt.setText(makeMoneyType(0)); // 공시지원금
		} else {
			pSupportTxt.setText(makeMoneyType(public_cost)); // 공시지원금
		}*/
		
		if(showMode == DEVICE_MODE) {
			pSupportTxt.setText(makeMoneyType(public_cost)); // 공시지원금
		} else {
			pSupportTxt.setText("0");
		}

		int installPurcharse = 0;
		
		if(showMode == DEVICE_MODE) {
			installPurcharse = phone_cost - public_cost - support_cost + mip_cost_addsuport;
		} else {
			installPurcharse = phone_cost + mip_cost_addsuport;
		}

		if (isShowMonthlyPP) {
			/*if (Const.SELECT_AGENCY == Const.TELECOM_SK && (priceDT.getId().equals("89") || priceDT.getId().equals("90"))) { // 클럽T
				cPlanTxt.setText(makeMoneyType(getFloorHundred(((installPurcharse + public_cost) / monthly) - (public_cost / monthly)))); // 구매할부원금
			} else {
				cPlanTxt.setText(makeMoneyType(getFloorHundred(installPurcharse / monthly))); // 구매할부원금
			}*/
			cPlanTxt.setText(makeMoneyType(getFloorTen(installPurcharse / monthly))); // 구매할부원금
		} else {
			/*if (Const.SELECT_AGENCY == Const.TELECOM_SK && (priceDT.getId().equals("89") || priceDT.getId().equals("90"))) { // 클럽T
				monthlyInstallmentPrincipal = installPurcharse + public_cost;
				cPlanTxt.setText(makeMoneyType(monthlyInstallmentPrincipal)); // 구매할부원금
			} else {
				monthlyInstallmentPrincipal = installPurcharse;
				cPlanTxt.setText(makeMoneyType(monthlyInstallmentPrincipal)); // 구매할부원금
			}*/
			monthlyInstallmentPrincipal = installPurcharse;
			cPlanTxt.setText(makeMoneyType(monthlyInstallmentPrincipal)); // 구매할부원금
		}

		int fDiscount = 0;
		
		if(showMode == DEVICE_MODE) {
			fDiscount = contrct_cost + add_scale_cost;
		} else {
			fDiscount = contrct_cost + (priceDT.getDisCost() * 24);
		}
		
		//System.out.println("contrct_cost : " + contrct_cost);

		if (isShowMonthlyPrice) {
			/*if (Const.SELECT_AGENCY == Const.TELECOM_SK && (priceDT.getId().equals("89") || priceDT.getId().equals("90"))) { // 클럽T
				cSaveTxt.setText(makeMoneyType(getFloorHundred((fDiscount / monthly + public_cost / 24)) * -1)); // 요금할인 TextView
			} else {
				if(monthly == 24) {
					cSaveTxt.setText(makeMoneyType(getFloorHundred(fDiscount / 24) * -1)); // 요금할인 TextView	
				} else {
					cSaveTxt.setText(makeMoneyType(getFloorHundred(fDiscount / 30) * -1)); // 요금할인 TextView
				}
			}*/
			if(showMode == DEVICE_MODE) {
				if(monthly == 24) {
					cSaveTxt.setText(makeMoneyType((int)(fDiscount / 24) * -1)); // 요금할인 TextView	
				} else {
					cSaveTxt.setText(makeMoneyType((int)(fDiscount / 30) * -1)); // 요금할인 TextView
				}
			} else {
				cSaveTxt.setText(makeMoneyType((int)(fDiscount / 24) * -1)); // 요금할인 TextView
			}
		} else {
			cSaveTxt.setText(makeMoneyType(fDiscount)); // 요금할인 TextView
		}

		/*if (Const.SELECT_AGENCY == Const.TELECOM_SK && (priceDT.getId().equals("89") || priceDT.getId().equals("90"))) { // 클럽T
			monthlyPayMoney = Integer.parseInt(priceDT.getDefcost()) + Integer.parseInt(priceDT.getDefcost()) / 10 
					+ getFloorHundred(phone_cost - support_cost - contrct_cost + mip_cost_addsuport - add_scale_cost) / monthly - public_cost / 24; 
		} else {
			monthlyPayMoney = Integer.parseInt(priceDT.getDefcost()) + Integer.parseInt(priceDT.getDefcost()) / 10 + realCostAddSupport / monthly;
		}*/
		
		int realTotalCost = 0;
		
		if(showMode == DEVICE_MODE) {
			realTotalCost = getFloorTen(phone_cost - public_cost - support_cost - (Integer.parseInt(priceDT.getContract()) * monthly) + mip_cost_addsuport - add_scale_cost);	
		} else {
			realTotalCost = getFloorTen(phone_cost - support_cost + mip_cost_addsuport - add_scale_cost - ((Integer.parseInt(priceDT.getContract()) * monthly) + (priceDT.getDisCost() * monthly)));
		}
		
		monthlyPayMoney = Integer.parseInt(priceDT.getDefcost()) + Integer.parseInt(priceDT.getDefcost()) / 10 + realTotalCost / monthly;

		if (isShowMonthlyTotal) {
			purchTxt.setText(makeMoneyType(getFloorTen(monthlyPayMoney)));
		} else {
			purchTxt.setText(makeMoneyType((int)(realCostAddSupport)));
		}
	}
	
	// 서버로 부터 리스트 받아오기. AsyncTask 사용
	public void setPhoneListFromServer() {
		
		cSelBtn.setEnabled(false);
		
		Log.d("rrobbie", "setPhoneListFromServer");

		detailTxtInit();

		new GetPhoneListDataTask().execute();
	}
	
	// 핸드폰 리스트
	private class GetPhoneListDataTask extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.setMessage("잠시만 기달려 주세요...");
			pd.setIndeterminate(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Const.PHONE_LIST_URL);
			JSONObject jsonObject = null;

			try {

				if (Const.VERBOSE) System.out.println("agency : " + Const.SELECT_AGENCY + ", menuf : " + Const.SELECT_MANUF);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("agency", Integer.toString(Const.SELECT_AGENCY)));
				nameValuePairs.add(new BasicNameValuePair("menuf", Const.SELECT_MANUF));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);

				HttpEntity httpEntity = response.getEntity();

				String str = EntityUtils.toString(httpEntity);

				jsonObject = new JSONObject(new String(str.getBytes("iso-8859-1"), "euc-kr"));

				if (Const.VERBOSE) System.out.println("str : " + str);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return jsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			try {
				JSONArray jArray = result.getJSONArray("data");

				phoneArray = new ArrayList<PhoneListDT>();

				for (int i = 0; i < jArray.length(); i++) {

					JSONObject c = jArray.getJSONObject(i);

					String id = c.getString("id");
					String name = c.getString("name");
					String name_kor = c.getString("name_kor");
					String cost = c.getString("cost");
					String imgName = c.getString("img_name");
					String netkind = c.getString("netkind");     
					String imgUrl = c.getString("img_url");
					
					int club = 0;
					if (Const.SELECT_AGENCY == Const.TELECOM_SK
							|| Const.SELECT_AGENCY == Const.TELECOM_KT
							|| Const.SELECT_AGENCY == Const.TELECOM_LG) {
						club = c.getInt("club");
					} else {
						club = -1;
					}

					PhoneListDT listDT = new PhoneListDT();
					listDT.setId(id);
					listDT.setCost(cost);
					listDT.setName(name);
					listDT.setNameKor(name_kor);
					listDT.setImgName(imgName);
					listDT.setNetkind(netkind);
					listDT.setClub(club);
					listDT.setRateAve((float)(c.getDouble("rate_ave")));
					listDT.setRateCnt(c.getInt("rate_count"));
					listDT.setImgUrl(imgUrl);

					phoneArray.add(listDT);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Const.PHONE_POSITION = 0;
			//Const.PHONE_LIST_ARRAY = phoneArray;

			showPhoneListPopup();

			pd.dismiss();
		}
	}
	
	// 폰리스트 팝업 Activity
	private void showPhoneListPopup() {
		Intent intent = new Intent(mActivity, SelectPhoneListPopupActivity.class);		
		intent.putExtra("arraylist", phoneArray);
		mActivity.startActivityForResult(intent, Const.SELECT_PHONE_LIST_RESULT_CODE);
	}
	
	public void setResultFromPhonList() {
		PhoneListDT listDT = phoneArray.get(Const.PHONE_POSITION);
		Const.PHONE_ID = listDT.getId();

		pSelBtn.setText(listDT.getNameKor());
		cSelBtn.setText("");
		cSelBtn.setEnabled(true);
		
		clubMnt = 0;
		monthly = 24; //기준개월수
		
		if(listDT.getClub() > 0) {
			Intent intent = new Intent(mActivity, ClubAlertPopupActivity.class);
			intent.putExtra("price", listDT.getClub());
			mActivity.startActivityForResult(intent, Const.CLUB_SELECT_PRICE_RESULT_CODE);
		} else {					
			new GetCostListDataTask().execute();					
		}
	}
	
	// 요금제 리스트(cost_data)
	private class GetCostListDataTask extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.setMessage("잠시만 기달려 주세요...");
			pd.setIndeterminate(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Const.COST_LIST_URL);
			JSONObject jsonObject = null;
			PhoneListDT phoneDT = phoneArray.get(Const.PHONE_POSITION);
			String id = phoneDT.getId();

			if (Const.VERBOSE) System.out.println("ID : " + id);

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("agency", Integer.toString(Const.SELECT_AGENCY)));
				nameValuePairs.add(new BasicNameValuePair("id", id));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);

				HttpEntity httpEntity = response.getEntity();

				String str = EntityUtils.toString(httpEntity);

				if (Const.VERBOSE) System.out.println("str : " + str);

				jsonObject = new JSONObject(new String(str.getBytes("iso-8859-1"), "euc-kr"));

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			costDT = new CostDT();

			try {
				JSONArray jArray = result.getJSONArray("data");

				JSONObject c = jArray.getJSONObject(0);

				costDT.setId(c.getString("id"));
				costDT.setName(c.getString("name"));

				ArrayList<String> arrayList = new ArrayList<String>();

				String format = "00";
				DecimalFormat formatter = new DecimalFormat(format);

				int totalCnt = 1;

				if (Const.SELECT_AGENCY == Const.TELECOM_SK) {
					totalCnt = 71;
				} else if (Const.SELECT_AGENCY == Const.TELECOM_KT) {
					totalCnt = 78;
				} else if (Const.SELECT_AGENCY == Const.TELECOM_LG) {
					totalCnt = 73;
				} else if (Const.SELECT_AGENCY == Const.TELECOM_CJ) {
					totalCnt = 42;
				} else if (Const.SELECT_AGENCY == Const.TELECOM_SV) {
					totalCnt = 37;
				} else {
					totalCnt = 16;
				}
               
				for (int i = 1; i < totalCnt; i++) {
					String test = c.getString("cost" + formatter.format(i));
					arrayList.add(test);
				}
                System.out.println("arrayList : " + arrayList.size());
				costDT.setCostArray(arrayList);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			new GetPriceListDataTask().execute();
		}
	}
	
	// 요금제 리스트(price)
	private class GetPriceListDataTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Const.PRICE_LIST_URL);
			JSONObject jsonObject = null;

			PhoneListDT listDT = phoneArray.get(Const.PHONE_POSITION);

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("agency", Integer.toString(Const.SELECT_AGENCY)));
				nameValuePairs.add(new BasicNameValuePair("netkind", listDT.getNetkind()));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);

				HttpEntity httpEntity = response.getEntity();

				String str = EntityUtils.toString(httpEntity);

				if (Const.VERBOSE) System.out.println("str : " + str);
				
				String temp = new String(str.getBytes("iso-8859-1"), "euc-kr");
				
//				temp = "{'data' : [{'img_name' : '베가 시크릿 노트', 'rating' : 2, 'comment' : '22ㆍ5888fry 6+￦?×‥1ㅣㄱㆍ.32', 'nickname' : 'tns4qztns4', 'input_time' : '2015-07-14 08:19:45'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '가성비에서는 오지지', 'nickname' : 'ㅋ', 'input_time' : '2015-07-11 14:56:23'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '카메라빼고 쓸만함... 카메라어플 따로 찾아서써야됨', 'nickname' : 'ㅇㅇ', 'input_time' : '2015-07-07 20:23:45'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '팬택 ......ㅡ', 'nickname' : 'ㄴㄴ', 'input_time' : '2015-06-04 15:18:29'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '중국에 안팔렸는데요. 파산신청한건데 무슨 ㅋㅋ', 'nickname' : '김곰돌', 'input_time' : '2015-06-02 16:10:23'}, {'img_name' : '베가 시크릿 노트', 'rating' : 1, 'comment' : '이미 베가는 중국쪽으로 팔려가서 싼거임', 'nickname' : '다크호스45', 'input_time' : '2015-05-22 07:39:36'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '가성대비짱 성능도최고', 'nickname' : '짱', 'input_time' : '2015-05-17 20:38:58'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : 'OS 4.4.2 지원합니다', 'nickname' : 'Pigeon', 'input_time' : '2015-05-11 06:37:04'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '베리굿', 'nickname' : '로트', 'input_time' : '2015-04-29 07:49:01'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '굿', 'nickname' : '짱', 'input_time' : '2015-02-21 18:18:11'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '베가 시크릿노트 쓸만한데요~ 사양은 노트3만한데 가격은 왜케 저렴한가여?', 'nickname' : '정직한', 'input_time' : '2015-02-15 23:16:05'}, {'img_name' : '베가 시크릿 노트', 'rating' : 1, 'comment' : 'sk gogo', 'nickname' : '엘지보다Sk', 'input_time' : '2015-02-03 11:43:17'} ]}";
				
				Log.d("rrobbie", "temp : " + temp );
				
				jsonObject = new JSONObject(temp);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			try {
				Log.d("rrobbie", "result : " + result );
				
				JSONArray jArray = result.getJSONArray("data");
				
				Log.d("rrobbie", "jArray : " + jArray );

				priceArray = new ArrayList<PriceListDT>();

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject c = jArray.getJSONObject(i);

					String id = c.getString("id");
					String name = c.getString("name");
					String defcost = c.getString("defcost");
					String fcost = c.getString("fcost");
					String voice = c.getString("voice");
					String letter = c.getString("letter");
					String data = c.getString("data");
					String data_unit = c.getString("data_unit");
					String contract = c.getString("contract");
					int    disCost = c.getInt("discost");

					PriceListDT priceDT = new PriceListDT();
					priceDT.setId(id);
					priceDT.setName(name);
					priceDT.setDefcost(defcost);
					priceDT.setFcost(fcost);
					priceDT.setVoice(voice);
					priceDT.setLetter(letter);
					priceDT.setData(data);
					priceDT.setData_unit(data_unit);
					priceDT.setContract(contract);
					priceDT.setDisCost(disCost);

					priceArray.add(priceDT);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			Const.PRICE_POSITION = 0;
			Const.PRICE_LIST_ARRAY = priceArray;

			PhoneListDT listDT = phoneArray.get(Const.PHONE_POSITION);

			phone_cost = Integer.parseInt(listDT.getCost());
			releTxt.setText(makeMoneyType(phone_cost - clubMnt));
			
			MainRightFragment.getInstance().setPhoneNm(listDT.getNameKor());
			new GetCommnetListDataTask().execute(new String[]{listDT.getNameKor()});

			// 선택된 핸드폰 이미지 가져오기.
			new Thread(new Runnable() {

				@Override
				public void run() {

					PhoneListDT listDT = phoneArray.get(Const.PHONE_POSITION);

					try {
						imgUrl = listDT.getImgUrl();
						URL url = new URL(Const.BASE_URL + "/m_rainbow/mobile/phone_imgs/" + listDT.getImgName() + ".jpg");
						URLConnection conn = url.openConnection();
						conn.connect();
						BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
						phoneImg = BitmapFactory.decodeStream(bis);

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (phoneImg != null) {
						Message msg = handler.obtainMessage();
						
						msg.what = Const.H_LEFT_IMG;
						
						Bundle bundle = new Bundle();
						bundle.putFloat("ave", listDT.getRateAve());
						bundle.putInt("cnt", listDT.getRateCnt());
                        msg.setData(bundle);
						
						handler.sendMessage(msg);
					}
				}
			}).start();
			
			// 선택된 핸드폰 상세이미지(스팩보기) 가져오기.
			new Thread(new Runnable() {

				@Override
				public void run() {

					PhoneListDT listDT = phoneArray.get(Const.PHONE_POSITION);

					try {
						URL url = new URL(Const.BASE_URL + "/m_rainbow/mobile/phone_detail_imgs/" + listDT.getImgName() + ".jpg");
						URLConnection conn = url.openConnection();
						conn.connect();
						BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
						phoneDetailImg = BitmapFactory.decodeStream(bis);

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (phoneDetailImg != null) {
						Message msg = handler.obtainMessage();

						msg.what = Const.H_RIGHT_IMG;

						handler.sendMessage(msg);
					}
				}
			}).start();
		}
	}
	
	// 요금제 리스트(price)
	private class GetCommnetListDataTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Const.COMMENT_LIST_URL);
			JSONObject jsonObject = null;

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("name", new String(params[0].getBytes("euc-kr"), "iso-8859-1")));				

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);

				HttpEntity httpEntity = response.getEntity();

				String str = EntityUtils.toString(httpEntity);				

				String temp = new String(str.getBytes("iso-8859-1"), "euc-kr");
				
//				temp = "{'data' : [{'img_name' : '베가 시크릿 노트', 'rating' : 2, 'comment' : '22ㆍ5888fry 6+￦?×‥1ㅣㄱㆍ.32', 'nickname' : 'tns4qztns4', 'input_time' : '2015-07-14 08:19:45'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '가성비에서는 오지지', 'nickname' : 'ㅋ', 'input_time' : '2015-07-11 14:56:23'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '카메라빼고 쓸만함... 카메라어플 따로 찾아서써야됨', 'nickname' : 'ㅇㅇ', 'input_time' : '2015-07-07 20:23:45'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '팬택 ......ㅡ', 'nickname' : 'ㄴㄴ', 'input_time' : '2015-06-04 15:18:29'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '중국에 안팔렸는데요. 파산신청한건데 무슨 ㅋㅋ', 'nickname' : '김곰돌', 'input_time' : '2015-06-02 16:10:23'}, {'img_name' : '베가 시크릿 노트', 'rating' : 1, 'comment' : '이미 베가는 중국쪽으로 팔려가서 싼거임', 'nickname' : '다크호스45', 'input_time' : '2015-05-22 07:39:36'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '가성대비짱 성능도최고', 'nickname' : '짱', 'input_time' : '2015-05-17 20:38:58'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : 'OS 4.4.2 지원합니다', 'nickname' : 'Pigeon', 'input_time' : '2015-05-11 06:37:04'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '베리굿', 'nickname' : '로트', 'input_time' : '2015-04-29 07:49:01'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '굿', 'nickname' : '짱', 'input_time' : '2015-02-21 18:18:11'}, {'img_name' : '베가 시크릿 노트', 'rating' : 5, 'comment' : '베가 시크릿노트 쓸만한데요~ 사양은 노트3만한데 가격은 왜케 저렴한가여?', 'nickname' : '정직한', 'input_time' : '2015-02-15 23:16:05'}, {'img_name' : '베가 시크릿 노트', 'rating' : 1, 'comment' : 'sk gogo', 'nickname' : '엘지보다Sk', 'input_time' : '2015-02-03 11:43:17'} ]}";
				
				Log.d("rrobbie", "temp : " + temp );
				
				jsonObject = new JSONObject(temp);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			try {
				JSONArray jArray = result.getJSONArray("data");
				
				Log.d("rrobbi", "jArray : " + jArray );
				
				if( jArray != null ) {
					ArrayList<CommentDT> commentArray = new ArrayList<CommentDT>();

					for (int i = 0; i < jArray.length(); i++) {
						JSONObject c = jArray.getJSONObject(i);
						
						CommentDT comDt = new CommentDT();
						
						comDt.setImgName(c.getString("img_name"));
						comDt.setRating(c.getInt("rating"));
						comDt.setNickName(c.getString("nickname"));
						comDt.setComment(c.getString("comment"));
						comDt.setTime(c.getString("input_time"));
						
						commentArray.add(comDt);
					}
					
					MainRightFragment.getInstance().setCommentList(commentArray);					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}			
			
			pd.dismiss();
		}
	}
	
	public void setResultFromClubPriceList(Intent data) {
		int resultPrice = data.getIntExtra("returnPrice", 0);
		
		if(Const.VERBOSE) System.out.println("resultPrice : " + resultPrice);
		
		if(resultPrice > 0) {
			if(Const.SELECT_AGENCY == Const.TELECOM_KT || Const.SELECT_AGENCY == Const.TELECOM_LG) {
				monthly = 18;	
			}
			
			clubMnt = resultPrice;					
		}
		
		new GetCostListDataTask().execute();
	}
	
	public void setResultFromPriceList() {
		Intent intent = new Intent(mActivity, SelectMonthlyPopupActivity.class);
		startActivityForResult(intent, 1003);
	}

	// 요금제 상세보기
	public void showDetailPrice() {
		Intent intent = new Intent(mActivity, SelectPaySystemPopupActivity.class);
		startActivityForResult(intent, 1004);		
	}

	// 공시지원금 가져오기
	public void setPublicCost(int defCost) {
		if (Const.SELECT_AGENCY == Const.TELECOM_SK) {
			if (defCost == 100010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(0));
			else if (defCost == 100000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(1));
			else if (defCost == 94000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(2));
			else if (defCost == 85000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(3));
			else if (defCost == 80010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(4));
			else if (defCost == 80000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(5));
			else if (defCost == 79000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(6));
			else if (defCost == 77000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(7));
			else if (defCost == 75000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(8));
			else if (defCost == 73500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(9));
			else if (defCost == 73000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(10));
			else if (defCost == 72000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(11));
			else if (defCost == 70500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(12));
			else if (defCost == 69000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(13));
			else if (defCost == 68500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(14));
			else if (defCost == 68000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(15));
			else if (defCost == 67500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(16));
			else if (defCost == 65000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(17));
			else if (defCost == 64500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(18));
			else if (defCost == 64000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(19));
			else if (defCost == 62500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(20));
			else if (defCost == 62000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(21));
			else if (defCost == 61500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(22));
			else if (defCost == 61010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(23));
			else if (defCost == 61000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(24));
			else if (defCost == 60500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(25));
			else if (defCost == 59500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(26));
			else if (defCost == 57500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(27));
			else if (defCost == 57000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(28));
			else if (defCost == 56000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(29));
			else if (defCost == 55000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(30));
			else if (defCost == 54500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(31));
			else if (defCost == 54000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(32));
			else if (defCost == 52000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(33));
			else if (defCost == 51010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(34));
			else if (defCost == 51000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(35));
			else if (defCost == 50500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(36));
			else if (defCost == 49500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(37));
			else if (defCost == 49000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(38));
			else if (defCost == 48000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(39));
			else if (defCost == 47010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(40));
			else if (defCost == 45500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(41));
			else if (defCost == 45000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(42));
			else if (defCost == 44500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(43));
			else if (defCost == 44000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(44));
			else if (defCost == 43500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(45));
			else if (defCost == 43000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(46));
			else if (defCost == 42010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(47));
			else if (defCost == 42000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(48));
			else if (defCost == 41000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(49));
			else if (defCost == 39000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(50));
			else if (defCost == 38500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(51));
			else if (defCost == 36500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(52));
			else if (defCost == 36010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(53));
			else if (defCost == 36000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(54));
			else if (defCost == 35000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(55));
			else if (defCost == 34000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(56));
			else if (defCost == 32500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(57));
			else if (defCost == 29910)
				public_cost = Integer.parseInt(costDT.getCostArray().get(58));
			else if (defCost == 29000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(59));
			else if (defCost == 28500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(60));
			else if (defCost == 28000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(61));
			else if (defCost == 24000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(62));
			else if (defCost == 19000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(63));
			else if (defCost == 15000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(64));
			else if (defCost == 14000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(65));
			else if (defCost == 11000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(66));
			else if (defCost == 10000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(67));
			else if (defCost == 9000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(68));
			else if (defCost == 8000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(69));
			else
				public_cost = 0;
		} else if (Const.SELECT_AGENCY == Const.TELECOM_KT) {
			if (defCost == 129000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(0));
			else if (defCost == 100000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(1));
			else if (defCost == 99000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(2));
			else if (defCost == 97000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(3));
			else if (defCost == 94000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(4));
			else if (defCost == 90000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(5));
			else if (defCost == 89000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(6));
			else if (defCost == 87000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(7));
			else if (defCost == 85000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(8));
			else if (defCost == 84500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(9));
			else if (defCost == 84000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(10));
			else if (defCost == 83000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(11));
			else if (defCost == 80000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(12));
			else if (defCost == 79000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(13));
			else if (defCost == 78000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(14));
			else if (defCost == 77000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(15));
			else if (defCost == 76000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(16));
			else if (defCost == 75000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(17));
			else if (defCost == 74000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(18));
			else if (defCost == 73500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(19));
			else if (defCost == 73000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(20));
			else if (defCost == 72000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(21));
			else if (defCost == 71000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(22));
			else if (defCost == 70000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(23));
			else if (defCost == 69000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(24));
			else if (defCost == 68000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(25));
			else if (defCost == 67000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(26));
			else if (defCost == 66000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(27));
			else if (defCost == 65500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(28));
			else if (defCost == 65000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(29));
			else if (defCost == 64500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(30));
			else if (defCost == 64000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(31));
			else if (defCost == 63000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(32));
			else if (defCost == 62000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(33));
			else if (defCost == 60000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(34));
			else if (defCost == 59000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(35));
			else if (defCost == 57000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(36));
			else if (defCost == 56000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(37));
			else if (defCost == 55500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(38));
			else if (defCost == 55000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(39));
			else if (defCost == 54500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(40));
			else if (defCost == 54000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(41));
			else if (defCost == 52000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(42));
			else if (defCost == 51000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(43));
			else if (defCost == 50000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(44));
			else if (defCost == 49000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(45));
			else if (defCost == 48500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(46));
			else if (defCost == 47000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(47));
			else if (defCost == 45000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(48));
			else if (defCost == 44000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(49));
			else if (defCost == 43000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(50));
			else if (defCost == 42500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(51));
			else if (defCost == 42000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(52));
			else if (defCost == 40000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(53));
			else if (defCost == 39000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(54));
			else if (defCost == 36000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(55));
			else if (defCost == 35000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(56));
			else if (defCost == 34000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(57));
			else if (defCost == 33000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(58));
			else if (defCost == 32000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(59));
			else if (defCost == 30000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(60));
			else if (defCost == 29000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(61));
			else if (defCost == 28000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(62));
			else if (defCost == 27500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(63));
			else if (defCost == 27000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(64));
			else if (defCost == 25000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(65));
			else if (defCost == 24000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(66));
			else if (defCost == 22500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(67));
			else if (defCost == 19000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(68));
			else if (defCost == 18000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(69));
			else if (defCost == 15000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(70));
			else if (defCost == 14000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(71));
			else if (defCost == 11000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(72));
			else if (defCost == 10000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(73));
			else if (defCost == 9000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(74));
			else if (defCost == 8800)
				public_cost = Integer.parseInt(costDT.getCostArray().get(75));
			else if (defCost == 8000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(76));
			else
				public_cost = 0;
		} else if (Const.SELECT_AGENCY == Const.TELECOM_LG) {
			if (defCost == 124000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(0));
			else if (defCost == 120000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(1));
			else if (defCost == 100010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(2));
			else if (defCost == 100000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(3));
			else if (defCost == 99000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(4));
			else if (defCost == 89900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(5));
			else if (defCost == 89000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(6));
			else if (defCost == 85000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(7));
			else if (defCost == 80010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(8));
			else if (defCost == 80000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(9));
			else if (defCost == 79000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(10));
			else if (defCost == 77000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(11));
			else if (defCost == 72000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(12));
			else if (defCost == 70000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(13));
			else if (defCost == 69500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(14));
			else if (defCost == 69010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(15));
			else if (defCost == 69000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(16));
			else if (defCost == 68000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(17));
			else if (defCost == 67000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(18));
			else if (defCost == 64000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(19));
			else if (defCost == 62000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(20));
			else if (defCost == 61500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(21));
			else if (defCost == 60900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(22));
			else if (defCost == 60000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(23));
			else if (defCost == 59010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(24));
			else if (defCost == 59000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(25));
			else if (defCost == 58500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(26));
			else if (defCost == 56500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(27));
			else if (defCost == 56000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(28));
			else if (defCost == 55010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(29));
			else if (defCost == 55000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(30));
			else if (defCost == 54500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(31));
			else if (defCost == 54000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(32));
			else if (defCost == 52500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(33));
			else if (defCost == 52000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(34));
			else if (defCost == 50900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(35));
			else if (defCost == 50500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(36));
			else if (defCost == 50010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(37));
			else if (defCost == 50000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(38));
			else if (defCost == 49500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(39));
			else if (defCost == 49000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(40));
			else if (defCost == 47000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(41));
			else if (defCost == 46900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(42));
			else if (defCost == 45500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(43));
			else if (defCost == 45000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(44));
			else if (defCost == 44500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(45));
			else if (defCost == 44010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(46));
			else if (defCost == 44000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(47));
			else if (defCost == 42500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(48));
			else if (defCost == 42000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(49));
			else if (defCost == 41900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(50));
			else if (defCost == 40000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(51));
			else if (defCost == 39000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(52));
			else if (defCost == 38010)
				public_cost = Integer.parseInt(costDT.getCostArray().get(53));
			else if (defCost == 38000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(54));
			else if (defCost == 37000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(55));
			else if (defCost == 35900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(56));
			else if (defCost == 35500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(57));
			else if (defCost == 35000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(58));
			else if (defCost == 34000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(59));
			else if (defCost == 31000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(60));
			else if (defCost == 29900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(61));
			else if (defCost == 27500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(62));
			else if (defCost == 24000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(63));
			else if (defCost == 19000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(64));
			else if (defCost == 15000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(65));
			else if (defCost == 14500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(66));
			else if (defCost == 13500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(67));
			else if (defCost == 12000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(68));
			else if (defCost == 10900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(69));
			else if (defCost == 9000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(70));
			else if (defCost == 8000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(71));
			else
				public_cost = 0;
		} else if (Const.SELECT_AGENCY == Const.TELECOM_CJ) {
            /*System.out.println("defCost : " + defCost);
			if (defCost >= 52000)
				add_scale_cost = 120000;*/

			if (defCost == 128000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(0));
			else if (defCost == 125000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(1));
			else if (defCost == 103000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(2));
			else if (defCost == 100000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(3));
			else if (defCost == 88000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(4));
			else if (defCost == 87000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(5));
			else if (defCost == 85000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(6));
			else if (defCost == 82000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(7));
			else if (defCost == 77000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(8));
			else if (defCost == 75000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(9));
			else if (defCost == 72000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(10));
			else if (defCost == 67000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(11));
			else if (defCost == 65000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(12));
			else if (defCost == 63000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(13));
			else if (defCost == 62000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(14));
			else if (defCost == 60000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(15));
			else if (defCost == 57000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(16));
			else if (defCost == 55000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(17));
			else if (defCost == 53000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(18));
			else if (defCost == 52000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(19));
			else if (defCost == 50000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(20));
			else if (defCost == 45000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(21));
			else if (defCost == 43000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(22));
			else if (defCost == 42000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(23));
			else if (defCost == 40000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(24));
			else if (defCost == 37000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(25));
			else if (defCost == 35000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(26));
			else if (defCost == 34000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(27));
			else if (defCost == 29000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(28));
			else if (defCost == 28000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(29));
			else if (defCost == 26000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(30));
			else if (defCost == 25000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(31));
			else if (defCost == 24000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(32));
			else if (defCost == 19000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(33));
			else if (defCost == 18000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(34));
			else if (defCost == 17900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(35));
			else if (defCost == 15000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(36));
			else if (defCost == 14000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(37));
			else if (defCost == 9900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(38));
			else if (defCost == 9000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(39));
			else if (defCost == 7000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(40));
			else
				public_cost = 0;
		} else if (Const.SELECT_AGENCY == Const.TELECOM_SV) {
			if (defCost == 100000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(0));
			else if (defCost == 97000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(1));
			else if (defCost == 85000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(2));
			else if (defCost == 82000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(3));
			else if (defCost == 75000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(4));
			else if (defCost == 69000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(5));
			else if (defCost == 65000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(6));
			else if (defCost == 59000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(7));
			else if (defCost == 55000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(8));
			else if (defCost == 49000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(9));
			else if (defCost == 45000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(10));
			else if (defCost == 39000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(11));
			else if (defCost == 35000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(12));
			else if (defCost == 34000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(13));
			else if (defCost == 32000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(14));
			else if (defCost == 29900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(15));
			else if (defCost == 29000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(16));
			else if (defCost == 28000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(17));
			else if (defCost == 25900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(18));
			else if (defCost == 24000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(19));
			else if (defCost == 23000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(20));
			else if (defCost == 22500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(21));
			else if (defCost == 19900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(22));
			else if (defCost == 19000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(23));
			else if (defCost == 18900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(24));
			else if (defCost == 16000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(25));
			else if (defCost == 15000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(26));
			else if (defCost == 14900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(27));
			else if (defCost == 13000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(28));
			else if (defCost == 10000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(29));
			else if (defCost == 9000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(30));
			else if (defCost == 8900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(31));
			else if (defCost == 8000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(32));
			else if (defCost == 6000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(33));
			else if (defCost == 5000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(34));
			else if (defCost == 4000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(35));
			else
				public_cost = 0;
		} else if (Const.SELECT_AGENCY == Const.TELECOM_UM) {			
			if (defCost == 70000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(0));
			else if (defCost == 60000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(1));
			else if (defCost == 52000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(2));
			else if (defCost == 50000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(3));
			else if (defCost == 43500)
				public_cost = Integer.parseInt(costDT.getCostArray().get(4));
			else if (defCost == 42000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(5));
			else if (defCost == 40000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(6));
			else if (defCost == 34000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(7));
			else if (defCost == 32000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(8));
			else if (defCost == 30000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(9));
			else if (defCost == 28900)
				public_cost = Integer.parseInt(costDT.getCostArray().get(10));
			else if (defCost == 28000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(11));
			else if (defCost == 24000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(12));
			else if (defCost == 19000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(13));
			else if (defCost == 9000)
				public_cost = Integer.parseInt(costDT.getCostArray().get(14));
			else
				public_cost = 0;
		}
	}
	
	public void setResultFromMonthlyList(Intent data) {
		int resultPos = data.getIntExtra("POS", 0);
		
		//mSelBtn.setText((resultPos+1)+ "개월");
		
		Intent intent = new Intent(mActivity, PaneltyPopupActivity.class);
		intent.putExtra("MONTH", resultPos+1);
		intent.putExtra("COST_DATA01", monthlyPayMoney);
		intent.putExtra("COST_DATA02", monthlyInstallmentPrincipal);
		intent.putExtra("COST_DATA03", public_cost);
		intent.putExtra("COST_DATA04", support_cost);
		startActivity(intent);
	}
	
	public void closeEditor() {
		releTxt.setVisibility(View.VISIBLE);
		aSupportTxt.setVisibility(View.VISIBLE);
		pSupportTxt.setVisibility(View.VISIBLE);
		
		editText00.setVisibility(View.GONE);
		editText01.setVisibility(View.GONE);
		editText02.setVisibility(View.GONE);
		
		isShowEdit = false;
	}

	private void updatePayment(Intent data) {
		deviceModeBtn.setBackgroundResource(R.drawable.main_money_select1_push);
		contrctModeBtn.setBackgroundResource(R.drawable.main_money_select2);
		deviceModeBtn.setEnabled(true);
		contrctModeBtn.setEnabled(true);
		showMode = DEVICE_MODE;

		monthly = data.getIntExtra("month", 24);

		PriceListDT priceDT = priceArray.get(Const.PRICE_POSITION);
		PhoneListDT phoneDT = phoneArray.get(Const.PHONE_POSITION);

		cSelBtn.setText(priceDT.getName()); //버튼에 요금제 TEXT삽입

		phone_cost = Integer.parseInt(phoneDT.getCost()) - clubMnt; //출고가
		public_cost = 0; //공시지원금

		aSupportTxt.setText("0원");

		int defCost = Integer.parseInt(Const.PRICE_LIST_ARRAY.get(Const.PRICE_POSITION).getFcost());
		setPublicCost(defCost);

		h_support_cost = (int)(public_cost * 0.15); //추가 지원금
		refresh();
		showDetailPrice();
		ableLowsBtns();
	}

	private void updateAccessory() {
		isCloseAcc = true;
		leftAni.setRepeatCount(0);
		leftAni.setRepeatMode(Animation.ABSOLUTE);
		leftAni.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				//			mAccessoryLayout.setVisibility(View.VISIBLE);
				//			mAccessoryClose.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//			mAccessoryLayout.setEnabled(true);
				//			mAccessoryClose.setEnabled(true);
			}
		});

	//	mAccessoryLayout.startAnimation(leftAni);

		timer = new CountDownTimer(8000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				if(isCloseAcc) {
					isCloseAcc = false;
					closeAccImg();
				}
			}
		}.start();

		showPurchasePopup();
	}

	private void updateAdvice(Intent data) {
		int value = data.getIntExtra("select", 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK) {
			if(requestCode == 1003) {
				updatePayment(data);
			} else if(requestCode == 1004) {
				updateAccessory();
			} else if( requestCode == 1005 ) {
				updateAdvice(data);
			}
		}
	}
}

package com.goodmorningrainbow.fragment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.goodmorningrainbow.adapter.CommentListAdapter;
import com.goodmorningrainbow.common.Const;
import com.goodmorningrainbow.common.IOnHandlerMessage;
import com.goodmorningrainbow.common.WeakRefHandler;
import com.goodmorningrainbow.dantongapp.CommnetPopupActivity;
import com.goodmorningrainbow.dantongapp.MainActivity;
import com.goodmorningrainbow.dantongapp.R;
import com.goodmorningrainbow.dantongapp.SelectManuPopupActivity;
import com.goodmorningrainbow.dantongapp.SelectPhoneListPopupActivity;
import com.goodmorningrainbow.model.CommentDT;
import com.goodmorningrainbow.model.PhoneListDT;

public class MainRightFragment extends Fragment implements IOnHandlerMessage{

	private static volatile MainRightFragment instance = null;
	
	private MainActivity mActivity = null;
	private View mView = null;
	private String phoneNm = null;
	
	private ImageView detailImg;
	private Button backBtn;
	private Button ratingBtn;
	private Button specBtn;
	private ListView listView;
	private ProgressDialog pd;
	private ImageView compareImg;
	private Button closeUnder;
	private LinearLayout compareLayout;
	
	private CommentListAdapter adapter;	
	
	private ArrayList<PhoneListDT> phoneArray;
	
	private WeakRefHandler handler;
	private Bitmap phoneImg = null;
	
	private Animation upAni;
	private Animation downAni;
	
	public static MainRightFragment getInstance() {
		if(instance == null) {
			synchronized (MainRightFragment.class) {
				if(instance == null) {
					instance = new MainRightFragment();
				}
			}
		}
		
		return instance;
	}
	
	public void setActivity(MainActivity activity) {
		this.mActivity = activity;
	}
	
	@Override
	public void handleMessage(Message msg) {
		if(phoneImg != null) {
			
			compareImg.setImageBitmap(phoneImg);
			
			upAni.setRepeatCount(0);
			upAni.setRepeatMode(Animation.ABSOLUTE);
			upAni.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				    compareLayout.setVisibility(View.VISIBLE);	
				    closeUnder.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					ratingBtn.setEnabled(false);
					specBtn.setEnabled(false);
				}
			});
			
			compareLayout.startAnimation(upAni);
		}		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		pd = new ProgressDialog(getActivity());
		
		handler = new WeakRefHandler(this);
		
		upAni = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_in);
		downAni = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_out);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_main_right_layout, container, false);
		
		detailImg = (ImageView)mView.findViewById(R.id.detailImg);
		backBtn = (Button)mView.findViewById(R.id.rightBackBtn);
		ratingBtn = (Button)mView.findViewById(R.id.ratingBtn);
		specBtn = (Button)mView.findViewById(R.id.compareBtn);
		listView = (ListView)mView.findViewById(R.id.listView);		
		compareImg = (ImageView)mView.findViewById(R.id.compareImg);
		closeUnder = (Button)mView.findViewById(R.id.close_under);
		compareLayout = (LinearLayout)mView.findViewById(R.id.comapreLayout);
		
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mActivity.moveToPage(Const.PAGE_LEFT);
				compareLayout.clearAnimation();
			}
		});
		
		ratingBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CommnetPopupActivity.class);
				startActivityForResult(intent, Const.COMMENT_POPUP_RESULT);
			}
		});
		
		specBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SelectManuPopupActivity.class);
				startActivityForResult(intent, Const.SELECT_MANUF_RESULT_CODE);
			}
		});
		
		closeUnder.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				compareLayout.startAnimation(downAni);
				
				downAni.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {					    
					    ratingBtn.setEnabled(true);
						specBtn.setEnabled(true);
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						compareLayout.setVisibility(View.GONE);
					}
				});
			}
		});
		
		return mView;
	}
	
	public void setDetailImg(Bitmap img) {
		detailImg.setImageBitmap(img);
	}
	
	public void setCommentList(ArrayList<CommentDT> arrayList) {
		if(arrayList != null) {
			adapter = new CommentListAdapter(getActivity(), R.layout.comment_list_item, arrayList);
			listView.setAdapter(adapter);
			
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(pd != null) {
			pd.dismiss();
		}
		if(resultCode == Activity.RESULT_OK) {
			if(requestCode == Const.COMMENT_POPUP_RESULT) {
				String nick = data.getStringExtra("nick");
				String comment = data.getStringExtra("comment");
				int rating = data.getIntExtra("rating", 0);
				
				new UpdateCommentDataTask().execute(new String[]{phoneNm, Integer.toString(rating), nick, comment});
			} else if(requestCode == Const.SELECT_MANUF_RESULT_CODE) {
				new GetPhoneListDataTask().execute(new String[]{data.getStringExtra("manuf")});
			} else if(requestCode == Const.SELECT_PHONE_LIST_RESULT_CODE) {
				final int comparePosition = data.getIntExtra("position", 0);				
								
				// 선택된 핸드폰 이미지 가져오기.
				new Thread(new Runnable() {

					@Override
					public void run() {

						PhoneListDT listDT = phoneArray.get(comparePosition);

						try {
							URL url = new URL(Const.BASE_URL + "/m_rainbow/mobile/phone_detail_imgs/" + listDT.getImgName() + ".jpg");
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
							
							handler.sendMessage(msg);
						}
					}
				}).start();
			}
		}
	}
	
	public void setPhoneNm(String phoneNm) {
		this.phoneNm = phoneNm;
	}
	
	// 핸드폰 리스트
	private class UpdateCommentDataTask extends AsyncTask<String, Void, JSONObject> {
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
			HttpPost httppost = new HttpPost(Const.COMMENT_UPDATE_URL);
			JSONObject jsonObject = null;

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("phone", new String(params[0].getBytes("euc-kr"), "iso-8859-1")));
				nameValuePairs.add(new BasicNameValuePair("rating", new String(params[1].getBytes("euc-kr"), "iso-8859-1")));
				nameValuePairs.add(new BasicNameValuePair("nick", new String(params[2].getBytes("euc-kr"), "iso-8859-1")));
				nameValuePairs.add(new BasicNameValuePair("comment", new String(params[3].getBytes("euc-kr"), "iso-8859-1")));
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
				boolean isResult = result.getBoolean("result");
				
                if(isResult) {
                	JSONArray jArray = result.getJSONArray("data");

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
    				
    				adapter = new CommentListAdapter(getActivity(), R.layout.comment_list_item, commentArray);
    				listView.setAdapter(adapter);
    				
    				adapter.notifyDataSetChanged();
    				
    				Toast.makeText(getActivity(), R.string.result_ok, Toast.LENGTH_SHORT).show();
    				
                } else {
                	String message = result.getString("message");
                	
                	Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pd.dismiss();
		}
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

				if (Const.VERBOSE) System.out.println("agency : " + Const.SELECT_AGENCY + ", menuf : " + params[0]);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("agency", Integer.toString(Const.SELECT_AGENCY)));
				nameValuePairs.add(new BasicNameValuePair("menuf", params[0]));
				
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
					listDT.setRateAve((float) (c.getDouble("rate_ave")));
					listDT.setRateCnt(c.getInt("rate_count"));

					phoneArray.add(listDT);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Intent intent = new Intent(getActivity(), SelectPhoneListPopupActivity.class);
			intent.putExtra("arraylist", phoneArray);
			
			startActivityForResult(intent, Const.SELECT_PHONE_LIST_RESULT_CODE);
		}
	}
}

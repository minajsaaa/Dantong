package com.goodmorningrainbow.dantongapp;

import java.io.IOException;
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.goodmorningrainbow.common.Const;

public class TitleActivity extends Activity {
	
	private Handler mHandler;
	private Runnable mRunnable;
	
	private ProgressDialog pd;
	private Activity  activity;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.activity_title);
    	
    	pd = new ProgressDialog(this);
    	activity = this;
    	
        mRunnable = new Runnable() {			
			@Override
			public void run() {                
				new GetVersionDataTask().execute();
			}
		};	
		
		mHandler = new Handler();
		mHandler.postDelayed(mRunnable, 2000);
	}

    @Override
    protected void onDestroy() {
    	mHandler.removeCallbacks(mRunnable);
    	super.onDestroy();
    }
    
    @Override
    public void onBackPressed() {
    }
    // 버전 확인 및 강업등.. 진행
    private class GetVersionDataTask extends AsyncTask<Void, Void, JSONObject> {
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
    	protected JSONObject doInBackground(Void... params) {
    		
    		HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Const.PHONE_UPDATE_URL);
			JSONObject jsonObject = null;
			
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("agency", Integer.toString(Const.SELECT_AGENCY)));
				nameValuePairs.add(new BasicNameValuePair("menuf", Const.SELECT_MANUF));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				HttpResponse response = httpclient.execute(httppost);
				
				HttpEntity httpEntity = response.getEntity();
				
				String str = EntityUtils.toString(httpEntity);
				
				if(Const.VERBOSE)System.out.println("str : " + str);
				
				jsonObject = new JSONObject(new String(str.getBytes("iso-8859-1"),"euc-kr"));
				
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
    			PackageManager pm = activity.getPackageManager();
        		PackageInfo info = pm.getPackageInfo(activity.getPackageName(), 0);
        		String versionName = info.versionName;
        		int versionCode = info.versionCode;
        		
				String serverName = result.getString("verName");
				int serverCode = result.getInt("verCode");
				
				if(serverCode > versionCode) {
					AlertDialog.Builder alert = new AlertDialog.Builder(activity);
					alert.setMessage("업데이트가 있습니다.  확인을 누르면 업데이트를 진행합니다").setCancelable(false).setPositiveButton("확인", 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Intent intent = new Intent(Intent.ACTION_VIEW);
									intent.setData(Uri.parse("market://details?id=com.goodmorningrainbow.dantongapp"));
									activity.startActivity(intent);
									finish();
								}
							}).setNegativeButton("취소", 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Intent intent = new Intent(activity, MainActivity.class);
				                    startActivity(intent);
				                    finish();
								}
							});
					
					AlertDialog alertDialog = alert.create();
					alertDialog.show();
				} else {
					Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                    finish();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
    		
    		pd.dismiss();
    	}
    }
}

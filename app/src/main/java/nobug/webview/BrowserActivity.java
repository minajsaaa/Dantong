package nobug.webview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.goodmorningrainbow.constant.UrlDefinition;
import com.goodmorningrainbow.dantongapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import nobug.helper.RequestHandler;

public class BrowserActivity extends AppCompatActivity implements IBrowserClientEvent {

    public static final String UPLOAD_URL = UrlDefinition.FILE_UPLOAD;
    public static final String UPLOAD_KEY = "image";
    public static final String TELEPHONE = "telephone";

    private int PICK_IMAGE_REQUEST = 1;

    private Uri filePath;
    private Bitmap bitmap;

    private Browser browser;

    //  =========================================================================================

    @Override
    public void onBackPressed() {
        historyBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: {
                historyBack();
                return false;
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    //  =========================================================================================


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        browser = (Browser) findViewById(R.id.browser);
        browser.addJavascriptInterface(new IWebView(), "android");
        browser.setEventListener(this);

        if( getIntent() != null ) {
            loadUrl(getIntent().getStringExtra("url"));
        }
    }

    //  ========================================================================================

    @Override
    public void onReceivedTitle(WebView webview, String title) {
        getSupportActionBar().setTitle(title);
    }

    //  ========================================================================================

    private void historyBack() {
        if (browser.canGoBack()) {
            browser.goBack();
            return;
        }

        super.onBackPressed();
    }

    private void loadUrl(String url) {
        if (url != null) {
            browser.loadUrl(url);
        }
    }

    //  ========================================================================================

    public class IWebView {

        @JavascriptInterface
        public void upload(String url) {
            showFileChooser();  //or uploadImage();
        }

    }

    //  ========================================================================================

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(BrowserActivity.this, "업로드중입니다", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                try {
                    Bitmap temp = params[0];
                    Bitmap bitmap1 = resizeBitmapImage(temp, 1024);
                    String uploadImage = getStringImage(bitmap1);

                    Log.e("rrobbie", bitmap1.getWidth() + " / " + bitmap1.getHeight());

                    HashMap<String,String> data = new HashMap<>();
                    data.put(UPLOAD_KEY, uploadImage);
                    data.put(TELEPHONE, getPhoneNumber());

                    String result = rh.sendPostRequest(UPLOAD_URL,data);

                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    private String getPhoneNumber() {
        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String telephone = mTelephonyManager.getLine1Number();
        return telephone;
    }

    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution) {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width > height)
        {
            if(maxResolution < width)
            {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        }
        else
        {
            if(maxResolution < height)
            {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    //  =========================================================================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                Log.e("rrobbie", "onActivityResult : " + bitmap );

                if( bitmap != null ) {
                    uploadImage();
                }

                Toast.makeText(this, "업로드 되었습니다", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

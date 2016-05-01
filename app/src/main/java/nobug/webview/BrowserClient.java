package nobug.webview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserClient extends WebViewClient {

    protected final String IMAGE = "image";
    protected final String M3U8 = ".m3u8";

    public static String MPEG = "application/vnd.apple.mpegurl";
    public static String HTTP = "http://";
    public static String HTTPS = "https://";
    public static String INTENT = "intent:";
    public static String TEL = "tel:";
    public static String RTSP = "rtsp://";

    protected Context mContext;

    //  ========================================================================================

    public BrowserClient(Context context) {
        mContext = context;
    }

    //  ========================================================================================

    protected void loadIntent(String url) {
        try {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            if (mContext.getPackageManager().resolveActivity(intent, 0) == null) {
                Uri uri = Uri.parse(String.format("market://details?id=%s", intent.getPackage()));
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                mContext.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadRtsp(String url) {
        try {
            mContext.startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse(url)) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadTel(String url) {
        try {
            mContext.startActivity(new Intent(Intent.ACTION_DEFAULT, Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadMymeType(String url, String mimeType) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), mimeType);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadM3u8(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), MPEG);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  =======================================================================================

    private void showHide(WebView view, int start, int end) {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(view, View.ALPHA, start, end);
        alphaAnimation.start();
    }

    //  =======================================================================================

    @Override
    public void onPageFinished(WebView view, final String url) {
        super.onPageFinished(view, url);
        showHide(view, 0, 1);
    }

    @Override
    public void onPageStarted(WebView view, final String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        showHide(view, 1, 0);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.toLowerCase().startsWith(INTENT)) {
            loadIntent(url);
        } else if (url.toLowerCase().indexOf(RTSP) != -1) {
            loadRtsp(url);
        } else if (url.toLowerCase().startsWith(TEL)) {
            loadTel(url);
        } else if (url.toLowerCase().startsWith(HTTP) || url.toLowerCase().startsWith(HTTPS)) {
            if (url.toLowerCase().indexOf(M3U8) != -1) {
                loadM3u8(url);
                return true;
            }

            try {
                String fileExtension = MimeTypeMap.getSingleton().getFileExtensionFromUrl(url);
                if (fileExtension != null) {
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                    if (mimeType != null && mimeType.indexOf(IMAGE) != -1) {
                        loadMymeType(url, mimeType);
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            view.loadUrl(url);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                mContext.startActivity(intent);
            } catch (Exception e) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }
        return true;
    }

}
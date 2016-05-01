package nobug.webview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

import com.goodmorningrainbow.dantongapp.R;

import java.io.File;

public class Browser extends WebView {

    private Context mContext;
    private String callbackKey = "android";

    private IBrowserClientEvent browserClientEvent;

    //  ========================================================================================

    public Browser(Context context) {
        super(context);
        mContext = context;
    }

    public Browser(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public Browser(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    //  ========================================================================================

    @Override
    public void loadUrl(String url) {
        try {
            Uri uri = Uri.parse(url);
            if(uri.getScheme() == null) {
                url = Protocol.HTTP + url;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.loadUrl(url);
    }

    //  =================================================================================================================

    protected void initialize() {
        setProperties(this, null);
        disableWebviewZoomControls();
    }

    protected void setProperties(Browser view, WebChromeClient webClient) {
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setPluginState(WebSettings.PluginState.ON);
        view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setUseWideViewPort(true);
        view.getSettings().setLoadWithOverviewMode(true);
        view.getSettings().setLoadsImagesAutomatically(true);
        view.getSettings().setBuiltInZoomControls(false);
        view.getSettings().setSupportZoom(false);
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        view.getSettings().setSupportMultipleWindows(false);
        view.getSettings().setUserAgentString(getSettings().getUserAgentString() + " ");
        view.getSettings().setGeolocationEnabled(true);
        view.getSettings().setAllowFileAccess(true);
        view.getSettings().setDatabaseEnabled(true);

        WebChromeClient client = (webClient == null) ? webChromeClient : webClient;
        view.setWebChromeClient(client);

        view.setWebViewClient(new BrowserClient(mContext));

        view.addJavascriptInterface(new Bridge(mContext), callbackKey);
        view.setDownloadListener(downloadListener);
    }

    @Override
    protected void onFinishInflate() {
        initialize();
        super.onFinishInflate();
    }

    //  =======================================================================================

    public void setEventListener(IBrowserClientEvent browserClientEvent) {
        this.browserClientEvent = browserClientEvent;
    }

    //  =======================================================================================

    private void downwload(String url) {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            try {
                Uri uri = Uri.parse(url);
                String fileName = uri.getLastPathSegment();

                DownloadManager.Request request = new DownloadManager.Request(uri);

                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                file.mkdirs();

                request.setTitle(mContext.getString(R.string.browser_title));
                request.setDescription(fileName);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                request.setVisibleInDownloadsUi(true);
                DownloadManager manager = (DownloadManager)getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(getContext(), mContext.getString(R.string.browser_download), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//  =================================================================================================================

    private void disableWebviewZoomControls() {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getSettings().setDisplayZoomControls(false);
        } else {
            try {
                ZoomButtonsController zoomButtonsController;
                zoomButtonsController = ((ZoomButtonsController)getClass().getMethod("getZoom", null).invoke(this, null));
                zoomButtonsController.getContainer().setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//  ================================================================================================

    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse(url), mimetype);
                getContext().startActivity(i);
            } catch (Exception ex) {
                downwload(url);
                ex.printStackTrace();
            }
        }
    };

//  ====================================================================================================================================

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, final int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            Dialog dialog = new AlertDialog.Builder(getContext())
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setCancelable(false)
                    .create();

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            });
            dialog.show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            Dialog dialog = new AlertDialog.Builder(getContext())
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    })
                    .setCancelable(false)
                    .create();

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            });
            dialog.show();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            browserClientEvent.onReceivedTitle(view, title);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public void onCloseWindow(WebView w) {
            super.onCloseWindow(w);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
            final String myOrigin = origin;
            final Callback myCallback = callback;

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setMessage( R.string.permissions )
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myCallback.invoke(myOrigin, true, true);
                        }
                    })
                    .setNegativeButton(R.string.rejection, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myCallback.invoke(myOrigin, false, false);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    };

    //  ==========================================================================================

}

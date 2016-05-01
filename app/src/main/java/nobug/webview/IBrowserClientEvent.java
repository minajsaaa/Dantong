package nobug.webview;

import android.webkit.WebView;

public interface IBrowserClientEvent {

    public void onReceivedTitle(WebView webview, String title);

}

package nobug.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class Bridge {
    Context mContext;

    public Bridge(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void sample(String url) {
        //  TODO

    }

}

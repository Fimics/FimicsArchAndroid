package com.peace.hybrid;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author 傅令杰
 */
public interface IWebViewInitializer {

    WebView createWebView();

    WebViewClient createWebViewClient();

    WebChromeClient createWebChromeClient();
}

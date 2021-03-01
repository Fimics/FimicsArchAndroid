package com.peace.hybrid.creator.impl

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.NonNull

open class WebViewClientImpl(
    @NonNull private val urlListener: IUrlListener? = null,
    @NonNull private var pageLoadListener: IPageLoadListener? = null
) : WebViewClient() {

    override fun onPageStarted(
        view: WebView?,
        url: String?, favicon: Bitmap?
    ) {
        super.onPageStarted(view, url, favicon)
        pageLoadListener?.onLoadStart(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        pageLoadListener?.onLoadFinished()
        super.onPageFinished(view, url)
    }

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        if (urlListener != null) {
            return urlListener.handle(view, request)
        }
        return false
    }
}
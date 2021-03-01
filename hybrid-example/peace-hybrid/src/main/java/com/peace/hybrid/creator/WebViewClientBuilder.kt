package com.peace.hybrid.creator

import androidx.annotation.NonNull
import com.peace.hybrid.creator.impl.IPageLoadListener
import com.peace.hybrid.creator.impl.IUrlListener
import com.peace.hybrid.creator.impl.WebViewClientImpl

class WebViewClientBuilder constructor(
    @NonNull private var urlListener: IUrlListener? = null,
    @NonNull private var pageLoadListener: IPageLoadListener? = null,
) {

    fun urlListener(listener: IUrlListener): WebViewClientBuilder {
        this.urlListener = listener
        return this
    }

    fun pageLoadListener(listener: IPageLoadListener): WebViewClientBuilder {
        this.pageLoadListener = listener
        return this
    }

    fun build(): WebViewClientImpl {
        return WebViewClientImpl(urlListener, pageLoadListener)
    }
}
package com.peace.hybrid.creator.impl

import android.webkit.WebResourceRequest
import android.webkit.WebView

interface IUrlListener {

    fun handle(view: WebView?, request: WebResourceRequest?): Boolean
}
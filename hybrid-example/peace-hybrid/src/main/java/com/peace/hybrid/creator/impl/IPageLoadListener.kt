package com.peace.hybrid.creator.impl

import android.graphics.Bitmap
import android.webkit.WebView
import androidx.annotation.Nullable

interface IPageLoadListener {

    fun onLoadStart(
        @Nullable view: WebView?,
        @Nullable url: String?,
        @Nullable favicon: Bitmap?
    )

    fun onLoadFinished()
}
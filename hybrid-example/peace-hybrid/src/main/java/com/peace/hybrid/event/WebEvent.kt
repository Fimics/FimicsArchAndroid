package com.peace.hybrid.event

import android.content.Context
import android.webkit.WebView
import com.peace.core.event.IEvent
import com.peace.hybrid.IWebContainer

/**
 * @author 傅令杰
 */
abstract class WebEvent : IEvent<String?> {

    private lateinit var mContainer: IWebContainer

    val context: Context?
        get() = mContainer.containerContext

    val webView: WebView
        get() = mContainer.webView

    val preLoadUrl: String?
        get() = mContainer.preLoadUrl

    val loadedUrl: String?
        get() = mContainer.loadedUrl

    val webContainer: IWebContainer
        get() = mContainer

    fun setWebContainer(container: IWebContainer) {
        this.mContainer = container
    }
}
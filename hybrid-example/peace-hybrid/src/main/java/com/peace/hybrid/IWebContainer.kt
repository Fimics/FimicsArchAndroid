package com.peace.hybrid

import android.content.Context
import android.webkit.WebView
import com.peace.hybrid.route.IRouter

/**
 * @author 傅令杰
 */
interface IWebContainer {

    /**
     * 当前使用的WebView
     */
    val webView: WebView

    /**
     * 从上一个Container传过来的Url或第一个页面的Url
     */
    val preLoadUrl: String?

    /**
     * 当前WebView已经加载完毕的Url
     */
    val loadedUrl: String?

    /**
     * 当前容器的Context（Activity或Fragment或View）
     */
    val containerContext: Context?

    val router: IRouter
}
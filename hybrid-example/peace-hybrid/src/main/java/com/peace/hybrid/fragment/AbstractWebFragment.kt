package com.peace.hybrid.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.peace.hybrid.IWebContainer
import com.peace.hybrid.route.IRouter
import com.peace.hybrid.route.RouteKeys
import com.peace.hybrid.route.Router
import com.peace.hybrid.IWebViewInitializer

/**
 * @author 傅令杰
 */
abstract class AbstractWebFragment : Fragment(), IWebContainer {

    private lateinit var mWebView: WebView
    private lateinit var mPreLoadUrl: String
    private var mIsWebViewAvailable = false


    override val webView: WebView
        get() = mWebView

    override val loadedUrl: String?
        get() = webView.url

    override val preLoadUrl: String?
        get() = mPreLoadUrl

    override val router: IRouter
        get() = Router(this)

    override val containerContext: Context?
        get() = context

    abstract fun setInitializer(): IWebViewInitializer

    private fun initWebView() {
        val initializer = setInitializer()
        mWebView = initializer.createWebView()
        mWebView.webViewClient = initializer.createWebViewClient()
        mWebView.webChromeClient = initializer.createWebChromeClient()
        mIsWebViewAvailable = true
        preLoadUrl?.let { mWebView.loadUrl(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPreLoadUrl =
            arguments?.getString(RouteKeys.URL.name) ?: throw NullPointerException("Null url")
        initWebView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mWebView
    }

    override fun onResume() {
        super.onResume()
        mWebView.onResume()
    }

    override fun onPause() {
        mWebView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        mIsWebViewAvailable = false
        mWebView.stopLoading()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mWebView.removeAllViews()
        mWebView.destroy()
        super.onDestroy()
    }
}
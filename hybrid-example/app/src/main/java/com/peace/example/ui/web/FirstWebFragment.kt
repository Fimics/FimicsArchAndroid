package com.peace.example.ui.web

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.peace.example.R
import com.peace.hybrid.IWebViewInitializer
import com.peace.hybrid.creator.WebViewCreator
import com.peace.hybrid.creator.impl.IPageLoadListener
import com.peace.hybrid.creator.impl.IUrlListener
import com.peace.hybrid.creator.WebChromeClientCreator
import com.peace.hybrid.creator.WebViewClientCreator
import com.peace.hybrid.fragment.AbstractWebFragment

/**
 * @author 傅令杰
 */
class FirstWebFragment : AbstractWebFragment(),
    IWebViewInitializer,
    IUrlListener,
    IPageLoadListener {

    private lateinit var navContainer: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navContainer = Navigation.findNavController(view)
    }

    override fun setInitializer(): IWebViewInitializer {
        return this
    }

    override fun createWebView(): WebView {
        return WebViewCreator(this).createDefault()
    }

    override fun createWebViewClient(): WebViewClient {
        return WebViewClientCreator
            .builder()
            .urlListener(this)
            .pageLoadListener(this)
            .build()
    }

    override fun createWebChromeClient(): WebChromeClient {
        return WebChromeClientCreator().createDefault()
    }

    override fun onLoadStart(view: WebView?, url: String?, favicon: Bitmap?) {
        Toast.makeText(context, "开始加载", Toast.LENGTH_SHORT).show()
    }

    override fun onLoadFinished() {
        Toast.makeText(context, "加载结束", Toast.LENGTH_SHORT).show()
    }

    override fun handle(view: WebView?, request: WebResourceRequest?): Boolean {
        val bundle = FirstWebFragmentArgs
            .Builder()
            .setURL(request?.url.toString())
            .build()
            .toBundle()
        navContainer
            .navigate(R.id.action_web_fragment_to_web_fragment, bundle)
        return true
    }
}
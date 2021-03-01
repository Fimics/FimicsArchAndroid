package com.peace.hybrid.route

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.peace.hybrid.IWebContainer

/**
 * @author 傅令杰
 */
class Router constructor(private val current: IWebContainer) : IRouter {

    override fun nextContainer(next: IWebContainer): Boolean {
        //如果是电话协议
        val perLoadUrl = next.preLoadUrl
        if (perLoadUrl != null) {
            if (perLoadUrl.contains("tel:")) {
                callPhone(current.containerContext, perLoadUrl)
                return true
            }
        }
        //do something
        return true
    }

    /**
     * 直接加载网页
     */
    override fun load(url: String): Boolean {
        //如果是电话协议
        if (url.contains("tel:")) {
            callPhone(current.containerContext, url)
            return true
        }
        val webView = current.webView
        webView.post {
            webView.loadUrl(url)
            //do something
        }
        return true
    }

    private fun callPhone(context: Context?, uri: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse(uri)
        intent.data = data
        context?.let { ContextCompat.startActivity(it, intent, null) }
    }
}
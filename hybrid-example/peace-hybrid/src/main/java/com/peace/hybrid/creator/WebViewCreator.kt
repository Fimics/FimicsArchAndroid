package com.peace.hybrid.creator

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import com.peace.hybrid.IWebContainer
import com.peace.hybrid.event.WebInterface
import com.peace.hybrid.global.HybridConfigurator
import kotlin.NullPointerException

/**
 * @author 傅令杰
 */
class WebViewCreator(private val container: IWebContainer) : IWebCreator<WebView> {

    override fun createDefault(): WebView {
        val context = container.containerContext
        if (context !== null) {
            val webView = WebView(context)
            WebView.setWebContentsDebuggingEnabled(true)
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(webView, true)
            //不能横向滚动
            webView.isHorizontalScrollBarEnabled = false
            //不能纵向滚动
            webView.isVerticalScrollBarEnabled = false
            //允许截图
//        webView.isDrawingCacheEnabled = true
            //屏蔽长按事件
            webView.setOnLongClickListener { true }
            //初始化WebSettings
            val settings = webView.settings
            @SuppressLint("SetJavaScriptEnabled")
            settings.javaScriptEnabled = true
            val ua = settings.userAgentString
            val agents = HybridConfigurator.instance.userAgent
            settings.userAgentString = ua + agents
            //解决HTTPS网站图片不显示问题
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            //隐藏缩放控件
            settings.builtInZoomControls = false
            settings.displayZoomControls = false
            //禁止缩放
            settings.setSupportZoom(false)
            //禁止保存密码
//        settings.savePassword = false
//        settings.saveFormData = false
            //强制全屏
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            //文件权限
            settings.allowFileAccess = true
//        settings.allowFileAccessFromFileURLs = true
//        settings.allowUniversalAccessFromFileURLs = true
            settings.allowContentAccess = true
            //缓存相关
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            //屏蔽任意命令执行漏洞
            webView.removeJavascriptInterface("searchBoxJavaBridge_")
            webView.removeJavascriptInterface("accessibility")
            webView.removeJavascriptInterface("accessibilityTraversal")
            //设置桥接接口
            val interfaceName = HybridConfigurator.instance.javascriptInterface
            webView.addJavascriptInterface(WebInterface(container), interfaceName)
            return webView
        }
        throw NullPointerException("Context is NULL")
    }
}
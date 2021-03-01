package com.peace.hybrid.global

import android.util.Log
import android.webkit.JavascriptInterface
import com.peace.core.util.addData
import com.peace.core.util.getData
import com.peace.hybrid.event.EventManager
import com.peace.hybrid.event.WebEvent

class HybridConfigurator private constructor() {

    companion object {
        val instance: HybridConfigurator
            get() = Holder.instance
    }

    private object Holder {
        val instance = HybridConfigurator()
    }

    fun withUserAgent(userAgent: String): HybridConfigurator {
        addData(HybridKey.KEY_USER_AGENT, userAgent)
        return this
    }

    fun withJavascriptInterface(javascriptInterface: String): HybridConfigurator {
        addData(HybridKey.KEY_JAVASCRIPT_INTERFACE, javascriptInterface)
        return this
    }

    fun withWebEvent(action: String, webEvent: WebEvent): HybridConfigurator {
        val manager = EventManager.instance
        manager.addEvent(action, webEvent)
        return this
    }

    val userAgent: String
        get() = getData(HybridKey.KEY_USER_AGENT)

    val javascriptInterface: String
        get() = getData(HybridKey.KEY_JAVASCRIPT_INTERFACE)

    fun configure() {
        Log.d("HybridConfigurator", "配置完成")
    }
}
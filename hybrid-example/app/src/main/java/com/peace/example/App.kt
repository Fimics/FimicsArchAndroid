package com.peace.example

import android.app.Application
import com.peace.example.event.ToastEvent
import com.peace.hybrid.global.HybridConfigurator

/**
 * @author 傅令杰
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        HybridConfigurator
            .instance
            .withJavascriptInterface("peace")
            .withUserAgent("peace agent")
            .withWebEvent("showToast", ToastEvent())
            .configure()
    }
}
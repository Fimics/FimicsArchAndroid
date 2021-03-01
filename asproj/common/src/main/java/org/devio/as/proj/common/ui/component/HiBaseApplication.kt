package org.devio.`as`.proj.common.ui.component

import android.app.Application
import com.google.gson.Gson
import org.devio.`as`.proj.common.flutter.HiFlutterCacheManager
import org.devio.hi.config.HiConfig
import org.devio.hi.library.log.HiConsolePrinter
import org.devio.hi.library.log.HiFilePrinter
import org.devio.hi.library.log.HiLogConfig
import org.devio.hi.library.log.HiLogConfig.JsonParser
import org.devio.hi.library.log.HiLogManager
import org.devio.hi.library.util.ActivityManager

open class HiBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLog()
        initConfig()
        HiFlutterCacheManager.instance?.preLoad(this)
//        HiFlutterCacheManager.instance?.preLoadDartVM(this)

    }

    private fun initConfig() {
        HiConfig.instance.init(object : org.devio.hi.config.core.JsonParser {
            override fun <T> fromJson(json: String, clazz: Class<T>): T? {
                return Gson().fromJson(json, clazz)
            }
        }, this)
    }

    private fun initLog() {
        HiLogManager.init(object : HiLogConfig() {
            override fun injectJsonParser(): JsonParser {
                return JsonParser { src: Any? -> Gson().toJson(src) }
            }

            override fun includeThread(): Boolean {
                return true
            }
        }, HiConsolePrinter(), HiFilePrinter.getInstance(cacheDir.absolutePath, 0))
        ActivityManager.instance.init(this)
    }
}
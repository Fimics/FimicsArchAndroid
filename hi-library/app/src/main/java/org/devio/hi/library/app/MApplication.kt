package org.devio.hi.library.app

import android.app.Application
import com.google.gson.Gson
import org.devio.hi.library.log.HiConsolePrinter
import org.devio.hi.library.log.HiFilePrinter
import org.devio.hi.library.log.HiLogConfig
import org.devio.hi.library.log.HiLogConfig.JsonParser
import org.devio.hi.library.log.HiLogManager

class MApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HiLogManager.init(
            object : HiLogConfig() {
                override fun injectJsonParser(): JsonParser? {
                    return JsonParser { src -> Gson().toJson(src) }
                }

                override fun getGlobalTag(): String {
                    return "MApplication"
                }

                override fun enable(): Boolean {
                    return true
                }

                override fun includeThread(): Boolean {
                    return true
                }

                override fun stackTraceDepth(): Int {
                    return 5
                }
            },
            HiConsolePrinter(),
            HiFilePrinter.getInstance(applicationContext.cacheDir.absolutePath, 0)
        )
    }
}
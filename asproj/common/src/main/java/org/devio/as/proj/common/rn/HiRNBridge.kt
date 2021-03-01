package org.devio.`as`.proj.common.rn

import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.react.bridge.*
import org.devio.`as`.proj.common.core.IHiBridge
import org.devio.`as`.proj.common.info.HiLocalConfig

class HiRNBridge(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext),
    IHiBridge<ReadableMap, Promise> {
    override fun getName(): String {
        return "HiRNBridge"
    }

    @ReactMethod
    override fun onBack(p: ReadableMap?) {
        currentActivity?.run {
            runOnUiThread {
                onBackPressed()
            }
        }
    }
    @ReactMethod
    override fun goToNative(p: ReadableMap) {
        ARouter.getInstance().build("/detail/main").withString(
            "goodsId",
            p.getString("goodsId")
        ).navigation()

    }
    @ReactMethod
    override fun getHeaderParams(callback: Promise) {
        val params: WritableMap = Arguments.createMap();
        params.putString("boarding-pass", HiLocalConfig.instance!!.boardingPass())
        params.putString("auth-token", HiLocalConfig.instance!!.authToken())
        callback.resolve(params)
    }
}
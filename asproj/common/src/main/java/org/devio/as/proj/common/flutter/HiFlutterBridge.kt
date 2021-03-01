package org.devio.`as`.proj.common.flutter

import com.alibaba.android.arouter.launcher.ARouter
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import org.devio.`as`.proj.common.core.IHiBridge
import org.devio.`as`.proj.common.info.HiLocalConfig
import org.devio.hi.library.util.ActivityManager

/**
 * Flutter和Native通信插件
 */
class HiFlutterBridge : MethodCallHandler,
    IHiBridge<Any?, MethodChannel.Result?> {
    private var methodChannels = mutableListOf<MethodChannel>()

    companion object {
        @JvmStatic
        var instance: HiFlutterBridge? = null
            private set

        @JvmStatic
        fun init(flutterEngine: FlutterEngine): HiFlutterBridge? {
            val methodChannel = MethodChannel(
                flutterEngine.dartExecutor,
                "HiFlutterBridge"
            )
            if (instance == null) {
                HiFlutterBridge().also { instance = it }
            }
            methodChannel.setMethodCallHandler(instance)
            //因多FlutterEngine后每个FlutterEngine需要单独注册一个MethodChannel，所以用集合将所有的MethodChannel保存起来
            instance!!.apply {
                methodChannels.add(methodChannel)
            }
            return instance
        }
    }

    fun fire(method: String, arguments: Any?) {
        methodChannels.forEach {
            it.invokeMethod(method, arguments)
        }
    }

    fun fire(method: String, arguments: Any?, callback: MethodChannel.Result?) {
        methodChannels.forEach {
            it.invokeMethod(method, arguments, callback)
        }
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) { //处理来自Dart的方法调用
        when (call.method) {
            "onBack" -> onBack(call.arguments)
            "getHeaderParams" -> getHeaderParams(result)
            "goToNative" -> goToNative(call.arguments)
            else -> result.notImplemented()
        }
    }

    override fun onBack(p: Any?) {
        if (ActivityManager.instance.getTopActivity(true) is HiFlutterActivity) {
            (ActivityManager.instance.getTopActivity(true) as HiFlutterActivity).onBackPressed()
        }
    }

    override fun goToNative(p: Any?) {
        if (p is Map<*, *>) {
            val action = p["action"]
            if (action == "goToDetail") {
                val goodsId = p["goodsId"]
                ARouter.getInstance().build("/detail/main").withString(
                    "goodsId",
                    goodsId as String?
                ).navigation()
            } else if (action == "goToLogin") {
                ARouter.getInstance().build("/account/login").navigation()
            }
        }
    }

    override fun getHeaderParams(callback: MethodChannel.Result?) {
        // boarding-pass 与 auth-token传递
        callback!!.success(
            mapOf(
                "boarding-pass" to HiLocalConfig.instance!!.boardingPass(),
                "auth-token" to HiLocalConfig.instance!!.authToken()
            )
        )
    }
}
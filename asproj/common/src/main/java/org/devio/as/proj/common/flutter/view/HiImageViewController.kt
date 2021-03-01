package org.devio.`as`.proj.common.flutter.view

import android.content.Context
import android.view.View
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView

class HiImageViewController(context: Context, messenger: BinaryMessenger, id: Int?, args: Any?) :
    PlatformView, MethodChannel.MethodCallHandler {
    private val imageView: HiImageView = HiImageView(context)
    private val methodChannel: MethodChannel

    init {
        methodChannel = MethodChannel(messenger, "HiImageView_$id")
        methodChannel.setMethodCallHandler(this)
        if (args is Map<*, *>) {
            imageView.setUrl(args["url"] as String)
        }
        print(args)
    }

    override fun getView(): View {
        return imageView
    }

    override fun dispose() {

    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "setUrl" -> {
                val url = call.argument<String>("url")
                if (url != null) {
                    imageView.setUrl(url)
                    result.success("setUrl success")
                } else {
                    result.error("-1", "url cannot be null", null)
                }
            }
            else -> result.notImplemented()
        }
    }
}
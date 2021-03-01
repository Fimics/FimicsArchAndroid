package org.devio.`as`.proj.common.rn.view

import androidx.annotation.Nullable
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp


class HiRNImageViewManager() :
    SimpleViewManager<HiRNImageView>() {
    override fun createViewInstance(reactContext: ThemedReactContext): HiRNImageView {
        return HiRNImageView(context = reactContext)
    }

    override fun getName(): String {
        return "HiRNImageView"
    }

    @ReactProp(name = "src")
    fun setSrc(view: HiRNImageView, @Nullable sources: String?) {
        sources?.let {
            view.setUrl(it)
        }
    }

    /**
     * 要把事件名topChange映射到 JavaScript 端的onChange回调属性上
     * ，需要在你的ViewManager中覆盖getExportedCustomBubblingEventTypeConstants方法，并在其中进行注册：
     */
    override fun getExportedCustomBubblingEventTypeConstants(): MutableMap<String, Any> {
        //将Native 端的事件名：onNativeClick 映射给JS端的：onJSClick
        return MapBuilder.builder<String, Any>()
            .put(
                "onNativeClick",
                MapBuilder.of(
                    "phasedRegistrationNames",//固定值
                    MapBuilder.of("bubbled", "onJSClick")
                )//bubbled 固定值
            )
            .build();
    }

}
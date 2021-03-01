package org.devio.`as`.proj.common.rn

import com.facebook.react.ReactRootView
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule

/**
 * 向RN发送事件
 * */

fun ReactRootView.fireEvent(eventName: String, params: WritableMap?) {
    reactInstanceManager?.currentReactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
        ?.emit(eventName, params)
}
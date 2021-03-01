package org.devio.`as`.proj.common.rn.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.RCTEventEmitter


class HiRNImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    fun setUrl(url: String) {
        Glide.with(this)
            .load(url)
            .into(this)
        setOnClickListener {
            fireEvent("onClick")
        }
    }

    /**
     * 向JS层传递Native UI的事件通知
     */
    fun fireEvent(message: String) {
        val event: WritableMap = Arguments.createMap()
        event.putString("message", message)
        val reactContext = context as ReactContext
        reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(
            id,
            "onNativeClick",
            event
        )
    }
}
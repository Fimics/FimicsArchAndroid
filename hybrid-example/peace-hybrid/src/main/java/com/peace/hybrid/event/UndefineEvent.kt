package com.peace.hybrid.event

import android.util.Log


/**
 * @author 傅令杰
 * 用来捕获未知的事件
 */
class UndefineEvent : WebEvent() {
    override fun execute(params: String?): String? {
        Log.e("UndefineEvent", params.toString())
        return null
    }
}

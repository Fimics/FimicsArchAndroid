package com.peace.hybrid.event

import android.util.Log
import android.webkit.JavascriptInterface
import com.peace.hybrid.IWebContainer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * @author 傅令杰
 */
class WebInterface constructor(private val container: IWebContainer) {

    @JavascriptInterface
    fun event(params: String?): String? {
        Log.d("WebInterface", params.toString())
        if (!params.isNullOrBlank()) {
            //直接获取值
//            val element = Json.parseToJsonElement(params)
//            val action = element.jsonObject["action"].toString()
            val paramsObj = Json.decodeFromString<WebParams>(params)
            val action = paramsObj.action

            val event = EventManager.instance.getEvent(action)
            event.setWebContainer(container)
            Log.d("WebInterface", event.toString())
            return event.execute(params)
        }
        throw NullPointerException("Params is NULL")
    }
}
package org.devio.`as`.proj.ability.pay.alipay

import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

object AliPayOrderInfoUtil {

    fun buildOrderParam(map: Map<String, String>): String {
        val sb = StringBuilder()
        for (entry in map.entries) {
            val key = entry.key
            val value = entry.value
            sb.append(
                buildKeyValue(
                    key,
                    value,
                    true
                )
            )

            sb.append("&")
        }

        sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }

    private fun buildKeyValue(key: String, value: String, encode: Boolean): String {
        val sb = StringBuilder()
        sb.append(key)
        sb.append("=")
        if (encode) {
            sb.append(URLEncoder.encode(value, "utf-8"))
        } else {
            sb.append(value)
        }
        return sb.toString()
    }

    fun getSign(map: Map<String, String>, rsaKey: String): String {
        val sb = StringBuilder()
        for (entry in map.entries) {
            val key = entry.key
            val value = entry.value
            sb.append(
                buildKeyValue(
                    key,
                    value,
                    false
                )
            )
            sb.append("&")
        }
        sb.deleteCharAt(sb.length - 1)

        val oriSign = SignUtils.sign(sb.toString(), rsaKey, true)
        return URLEncoder.encode(oriSign, "UTF-8")

    }

    /**
     * 订单号必须唯一
     */
    fun getOutTradeNo(): String {
        val format = SimpleDateFormat("MMddHHmmss", Locale.getDefault())
        val date = Date()
        var key = format.format(date)
        val r = Random()
        key = key + r.nextInt()
        key = key.substring(0, 15)
        return key
    }
}
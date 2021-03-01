package org.devio.`as`.proj.ability.pay.alipay

class PayResult constructor(map: Map<String, String?>) {
    val resultInfo = map.get("result")//支付结果
    val resultStatus = map.get("resultStatus")//支付结果状态码
    val memo = map.get("memo")//错误信息提示信息
}
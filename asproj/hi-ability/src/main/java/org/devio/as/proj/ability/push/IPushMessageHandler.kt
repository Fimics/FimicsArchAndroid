package org.devio.`as`.proj.ability.push

import android.content.Context
import org.json.JSONObject

interface IPushMessageHandler {
    //自定义 处理  通知栏的 点击行为
    fun dealWithCustomAction(context: Context?, message: JSONObject?) {

    }

    //自己解析 自定义的消息格式，弹窗，notification,上传log文件
    fun dealWithCustomMessage(context: Context?, message: JSONObject?) {

    }

    //自定义notification的样式，消息格式是符合UMeng  消息推送格式的
    fun dealWithNotificationMessage(context: Context?, jsonObject: JSONObject) {

    }

    //向UmENG注册推送服务成功，那倒devicetoken
    fun onRegisterSuccess(deviceToken: String) {

    }

    //服务注册失败
    fun onRegisterFailed(s: String, s1: String) {

    }
}
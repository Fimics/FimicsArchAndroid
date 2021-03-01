package org.devio.`as`.proj.ability.push

import android.content.Intent
import com.umeng.message.UmengNotifyClickActivity
import org.android.agoo.common.AgooConstants

/**
 * 小米对后台进程做了诸多限制。若使用一键清理，应用的channel进程被清除，将接收不到推送。
 * 通过接入小米托管弹窗功能，可有效防止以上情况，增加推送消息的送达率。
 * 通知将由小米系统托管弹出，点击通知栏将跳转到指定的Activity。
 * 该Activity需继承自UmengNotifyClickActivity，同时实现父类的onMessage方法，对该方法的intent参数进一步解析即可，
 * 该方法异步调用，不阻塞主线程。
 */
open class PushReceiverActivity : UmengNotifyClickActivity() {
    override fun onMessage(intent: Intent) {
        super.onMessage(intent) //此方法必须调用，否则无法统计打开数
        val body: String? = intent.getStringExtra(AgooConstants.MESSAGE_BODY)
        body?.apply {
            PushInitialization.onOEMPush(this)
        }
    }
}
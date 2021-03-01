package org.devio.`as`.proj.ability.push

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.message.UmengMessageHandler
import com.umeng.message.UmengNotificationClickHandler
import com.umeng.message.entity.UMessage
import org.android.agoo.huawei.HuaWeiRegister
import org.android.agoo.oppo.OppoRegister
import org.android.agoo.vivo.VivoRegister
import org.android.agoo.xiaomi.MiPushRegistar
import org.devio.`as`.proj.ability.BuildConfig
import org.json.JSONObject


object PushInitialization {
    const val TAG = "PushInitialization"
    private var iPushMessageHandler: IPushMessageHandler? = null
    fun init(
        application: Application,
        channel: String,
        iPushMessageHandler: IPushMessageHandler? = null
    ) {
        initUmengPushSdk(application, channel, iPushMessageHandler)
        initOEMPushSdk(application)
        this.iPushMessageHandler = iPushMessageHandler
    }

    private fun initOEMPushSdk(application: Application) {
        //注册成功后会在tag：MiPushBroadcastReceiver下面打印log： onCommandResult is called. regid= xxxxxxxxxxxxxxxxxxxxxxx接收到小米消息则会打印log：
        //onReceiveMessage,msg= xxxxxxxxxxxxxxxxxxxxxxx
        //仅在小米MIUI设备上生效。
        //集成小米push的版本暂不支持多包名。
        MiPushRegistar.register(application, BuildConfig.MIPUSH_APP_ID, BuildConfig.MIPUSH_API_KEY)


        // 仅在华为EMUI设备上生效。
        // 注册成功后会在tag：HuaWeiReceiver下面打印log： 获取token成功，token= xxxxxxxxxxxxxxxxxxxxxxx
        //接收到华为消息则会打印log： HuaWeiReceiver,content= xxxxxxxxxxxxxxxxxxxxxxx
        HuaWeiRegister.register(application)


        OppoRegister.register(application, BuildConfig.OPPO_API_KEY, BuildConfig.OPPO_API_SECRET)
        VivoRegister.register(application)

    }

    private fun initUmengPushSdk(
        application: Application,
        channel: String,
        iPushMessageHandler: IPushMessageHandler? = null
    ) {
        // 在此处调用基础组件包提供的初始化函数 相应信息可在应用管理 -> 应用信息 中找到 http://message.umeng.com/list/apps
        // 参数一：当前上下文context；
        // 参数二：应用申请的Appkey（需替换）；
        // 参数三：渠道名称；
        // 参数四：设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机；
        // 参数五：Push推送业务的secret 填充Umeng Message Secret对应信息（需替换）
        UMConfigure.init(
            application,
            BuildConfig.UMENG_API_KEY,
            channel,
            UMConfigure.DEVICE_TYPE_PHONE,
            BuildConfig.UMENG_MESSAGE_SECRET
        )

        //获取消息推送代理示例
        val mPushAgent = PushAgent.getInstance(application)
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(deviceToken: String) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.e(TAG, "注册成功：deviceToken：-------->  $deviceToken")
                iPushMessageHandler?.onRegisterSuccess(deviceToken)
            }

            override fun onFailure(s: String, s1: String) {
                iPushMessageHandler?.onRegisterFailed(s, s1)
                Log.e(TAG, "注册失败：-------->  s:$s,s1:$s1")
            }
        })

        //自定义行为
        mPushAgent.messageHandler = object : UmengMessageHandler() {
            override fun dealWithNotificationMessage(p0: Context?, p1: UMessage?) {
                if (iPushMessageHandler != null) {
                    iPushMessageHandler.dealWithNotificationMessage(p0, JSONObject())
                } else {
                    super.dealWithNotificationMessage(p0, p1)
                }
            }

            override fun dealWithCustomMessage(p0: Context?, p1: UMessage?) {
                if (iPushMessageHandler != null) {
                    iPushMessageHandler.dealWithCustomMessage(p0, JSONObject())
                } else {
                    super.dealWithCustomMessage(p0, p1)
                }
            }
        }
        mPushAgent.notificationClickHandler = object : UmengNotificationClickHandler() {
            override fun dealWithCustomAction(p0: Context?, p1: UMessage?) {
                if (iPushMessageHandler != null) {
                    //需要自己按照应用 约定的数据结构 去解析UMessage---->JSONOBJECT
                    iPushMessageHandler.dealWithCustomAction(p0, JSONObject())
                } else {
                    super.dealWithCustomAction(p0, p1)
                }
            }
        }

        application.registerActivityLifecycleCallbacks(object : SimpleLifecycleCallbacks() {
            override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                super.onActivityPreCreated(activity, savedInstanceState)
                PushAgent.getInstance(activity.application).onAppStart();
            }
        })
    }

    fun onOEMPush(message: String) {
        iPushMessageHandler?.dealWithCustomMessage(null, JSONObject(message))
    }
}
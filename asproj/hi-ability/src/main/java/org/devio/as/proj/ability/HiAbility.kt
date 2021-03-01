package org.devio.`as`.proj.ability

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.umeng.commonsdk.statistics.common.DeviceConfig
import org.devio.`as`.proj.ability.analyse.AnalyseUtil
import org.devio.`as`.proj.ability.pay.alipay.AliPayHelper
import org.devio.`as`.proj.ability.pay.alipay.PayResult
import org.devio.`as`.proj.ability.push.IPushMessageHandler
import org.devio.`as`.proj.ability.push.PushInitialization
import org.devio.`as`.proj.ability.scan.ScanActivity
import org.devio.`as`.proj.ability.share.ShareBundle
import org.devio.`as`.proj.ability.share.ShareManager
import org.devio.hi.library.util.AppGlobals
import org.devio.hi.library.util.HiViewUtil

object HiAbility {
    private val scanResultLiveData = MutableLiveData<String>()
    fun init(
        application: Application, channel: String,
        iPushMessageHandler: IPushMessageHandler? = null
    ) {
        if (HiViewUtil.inMainProcess(application)) {
            PushInitialization.init(application, channel, iPushMessageHandler)
            AnalyseUtil.init(application, channel)
        }
    }


    /**
     * 唤起分享面板
     */
    fun share(context: Context, shareBundle: ShareBundle) {
        ShareManager.share(context, shareBundle)
    }

    /**
     * 打开扫码页,结果通过observer来接收
     */
    fun openScanActivity(activity: Activity, observer: Observer<String>) {
        if (activity is LifecycleOwner) {
            scanResultLiveData.observe(activity, observer)
        }
        activity.startActivity(Intent(activity, ScanActivity::class.java))
    }

    internal fun onScanResult(scanResult: String) {
        scanResultLiveData.postValue(scanResult)
        scanResultLiveData.value = null
    }

    fun traceEvent(eventId: String, values: Map<String, Any>) {
        AnalyseUtil.traceEvent(AppGlobals.get()!!, eventId, values)
    }

    /**
     * 是用来获取设备的唯一ID 和mac
     */
    fun getTestDeviceInfo(context: Context?): Array<String?>? {
        val deviceInfo = arrayOfNulls<String>(2)
        try {
            if (context != null) {
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context)
                deviceInfo[1] = DeviceConfig.getMac(context)
            }
        } catch (e: Exception) {
        }
        return deviceInfo
    }

    fun aliPay(activity: Activity, orderInfo: String, observer: Observer<PayResult>) {
        AliPayHelper.pay(activity, orderInfo, observer)
    }
}
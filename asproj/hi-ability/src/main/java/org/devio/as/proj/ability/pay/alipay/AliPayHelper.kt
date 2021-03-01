package org.devio.`as`.proj.ability.pay.alipay

import android.app.Activity
import androidx.lifecycle.Observer
import com.alipay.sdk.app.PayTask
import org.devio.hi.library.executor.HiExecutor
import org.devio.hi.library.log.HiLog

object AliPayHelper {
    fun pay(activity: Activity, orderInfo: String, observer: Observer<PayResult>) {
        HiExecutor.execute(runnable = Runnable {
            val aliPayTask = PayTask(activity)

            val resultMap = aliPayTask.payV2(orderInfo, true)

            val payResult = PayResult(resultMap)

            observer.onChanged(payResult)
        })
    }
}
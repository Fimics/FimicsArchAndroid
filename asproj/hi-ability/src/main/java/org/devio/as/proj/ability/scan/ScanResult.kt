package org.devio.`as`.proj.ability.scan

import com.huawei.hms.ml.scan.HmsScan
import org.devio.`as`.proj.ability.HiAbility

object ScanResult {
    internal fun onScanResult(hmsScan: HmsScan) {
        var scanResult: String? = null
        if (hmsScan.getScanTypeForm() === HmsScan.SMS_FORM) {
            //解析成SMS的结构化数据
            val smsContent: HmsScan.SmsContent = hmsScan.getSmsContent()
            val content = smsContent.getMsgContent()
            val phoneNumber = smsContent.getDestPhoneNumber()

            scanResult = phoneNumber
        } else if (hmsScan.getScanTypeForm() === HmsScan.WIFI_CONNECT_INFO_FORM) {
            //解析成Wi-Fi的结构化数据
            val wifiConnectionInfo: HmsScan.WiFiConnectionInfo = hmsScan.wiFiConnectionInfo
            val password = wifiConnectionInfo.getPassword()
            val ssidNumber = wifiConnectionInfo.getSsidNumber()
            val cipherMode = wifiConnectionInfo.getCipherMode()

            scanResult = ssidNumber
        } else {
            scanResult = hmsScan.originalValue
        }
       scanResult?.apply { HiAbility.onScanResult(scanResult) }
    }
}
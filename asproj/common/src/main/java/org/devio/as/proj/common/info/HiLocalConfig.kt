package org.devio.`as`.proj.common.info

import org.devio.`as`.proj.common.utils.SPUtil

class HiLocalConfig : LocalConfig {
    //访问令牌
    override fun authToken(): String {
        return "MjAyMC0wNi0yMyAwMzoyNTowMQ=="
    }

    //登录凭证
    override fun boardingPass(): String? {
        return SPUtil.getString("boarding_pass")
    }

    companion object {
        @get:Synchronized
        var instance: HiLocalConfig? = null
            get() {
                if (field == null) {
                    field = HiLocalConfig()
                }
                return field
            }
            private set
    }
}

internal interface LocalConfig {
    fun authToken(): String
    fun boardingPass(): String?
}
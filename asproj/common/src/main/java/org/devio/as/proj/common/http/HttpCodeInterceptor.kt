package org.devio.`as`.proj.common.http

import android.os.Bundle
import org.devio.`as`.proj.common.route.HiRoute
import org.devio.hi.library.restful.HiInterceptor
import org.devio.hi.library.restful.HiResponse

/**
 * 根据response 的 code 自动路由到相关页面
 */
class HttpCodeInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        val response = chain.response();
        if (!chain.isRequestPeriod && response != null) {
            when (response.code) {
                HiResponse.RC_NEED_LOGIN -> {
                    HiRoute.startActivity(null, destination = "/account/login")
                }
                //token过期
                //a | b
                HiResponse.RC_AUTH_TOKEN_EXPIRED, (HiResponse.RC_AUTH_TOKEN_INVALID), (HiResponse.RC_USER_FORBID) -> {
                    var helpUrl: String? = null
                    if (response.errorData != null) {
                        helpUrl = response.errorData!!["helpUrl"]
                    }

                    val bundle = Bundle()
                    bundle.putString("degrade_title", "非法访问")
                    bundle.putString("degrade_desc", response.msg)
                    bundle.putString("degrade_action", helpUrl)
                    HiRoute.startActivity(null, bundle, "/degrade/global/activity")
                }
            }
        }
        return false
    }

}
package org.devio.`as`.proj.common.http

import org.devio.`as`.proj.service_login.LoginServiceProvider
import org.devio.hi.library.log.HiLog
import org.devio.hi.library.restful.HiInterceptor
import org.devio.hi.library.restful.HiRequest

class BizInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        val request = chain.request()
        val response = chain.response()
        if (chain.isRequestPeriod) {

            val boardingPass = LoginServiceProvider.getBoardingPass() ?: ""
            request.addHeader("boarding-pass", boardingPass)
            request.addHeader("auth-token", "MTU5Mjg1MDg3NDcwNw==")
        } else if (response != null) {
            var outputBuilder = StringBuilder()
            val httpMethod: String =
                if (request.httpMethod == HiRequest.METHOD.GET) "GET" else "POST"
            val requestUrl: String = request.endPointUrl()
            outputBuilder.append("\n$requestUrl==>$httpMethod\n")


            if (request.headers != null) {
                outputBuilder.append("【headers\n")
                request.headers!!.forEach(action = {
                    outputBuilder.append(it.key + ":" + it.value)
                    outputBuilder.append("\n")
                })
                outputBuilder.append("headers】\n")
            }

            if (request.parameters != null && request.parameters!!.isNotEmpty()) {
                outputBuilder.append("【parameters==>\n")
                request.parameters!!.forEach(action = {
                    outputBuilder.append(it.key + ":" + it.value + "\n")
                })
                outputBuilder.append("parameters】\n")
            }

            outputBuilder.append("【response==>\n")
            outputBuilder.append(response.rawData + "\n")
            outputBuilder.append("response】\n")

            HiLog.dt("BizInterceptor Http:", outputBuilder.toString())
        }

        return false
    }

}
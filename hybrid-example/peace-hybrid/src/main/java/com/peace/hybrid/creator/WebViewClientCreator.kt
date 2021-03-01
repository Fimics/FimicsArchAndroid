package com.peace.hybrid.creator

import com.peace.hybrid.creator.impl.WebViewClientImpl

/**
 * @author 傅令杰
 */
class WebViewClientCreator : IWebCreator<WebViewClientImpl> {

    override fun createDefault(): WebViewClientImpl {
        return WebViewClientImpl()
    }

    companion object {
        fun builder(): WebViewClientBuilder {
            return WebViewClientBuilder()
        }
    }
}
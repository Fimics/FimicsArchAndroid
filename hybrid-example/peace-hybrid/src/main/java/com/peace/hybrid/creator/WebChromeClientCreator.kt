package com.peace.hybrid.creator

import com.peace.hybrid.creator.impl.WebChromeClientImpl

class WebChromeClientCreator : IWebCreator<WebChromeClientImpl> {

    override fun createDefault(): WebChromeClientImpl {
        return WebChromeClientImpl()
    }
}
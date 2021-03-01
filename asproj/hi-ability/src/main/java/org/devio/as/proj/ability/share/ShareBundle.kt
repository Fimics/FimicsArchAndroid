package org.devio.`as`.proj.ability.share

class ShareBundle {
    //消息类型,你也可以指定image/*,text/plain......
    var type: String = "*/*"

    /**
     * 卡片消息的title
     */
    var title: String = ""

    /**
     * 卡片消息的见解
     */
    var summary: String = ""

    /**
     * 卡片消息点击跳转页面
     */
    var targetUrl = ""

    /**
     * 卡片消息的缩略图URL
     */
    var thumbUrl = ""

    /**
     * QQ分享渠道 分享成功后 弹出上左边按钮的应用的名称
     */
    var appName = ""

    /**
     * * channels 是调用方想要分享面板展示出来的渠道
     * 但是此时 某些渠道（QQ）就没有安装，那么我们需要查询本机已安装的应用列表
     * 和channels 做个过滤
     */
    var channels: List<String>? = null
}
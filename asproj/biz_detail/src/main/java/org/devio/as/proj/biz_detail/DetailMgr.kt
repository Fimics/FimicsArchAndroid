package org.devio.`as`.proj.biz_detail

object DetailMgr {
    val list = arrayListOf<Function<String>>()
    fun addListener(function: Function<String>) {
        list.add(function)
    }
}
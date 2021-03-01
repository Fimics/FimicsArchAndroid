package com.peace.core.util

import androidx.annotation.IdRes

private val mDataMap = HashMap<Any, Any>()

fun addData(key: String, value: Any) {
    mDataMap[key] = value
}

fun addData(key: Enum<*>, value: Any) {
    mDataMap[key] = value
}

fun addData(@IdRes key: Int, value: Any) {
    mDataMap[key] = value
}

fun <T> getData(key: String): T {
    @Suppress("UNCHECKED_CAST")
    return mDataMap[key] as T
}

fun <T> getData(key: Enum<*>): T {
    @Suppress("UNCHECKED_CAST")
    return mDataMap[key] as T
}

fun <T> getData(@IdRes key: Int): T {
    @Suppress("UNCHECKED_CAST")
    return mDataMap[key] as T
}

fun removeData(key: String) {
    mDataMap.remove(key)
}

fun removeData(key: Enum<*>) {
    mDataMap.remove(key)
}

fun removeData(@IdRes key: Int) {
    mDataMap.remove(key)
}
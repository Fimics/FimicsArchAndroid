package org.devio.`as`.proj.common.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private const val MD_FORMAT = "MM-dd"
    private const val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss"
    fun getMDDate(date: Date): String {
        val sdf = SimpleDateFormat(MD_FORMAT, Locale.CHINA)
        return sdf.format(date)
    }

    fun getMDDate(dateString: String): String {
        val sdf = SimpleDateFormat(DEFAULT_FORMAT, Locale.CHINA)
        return getMDDate(sdf.parse(dateString)!!)
    }


}
package org.devio.`as`.proj.common.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.devio.hi.library.util.AppGlobals

@SuppressLint("ApplySharedPref")
object SPUtil {
    private const val CACHE_FILE = "cache_file"
    fun putString(key: String, value: String?) {
        val shared = getShared()
        shared?.edit()?.putString(key, value)?.commit()
    }

    fun getString(key: String): String? {
        val shared = getShared()
        if (shared != null) {
            return shared.getString(key, null)
        }
        return null
    }


    fun putBoolean(key: String, value: Boolean) {
        val shared = getShared()
        shared?.edit()?.putBoolean(key, value)?.commit()
    }

    fun getBoolean(key: String): Boolean {
        val shared = getShared()
        if (shared != null) {
            return shared.getBoolean(key, false)
        }
        return false
    }


    fun putInt(key: String, value: Int) {
        val shared = getShared()
        shared?.edit()?.putInt(key, value)?.commit()
    }

    fun getInt(key: String): Int {
        val shared = getShared()
        if (shared != null) {
            return shared.getInt(key, 0)
        }
        return 0
    }


    private fun getShared(): SharedPreferences? {
        val application: Application? = AppGlobals.get()
        if (application != null) {
            return application.getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE)
        }
        return null
    }
}
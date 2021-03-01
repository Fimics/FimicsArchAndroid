package org.devio.`as`.proj.common.ext

import android.widget.Toast
import org.devio.hi.library.util.AppGlobals

fun <T> T.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(
    AppGlobals.get()!!,
    message, duration
).show()
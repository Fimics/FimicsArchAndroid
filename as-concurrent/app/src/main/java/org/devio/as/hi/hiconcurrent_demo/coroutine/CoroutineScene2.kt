package org.devio.`as`.hi.hiconcurrent_demo.coroutine

import android.util.Log
import kotlinx.coroutines.delay

object CoroutineScene2 {

    private val TAG: String = "CoroutineScene2"

    suspend fun request1(): String {
        val request2 = request2();
        return "result from request1 " + request2;
    }

    suspend fun request2(): String {
        delay(2 * 1000)
        Log.e(TAG, "request2 completed")
        return "result from request2"
    }
}
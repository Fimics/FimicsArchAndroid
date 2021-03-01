package org.devio.`as`.hi.hiconcurrent_demo.coroutine

import android.util.Log
import kotlinx.coroutines.*


object CoroutineScene {

    private val TAG: String = "CoroutineScene"

    /**
     * 以此启动三个子线程, 并且同步的方式拿到他们的返回值，进而更新UI
     */
    fun startScene1() {
        GlobalScope.launch(Dispatchers.Unconfined) {
            Log.e(TAG, "coroutine is running")
            val result1 = request1()
            val result2 = request2(result1)
            val result3 = request3(result2)

            updateUI(result3)
        }

        Log.e(TAG, "coroutine has launched")
    }

    /**
     * 启动一个线程,先执行request1，完了之后，同时运行request2 和request3, 这俩并发都结束了才执行updateIU
     */
    fun startScene2() {

        GlobalScope.launch(Dispatchers.Main) {

            Log.e(TAG, "coroutine is running")

            val result1 = request1()
//           request2()
//           request3()

            val deferred2 = GlobalScope.async { request2(result1) }
            val deferred3 = GlobalScope.async { request3(result1) }

            // deferred2.await();
            //deferred3.await()
            updateUI(deferred2.await(), deferred3.await())
        }

        Log.e(TAG, "coroutine has started")
    }

    private fun updateUI(result2: String, result3: String) {
        Log.e(TAG, "updateui work on ${Thread.currentThread().name}")
        Log.e(TAG, "paramter:" + result3 + "---" + result2)
    }

    private fun updateUI(result3: String) {
        Log.e(TAG, "updateui work on ${Thread.currentThread().name}")
        Log.e(TAG, "paramter:" + result3)
    }

    //suspend关键字的作用--->
    //delay既然是IO异步任务,是如何做到延迟协程 中的代码向下执行的？
    suspend fun request1(): String {
        delay(2 * 1000)  //不会暂停线程,但会暂停当前所在的协程
        //Thread.sleep(2000)  让线程休眠

        Log.e(TAG, "request1 work on ${Thread.currentThread().name}")
        return "result from request1"
    }

    suspend fun request2(result1: String): String {
        delay(2 * 1000)

        Log.e(TAG, "request2 work on ${Thread.currentThread().name}")
        return "result from request2"
    }


    suspend fun request3(result2: String): String {
        delay(2 * 1000)

        Log.e(TAG, "request3 work on ${Thread.currentThread().name}")
        return "result from request3"
    }

}
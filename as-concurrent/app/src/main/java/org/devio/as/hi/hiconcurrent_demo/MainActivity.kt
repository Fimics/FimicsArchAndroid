package org.devio.`as`.hi.hiconcurrent_demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import org.devio.`as`.hi.hiconcurrent_demo.coroutine.CoroutineScene
import org.devio.`as`.hi.hiconcurrent_demo.coroutine.CoroutineScene2_decompiled
import kotlin.coroutines.Continuation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text1.setOnClickListener {

            //CoroutineScene.startScene2()

            val callback =Continuation<String>(Dispatchers.Main){result->
                Log.e("MainActivity",result.getOrNull());
            }
            CoroutineScene2_decompiled.request1(callback)

        }



















        //            val content = CoroutineScene3.parseAssetsFile(assets, "config.json")
//
//            Log.e("coroutine", content)
//            Log.e("coroutine", "after click")

//            lifecycleScope.async {
//
//            }
//
//            lifecycleScope.launchWhenCreated {
//                //是指当我们的宿主的生命周期 至少为oncreate的时候 才会启动
//                whenCreated {
//                    //这里的代码 只有当宿主的生命周期为oncreate才会执行，否则都是暂停
//                }
//
//                whenResumed {
//                    //这里的代码 只有宿主的生命周期 为 onresume才会执行 否则暂停
//                }
//
//                whenStarted {
//
//                    //这里的代码 只有当宿主的舍命周期为 onstart的时候 才会执行 否则暂停
//                }
//            }
//
//
//            lifecycleScope.launchWhenStarted {
//                //是指我们的宿主的生命周期至少为onstart的时候 才会启动
//
//            }
//
//            lifecycleScope.launchWhenResumed {
//
//            }

//            GlobalScope.launch {
//
//            }
    }
}


//    val value = object : Continuation<String> {
//        override fun resumeWith(result: Result<String>) {
//            val result = result.getOrNull()
//            Log.e("MainActivity", "coroutine completed--->" + result)
//        }
//
//        override val context: CoroutineContext
//            get() = Dispatchers.Main
//    }
//
//    Coroutine1_decompiled.main(value)
//    Log.e("MainActivity", "after start coroutine")

package org.devio.`as`.proj.ability.analyse

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import org.devio.`as`.proj.ability.BuildConfig
import org.devio.`as`.proj.ability.push.SimpleLifecycleCallbacks

internal object AnalyseUtil {
    fun init(application: Application, channel: String) {
        UMConfigure.init(
            application,
            BuildConfig.UMENG_API_KEY,
            channel,
            UMConfigure.DEVICE_TYPE_PHONE,
            null
        )
        //MANUAL 需要手动在activity的生命周期调用MobclickAgent.onPause(this);  or MobclickAgent.onResume(this);
        //auto 但是对于fragment 或 自定义的是使用时长统计，需要自行通过onPageStart 和onPageEnd 来上报
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)

        application.registerActivityLifecycleCallbacks(object : SimpleLifecycleCallbacks() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                super.onActivityCreated(activity, savedInstanceState)
                if (activity is FragmentActivity) {
                    val sfm = activity.supportFragmentManager
                    sfm.registerFragmentLifecycleCallbacks(object :
                        FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                            super.onFragmentResumed(fm, f)
                            if (f is IAnalysePage) {
                                onPageStart(f.getPageName())
                            }
                        }

                        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                            super.onFragmentPaused(fm, f)
                            if (f is IAnalysePage) {
                                onPageEnd(f.getPageName())
                            }
                        }
                    }, true)
                    //假设 一个activity 嵌套了一个fragment A.-->fragment B,fragment C
                }
            }
        })
    }

    /**
     * 在AUTO或MANUAL模式下，如果需要对非Activity页面，如Fragment、自定义View等非标准页面进行统计。
     * 需要通过MobclickAgent.onPageStart/MobclickAgent.onPageEnd接口在合适的时机进行页面统计。
     *一次成对的 onPageStart -> onPageEnd 调用，对应一次非Activity页面(如：Fragment)生命周期统计。
     */
    fun onPageStart(pageName: String) {
        MobclickAgent.onPageStart(pageName)
    }

    fun onPageEnd(pageName: String) {
        MobclickAgent.onPageEnd(pageName)
    }

    /**
     * 埋点,事件上报
     * eventId需要事先定义好https://mobile.umeng.com/platform/5fa6c6591c520d3073a2655f/setting/event/list
     */
    fun traceEvent(context: Context, eventId: String, values: Map<String, Any>) {
        MobclickAgent.onEventObject(context, eventId, values)
    }

}
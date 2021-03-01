package org.devio.`as`.proj.common.rn

import android.app.Activity
import android.os.Bundle
import android.os.Looper
import android.view.ViewGroup
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
import org.devio.`as`.proj.common.BuildConfig
import org.devio.`as`.proj.common.rn.view.HiRNImageViewPackage

/**
 * RN优化提升加载速度，实现秒开RN模块
 * 0.如何让预加载不损失"首页"性能
 * 1.预加载时不让RN其控制activity,比如修改activity状态栏
 * 2.如何让RN获取到真实的Activity而不是预加载时的Activity
 * 3.如何在当第二次获取RN缓存时不出现空白页
 */
class HiRNCacheManager private constructor() {
    /**
     * 预加载RN
     */
    fun preLoad(activity: Activity, moduleName: String, initParams: Bundle) {
        //在线程空闲的时候执行加载任务
        Looper.myQueue().addIdleHandler {
            val reactRootView = initReactRootView(activity, moduleName, initParams)
            //预加载时，不让其控制activity，比如修改activity状态栏
            reactRootView.reactInstanceManager?.onHostDestroy(activity)
            CACHED_VIEW[moduleName] = reactRootView
            false
        }
    }

    /**
     * 获取预加载的RN
     */
    fun getCachedReactRootView(
        activity: Activity,
        moduleName: String,
        initParams: Bundle
    ): ReactRootView? {
        var reactRootView = CACHED_VIEW[moduleName]
        if (reactRootView == null) {
            reactRootView = initReactRootView(activity, moduleName, initParams)
        }
        reactRootView.reactInstanceManager?.apply {
            //替换预加载时的Activity
            onHostResume(activity)
            onHostDestroy(activity)
            onHostResume(activity)
        }
        detachRootView(moduleName)
        return reactRootView
    }

    /**
     * 从父容器中移除RN
     */
    fun detachRootView(moduleName: String) {
        val rootView = CACHED_VIEW[moduleName] ?: return
        val parent = rootView.parent as? ViewGroup
        parent?.removeView(rootView)
    }

    /**
     * 初始化RN
     */
    fun initReactRootView(
        activity: Activity,
        moduleName: String,
        initParams: Bundle
    ): ReactRootView {
        val reactRootView = ReactRootView(activity)
        val reactInstanceManager = ReactInstanceManager.builder()
            .setApplication(activity.application)
            .setCurrentActivity(activity)
            .setBundleAssetName("index.android.bundle")
            .setJSMainModulePath("index")
            .addPackage(MainReactPackage())
            .addPackage(HiReactPackage())
            .addPackage(HiRNImageViewPackage())
            .setUseDeveloperSupport(BuildConfig.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build()
        //和index.js中AppRegistry.registerComponent()注册的module保持对应
        reactRootView.startReactApplication(reactInstanceManager, moduleName, initParams)
        return reactRootView;
    }
    fun destroy(moduleName: String){
        val reactRootView= CACHED_VIEW[moduleName]
        reactRootView?.apply {
            reactRootView.unmountReactApplication()
        }
        CACHED_VIEW.remove(moduleName)
    }
    companion object {
        const val MODULE_NAME_BROWSING = "rn_module_browsing"
        const val MODULE_NAME_BRIDGE_DEMO = "rn_module_bridgeDemo"

        @JvmStatic
        @get:Synchronized
        var instance: HiRNCacheManager? = null
            get() {
                if (field == null) {
                    field = HiRNCacheManager()
                }
                return field;
            }
            private set
        private val CACHED_VIEW: MutableMap<String, ReactRootView> = HashMap()
    }
}
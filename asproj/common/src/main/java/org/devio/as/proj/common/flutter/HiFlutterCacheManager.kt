package org.devio.`as`.proj.common.flutter

import android.content.Context
import android.os.Handler
import android.os.Looper
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.FlutterJNI
import io.flutter.embedding.engine.dart.DartExecutor.DartEntrypoint
import io.flutter.embedding.engine.loader.FlutterLoader
import io.flutter.view.FlutterMain
import org.devio.`as`.proj.common.flutter.view.HiImageViewPlugin
import org.devio.hi.library.log.HiLog
import org.devio.hi.library.util.HiFileUtil

/**
 * Flutter优化提升加载速度，实现秒开Flutter模块
 * 0.如何让预加载不损失"首页"性能
 * 1.如何实例化多个Flutter引擎并分别加载不同的dart 入口文件
 */
class HiFlutterCacheManager private constructor() {
    /**
     * 预加载FlutterEngine
     */
    fun preLoad(
        context: Context
    ) {
        val messageQueue = Looper.myQueue()
        HiFileUtil.copyAssetsFile2FilesDir(context, HiFlutterLoader.FIX_SO, listener = {
            //在线程空闲时执行预加载任务
            messageQueue.addIdleHandler {
                initFlutterEngine(context, MODULE_NAME_FAVORITE)
                initFlutterEngine(context, MODULE_NAME_RECOMMEND)
                false
            }
        })
    }

    /**
     * 获取预加载的FlutterEngine
     */
    fun getCachedFlutterEngine(moduleName: String, context: Context?): FlutterEngine? {
        var engine = FlutterEngineCache.getInstance()[moduleName]
        if (engine == null && context != null) {
            engine = initFlutterEngine(context, moduleName)
        }
        return engine!!
    }

    fun hastCached(moduleName: String): Boolean {
        return FlutterEngineCache.getInstance().contains(moduleName)
    }

    /**
     * 销毁FlutterEngine
     */
    fun destroyCached(moduleName: String) {
        FlutterEngineCache.getInstance()[moduleName]?.apply {
            destroy()
        }
        FlutterEngineCache.getInstance().remove(moduleName)
    }

    /**
     * DartVM 预热
     */
    fun preLoadDartVM(context: Context) {
        val settings = FlutterLoader.Settings()
        FlutterLoader.getInstance().startInitialization(context, settings);
        val mainHandler = Handler(Looper.getMainLooper())
        FlutterLoader.getInstance().ensureInitializationCompleteAsync(
            context, arrayOf(),
            mainHandler
        ) {
            HiLog.i("Flutter preLoadDartVM done")
        }

    }

    /**
     * 初始化FlutterEngine
     */
    private fun initFlutterEngine(
        context: Context,
        moduleName: String
    ): FlutterEngine {
        // Instantiate a FlutterEngine.
        val flutterEngine = FlutterEngine(context, HiFlutterLoader.get(), FlutterJNI())
        //插件注册要紧跟引擎初始化之后，否则会有在dart中调用插件因为还未初始化完成而导致的时序问题
        HiFlutterBridge.init(flutterEngine)
        HiImageViewPlugin.registerWith(flutterEngine)
        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            //  Flutter 1.24 FlutterMain被废弃了，用FlutterMain.findAppBundlePath()换成HiFlutterLoader.get()
            //  ，以fix java.lang.NullPointerException: Attempt to read from field 'java.lang.String io.flutter.embedding.engine.loader.FlutterApplicationInfo.flutterAssetsDir' on a null object reference
            DartEntrypoint(
                HiFlutterLoader.get().findAppBundlePath(),
                moduleName
            )
        )
        // Cache the FlutterEngine to be used by FlutterActivity or FlutterFragment.
        FlutterEngineCache.getInstance().put(moduleName, flutterEngine)
        return flutterEngine
    }


    companion object {
        const val MODULE_NAME_FAVORITE = "main"
        const val MODULE_NAME_RECOMMEND = "recommend"

        @JvmStatic
        @get:Synchronized
        var instance: HiFlutterCacheManager? = null
            get() {
                if (field == null) {
                    field = HiFlutterCacheManager()
                }
                return field
            }
            private set
    }
}
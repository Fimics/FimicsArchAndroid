package org.devio.`as`.proj.common.flutter

import android.content.Context
import io.flutter.embedding.engine.loader.FlutterLoader
import java.io.File

/**
 * 利用反射将新的Flutter的代码so，注入到Flutter的so的加载流程中
 * Flutter 1.18或以下版本支持热修复，详见说明：https://class.imooc.com/course/qadetail/262865
 */
class HiFlutterLoader : FlutterLoader() {
    companion object {
        const val FIX_SO = "libappfix.so"
        private var instance: HiFlutterLoader? = null
            get() {
                if (field == null) {
                    field = HiFlutterLoader()
                }
                return field
            }

        fun get(): HiFlutterLoader {
            return instance!!
        }
    }

    override fun ensureInitializationComplete(
        applicationContext: Context,
        args: Array<out String>?
    ) {
        val path = applicationContext.filesDir
        val soFile = File(path, FIX_SO)
        if (soFile.exists()) {
            try {
                //反射FlutterLoader:aotSharedLibraryName将libapp.so替换成新的so路径
                val field =
                    FlutterLoader::class.java.getDeclaredField("aotSharedLibraryName")
                field.isAccessible = true
                field[this] = soFile.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        //在FlutterJNI.nativeInit之前将so路径替换好
        super.ensureInitializationComplete(applicationContext, args)
    }
}
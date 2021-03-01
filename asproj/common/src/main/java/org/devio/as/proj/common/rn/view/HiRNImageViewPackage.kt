package org.devio.`as`.proj.common.rn.view

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import java.util.*

class HiRNImageViewPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): MutableList<NativeModule> {
        return ArrayList()
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): MutableList<ViewManager<*, *>> {
        val modules: MutableList<ViewManager<*, *>> =
            ArrayList()
        modules.add(HiRNImageViewManager())
        return modules
    }
}
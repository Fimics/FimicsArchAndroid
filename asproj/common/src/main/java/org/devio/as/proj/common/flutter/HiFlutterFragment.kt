package org.devio.`as`.proj.common.flutter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.flutter.embedding.android.FlutterTextureView
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import kotlinx.android.synthetic.main.fragment_flutter.*
import org.devio.`as`.proj.common.R
import org.devio.`as`.proj.common.flutter.HiFlutterBridge.Companion.instance
import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import org.devio.hi.library.util.AppGlobals

abstract class HiFlutterFragment(moduleName: String) : HiBaseFragment() {
    private val flutterEngine: FlutterEngine?
    protected var flutterView: FlutterView? = null
    private val cached = HiFlutterCacheManager.instance!!.hastCached(moduleName)

    init {
        flutterEngine = HiFlutterCacheManager.instance!!.getCachedFlutterEngine(
            moduleName,
            AppGlobals.get()
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 注册flutter/platform_views 插件以便能够处理native view
        if (!cached) {
            flutterEngine?.platformViewsController?.attach(
                activity,
                flutterEngine.renderer,
                flutterEngine.dartExecutor
            )
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_flutter
    }

    fun setTitle(titleStr: String) {
        rl_title.visibility = View.VISIBLE
        title_line.visibility = View.VISIBLE
        title.text = titleStr
        //Native to flutter
        title.setOnClickListener {
            instance!!.fire("onRefresh", "so easy")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (layoutView as ViewGroup).addView(createFlutterView(activity!!))
    }

    private fun createFlutterView(context: Context): FlutterView {
        //使用FlutterTextureView来进行渲染，以规避FlutterSurfaceView压后台回来后界面被复用的问题
        val flutterTextureView = FlutterTextureView(activity!!)
        flutterView = FlutterView(context, flutterTextureView)
        return flutterView!!
    }

    override fun onStart() {
        flutterView!!.attachToFlutterEngine(flutterEngine!!)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        //for flutter >= v1.17
        flutterEngine!!.lifecycleChannel.appIsResumed()
    }

    override fun onPause() {
        super.onPause()
        flutterEngine!!.lifecycleChannel.appIsInactive()
    }

    override fun onStop() {
        super.onStop()
        flutterEngine!!.lifecycleChannel.appIsPaused()
    }

    override fun onDetach() {
        super.onDetach()
        flutterEngine!!.lifecycleChannel.appIsDetached()
    }

    override fun onDestroy() {
        super.onDestroy()
        flutterView?.detachFromFlutterEngine()
    }
}
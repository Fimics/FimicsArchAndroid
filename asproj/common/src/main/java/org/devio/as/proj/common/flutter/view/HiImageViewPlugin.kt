package org.devio.`as`.proj.common.flutter.view

import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.shim.ShimPluginRegistry
import io.flutter.plugin.common.PluginRegistry

/**
 * 1. 如何获取PluginRegistry.Registrar
 * 2. 非继承FlutterActivity时如何注册native view
 * 3. 如何调用native view传递参数
 * 4. 运行架构图
 * tips:使用FlutterActivity时注册方式需要通过发布插件的方式让借助Flutter的GeneratedPluginRegistrant自动注册
 */
object HiImageViewPlugin {
    fun registerWith(registrar: PluginRegistry.Registrar) {
        val viewFactory = HiImageViewFactory(registrar.messenger())
        registrar.platformViewRegistry().registerViewFactory("HiImageView", viewFactory)
    }

    fun registerWith(flutterEngine: FlutterEngine) {
        val shimPluginRegistry = ShimPluginRegistry(flutterEngine)
        registerWith(shimPluginRegistry.registrarFor("HiFlutter"))

    }
}
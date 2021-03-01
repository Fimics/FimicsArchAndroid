package org.devio.`as`.proj.debug

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class HiDebug(val name: String, val desc: String)
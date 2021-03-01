package org.devio.design.pattern.singleton

/**
 * 懒汉式-Kotlin实现
 */
class Singleton2Kotlin private constructor() {
    companion object {
        private var instance: Singleton2Kotlin? = null
            get() {
                if (field == null) {
                    field = Singleton2Kotlin()
                }
                return field
            }
        @Synchronized
        fun get(): Singleton2Kotlin {
            return instance!!
        }
    }
}

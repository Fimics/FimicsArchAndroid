package org.devio.design.pattern.singleton

/**
 * 静态内部类式单例-Kotlin实现
 */
class Singleton4Kotlin private constructor() {
    companion object {
        val instance = SingletonProvider.holder
    }

    private object SingletonProvider {
        val holder = Singleton4Kotlin()
    }
}

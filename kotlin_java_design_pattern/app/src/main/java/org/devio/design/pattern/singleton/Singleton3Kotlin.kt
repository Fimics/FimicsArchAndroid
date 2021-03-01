package org.devio.design.pattern.singleton

/**
 * 双重校验锁-Kotlin实现
 */
//无参数情况
class Singleton3Kotlin private constructor() {
    companion object {
        val INSTANCE: Singleton3Kotlin by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Singleton3Kotlin()
        }
    }
}

//有参数情况
class Singleton3KotlinParam private constructor(v1: Int) {
    companion object {
        @Volatile
        private var instance: Singleton3KotlinParam? = null
        fun getInstance(v1: Int) =
            instance ?: synchronized(this) {
                instance ?: Singleton3KotlinParam(v1).also { instance = it }
            }
    }
}
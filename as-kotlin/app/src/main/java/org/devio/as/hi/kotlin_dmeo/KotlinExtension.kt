package org.devio.`as`.hi.kotlin_dmeo

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes


fun main() {
    val test = mutableListOf(1, 2, 3)
    test.swap(1, 2)
    println(test)
    //在被扩展类的外部使用
    Jump().doubleJump(2f)
    Jump().test()

    val test2 = mutableListOf("Android Q", "Android N", "Android M")
    test2.swap1(0, 1)
    println(test2)

    val listString = listOf("Android Q", "Android N", "Android M")
    println("listString.last${listString.last}")

    Jump.print("伴生对象的扩展")

}

fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}


class Jump {
    companion object {}

    fun test() {
        println("jump test")
        //在被扩展类的内部使用
        doubleJump(1f)
    }
}

fun Jump.doubleJump(howLong: Float): Boolean {
    println("jump:$howLong")
    println("jump:$howLong")
    return true
}

//泛型化扩展函数
fun <T> MutableList<T>.swap1(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

/**---------扩展属性---------**/

//为String添加一个lastChar属性，用于获取字符串的最后一个字符
val String.lastChar: Char get() = this.get(this.length - 1)

///为List添加一个last属性用于获取列表的最后一个元素，this可以省略
val <T>List<T>.last: T get() = get(size - 1)

/**---------为伴生对象添加扩展---------**/
fun Jump.Companion.print(str: String) {
    println(str)
}

/**---------Kotlin中常用的扩展---------**/

/**
 * let
 */
fun testLet(str: String?) {
    //限制str2的作用域
    str.let {
        val str2 = "let扩展"
        println(it + str2)
    }
//    println(str2)//报错

    //避免为null的操作
    str?.let {
        println(it.length)
    }
}

/**
 * run
 */
data class Room(val address: String, val price: Float, val size: Float)

fun testRun(room: Room) {
    room.run {
        println("Room:$address,$price,$size")
    }
}


/**
 * apply
 */

fun testApply() {
    ArrayList<String>().apply {
        add("1")
        add("2")
        add("3")
        println("$this")
    }.let { println(it) }
}


/**---------案例:使用Kotlin扩展为控件绑定监听器减少模板代码---------**/
//为Activity添加find扩展方法，用于通过资源id获取控件
fun <T : View> Activity.find(@IdRes id: Int): T {
    return findViewById(id)
}

//为Int添加onClick扩展方法，用于为资源id对应的控件添加onClick监听
fun Int.onClick(activity: Activity, click: () -> Unit) {
    activity.find<View>(this).apply {
        setOnClickListener {
            click()
        }
    }
}
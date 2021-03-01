package org.devio.`as`.hi.kotlin.demo.kotlin


fun main() {
//    Coke().price(Sweet())
//    BlueColor(Blue()).printColor()
    testSum()
}

/**---------泛型接口---------**/
interface Drinks<T> {
    fun taste(): T
    fun price(t: T)
}

open class Sweet {
    val price = 5
}

class Coke : Drinks<Sweet> {
    override fun taste(): Sweet {
        println("Sweet")
        return Sweet()
    }

    override fun price(t: Sweet) {
        println("Coke price：${t.price}")
    }
}

/**---------泛型类---------**/

abstract class Color<T>(var t: T/*泛型字段*/) {
    abstract fun printColor()
}

class Blue {
    val color = "blue"
}

class BlueColor(t: Blue) : Color<Blue>(t) {
    override fun printColor() {
        println("color:${t.color}")
    }

}

/**---------泛型方法---------**/
fun <T> fromJson(json: String, tClass: Class<T>): T? {
    /*获取T的实例*/
    val t: T? = tClass.newInstance()
    return t
}

/**---------泛型约束---------**/

fun <T : Comparable<T>?> sort(list: List<T>?){}

fun test12() {
    sort(listOf(1, 2, 3)) // OK，Int 是 Comparable<Int> 的子类型
//    sort(listOf(Blue())) // 错误：Blue 不是 Comparable<Blue> 的子类型
}

//多个上界的情况
fun <T> test(list: List<T>, threshold: T): List<T>
        where T : CharSequence,
              T : Comparable<T> {
    return list.filter { it > threshold }.map { it }
}

/**---------泛型中的out与in---------**/

fun sumOfList(list: List<out Number>): Number {
    var result = 0f
    for (v in list) {
        result += v.toFloat()
    }
    return result
}

fun testSum() {
    val result = sumOfList(listOf(1.5, 2, 3, 5.5))
    println("sumOfList:$result")
}

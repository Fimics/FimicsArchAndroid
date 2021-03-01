package org.devio.`as`.hi.kotlin_dmeo

import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi

/**
 * Kotlin方法与Lambda表达式
 */
fun main() {
//    println("functionLearn：${functionLearn(101)}")
//    testVarargs()
//    println("magic:${magic()}")
    testClosure(1)(2) {
        println(it)
    }
    testDeco()

    val list = listOf(1, 2, 3)
    val result = list.sum {
        println(it)
    }
    println("计算结果：${result}")

    val listString = listOf("1", "2", "3")
    val result2 = listString.toIntSum()(2)
    println("计算结果：${result2}")
}

/**---------方法声明---------**/
fun functionLearn(days: Int): Boolean {
    Person().test1()
    Person.test2()
    println("NumUtil.double(2):${NumUtil.double(2)}")
    println("double(3):${double(3)}")
    return days > 100
}


class Person {
    /**
     * 成员方法
     */
    fun test1() {
        println("成员方法")
    }

    /**
     * 类方法：Kotlin中并没有static关键字，不过我们可以借助companion object 来实现类方法的目的
     */
    companion object {
        fun test2() {
            println("companion object 实现的类方法")
        }
    }
}

/**
 * 整个静态类
 */
object NumUtil {
    fun double(num: Int): Int {
        return num * 2
    }
}
/**
 * 全局静态直接新建一个 Kotlin file 然后定义一些常量、方法
 */

/**
 * 单表达式方法，当方法返回单个表达式时，可以省略花括号并且在 = 符号之后指定代码体即可：
 */
fun double(x: Int): Int = x * 2

/**
 * 当返回值类型可由编译器推断时，显式声明返回类型是可选的：
 */

fun double2(x: Int) = x * 2

/**---------方法参数---------**/

/**
 * 默认值，方法参数可以有默认值，当省略相应的参数时使用默认值。与其Java相比，这可以减少重载数量：
 */
fun read(b: Array<Byte>, off: Int = 0, len: Int = b.size) { /*……*/
}

/**
 * 如果一个默认参数在一个无默认值的参数之前，那么该默认值只能通过使用具名参数调用该方法来使用：
 */
fun foo(bar: Int = 0, baz: Int) {}

fun defaultValue() {
    foo(baz = 1) // 使用默认值 bar = 0
    foo(1) { println("hello") }// 使用默认值 baz = 1
    foo(qux = { println("hello") })// 使用两个默认值 bar = 0 与 baz = 1
    foo { println("hello") }
}

/**
 * 如果在默认参数之后的最后一个参数是 lambda 表达式，那么它既可以作为具名参数在括号内传入，也可以在括号外传入
 */
fun foo(bar: Int = 0, baz: Int = 1, qux: () -> Unit) {} // 使用两个默认值 bar = 0 与 baz = 1

/**
 * 可变数量的参数（Varargs）
 */

fun append(vararg str: Char): String {
    val result = StringBuffer()
    for (char in str) {
        result.append(char)
    }
    return result.toString()
}

fun testVarargs() {
    println(append('h', 'e', 'l', 'l', 'o'))
    val world = charArrayOf('w', 'o', 'r', 'l', 'd')
    val result = append('h', 'e', 'l', 'l', 'o', ' ', *world)
    println(result)
}

/**---------方法作用域---------**/

/**
 * 局部方法
 */
fun magic(): Int {
    fun foo(v: Int): Int {
        return v * v
    }

    val v1 = (0..100).random()

    return foo(v1)
}
/**---------高级特性---------**/


/**
 * 高阶函数-函数作为参数
 **/
//这是一个求List<Int>元素和的扩展，它因为接受一个函数类型的callback参数而被成为高阶函数
fun List<Int>.sum(callback: (Int) -> Unit): Int {
    var result = 0
    for (v in this) {
        result += v
        callback(v)
    }
    return result;
}

/**
 * 高阶函数-函数作为返回值
 **/
//这是一个求List<String>元素和的扩展，它返回一个(scale: Int) -> Float 函数而被成为高阶函数
fun List<String>.toIntSum(): (scale: Int) -> Float {
    println("第一层函数")
    return fun(scale): Float {
        var result = 0f
        for (v in this) {
            result += v.toInt() * scale
        }
        return result
    }
}


/**
 * 闭包，testClosure接收一个Int类型的参数，返回一个带有如下参数的方法`(Int, (Int) -> Unit)`
 * ，该方法第一个参数是Int类型，第二个参数是一个接收Int类型参数的方法;
 * testClosure也是高阶方法
 */
fun testClosure(v1: Int): (v2: Int, (Int) -> Unit) -> Unit {
    return fun(v2: Int, printer: (Int) -> Unit) {//匿名方法
        printer(v1 + v2)
    }
    //难度较高，慢慢理解，参考lambda
}

/**
 * 解构声明
 */

data class Result(val msg: String, val code: Int)

fun testDeco() {
    var result = Result("success", 0)
    result = result()
    val (msg, code) = result
    println("msg:${msg}")
    println("code:${code}")
}

fun result(): Result {
    return Result("good", 1)
}


/**
 * 方法字面值
 */
//定义了一个变量 tmp，而该变量的类型就是 (Int) -> Boolean
var tmp: ((Int) -> Boolean)? = null

fun literal() {
    // { num -> (num > 10) }即是一个方法字面值
    tmp = { num -> (num > 10) }
}

/**---------Lambda表达式---------**/

fun bindListener(view: View) {
    view.setOnClickListener { v ->
        Toast.makeText(v.context, "Lambda简洁之道", Toast.LENGTH_LONG).show()
    }
}

/**
 * 无参数的情况
 */
// 原方法
fun test() {
    println("无参数")
}

// lambda代码
val test1 = { println("无参数") }


/**
 * 有参数的情况,这里举例一个两个参数的例子，目的只为大家演示
 */

// 源代码
fun test2(a: Int, b: Int): Int {
    return a + b
}

// lambda
val test3: (Int, Int) -> Int = { a, b -> a + b }

// 或者
val test4 = { a: Int, b: Int -> a + b }


/**
 * lambda表达式作为方法中的参数的时候
 */

// 源代码
fun test(a: Int, b: Int): Int {
    return a + b
}

fun sum(num1: Int, num2: Int): Int {
    return num1 + num2
}

// lambda
fun test(a: Int, b: (num1: Int, num2: Int) -> Int): Int {
    return a + b(3, 5)
}

/**---------Lambda实践---------**/
//如何使用it
fun test1() {
    // 这里举例一个语言自带的一个高阶方法filter,此方法的作用是过滤掉不满足条件的值。
    val arr = arrayOf(1, 3, 5, 7, 9)
    // 过滤掉数组中元素小于2的元素，取其第一个打印。这里的it就表示每一个元素。
    println(arr.filter { it < 5 }.component1())

    //结合上文的
    testClosure(1)(2) {
        println(it)
    }
}

//如何使用_
@RequiresApi(Build.VERSION_CODES.N)
fun test2() {
    val map = mapOf("key1" to "value1", "key2" to "value2", "key3" to "value3")
    map.forEach { (key, value) ->
        println("$key \t $value")
    }

    // 不需要key的时候
    map.forEach { (_, value) ->
        println(value)
    }
}

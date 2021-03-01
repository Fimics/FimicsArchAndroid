package org.devio.`as`.hi.kotlin_dmeo

import android.view.View
import java.util.*

fun main() {
//    InitDemo("test")
//    Animal(22)// Kotlin 中没有“new”关键字
    testDataUtil()
}
/**---------构造方法---------**/
/**
 * 主构造方法
 */
class KotlinClass constructor(name: String) { /*……*/ }

//没有任何注解或者可见性修饰符，可以省略这个 constructor 关键字
class KotlinClass1(name: String) { /*……*/ }

class InitDemo(name: String) {
    /**
     * 关于：.also(::println)的理解
     * ::表示创建成员引用或类引用，通过它来传递println方法来作为also的参数
     * 那also又是什么呢？源码跟进 -> Kotlin扩展
     */
    val firstProp = "First Prop: $name".also(::println)

    init {
        println("First initializer block that prints $name")
    }

    val secondProp = "Second Prop: ${name.length}".also(::println)

    init {
        println("Second initializer block that prints ${name.length}")
    }
}

//构造方法的参数作为类的属性并赋值，KotlinClass2在初始化时它的name与score属性会被赋值
class KotlinClass2(val name: String, var score: Int) { /*……*/ }

/**
 * 次构造方法
 */

class KotlinClass3(val view: View) {
    var views: MutableList<View> = mutableListOf()

    init {//初始化代码块会在次构造方法之前执行
        println("init")
    }

    constructor(view: View, index: Int) : this(view) {
        views.add(view)
        println("constructor")
    }
}

/**---------继承与覆盖---------**/
open class Animal(age: Int) {
    init {
        println(age)
    }

    open val foot: Int = 0
    open fun eat() {

    }
}

/**
 * 继承
 */
//派生类有主构造方法的情况
class Dog(age: Int) : Animal(age) {
    override val foot = 4
    override fun eat() {
    }
}

//派生类无柱构造方法的情况
class Cat : Animal {
    constructor(age: Int) : super(age)
}

/**---------属性的声明---------**/
class Shop {
    var name: String = "Android"
    var address: String? = null
    val isClose: Boolean
        get() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 11
    var score: Float = 0.0f
        get() = if (field < 0.2f) 0.2f else field * 1.5f
        set(value) {
            println(value)
        }
}

fun copyShop(shop: Shop): Shop {
    val shop = Shop()
    shop.name = shop.name
    shop.address = shop.address
    // ……
    return shop
}

/**---------延迟初始化属性与变量---------**/
class Test {
    lateinit var shop: Shop
    fun setup() {
        shop = Shop()
    }

    fun test() {
        if (::shop.isInitialized)
            println(shop.address)
    }
}

/**---------抽象类---------**/

abstract class Printer {
    abstract fun print()
}

class FilePrinter : Printer() {
    override fun print() {
    }
}

/**---------接口---------**/

interface Study {
    var time: Int// 抽象的
    fun discuss()
    fun earningCourses() {
        println("Android 架构师")
    }
}

//在主构造方法中覆盖接口的字段
class StudyAS(override var time: Int) : Study {
    //在类体中覆盖接口的字段
    //    override var time: Int = 0
    override fun discuss() {

    }
}

/**---------解决覆盖冲突---------**/

interface A {
    fun foo() {
        println("A")
    }
}

interface B {
    fun foo() {
        print("B")
    }
}

class D : A, B {
    override fun foo() {
        super<A>.foo()
        super<B>.foo()
    }
}

/**---------数据类---------**/

data class Address(val name: String, val number: Int) {
    var city: String = ""
    fun print() {
        println(city)
    }
}

fun testAddress() {
    val address = Address("Android", 1000)
    address.city = "Beijing"
    val (name, city) = address
    println("name:$name city:$city")
}


/**---------对象表达式---------**/
open class Address2(name: String) {
    open fun print() {

    }
}

class Shop2 {
    var address: Address2? = null
    fun addAddress(address: Address2) {
        this.address = address
    }

    // 私有方法，所以其返回类型是匿名对象类型
    private fun foo() = object {
        val x: String = "x"
    }

    // 公有方法，所以其返回类型是 Any
    fun publicFoo() = object {
        val x: String = "x"
    }

    fun bar() {
        val x1 = foo().x        // 没问题
//        val x2 = publicFoo().x  // 错误：未能解析的引用“x”
    }
}

fun test3() {
    //如果超类型有一个构造方法，则必须传递适当的构造方法参数给它
    Shop2().addAddress(object : Address2("Android") {
        override fun print() {
            super.print()
        }
    })
}

//只需要一个简单对象

fun foo() {
    val point = object {
        var x: Int = 0
        var y: Int = 0
    }
    print(point.x + point.y)
}

/**---------对象声明---------**/
object DataUtil {
    fun <T> isEmpty(list: ArrayList<T>?): Boolean {
        return list?.isEmpty() ?: false
    }
}

fun testDataUtil() {
    val list = arrayListOf("1")
    println(DataUtil.isEmpty(list))
}

/**---------伴生对象---------**/
class Student(val name: String) {
    companion object {
        val student = Student("Android")
        fun study() {
            println("Android 架构师")
        }
    }

    var age = 16
    fun printName() {
        println("My name is $name")
    }
}

fun testStudent() {
    println(Student.student)
    Student.study()
}

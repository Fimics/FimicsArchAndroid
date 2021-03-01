package org.devio.`as`.hi.kotlin_dmeo

import kotlin.math.abs

fun main() {
    println("---main--- ")
//    baseType()

//    arrayType()
    collectionType()
//    collectionSort()
}

fun baseType() {
    val num1 = -1.68 //Double
    val num2 = 2 //Int

    val num3 = 2f //Float
    val int1 = 3
    println("num1:$num1 num2:$num2 num3:$num3 int1:$int1")

    println(abs(num1))

    println(num1.toInt())//转换成Int

    printType(num1)
    printType(num2)
    printType(num3)
    printType(int1)
}

fun printType(param: Any) {
    println("$param is ${param::class.simpleName} type")
}

/**
 * 数组
 */
fun arrayType() {
    //arrayOf
    val array = arrayOf(1, 2, 3)

    //arrayOfNulls
    val array1 = arrayOfNulls<Int>(3)
    array1[0] = 4
    array1[1] = 5
    array1[2] = 6

    //Array(5)的构造函数
    val array2 = Array(5) { i -> (i * i).toString() }

//    intArrayOf(),doubleArrayOf()
    val x: IntArray = intArrayOf(1, 2, 3)
    println("x[0] + x[1] = ${x[0] + x[1]}")

    // 大小为 5、值为 [0, 0, 0, 0, 0] 的整型数组
    val array3 = IntArray(5)

    // 例如：用常量初始化数组中的值
    // 大小为 5、值为 [42, 42, 42, 42, 42] 的整型数组
    val array4 = IntArray(5) { 42 }

    // 例如：使用 lambda 表达式初始化数组中的值
    // 大小为 5、值为 [0, 1, 2, 3, 4] 的整型数组（值初始化为其索引值）
    val array5 = IntArray(5) { it * 1 }

    println(array5[4])

    /****遍历数组的常用5中方式****/
    //数组遍历
    for (item in array) {
        println(item)
    }

    //带索引遍历数组
    for (i in array.indices) {
        println("$i -> ${array[i]}")
    }

    // 遍历元素(带索引)
    for ((index, item) in array.withIndex()) {
        println("$index -> $item ")
    }
    //forEach遍历数组
    array.forEach { println(it) }

    // forEach增强版
    array.forEachIndexed { index, item ->
        println("$index -> $item ")
    }
}

/**
 * 集合
 */
fun collectionType() {
    //不可变集合
    val stringList = listOf("one", "two", "one")
    println(stringList)

    val stringSet = setOf("one", "two", "one")
    println(stringSet)

    //可变集合

    val numbers = mutableListOf(1, 2, 3, 4)
    numbers.add(5)
    numbers.removeAt(1)
    numbers[0] = 0
    println(numbers)


    val hello = mutableSetOf("H", "e", "l", "l", "o")//自动过滤重复元素
    hello.remove("o")
    println(hello)

//集合的加减操作
    hello += setOf("w", "o", "r", "l", "d")
    println(hello)
    /**Map<K, V> 不是 Collection 接口的继承者；但是它也是 Kotlin 的一种集合类型**/
    val numberMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 4, "key5" to 5)
    println("All keys:${numberMap.keys}")
    println("All values:${numberMap.values}")

    if ("key2" in numberMap) println("Value by key key2:${numberMap["key2"]}")
    if (1 in numberMap.values) println("1 is in the map")
    if (numberMap.containsValue(1)) println("1 is in the map")

    /**
     * Q1：两个具有相同键值对，但顺序不同的map相等吗？为什么？
     * 无论键值对的顺序如何，包含相同键值对的两个 Map 是相等的
     */
    val anotherMap = mapOf("key2" to 2, "key1" to 1, "key3" to 3, "key4" to 4, "key5" to 5)
    println("anotherMap == numberMap:${anotherMap == numberMap}")
    anotherMap.equals(numberMap)
    /**
     * Q2：两个具有相同元素，但顺序不同的list相等吗？为什么？
     */
}

/**
 * 集合排序
 */
fun collectionSort() {
    val number3 = mutableListOf(1, 2, 3, 4)
    //随机排序
    number3.shuffle()
    println(number3)

    number3.sort()//从小到大
    number3.sortDescending()//从大到小
    println(number3)

    //条件排序

    data class Language(var name: String, var score: Int)

    val languageList: MutableList<Language> = mutableListOf()
    languageList.add(Language("Java", 80))
    languageList.add(Language("Kotlin", 90))
    languageList.add(Language("Dart", 99))
    languageList.add(Language("C", 80))
    //使用sortBy进行排序，适合单条件排序
    languageList.sortBy { it.score }
    println(languageList)

    //使用sortWith进行排序，适合多条件排序
    languageList.sortWith(compareBy({
        //it变量是lambda中的隐式参数
        it.score
    }, { it.name }))
    println(languageList)
}
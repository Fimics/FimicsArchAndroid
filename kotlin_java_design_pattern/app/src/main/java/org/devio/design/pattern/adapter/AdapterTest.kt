package org.devio.design.pattern.adapter

fun main() {
    //类适配器模式
    val adapterClass = AdapterClass()
    adapterClass.apply {
        request1()
        request2()
    }

    //对象配器模式
    val adaptee = Adaptee()
    val adapterObj = AdapterObj(adaptee)
    adapterObj.apply {
        request1()
        request2()
    }
}
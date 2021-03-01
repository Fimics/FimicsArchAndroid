package org.devio.design.pattern.adapter

/**
 * 对象适配器模式
 */
class AdapterObj(private var adaptee: Adaptee) : Target {
    override fun request1() {
        adaptee.request1()
    }

    override fun request2() {
        println("AdapterObj:request2()")
    }
}
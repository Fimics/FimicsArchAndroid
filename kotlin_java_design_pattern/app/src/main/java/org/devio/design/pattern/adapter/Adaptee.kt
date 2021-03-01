package org.devio.design.pattern.adapter

/**
 * 现在需要适配的接口(适配者类)
 */
open class Adaptee {
    fun request1() {
        println("Adaptee:request1")
    }
}
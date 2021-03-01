package org.devio.design.pattern.adapter

/**
 * 类适配器模式
 */
class AdapterClass : Adaptee(), Target {
    /**
     * 由于源类Adaptee没有方法request2()
     * 因此适配器需要补充上这个方法
     */
    override fun request2() {
        println("AdapterClass:request2")
    }

}
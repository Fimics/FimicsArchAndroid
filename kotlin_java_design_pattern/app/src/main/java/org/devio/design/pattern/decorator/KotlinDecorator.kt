package org.devio.design.pattern.decorator

fun Panda.bamboo(decorator: () -> Unit) {
    eat()
    println("可以吃竹子")
    decorator()
}

fun Panda.carrot(decorator: () -> Unit) {
    println("可以吃胡萝卜")
    decorator()
}

fun main() {
    Panda().run {
        bamboo {
            carrot {
            }
        }
    }
}
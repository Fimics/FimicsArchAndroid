package org.devio.design.pattern.builder

fun main() {
    val penJava = PenJava.Builder().color("yellow").width(3f).round(true).build()
    penJava.write()

    val pen = Pen()
    //通过with来实现
    with(pen, {
        color = "red"
        width = 2f
        round = true
    })
    pen.write()

    //通过apply来实现
    pen.apply {
        color = "gray"
        width = 6f
        round = false
        write()
    }
}
package org.devio.design.pattern.builder

class Pen {
    var color: String = "white"
    var width: Float = 1.0f
    var round: Boolean = false
    fun write() {
        println("color:${color},width:${width},round:${round}")
    }
}
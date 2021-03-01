package org.devio.`as`.hi.kotlin_dmeo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val textView = find<TextView>(R.id.test)
        R.id.test.onClick(this) {
            test.text = "Kotlin扩展"
        }

    }

    fun test(str: String?) {
        if (str.isNullOrEmpty()) {
            println("str.isNullOrEmpty():${str.isNullOrEmpty()}")
        } else if (str.isNullOrBlank()) {
            println("str.isNullOrBlank():${str.isNullOrBlank()}")
        }
    }
}

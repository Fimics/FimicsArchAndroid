package com.peace.example.event

import android.util.Log
import android.widget.Toast
import com.peace.hybrid.event.WebEvent

class ToastEvent : WebEvent() {

    override fun execute(params: String?): String? {
        Log.d("ToastEvent",params.toString())
        Toast.makeText(context, params, Toast.LENGTH_SHORT).show()
        return "结果"
    }
}
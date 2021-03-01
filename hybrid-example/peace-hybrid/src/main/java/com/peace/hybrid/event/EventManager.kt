package com.peace.hybrid.event

import java.util.*

/**
 * @author 傅令杰
 */
class EventManager private constructor() {

    companion object {
        private val eventMap = HashMap<String, WebEvent>()

        val instance: EventManager
            get() = Holder.instance
    }

    private object Holder {
        val instance = EventManager()
    }

    fun addEvent(name: String, event: WebEvent) {
        eventMap[name] = event
    }

    fun getEvent(action: String): WebEvent {
        return eventMap[action] ?: return UndefineEvent()
    }
}

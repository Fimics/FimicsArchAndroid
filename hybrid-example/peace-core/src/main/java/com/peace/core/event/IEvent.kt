package com.peace.core.event

/**
 * @author 傅令杰
 */
interface IEvent<T> {

    fun execute(params: T): T
}

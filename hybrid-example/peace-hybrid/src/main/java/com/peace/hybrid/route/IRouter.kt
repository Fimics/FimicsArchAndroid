package com.peace.hybrid.route

import com.peace.hybrid.IWebContainer

/**
 * @author 傅令杰
 */
interface IRouter {

    fun nextContainer(next: IWebContainer): Boolean
    
    fun load(url: String): Boolean
}
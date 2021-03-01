package org.devio.`as`.proj.common.core

interface IHiBridge<P, Callback> {
    fun onBack(p: P?)
    fun goToNative(p: P)
    fun getHeaderParams(callback: Callback)
}
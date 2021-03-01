package com.peace.hybrid.event

import kotlinx.serialization.Serializable

@Serializable
data class WebParams(val action: String, val params: String? = null)
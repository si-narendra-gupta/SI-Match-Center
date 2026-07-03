package com.sportz.base.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("content")
    val content: T?,
    @SerialName("status")
    val status: Int?
)
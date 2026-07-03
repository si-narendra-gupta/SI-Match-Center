package com.sportz.si_matchcenter.data.model.lastsixball

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    @SerialName("type")
    val type: String? = null,
    @SerialName("value")
    val value: Int? = null
)

package com.sportz.si_matchcenter.data.model.spidergraph

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Shot(
    @SerialName("Angle") val angle: String?,
    @SerialName("Distance") val distance: String?,
    @SerialName("Id") val id: String?,
    @SerialName("Runs") val runs: String?,
    @SerialName("Zone") val zone: String?
)

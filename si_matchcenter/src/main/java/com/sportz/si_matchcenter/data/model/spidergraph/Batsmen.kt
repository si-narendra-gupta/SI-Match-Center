package com.sportz.si_matchcenter.data.model.spidergraph


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Batsmen(
    @SerialName("Batsman") val name: String?,
    @SerialName("Style") val style: String?,
    @SerialName("Shots") val shots: List<Shot>?,
)

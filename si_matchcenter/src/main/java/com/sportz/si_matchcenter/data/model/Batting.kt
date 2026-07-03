package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Batting(
    @SerialName("Average") val average: String? = null,
    @SerialName("Runs") val runs: String? = null,
    @SerialName("Balls") val balls: String? = null,
    @SerialName("Strikerate") val strikeRate: String? = null,
    @SerialName("Style") val style: String? = null
)

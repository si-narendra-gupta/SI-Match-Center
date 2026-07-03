package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bowling(
    @SerialName("Average") val average: String? = null,
    @SerialName("Economyrate") val economyRate: String? = null,
    @SerialName("Runsconceded") val runsConceded: String? = null,
    @SerialName("Overs") val overs: String? = null,
    @SerialName("Style") val style: String? = null,
    @SerialName("Wickets") val wickets: String? = null
)

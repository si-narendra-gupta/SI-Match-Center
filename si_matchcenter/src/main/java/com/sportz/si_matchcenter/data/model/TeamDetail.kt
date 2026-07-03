package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamDetail(
    @SerialName("Name_Full") val nameFull: String? = null,
    @SerialName("Name_Short") val nameShort: String? = null,
    @SerialName("Players") val players: Players? = null
)

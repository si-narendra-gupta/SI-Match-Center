package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Officials(
    @SerialName("Referee") val referee: String? = null,
    @SerialName("Referee_Id") val refereeId: String? = null,
    @SerialName("Umpire1_Id") val umpire1Id: String? = null,
    @SerialName("Umpire1_Name") val umpire1Name: String? = null,
    @SerialName("Umpire2_Id") val umpire2Id: String? = null,
    @SerialName("Umpire2_Name") val umpire2Name: String? = null,
    @SerialName("Umpire3_Id") val umpire3Id: String? = null,
    @SerialName("Umpire3_Name") val umpire3Name: String? = null,
    @SerialName("Umpires") val umpires: String? = null
)

package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FallOfWicket(
    @SerialName("Ball_number")
    val ballNumber: String?,
    @SerialName("Batsman")
    val batsman: String?,
    @SerialName("Overs")
    val overs: String?,
    @SerialName("Review_status_id")
    val reviewStatusId: Int?,
    @SerialName("Score")
    val score: String?,
    @SerialName("Wicket_No")
    val wicketNo: Int?
)
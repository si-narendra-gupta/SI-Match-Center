package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ManhattanResponse(
    @SerialName("Overbyover")
    val overByOver: List<OverByOver>? = null
)

@Serializable
data class OverByOver(
    @SerialName("Over")
    val over: Int? = null,
    @SerialName("Runs")
    val runs: String? = null,
    @SerialName("Wickets")
    val wickets: String? = null,
    @SerialName("Batsmen")
    val batsmen: Map<String, Batsman>? = null,
    @SerialName("Bowlers")
    val bowlers: Map<String, Bowler>? = null
)

@Serializable
data class Batsman(
    @SerialName("Batsman")
    val name: String? = null,
    @SerialName("Runs")
    val runs: String? = null,
    @SerialName("Isout")
    val isOut: Boolean? = false
)

@Serializable
data class Bowler(
    @SerialName("Bowler")
    val name: String? = null,
    @SerialName("Runs")
    val runs: String? = null
)

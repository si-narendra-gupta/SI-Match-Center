package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchCenterResponse(
    @SerialName("Innings") val innings: List<Inning>? = null,
    @SerialName("Superovers") val superOvers: List<SuperOverResponse>? = null,
    @SerialName("Matchdetail") val matchDetail: MatchDetail? = null,
    @SerialName("SerialNumber") val serialNumber: Int? = null,
    @SerialName("Teams") val teams: HashMap<String, TeamDetail>? = null
) {
    val superOverInnings: List<Inning>
        get() = superOvers?.flatMap { it.innings ?: emptyList() } ?: emptyList()

    val allInnings: List<Inning>
        get() = (innings ?: emptyList()) + superOverInnings
}


@Serializable
data class SuperOverResponse(
    @SerialName("Equation") val equation: String? = null,
    @SerialName("Number") val number: Int? = null,
    @SerialName("Winningteam") val winningTeam: String? = null,
    @SerialName("Winningteam_name") val winningTeamName: String? = null,
    @SerialName("Status") val status: String? = null,
    @SerialName("Result") val result: String? = null,
    @SerialName("Innings") val innings: List<Inning>? = null,
)

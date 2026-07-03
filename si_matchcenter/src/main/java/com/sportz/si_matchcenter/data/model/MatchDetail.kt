package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchDetail(
    @SerialName("Equation") val equation: String? = null,
    @SerialName("Lineup_status") val lineupStatus: Int? = null,
    @SerialName("Match") val match: IPLMatch? = null,
    @SerialName("Match_display_status") val matchDisplayStatus: String? = null,
    @SerialName("Officials") val officials: Officials? = null,
    @SerialName("Player_Match") val playerMatch: String? = null,
    @SerialName("Player_Match_Id") val playerMatchId: String? = null,
    @SerialName("Player_Match_TeamId") val playerMatchTeamId: String? = null,
    @SerialName("Raw_margin_value") val rawMarginValue: String? = null,
    @SerialName("Raw_matchresult") val rawMatchResult: String? = null,
    @SerialName("Raw_result_extras") val rawResultExtras: String? = null,
    @SerialName("Required_Rpb") val requiredRpb: String? = null,
    @SerialName("Required_Runrate") val requiredRunrate: String? = null,
    @SerialName("Result") val result: String? = null,
    @SerialName("Series") val series: Series? = null,
    @SerialName("Status") val status: String? = null,
    @SerialName("Prematch") val prematch: String? = null,
    @SerialName("Status_Id") val statusId: String? = null,
    @SerialName("Team_Away") val teamAway: String? = null,
    @SerialName("Team_Home") val teamHome: String? = null,
    @SerialName("Toss_elected_to") val tossElectedTo: String? = null,
    @SerialName("Tosswonby") val tossWonBy: String? = null,
    @SerialName("Venue") val venue: Venue? = null,
    @SerialName("Verification_Completed") val verificationCompleted: Boolean? = null,
    @SerialName("Weather") val weather: String? = null,
    @SerialName("Winmargin") val winMargin: String? = null,
    @SerialName("Winningteam") val winningTeam: String? = null
)

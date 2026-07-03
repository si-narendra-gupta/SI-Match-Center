package com.sportz.si_matchcenter.business.domain.model

import androidx.compose.runtime.Immutable
import java.io.Serializable

@Immutable
data class Participant(
    val id: String?,
    val name: String?,
    val shortName: String?,
    val teamImageUrl: String?,
    val highlight: Boolean?,
    // Live scores
    val runs: String? = null,
    val wickets: String? = null,
    val overs: String? = null,
    val runRate: String? = null,
    val showYetToBat: Boolean = false,
    val squad: List<TeamSquadPlayer> = emptyList(),
    val battingScoreBoard: List<TeamSquadPlayer?> = emptyList(),
    val bowlingScoreBoard: List<TeamSquadPlayer?> = emptyList(),
    val fallOfWicketPlayers: List<TeamSquadPlayer?> = emptyList(),
    val extraRuns: ExtraRuns? = null,
    val bowlingTeamId: String? = null,
    val bowlingTeamName: String? = null,
    val bowlingTeamShortName: String? = null,
    val bowlingTeamImageUrl: String? = null,
    val isSuperOver: Boolean = false,
    val superOverNumber: Int? = null
) : Serializable

package com.sportz.si_matchcenter.business.domain.model

import androidx.compose.runtime.Immutable
import java.io.Serializable

@Immutable
data class TeamSquadPlayer(
    val id: String?,
    val name: String?,
    val firstName: String?,
    val lastName: String?,
    val role: String?,
    val position: String?,
    val isCaptain: Boolean = false,
    val isKeeper: Boolean = false,
    val isImpactPlayer: Boolean = false,
    val iSConfirmXI: Boolean = false,
    val nationality: String?,
    val nationalityId: Int?,
    val battingStats: BattingStats? = null,
    val bowlingStats: BowlingStats? = null,
    val fallOfWicket: FallOfWicketDTO? = null,
    val playerInName: String? = null,
    val playerOutName: String? = null,
    val playerInId: String? = null,
    val playerOutId: String? = null,
) : Serializable

@Immutable
data class BattingStats(
    val runs: String? = null,
    val balls: String? = null,
    val fours: String? = null,
    val sixes: String? = null,
    val strikeRate: String? = null,
    val howOutShort: String? = null
) : Serializable

@Immutable
data class BowlingStats(
    val overs: String? = null,
    val maidens: String? = null,
    val runs: String? = null,
    val wickets: String? = null,
    val economyRate: String? = null
) : Serializable

@Immutable
data class FallOfWicketDTO(
    val score: String?, val wicketNo: String?, val overs: String?
) : Serializable

@Immutable
data class ExtraRuns(
    val byes: String?,
    val legByes: String?,
    val wides: String?,
    val noBalls: String?,
    val penalty: String?,
) : Serializable {
    val totalExtraRuns: Int
        get() = (byes?.toIntOrNull() ?: 0) + (legByes?.toIntOrNull() ?: 0) + (wides?.toIntOrNull()
            ?: 0) + (noBalls?.toIntOrNull() ?: 0) + (penalty?.toIntOrNull() ?: 0)

    val displayText: String
        get() = "$totalExtraRuns Runs (b ${byes ?: "0"}, " +
                "lb ${legByes ?: "0"}, " +
                "w ${wides ?: "0"}, " +
                "nb ${noBalls ?: "0"}, " +
                "p ${penalty ?: "0"})"
}

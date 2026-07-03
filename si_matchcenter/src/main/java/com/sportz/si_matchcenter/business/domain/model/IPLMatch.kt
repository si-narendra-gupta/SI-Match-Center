package com.sportz.si_matchcenter.business.domain.model

import androidx.compose.runtime.Immutable
import java.io.Serializable

@Immutable
data class IPLMatch(
    val matchId: String?,
    val matchGameId: String?,
    val teamNameAvsTeamNameB: String?,
    val matchNo: String?,
    val seriesName: String?,
    val beautifiedStartDate: String?,
    val startDate: String?,
    val time: String?,
    val eventState: EventState,
    val venueName: String?,
    val matchDisplayStatus: String?,
    val eventStatus: String?,
    val eventSubStatus: String?,
    val eventStatusId: String?,
    val homeTeamId: String? = null,
    val awayTeamId: String? = null,
    val participants: List<Participant>?,
    val playerOfTheMatch: String? = null,
    val topBatters: List<TopPlayer>? = null,
    val topBowlers: List<TopPlayer>? = null,
    val weather: String? = null,
    val toss: String? = null,
    val umpires: String? = null,
    val referee: String? = null,
    val totalInnings: Int = 0, // inning + super over innings
    val playedInnings: Int = 0,
    val innings: List<Inning>? = null,
    val customMetaData: CustomMetaData? = null,
) : Serializable {

    fun getInningByIndex(index: Int): Inning? {
        return innings?.find { it.apiInningIndex == index }
    }

    val currentInning: Inning?
        get() = innings?.lastOrNull()

    val isLive: Boolean
        get() = eventState == EventState.LIVE

    val isResult: Boolean
        get() = eventState == EventState.RESULT
}

@Immutable
data class TopPlayer(
    val name: String,
    val runs: String? = null,
    val balls: String? = null,
    val wickets: String? = null,
    val overs: String? = null,
    val isCurrent: Boolean = false
) : Serializable

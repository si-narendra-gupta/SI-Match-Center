package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IPLMatch(
    @SerialName("Code") val code: String? = null,
    @SerialName("Comp_type_id") val compTypeId: String? = null,
    @SerialName("Coverage_level") val coverageLevel: String? = null,
    @SerialName("Coverage_level_id") val coverageLevelId: String? = null,
    @SerialName("Date") val date: String? = null,
    @SerialName("Daynight") val dayNight: String? = null,
    @SerialName("End_date") val endDate: String? = null,
    @SerialName("End_time") val endTime: String? = null,
    @SerialName("Group") val group: String? = null,
    @SerialName("Id") val id: String? = null,
    @SerialName("League") val league: String? = null,
    @SerialName("League_Id") val leagueId: String? = null,
    @SerialName("Live") val live: Boolean? = null,
    @SerialName("Livecoverage") val liveCoverage: String? = null,
    @SerialName("Match_ordinal") val matchOrdinal: String? = null,
    @SerialName("Number") val number: String? = null,
    @SerialName("Offset") val offset: String? = null,
    @SerialName("Recent") val recent: Boolean? = null,
    @SerialName("Revised_time_utc") val revisedTimeUtc: String? = null,
    @SerialName("StartDate") val startDate: String? = null,
    @SerialName("Time") val time: String? = null,
    @SerialName("Type") val type: String? = null,
    @SerialName("Upcoming") val upcoming: Boolean? = null,
    @SerialName("first_ball_bowled") val firstBallBowled: Boolean? = null,
    @SerialName("is_provisional_date") val isProvisionalDate: Boolean? = null,
    @SerialName("is_provisional_time") val isProvisionalTime: Boolean? = null,
    @SerialName("is_rescheduled") val isRescheduled: Boolean? = null,
    @SerialName("match_in_progress") val matchInProgress: Boolean? = null
)

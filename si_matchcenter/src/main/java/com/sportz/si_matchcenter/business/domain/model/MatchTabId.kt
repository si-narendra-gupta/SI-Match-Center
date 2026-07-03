package com.sportz.si_matchcenter.business.domain.model

enum class MatchTabId(val value: String) {
    COMMENTARY("commentary"),
    SCORECARD("scorecard"),
    TEAMS("teams"),
    GRAPHS("graphs"),
    MATCH_INFO("match_info");

    companion object {
        fun fromValue(value: String?): MatchTabId? {
            return entries.find { it.value == value }
        }
    }
}

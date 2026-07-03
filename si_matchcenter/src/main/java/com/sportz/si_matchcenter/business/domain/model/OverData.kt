package com.sportz.si_matchcenter.business.domain.model

data class OverData(
    val overNumber: Int,
    val team1Runs: Int,
    val team1Wickets: Int,
    val team2Runs: Int,
    val team2Wickets: Int,
    val isTeam1Over: Boolean = false,
    val isTeam2Over: Boolean = false
)

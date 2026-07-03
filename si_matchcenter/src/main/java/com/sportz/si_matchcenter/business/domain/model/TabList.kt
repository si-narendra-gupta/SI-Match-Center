package com.sportz.si_matchcenter.business.domain.model

data class TabList(
    val live: List<MatchTabItem?>?,
    val result: List<MatchTabItem?>?,
    val upcoming: List<MatchTabItem?>?
)
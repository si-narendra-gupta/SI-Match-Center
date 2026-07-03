package com.sportz.si_matchcenter.business.domain.model.themecolor

data class CustomTabDTO(
    val commentary_card: CommentaryCard?,
    val screen: Screen?,
    val sub_tab: SubTab?,
    val tab: Tab?,
    val match_info: MatchInfo?,
    val teams: PlayingTeam?,
    val scorecard: Scorecard?,
    val graph : GraphColor?
)
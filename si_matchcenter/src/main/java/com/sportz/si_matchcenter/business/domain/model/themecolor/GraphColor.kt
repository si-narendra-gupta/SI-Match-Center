package com.sportz.si_matchcenter.business.domain.model.themecolor

data class GraphColor(
    val active_arrow: String?,
    val inactive_arrow: String?,
    val data_point: String?,
    val first_team_bar_indicator: String?,
    val overs: String?,
    val runs: String?,
    val second_team_bar_indicator: String?,
    val team_name: String?,
    val wicket_background: String?,
    val wicket_text: String?,
    val tab: Tab?,
    val spider: SpiderColor?
)
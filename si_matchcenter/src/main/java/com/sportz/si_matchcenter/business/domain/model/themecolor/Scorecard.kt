package com.sportz.si_matchcenter.business.domain.model.themecolor

data class Scorecard(
    val inning_team: InningTeam?,
    val impact_player: ImpactPlayer?,
    val extra_total: ExtraTotal?,
    val fall_of_wicket: FallOfWicket?
)
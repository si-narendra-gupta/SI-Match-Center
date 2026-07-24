package com.sportz.si_matchcenter.data.remote

import com.sportz.match_center_base.helper.MatchCenterBaseConfigContract
import com.sportz.si_matchcenter.business.domain.model.MatchTabList

interface SiFeedFixtureConfigContractMatchCenter : MatchCenterBaseConfigContract{
    fun getTeamLogo(
        teamId: String
    ): String
    fun getCurrentTeamId(): String
    fun getMatchCenterUrl(gameId: String): String
    fun getBallByBallUrl(matchId: String, inning: Int): String
    fun getManhattanUrl(gameId: String, inning: Int): String
    fun getSpiderUrl(gameId: String, inning: Int): String
    fun getMatchTabs(): MatchTabList? = null
}

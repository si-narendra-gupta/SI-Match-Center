package com.sportz.si_matchcenter.data.remote

import android.content.res.Resources
import com.sportz.si_matchcenter.business.domain.model.themecolor.MatchCenterThemeColors
import com.sportz.si_matchcenter.business.domain.model.MatchTabList

interface SiFeedFixtureConfigContract {

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

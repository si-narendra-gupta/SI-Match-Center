package com.sportz.si_matchcenter.business.repository

import com.sportz.match_center_base.helper.Result
import com.sportz.si_matchcenter.business.domain.model.CustomMetaData
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.business.domain.model.IPLMatch

interface MatchCenterRepository {
    suspend fun getMatchListing(gameID: String): Result<IPLMatch>
    suspend fun getWallStream(matchId: String, eventState: EventState, inning: Int = 1): Result<CustomMetaData?>
    suspend fun getManhattanData(gameID: String, inning: Int): Result<com.sportz.si_matchcenter.data.model.ManhattanResponse>
    suspend fun getSpiderGraph(gameID: String, inning: Int): Result<com.sportz.si_matchcenter.business.domain.model.SpiderZone>
}

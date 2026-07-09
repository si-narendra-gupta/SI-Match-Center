package com.sportz.si_matchcenter.data.service

import com.sportz.base.helper.KtorServiceHelper
import com.sportz.base.helper.toApiResult
import com.sportz.si_matchcenter.data.model.MatchCenterResponse
import com.sportz.si_matchcenter.data.model.lastsixball.CommentaryDataModel
class MatchCenterService(
    private val ktorServiceHelper: KtorServiceHelper
) {
    suspend fun getMatchListing(url: String) =
        ktorServiceHelper.getApiCall<MatchCenterResponse>(url).toApiResult()

    suspend fun getWallStream(url: String) =
        ktorServiceHelper.getApiCall<CommentaryDataModel>(url).toApiResult()

    suspend fun getManhattanData(url: String) =
        ktorServiceHelper.getApiCall<com.sportz.si_matchcenter.data.model.ManhattanResponse>(url).toApiResult()

    suspend fun getSpiderGraph(url: String) =
        ktorServiceHelper.getApiCall<com.sportz.si_matchcenter.data.model.spidergraph.SpiderGraph>(url).toApiResult()
}

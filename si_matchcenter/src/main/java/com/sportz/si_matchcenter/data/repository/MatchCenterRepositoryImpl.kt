package com.sportz.si_matchcenter.data.repository

import com.sportz.base.helper.Result
import com.sportz.base.helper.mapToDomainResult
import com.sportz.si_matchcenter.business.domain.model.CustomMetaData
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.business.domain.model.IPLMatch
import com.sportz.si_matchcenter.business.repository.MatchCenterRepository
import com.sportz.si_matchcenter.data.mapper.MatchCenterMapper
import com.sportz.si_matchcenter.data.mapper.SpiderGraphMapper
import com.sportz.si_matchcenter.data.mapper.CommentaryMapper
import com.sportz.si_matchcenter.data.remote.SiFeedFixtureConfigContract
import com.sportz.si_matchcenter.data.service.MatchCenterService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
class MatchCenterRepositoryImpl(
    private val matchCenterService: MatchCenterService,
    private val matchCenterMapper: MatchCenterMapper,
    private val spiderGraphMapper: SpiderGraphMapper,
    private val commentaryMapper: CommentaryMapper,
    val siFeedFixtureConfigContract: SiFeedFixtureConfigContract
) : MatchCenterRepository {

    override suspend fun getMatchListing(gameID: String): Result<IPLMatch> = coroutineScope {
        val response = matchCenterService.getMatchListing(
            url = siFeedFixtureConfigContract.getMatchCenterUrl(gameID)
        )
        val matchResult = response.mapToDomainResult(
            transform = { matchCenterMapper.toDomain(it) })

        if (matchResult is Result.Success<IPLMatch>) {
            val match = matchResult.data
            val matchId = match.matchId
            if (!matchId.isNullOrEmpty()) {
                val lastInningIndex = match.innings?.lastOrNull()?.apiInningIndex ?: 1
                val wallStreamDeferred = async {
                    getWallStream(matchId, match.eventState, lastInningIndex)
                }

                val wallStreamResult = wallStreamDeferred.await()
                if (wallStreamResult is Result.Success<CustomMetaData?>) {
                    return@coroutineScope Result.Success(match.copy(customMetaData = wallStreamResult.data))
                }
            }
        }
        matchResult
    }

    override suspend fun getWallStream(
        matchId: String, eventState: EventState, inning: Int
    ): Result<CustomMetaData?> =
        matchCenterService.getWallStream(siFeedFixtureConfigContract.getBallByBallUrl(matchId, inning))
            .mapToDomainResult(
                transform = { response -> commentaryMapper.toDomain(response, eventState) }
            )

    override suspend fun getManhattanData(
        gameID: String,
        inning: Int
    ): Result<com.sportz.si_matchcenter.data.model.ManhattanResponse> =
        matchCenterService.getManhattanData(siFeedFixtureConfigContract.getManhattanUrl(gameID, inning))
            .mapToDomainResult(transform = { it })

    override suspend fun getSpiderGraph(
        gameID: String,
        inning: Int
    ): Result<com.sportz.si_matchcenter.business.domain.model.SpiderZone> =
        matchCenterService.getSpiderGraph(siFeedFixtureConfigContract.getSpiderUrl(gameID, inning))
            .mapToDomainResult(transform = { spiderGraphMapper.toDomain(it) })
}

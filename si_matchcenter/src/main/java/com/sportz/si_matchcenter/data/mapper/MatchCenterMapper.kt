package com.sportz.si_matchcenter.data.mapper

import com.sportz.base.helper.EntityMapper
import com.sportz.base.utils.CalendarUtils
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.business.domain.model.IPLMatch
import com.sportz.si_matchcenter.business.domain.model.Inning
import com.sportz.si_matchcenter.data.model.MatchCenterResponse
class MatchCenterMapper(
    private val playerMapper: PlayerMapper,
    private val participantMapper: ParticipantMapper
) : EntityMapper<MatchCenterResponse, IPLMatch> {

    override fun toDomain(entity: MatchCenterResponse): IPLMatch {
        val matchDetail = entity.matchDetail
        val matchData = matchDetail?.match
        val teamHomeId = matchDetail?.teamHome
        val teamAwayId = matchDetail?.teamAway
        val eventState = getEventState(matchData, matchDetail?.lineupStatus)

        val allPlayerList = playerMapper.getAllPlayersFromTeams(entity.teams)
        val participants = participantMapper.mapParticipants(entity, eventState, teamHomeId, teamAwayId, allPlayerList)

        val teamHomeShort = entity.teams?.get(teamHomeId)?.nameShort
        val teamAwayShort = entity.teams?.get(teamAwayId)?.nameShort

        val (topBatters, topBowlers) = playerMapper.getTopPerformers(
            entity.allInnings,
            eventState == EventState.LIVE,
            allPlayerList
        )

        val tossText = getTossText(entity)

        return IPLMatch(
            matchId = matchData?.id,
            matchGameId = matchData?.code,
            teamNameAvsTeamNameB = "$teamHomeShort vs $teamAwayShort",
            matchNo = matchData?.number,
            seriesName = matchDetail?.series?.name,
            beautifiedStartDate = CalendarUtils.convertDateToSpecifiedDate(
                dateString = matchData?.startDate,
                dateFormat = CalendarUtils.MATCH_FULL_DATE_WITH_OFFSET,
                requiredDateFormat = CalendarUtils.MATCH_REQUIRED_DATE_FORMAT
            ),
            startDate = matchData?.startDate,
            time = CalendarUtils.convertDateToSpecifiedDate(
                dateString = matchData?.startDate,
                dateFormat = CalendarUtils.MATCH_FULL_DATE_WITH_OFFSET,
                requiredDateFormat = CalendarUtils.MATCH_TIME
            ),
            eventState = eventState,
            venueName = matchDetail?.venue?.name,
            eventStatus = matchDetail?.status,
            eventSubStatus = if (matchDetail?.statusId == "127") tossText else matchDetail?.prematch
                ?: matchDetail?.equation,
            eventStatusId = matchDetail?.statusId,
            homeTeamId = teamHomeId,
            awayTeamId = teamAwayId,
            participants = participants,
            matchDisplayStatus = matchDetail?.matchDisplayStatus,
            playerOfTheMatch = matchDetail?.playerMatch,
            topBatters = topBatters,
            topBowlers = topBowlers,
            weather = matchDetail?.weather,
            toss = tossText,
            umpires = matchDetail?.officials?.umpires,
            referee = matchDetail?.officials?.referee,
            totalInnings = entity.allInnings.size,
            playedInnings = entity.innings?.size ?: 0,
            innings = mapInnings(entity)
        )
    }

    private fun mapInnings(entity: MatchCenterResponse): List<Inning> {
        val inningList = mutableListOf<Inning>()
        entity.innings?.forEachIndexed { index, inning ->
            inningList.add(
                Inning(
                    number = inning.number,
                    battingTeamId = inning.battingTeam,
                    battingTeamShortName = entity.teams?.get(inning.battingTeam)?.nameShort,
                    total = inning.total,
                    wickets = inning.wickets,
                    overs = inning.overs,
                    apiInningIndex = index + 1
                )
            )
        }
        entity.superOvers?.forEach { superOver ->
            val soNum = superOver.number ?: 1
            superOver.innings?.forEachIndexed { index, inning ->
                inningList.add(
                    Inning(
                        number = inning.number,
                        battingTeamId = inning.battingTeam,
                        battingTeamShortName = "${entity.teams?.get(inning.battingTeam)?.nameShort} (SO-$soNum)",
                        total = inning.total,
                        wickets = inning.wickets,
                        overs = inning.overs,
                        isSuperOver = true,
                        superOverNumber = soNum,
                        apiInningIndex = (soNum * 2) + 3 + index
                    )
                )
            }
        }
        return inningList
    }

    private fun getEventState(
        matchData: com.sportz.si_matchcenter.data.model.IPLMatch?, lineupStatus: Int?
    ): EventState = when {
        matchData?.live == true -> EventState.LIVE
        matchData?.upcoming == true -> EventState.UPCOMING
        else -> if (lineupStatus == 2) EventState.RESULT else EventState.UPCOMING
    }

    private fun getTossText(entity: MatchCenterResponse): String? {
        val tossWonBy = entity.matchDetail?.tossWonBy
        val tossElectedTo = entity.matchDetail?.tossElectedTo
        val tossWinnerName = entity.teams?.get(tossWonBy)?.nameFull ?: ""
        return if (tossWinnerName.isNotEmpty() && !tossElectedTo.isNullOrEmpty()) {
            "$tossWinnerName elected to $tossElectedTo"
        } else null
    }
}

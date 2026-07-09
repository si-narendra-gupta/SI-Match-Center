package com.sportz.si_matchcenter.data.mapper

import com.sportz.si_matchcenter.business.domain.model.BattingStats
import com.sportz.si_matchcenter.business.domain.model.BowlingStats
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.business.domain.model.ExtraRuns
import com.sportz.si_matchcenter.business.domain.model.FallOfWicketDTO
import com.sportz.si_matchcenter.business.domain.model.Participant
import com.sportz.si_matchcenter.business.domain.model.TeamSquadPlayer
import com.sportz.si_matchcenter.data.model.Inning
import com.sportz.si_matchcenter.data.model.MatchCenterResponse
import com.sportz.si_matchcenter.data.model.TeamDetail
import com.sportz.si_matchcenter.data.remote.SiFeedFixtureConfigContract
class ParticipantMapper(
    private val siFeedFixtureConfigContract: SiFeedFixtureConfigContract,
    private val playerMapper: PlayerMapper
) {

    fun mapParticipants(
        entity: MatchCenterResponse,
        eventState: EventState,
        teamHomeId: String?,
        teamAwayId: String?,
        allPlayerList: List<TeamSquadPlayer>
    ): List<Participant> {
        val participantList = mutableListOf<Participant>()

        if (eventState == EventState.UPCOMING) {
            listOfNotNull(teamHomeId, teamAwayId).forEach { teamId ->
                entity.teams?.get(teamId)?.let { teamDetail ->
                    participantList.add(mapBasicParticipant(teamId, teamDetail))
                }
            }
        } else {
            val regularBattingTeams = entity.innings?.mapNotNull { it.battingTeam }?.distinct() ?: emptyList()
            val allRegularTeams = regularBattingTeams.toMutableList()

            listOfNotNull(teamHomeId, teamAwayId).forEach { teamId ->
                if (teamId !in allRegularTeams) allRegularTeams.add(teamId)
            }

            allRegularTeams.forEach { teamId ->
                val inning = entity.innings?.find { it.battingTeam == teamId }
                if (inning != null) {
                    participantList.add(mapInningToParticipant(entity, inning, allPlayerList, eventState))
                } else {
                    participantList.add(mapYetToBatParticipant(entity, teamId, teamHomeId, teamAwayId))
                }
            }

            entity.superOvers?.forEach { superOver ->
                superOver.innings?.forEach { inning ->
                    participantList.add(
                        mapInningToParticipant(
                            entity = entity,
                            inning = inning,
                            allPlayerList = allPlayerList,
                            eventState = eventState,
                            isSuperOver = true,
                            superOverNumber = superOver.number
                        )
                    )
                }
            }
        }
        return participantList
    }

    private fun mapBasicParticipant(teamId: String, teamDetail: TeamDetail): Participant = Participant(
        id = teamId,
        name = teamDetail.nameFull,
        shortName = teamDetail.nameShort,
        teamImageUrl = siFeedFixtureConfigContract.getTeamLogo(teamId),
        highlight = false,
        squad = playerMapper.mapTeamPlayerList(teamDetail.players)
    )

    private fun mapYetToBatParticipant(
        entity: MatchCenterResponse,
        teamId: String,
        teamHomeId: String?,
        teamAwayId: String?
    ): Participant {
        val teamDetail = entity.teams?.get(teamId)
        val bowlingTeamId = if (teamId == teamHomeId) teamAwayId else teamHomeId
        val bowlingTeamDetail = entity.teams?.get(bowlingTeamId)

        return Participant(
            id = teamId,
            name = teamDetail?.nameFull,
            shortName = teamDetail?.nameShort,
            teamImageUrl = siFeedFixtureConfigContract.getTeamLogo(teamId),
            highlight = false,
            showYetToBat = true,
            squad = playerMapper.mapTeamPlayerList(teamDetail?.players),
            bowlingTeamId = bowlingTeamId,
            bowlingTeamName = bowlingTeamDetail?.nameFull,
            bowlingTeamShortName = bowlingTeamDetail?.nameShort,
            bowlingTeamImageUrl = siFeedFixtureConfigContract.getTeamLogo(bowlingTeamId.orEmpty())
        )
    }

    private fun mapInningToParticipant(
        entity: MatchCenterResponse,
        inning: Inning,
        allPlayerList: List<TeamSquadPlayer>,
        eventState: EventState,
        isSuperOver: Boolean = false,
        superOverNumber: Int? = null
    ): Participant {
        val teamId = inning.battingTeam ?: ""
        val teamDetail = entity.teams?.get(teamId)
        val bowlingTeamId = inning.bowlingTeam ?: ""
        val bowlingTeamDetail = entity.teams?.get(bowlingTeamId)
        val suffix = if (isSuperOver) " (SO-$superOverNumber)" else ""

        val battingScoreBoard = inning.batsmenList?.mapNotNull { batsmen ->
            allPlayerList.firstOrNull { it.id == batsmen.batsmanId }?.copy(
                battingStats = BattingStats(
                    runs = batsmen.runs,
                    balls = batsmen.balls,
                    fours = batsmen.fours,
                    sixes = batsmen.sixes,
                    strikeRate = batsmen.strikeRate,
                    howOutShort = batsmen.howOutShort
                ),
                position = batsmen.number?.toString()
            )
        }?.sortedBy { it.position?.toIntOrNull() ?: Int.MAX_VALUE } ?: emptyList()

        val bowlingScoreBoard = inning.bowlersList?.mapNotNull { bowlers ->
            allPlayerList.firstOrNull { it.id == bowlers.bowlerId }?.copy(
                bowlingStats = BowlingStats(
                    overs = bowlers.overs,
                    maidens = bowlers.maidens,
                    runs = bowlers.runs,
                    wickets = bowlers.wickets,
                    economyRate = bowlers.economyRate
                ),
                position = bowlers.number?.toString()
            )
        }?.sortedBy { it.position?.toIntOrNull() ?: Int.MAX_VALUE } ?: emptyList()

        val fallOfWicket = inning.fallOfWickets?.mapNotNull { fow ->
            allPlayerList.firstOrNull { it.id == fow.batsman }?.copy(
                fallOfWicket = FallOfWicketDTO(
                    score = fow.score,
                    wicketNo = fow.wicketNo?.toString(),
                    overs = fow.overs
                )
            )
        } ?: emptyList()

        val extraRuns = ExtraRuns(
            byes = inning.byes,
            legByes = inning.legByes,
            wides = inning.wides,
            noBalls = inning.noBalls,
            penalty = inning.penalty,
        )

        val isHighlight = when (eventState) {
            EventState.RESULT -> teamId == entity.matchDetail?.winningTeam
            EventState.LIVE -> entity.allInnings.lastOrNull() == inning
            else -> false
        }

        val teamInning = entity.innings?.findLast { it.battingTeam == teamId }

        return Participant(
            id = teamId,
            name = (teamDetail?.nameFull ?: "") + suffix,
            shortName = (teamDetail?.nameShort ?: "") + suffix,
            teamImageUrl = siFeedFixtureConfigContract.getTeamLogo(teamId),
            highlight = isHighlight,
            runs = inning.total,
            wickets = inning.wickets,
            overs = inning.overs,
            runRate = inning.runRate,
            showYetToBat = eventState in setOf(EventState.LIVE, EventState.RESULT) && teamInning == null,
            squad = playerMapper.mapTeamPlayerList(teamDetail?.players),
            battingScoreBoard = battingScoreBoard,
            bowlingScoreBoard = bowlingScoreBoard,
            fallOfWicketPlayers = fallOfWicket,
            extraRuns = extraRuns,
            bowlingTeamId = bowlingTeamId,
            bowlingTeamName = bowlingTeamDetail?.nameFull,
            bowlingTeamShortName = bowlingTeamDetail?.nameShort,
            bowlingTeamImageUrl = siFeedFixtureConfigContract.getTeamLogo(bowlingTeamId),
            isSuperOver = isSuperOver,
            superOverNumber = superOverNumber
        )
    }
}

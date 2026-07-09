package com.sportz.si_matchcenter.data.mapper

import com.sportz.si_matchcenter.business.domain.model.TeamSquadPlayer
import com.sportz.si_matchcenter.business.domain.model.TopPlayer
import com.sportz.si_matchcenter.data.model.Inning
import com.sportz.si_matchcenter.data.model.Players
import com.sportz.si_matchcenter.data.model.TeamDetail
class PlayerMapper {

    fun mapTeamPlayerList(players: Players?): List<TeamSquadPlayer> {
        return players?.map { (id, player) ->
            val fullName = player.nameFull ?: ""
            val firstName = fullName.substringBefore(" ")
            val lastName = fullName.substringAfter(" ", "")

            TeamSquadPlayer(
                id = id,
                name = fullName,
                firstName = firstName,
                lastName = lastName,
                role = player.role ?: player.skillName,
                position = player.position,
                isCaptain = player.isCaptain ?: false,
                isKeeper = player.isKeeper ?: false,
                isImpactPlayer = player.isSuperSub ?: false,
                iSConfirmXI = player.confirmXi ?: false,
                nationality = player.nationality,
                nationalityId = player.nationalityId,
                battingStats = null,
                bowlingStats = null,
                fallOfWicket = null,
                playerInName = player.playerInName,
                playerOutName = player.playerOutName,
                playerInId = player.playerIn,
                playerOutId = player.playerOut
            )
        }?.sortedBy { it.position?.toIntOrNull() ?: Int.MAX_VALUE } ?: emptyList()
    }

    fun getAllPlayersFromTeams(teams: HashMap<String, TeamDetail>?): List<TeamSquadPlayer> {
        return teams?.values?.flatMap { teamDetail ->
            mapTeamPlayerList(teamDetail.players)
        } ?: emptyList()
    }

    fun getTopPerformers(
        allInnings: List<Inning>,
        isLive: Boolean,
        allPlayerList: List<TeamSquadPlayer>
    ): Pair<List<TopPlayer>, List<TopPlayer>> {
        if (isLive) {
            val currentInning = allInnings.lastOrNull()
            if (currentInning != null) {
                // Live Batsmen
                val liveBatsmen = currentInning.batsmenList?.filter { it.isBatting == true } ?: emptyList()
                val nonStriker = liveBatsmen.find { it.isOnStrike != true }
                val striker = liveBatsmen.find { it.isOnStrike == true }

                val topBatters = mutableListOf<TopPlayer>()

                striker?.let { batsman ->
                    val playerName = allPlayerList.find { it.id == batsman.batsmanId }?.name ?: ""
                    topBatters.add(TopPlayer(name = playerName, runs = batsman.runs, balls = batsman.balls, isCurrent = true))
                }
                nonStriker?.let { batsman ->
                    val playerName = allPlayerList.find { it.id == batsman.batsmanId }?.name ?: ""
                    topBatters.add(TopPlayer(name = playerName, runs = batsman.runs, balls = batsman.balls, isCurrent = false))
                }

                // Live Bowlers
                val currentBowler = currentInning.bowlersList?.find { it.isBowlingNow == true && it.isBowlingTandem == true }
                val tandemBowler = currentInning.bowlersList?.find { it.isBowlingNow != true && it.isBowlingTandem == true }

                val topBowlers = mutableListOf<TopPlayer>()
                currentBowler?.let { bowler ->
                    val playerName = allPlayerList.find { it.id == bowler.bowlerId }?.name ?: ""
                    topBowlers.add(TopPlayer(name = playerName, wickets = bowler.wickets, overs = bowler.overs, runs = bowler.runs, isCurrent = true))
                }
                tandemBowler?.let { bowler ->
                    val playerName = allPlayerList.find { it.id == bowler.bowlerId }?.name ?: ""
                    topBowlers.add(TopPlayer(name = playerName, wickets = bowler.wickets, overs = bowler.overs, runs = bowler.runs, isCurrent = false))
                }

                if (topBatters.isNotEmpty() || topBowlers.isNotEmpty()) {
                    return Pair(topBatters, topBowlers)
                }
            }
        }

        val bestBatsmen = allInnings.flatMap { it.bestPerformers?.batsmen ?: emptyList() }
        val bestBowlers = allInnings.flatMap { it.bestPerformers?.bowlers ?: emptyList() }

        val topBatters = bestBatsmen
            .sortedByDescending { it.runs?.toIntOrNull() ?: 0 }
            .take(2)
            .map { TopPlayer(name = it.name ?: "", runs = it.runs, balls = it.balls) }

        val topBowlers = bestBowlers
            .sortedByDescending { it.wickets?.toIntOrNull() ?: 0 }
            .take(2)
            .map { TopPlayer(name = it.name ?: "", wickets = it.wickets, overs = it.overs, runs = it.runs) }

        return Pair(topBatters, topBowlers)
    }
}

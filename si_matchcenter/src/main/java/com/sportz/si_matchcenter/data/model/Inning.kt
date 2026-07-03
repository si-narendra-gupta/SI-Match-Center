package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Inning(
    @SerialName("AllotedBalls") val allotedBalls: String? = null,
    @SerialName("AllottedOvers") val allottedOvers: String? = null,
    @SerialName("At_this_stage") val atThisStage: String? = null,
    @SerialName("BallNo") val ballNo: String? = null,
    @SerialName("Battingteam") val battingTeam: String? = null,
    @SerialName("Bowlingteam") val bowlingTeam: String? = null,
    @SerialName("Byes") val byes: String? = null,
    @SerialName("Legbyes") val legByes: String? = null,
    @SerialName("Noballs") val noBalls: String? = null,
    @SerialName("Number") val number: String? = null,
    @SerialName("OverNo") val overNo: String? = null,
    @SerialName("Overs") val overs: String? = null,
    @SerialName("Penalty") val penalty: String? = null,
    @SerialName("Runrate") val runRate: String? = null,
    @SerialName("Runsperball") val runsPerBall: String? = null,
    @SerialName("StartTimeStamp") val startTimeStamp: String? = null,
    @SerialName("StatusId") val statusId: Int? = null,
    @SerialName("Target") val target: String? = null,
    @SerialName("Total") val total: String? = null,
    @SerialName("Total_Balls_Bowled") val totalBallsBowled: String? = null,
    @SerialName("Wickets") val wickets: String? = null,
    @SerialName("Wides") val wides: String? = null,
    @SerialName("Best_Performers") val bestPerformers: BestPerformers? = null,
    @SerialName("Batsmen") val batsmenList: List<Batsmen>? = null,
    @SerialName("Bowlers") val bowlersList: List<Bowlers>? = null,
    @SerialName("FallofWickets") val fallOfWickets: List<FallOfWicket>? = null
)

@Serializable
data class BestPerformers(
    @SerialName("Batsmen") val batsmen: List<BestBatsman>? = null,
    @SerialName("Bowlers") val bowlers: List<BestBowler>? = null
)

@Serializable
data class BestBatsman(
    @SerialName("Player_Id") val playerId: String? = null,
    @SerialName("Player_Name") val name: String? = null,
    @SerialName("Runs") val runs: String? = null,
    @SerialName("Balls") val balls: String? = null,
    @SerialName("Strikerate") val strikeRate: String? = null
)

@Serializable
data class BestBowler(
    @SerialName("Player_Id") val playerId: String? = null,
    @SerialName("Player_Name") val name: String? = null,
    @SerialName("Wickets") val wickets: String? = null,
    @SerialName("Runs") val runs: String? = null,
    @SerialName("Overs") val overs: String? = null,
    @SerialName("Economyrate") val economyRate: String? = null
)

@Serializable
data class Batsmen(
    @SerialName("Number") val number: Int? = null,
    @SerialName("Batsman") val batsmanId: String? = null,
    @SerialName("Runs") val runs: String? = null,
    @SerialName("Balls") val balls: String? = null,
    @SerialName("Fours") val fours: String? = null,
    @SerialName("Sixes") val sixes: String? = null,
    @SerialName("Dots") val dots: String? = null,
    @SerialName("Strikerate") val strikeRate: String? = null,
    @SerialName("Howout_short") val howOutShort: String? = null,
    @SerialName("Bowler") val bowlerId: String? = null,
    @SerialName("Fielder") val fielderId: String? = null,
    @SerialName("Is_substituted") val isSubstituted: Boolean? = null,
    @SerialName("Isbatting") val isBatting: Boolean? = null,
    @SerialName("Isonstrike") val isOnStrike: Boolean? = null
)

@Serializable
data class Bowlers(
    @SerialName("Bowler") val bowlerId: String? = null,
    @SerialName("Number") val number: Int? = null,
    @SerialName("Overs") val overs: String? = null,
    @SerialName("Maidens") val maidens: String? = null,
    @SerialName("Runs") val runs: String? = null,
    @SerialName("Wickets") val wickets: String? = null,
    @SerialName("Economyrate") val economyRate: String? = null,
    @SerialName("Is_substituted") val isSubstituted: Boolean? = null,
    @SerialName("Isbowlingnow") val isBowlingNow: Boolean? = null,
    @SerialName("Isbowlingtandem") val isBowlingTandem: Boolean? = null
)

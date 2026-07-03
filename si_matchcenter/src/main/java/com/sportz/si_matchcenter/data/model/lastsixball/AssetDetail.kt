package com.sportz.si_matchcenter.data.model.lastsixball

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetDetail(
    @SerialName("BattingTeam_Id")
    val battingTeamId: String? = null,
    @SerialName("BowlingTeam_Id")
    val bowlingTeamId: String? = null,
    @SerialName("Over")
    val over: String? = null,
    @SerialName("Runs")
    val runs: String? = null,
    @SerialName("Detail")
    val detail: String? = null,
    @SerialName("Isball")
    val isBall: Boolean? = null,
    @SerialName("Bowler")
    val bowler: String? = null,
    @SerialName("Batsman")
    val batsman: String? = null,
    @SerialName("Bowler_Name")
    val bowlerName: String? = null,
    @SerialName("Batsman_Name")
    val batsmanName: String? = null,
    @SerialName("Commentary")
    val commentary: String? = null,
    @SerialName("Over_Summary")
    val overSummary: OverSummary? = null,
    @SerialName("OverNo")
    val overNo: String? = null,
    @SerialName("BallNo")
    val ballNo: String? = null,
    @SerialName("Ball_Number")
    val ballNumber: String? = null,
    @SerialName("This_Over")
    val thisOver: String? = null,
    @SerialName("Ball_Line_Length")
    val ballLineLength: String? = null,
    @SerialName("is_over_end")
    val isOverEnd: Boolean? = null,
    @SerialName("Iswicket")
    val isWicket: Boolean? = null,
    @SerialName("Review_Detail")
    val reviewDetail: ReviewDetail? = null
)

@Serializable
data class OverSummary(
    @SerialName("Over")
    val over: String? = null,
    @SerialName("Runs")
    val runs: String? = null,
    @SerialName("Wickets")
    val wickets: String? = null,
    @SerialName("Score")
    val score: String? = null
)

@Serializable
data class ReviewDetail(
    @SerialName("Batting_Review_Count")
    val battingReviewCount: String? = null,
    @SerialName("Bowling_Review_Count")
    val bowlingReviewCount: String? = null,
    @SerialName("Team_Id")
    val teamId: Int? = null,
    @SerialName("Review_Type")
    val reviewType: String? = null,
    @SerialName("Review_Type_Id")
    val reviewTypeId: Int? = null,
    @SerialName("Review_For")
    val reviewFor: String? = null,
    @SerialName("Review_For_Id")
    val reviewForId: Int? = null,
    @SerialName("Review_Output")
    val reviewOutput: String? = null,
    @SerialName("Review_Output_Id")
    val reviewOutputId: Int? = null
)

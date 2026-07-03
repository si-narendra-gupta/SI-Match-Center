package com.sportz.si_matchcenter.business.domain.model

import java.io.Serializable

data class CustomMetaData(
    val commentary: String? = null,
    val ballByBall: List<String>? = null,
    val commentaryList: List<BallDetail>? = null
) : Serializable

data class BallDetail(
    val overLabel: String? = null,
    val overNo: String? = null,
    val ballNo: String? = null,
    val runs: String? = null,
    val commentary: String? = null,
    val isBall: Boolean? = null,
    val isWicket: Boolean? = null,
    val bowlerName: String? = null,
    val batsmanName: String? = null,
    val overSummary: DomainOverSummary? = null,
    val detail: String? = null
) : Serializable

data class DomainOverSummary(
    val over: String? = null,
    val runs: String? = null,
    val wickets: String? = null,
    val score: String? = null
) : Serializable

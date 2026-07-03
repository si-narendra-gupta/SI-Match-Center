package com.sportz.si_matchcenter.business.domain.model

import java.io.Serializable

data class Inning(
    val number: String?,
    val battingTeamId: String?,
    val battingTeamShortName: String?,
    val total: String?,
    val wickets: String?,
    val overs: String?,
    val isSuperOver: Boolean = false,
    val superOverNumber: Int? = null,
    val apiInningIndex: Int = 1
) : Serializable

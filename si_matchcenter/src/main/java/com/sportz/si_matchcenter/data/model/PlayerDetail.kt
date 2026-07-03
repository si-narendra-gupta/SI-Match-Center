package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDetail(
    @SerialName("Batting") val batting: Batting? = null,
    @SerialName("Bowling") val bowling: Bowling? = null,
    @SerialName("Confirm_XI") val confirmXi: Boolean? = null,
    @SerialName("Is_substituted") val isSubstituted: Boolean? = null,
    @SerialName("Matches") val matches: String? = null,
    @SerialName("Name_Full") val nameFull: String? = null,
    @SerialName("Name_Short") val nameShort: String? = null,
    @SerialName("Player_in") val playerIn: String? = null,
    @SerialName("Player_in_name") val playerInName: String? = null,
    @SerialName("Player_out") val playerOut: String? = null,
    @SerialName("Player_out_name") val playerOutName: String? = null,
    @SerialName("Position") val position: String? = null,
    @SerialName("Role") val role: String? = null,
    @SerialName("Skill") val skill: String? = null,
    @SerialName("Skill_Name") val skillName: String? = null,
    @SerialName("nationality") val nationality: String? = null,
    @SerialName("nationality_id") val nationalityId: Int? = null,
    @SerialName("prev_match_played") val prevMatchPlayed: Boolean? = null,
    @SerialName("Iscaptain") val isCaptain: Boolean? = null,
    @SerialName("Iskeeper") val isKeeper: Boolean? = null,
    @SerialName("Issupersub") val isSuperSub : Boolean? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("status_id") val statusId: Int? = null
)

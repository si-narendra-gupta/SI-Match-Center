package com.sportz.si_matchcenter.data.model.lastsixball

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Asset(
    @SerialName("asset_order")
    val assetOrder: Int? = null,
    @SerialName("body")
    val body: String? = null,
    @SerialName("custom_metadata")
    val customMetadata: CustomMetadata? = null,
    @SerialName("display_pic")
    val displayPic: JsonElement? = null,
    @SerialName("display_value")
    val displayValue: JsonElement? = null,
    @SerialName("filter_id")
    val filterId: Int? = null,
    @SerialName("flag")
    val flag: Int? = null,
    @SerialName("headline")
    val headline: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("publish_time")
    val publishTime: String? = null,
    @SerialName("publish_time_utc")
    val publishTimeUtc: String? = null,
    @SerialName("source")
    val source: String? = null,
    @SerialName("source_id")
    val sourceId: Int? = null,
    @SerialName("tags")
    val tags: List<JsonElement?>? = null,
    @SerialName("type")
    val type: String? = null
)

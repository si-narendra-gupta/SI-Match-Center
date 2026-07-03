package com.sportz.si_matchcenter.data.model.lastsixball

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentaryDataModel(
    @SerialName("assets")
    val assets: List<Asset?>? = null,
    @SerialName("deleted_assets")
    val deletedAssets: String? = null,
    @SerialName("edition")
    val edition: String? = null,
    @SerialName("edition_id")
    val editionId: Int? = null,
    @SerialName("sport")
    val sport: String? = null,
    @SerialName("sport_id")
    val sportId: Int? = null,
    @SerialName("tag")
    val tag: Tag? = null
)

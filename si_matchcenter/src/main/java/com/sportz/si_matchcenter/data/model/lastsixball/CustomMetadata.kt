package com.sportz.si_matchcenter.data.model.lastsixball

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class CustomMetadata(
    @SerialName("asset")
    val asset: String? = null
) {
    fun getAssetDetail(): AssetDetail? {
        return asset?.let {
            try {
                Json { ignoreUnknownKeys = true }.decodeFromString<AssetDetail>(it)
            } catch (e: Exception) {
                null
            }
        }
    }
}

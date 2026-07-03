package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Venue(
    @SerialName("City") val city: String? = null,
    @SerialName("Country") val country: String? = null,
    @SerialName("Id") val id: String? = null,
    @SerialName("Latitude") val latitude: String? = null,
    @SerialName("Longitude") val longitude: String? = null,
    @SerialName("Name") val name: String? = null,
    @SerialName("Neutralvenue") val neutralVenue: String? = null
)

package com.sportz.si_matchcenter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Series(
    @SerialName("Calendar_file_name") val calendarFileName: String? = null,
    @SerialName("Has_standings") val hasStandings: String? = null,
    @SerialName("Id") val id: String? = null,
    @SerialName("Name") val name: String? = null,
    @SerialName("Series_completed") val seriesCompleted: Boolean? = null,
    @SerialName("Series_end_date") val seriesEndDate: String? = null,
    @SerialName("Series_end_date_gmt") val seriesEndDateGmt: String? = null,
    @SerialName("Series_end_date_ist") val seriesEndDateIst: String? = null,
    @SerialName("Series_match_count") val seriesMatchCount: Int? = null,
    @SerialName("Series_short_display_name") val seriesShortDisplayName: String? = null,
    @SerialName("Series_start_date") val seriesStartDate: String? = null,
    @SerialName("Series_start_date_gmt") val seriesStartDateGmt: String? = null,
    @SerialName("Series_start_date_ist") val seriesStartDateIst: String? = null,
    @SerialName("Series_type") val seriesType: String? = null,
    @SerialName("Status") val status: String? = null,
    @SerialName("Tour") val tour: String? = null,
    @SerialName("Tour_Name") val tourName: String? = null,
    @SerialName("is_provisional_date") val isProvisionalDate: Boolean? = null
)

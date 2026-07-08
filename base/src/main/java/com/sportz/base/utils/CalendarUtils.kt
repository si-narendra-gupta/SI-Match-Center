package com.sportz.base.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object CalendarUtils {

    const val MATCH_FULL_DATE_WITH_OFFSET = "M/d/yyyy'T'HH:mmXXX"
    const val MATCH_REQUIRED_DATE_FORMAT = "EEEE d'th' MMM"
    const val MATCH_TIME = "HH:mm"

    fun convertDateToSpecifiedDate(
        dateString: String?,
        dateFormat: String,
        requiredDateFormat: String,
        targetZoneId: String = TimeZone.getDefault().id
    ): String? {
        return try {
            if (dateString == null) return null
            val inputFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            val date = inputFormat.parse(dateString) ?: return null
            
            val outputFormat = SimpleDateFormat(requiredDateFormat, Locale.ENGLISH)
            outputFormat.timeZone = TimeZone.getTimeZone(targetZoneId)
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun convertDateToMillis(
        dateString: String?,
        dateFormat: String,
    ): Long {
        return try {
            if (dateString == null) {
                return -1
            }
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
            val date = simpleDateFormat.parse(dateString)
            date?.time ?: -1L
        } catch (e: Exception) {
            -1L
        }
    }
}

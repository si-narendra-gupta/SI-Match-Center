package com.sportz.base.utils

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object CalendarUtils {

    const val PUBLISHED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val SCORE_CARD_MATCH_DATE_FORMAT = "yyyy-MM-dd'T'HH:mmZ"
    const val DOB_DATE_FORMAT = "yyyy-MM-dd"
    const val MATCH_FULL_DATE_WITH_OFFSET = "M/d/yyyy'T'HH:mmXXX"
    const val MATCH_REQUIRED_DATE_FORMAT = "EEEE d'th' MMM"
    const val MATCH_TIME = "HH:mm"

    fun convertDateStringToSpecifiedDateString(
        dateString: String?,
        dateFormat: String,
        requiredDateFormat: String,
        targetZoneId: ZoneId = ZoneId.systemDefault()
    ): String? {
        return try {
            if (dateString == null) return null
            val formatter = DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH)
            val dateTime = LocalDateTime.parse(dateString, formatter)
            val zonedDateTime = dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(targetZoneId)
            zonedDateTime.format(DateTimeFormatter.ofPattern(requiredDateFormat, Locale.ENGLISH))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getPublishedDuration(
        context: android.content.Context,
        dateString: String?,
        dateFormat: String
    ): String? {
        return try {
            if (dateString == null) return null
            val formatter = DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH)
            val publishedTime = LocalDateTime.parse(dateString, formatter).atZone(ZoneId.systemDefault()).toInstant()
            val now = Instant.now()
            val duration = Duration.between(publishedTime, now)

            when {
                duration.toDays() >= 365 -> "${duration.toDays() / 365}y"
                duration.toDays() >= 30 -> "${duration.toDays() / 30}m"
                duration.toDays() >= 7 -> "${duration.toDays() / 7}w"
                duration.toDays() >= 1 -> "${duration.toDays()}d"
                duration.toHours() >= 1 -> "${duration.toHours()}h"
                duration.toMinutes() >= 1 -> "${duration.toMinutes()}min"
                duration.seconds >= 3 -> "${duration.seconds}s"
                else -> context.getString(com.sportz.base.R.string.just_now)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getCurrentDate(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DOB_DATE_FORMAT))
    }

    fun getGreetings(context: android.content.Context): String {
        val currentHour = LocalDateTime.now().hour
        val stringRes = when (currentHour) {
            in 5..11 -> com.sportz.base.R.string.greeting_morning
            in 12..16 -> com.sportz.base.R.string.greeting_afternoon
            in 17..23 -> com.sportz.base.R.string.greeting_evening
            else -> com.sportz.base.R.string.greeting_default
        }
        return context.getString(stringRes)
    }
    fun convertDateStringToMillis(
        dateString: String?,
        dateFormat: String,
    ): Long {
        return try {
            if (dateString == null) {
                return -1
            }
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
            val date = simpleDateFormat.parse(dateString)
            if (date != null) {
                return date.time
            } else -1L
        } catch (e: Exception) {
            -1L
        }
    }
}

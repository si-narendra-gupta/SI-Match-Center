package com.sportz.base.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

    fun getPublishedDuration(
        context: android.content.Context,
        dateString: String?,
        dateFormat: String
    ): String? {
        return try {
            if (dateString == null) return null
            val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            val publishedDate = formatter.parse(dateString) ?: return null
            val now = Date()
            
            val diffInMillis = now.time - publishedDate.time
            val diffInSeconds = diffInMillis / 1000
            val diffInMinutes = diffInSeconds / 60
            val diffInHours = diffInMinutes / 60
            val diffInDays = diffInHours / 24

            when {
                diffInDays >= 365 -> "${diffInDays / 365}y"
                diffInDays >= 30 -> "${diffInDays / 30}m"
                diffInDays >= 7 -> "${diffInDays / 7}w"
                diffInDays >= 1 -> "${diffInDays}d"
                diffInHours >= 1 -> "${diffInHours}h"
                diffInMinutes >= 1 -> "${diffInMinutes}min"
                diffInSeconds >= 3 -> "${diffInSeconds}s"
                else -> context.getString(com.sportz.base.R.string.just_now)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getCurrentDate(): String {
        return SimpleDateFormat(DOB_DATE_FORMAT, Locale.ENGLISH).format(Date())
    }

    fun getGreetings(context: android.content.Context): String {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
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
            date?.time ?: -1L
        } catch (e: Exception) {
            -1L
        }
    }
}

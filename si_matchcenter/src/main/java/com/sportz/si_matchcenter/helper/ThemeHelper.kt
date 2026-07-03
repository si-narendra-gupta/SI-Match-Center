package com.sportz.si_matchcenter.helper

import com.google.gson.Gson
import com.sportz.si_matchcenter.business.domain.model.themecolor.MatchCenterThemeColors
import com.sportz.si_matchcenter.business.domain.model.themecolor.MatchCenterThemeColors.Companion.default

object ThemeHelper {
    fun getMatchCenterThemeColors(theme: String?, gson: Gson): MatchCenterThemeColors? {
        val themeJson = if (theme.isNullOrEmpty()) ThemeConstants.DEFAULT_THEME else theme
        return try {
            gson.fromJson(themeJson, MatchCenterThemeColors::class.java) ?: MatchCenterThemeColors.default()
        } catch (_: Exception) {
            try {
                gson.fromJson(ThemeConstants.DEFAULT_THEME, MatchCenterThemeColors::class.java) ?: MatchCenterThemeColors.default()
            } catch (_: Exception) {
                MatchCenterThemeColors.default()
            }
        }
    }
}

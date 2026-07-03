package com.sportz.si_matchcenter.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.sportz.si_matchcenter.business.domain.model.themecolor.MatchCenterThemeColors
import androidx.core.graphics.toColorInt
import com.sportz.si_matchcenter.business.domain.model.themecolor.MatchCenterThemeColors.Companion.default

val LocalMatchCenterColors = staticCompositionLocalOf { 
    MatchCenterThemeColors.default()
}

object MatchCenterTheme {
    val colors: MatchCenterThemeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalMatchCenterColors.current
}

fun String?.toColor(default: Color = Color.Transparent): Color {
    val colorString = this ?: return default
    return try {
        Color(colorString.toColorInt())
    } catch (e: Exception) {
        default
    }
}

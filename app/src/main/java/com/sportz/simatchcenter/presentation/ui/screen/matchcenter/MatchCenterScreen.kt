package com.sportz.simatchcenter.presentation.ui.screen.matchcenter

import androidx.compose.runtime.Composable
import com.sportz.si_matchcenter.presentation.ui.screen.MatchCenterContent

@Composable
fun MatchCenterScreen(
    gameId: String, theme: String? = null
) {
    MatchCenterContent(gameId = gameId, theme = theme)
}

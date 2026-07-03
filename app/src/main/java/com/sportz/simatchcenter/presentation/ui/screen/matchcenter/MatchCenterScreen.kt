package com.sportz.simatchcenter.presentation.ui.screen.matchcenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sportz.si_matchcenter.presentation.ui.screen.MatchCenterContent

@Composable
fun MatchCenterScreen(
    gameId: String, theme: String? = null
) {
    Scaffold(
        topBar = {
            ToolBar(title = "Match Center", showBack = true, shareClick = {}, backClick = {

            })
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Transparent)
        ) {
            MatchCenterContent(gameId = gameId, theme = theme)
        }
    }
}

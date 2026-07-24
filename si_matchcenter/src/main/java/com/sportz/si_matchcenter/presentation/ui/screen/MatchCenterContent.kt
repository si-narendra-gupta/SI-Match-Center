package com.sportz.si_matchcenter.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sportz.match_center_base.ui.common.MatchCenterBaseState
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.business.domain.model.MatchTabId
import com.sportz.si_matchcenter.business.domain.model.MatchTabItem
import com.sportz.si_matchcenter.business.domain.model.themecolor.MatchCenterThemeColors
import com.sportz.si_matchcenter.business.domain.model.themecolor.MatchCenterThemeColors.Companion.default
import com.sportz.si_matchcenter.presentation.ui.theme.LocalMatchCenterColors
import com.sportz.si_matchcenter.presentation.ui.theme.toColor
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterIntent
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterUiState
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterViewModelMatchCenter

@Composable
fun MatchCenterContent(
    gameId: String,
    viewModel: MatchCenterViewModelMatchCenter,
    theme: String? = null
) {

    LaunchedEffect(theme) {
        viewModel.setTheme(theme)
    }

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(gameId) {
        viewModel.handleIntent(MatchCenterIntent.LoadMatches(gameId))
    }

    val themeColors =
        (uiState as? MatchCenterBaseState.Success)?.data?.themeColors ?: MatchCenterThemeColors.default()

    CompositionLocalProvider(LocalMatchCenterColors provides themeColors) {
        val backgroundColor = themeColors.screen?.background.toColor()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (backgroundColor != Color.Transparent) Modifier.background(backgroundColor) else Modifier
                )
        ) {
            when (val state = uiState) {
                is MatchCenterBaseState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is MatchCenterBaseState.Success -> {
                    MatchCenterSuccessView(
                        data = state.data,
                        onTabSelected = { tab -> viewModel.selectedTab(tab) },
                        viewModel = viewModel
                    )
                }

                is MatchCenterBaseState.Empty -> {
                    Text(text = "No matches available", modifier = Modifier.align(Alignment.Center))
                }

                is MatchCenterBaseState.Error -> {
                    Text(
                        text = state.message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun MatchCenterSuccessView(
    data: MatchCenterUiState, onTabSelected: (MatchTabItem) -> Unit, viewModel: MatchCenterViewModelMatchCenter
) {
    val themeColors = viewModel.getCurrentUiData()?.themeColors

    val match = data.matches ?: return
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {

        // External Title
        Text(
            text = "${match.teamNameAvsTeamNameB},${match.matchNo}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = themeColors?.header?.title.toColor(),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if ((match.eventState == EventState.LIVE) || (match.eventState == EventState.RESULT)) {
            LiveMatchInfo(match, themeColors)
        } else {
            UpcomingMatchInfo(match, themeColors)
        }

        if (data.tabItems.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            MatchCenterTabSelector(
                tabs = data.tabItems,
                selectedTab = data.selectedTab,
                onTabSelected = onTabSelected,
                matchDetails = themeColors?.match_details
            )
        }

        // Tab Content
        when (data.selectedTab?.tabId) {
            MatchTabId.MATCH_INFO -> MatchInfoScreen(match, themeColors?.match_details?.match_info)
            MatchTabId.TEAMS -> TeamsScreen(
                match,
                themeColors?.match_details?.sub_tab,
                themeColors?.match_details?.teams,
                viewModel
            )

            MatchTabId.COMMENTARY -> CommentaryScreen(match, viewModel, data)
            MatchTabId.SCORECARD -> ScorecardScreen(match, themeColors?.match_details, viewModel)
            MatchTabId.GRAPHS -> GraphsScreen(
                match,
                subTabs = data.selectedTab.sub_tabs?.filter { it.visible == true } ?: emptyList(),
                viewModel,
                colortheme = themeColors?.match_details)

            else -> {
                // Default or empty content
            }
        }
    }
}

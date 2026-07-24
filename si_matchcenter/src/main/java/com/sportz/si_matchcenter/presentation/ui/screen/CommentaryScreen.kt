package com.sportz.si_matchcenter.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportz.si_matchcenter.business.domain.model.BallDetail
import com.sportz.si_matchcenter.business.domain.model.DomainOverSummary
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.business.domain.model.IPLMatch
import com.sportz.si_matchcenter.business.domain.model.themecolor.Ball
import com.sportz.si_matchcenter.business.domain.model.themecolor.CommentaryCard
import com.sportz.si_matchcenter.presentation.ui.theme.toColor
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterIntent
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterUiState
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterViewModelMatchCenter

@Composable
fun CommentaryScreen(
    match: IPLMatch, viewModel: MatchCenterViewModelMatchCenter, uiState: MatchCenterUiState
) {

    val colortheme = viewModel.getCurrentUiData()?.themeColors?.match_details

    if (match.eventState == EventState.LIVE || match.eventState == EventState.RESULT) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Inning Tabs
            val innings = match.innings ?: emptyList()
            val selectedInningData = innings.find { it.apiInningIndex == uiState.selectedInning }
            val teamName = selectedInningData?.battingTeamShortName ?: ""

            if (innings.isNotEmpty()) {
                val scrollState = rememberScrollState()
                
                LaunchedEffect(uiState.selectedInning) {
                    val index = innings.indexOfFirst { it.apiInningIndex == uiState.selectedInning }
                    if (index >= 0) {
                        // Approximate the scroll position since we don't have the exact item widths in a regular Row
                        // This will move it towards the visible area
                        val scrollPos = (index * 100).coerceAtMost(scrollState.maxValue)
                        scrollState.animateScrollTo(scrollPos)
                    }
                }
                Box {
                    // Gray bottom line
                    HorizontalDivider(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        thickness = 1.dp,
                        color = colortheme?.sub_tab?.divider.toColor()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(if (innings.size > 2) Modifier.horizontalScroll(scrollState) else Modifier)
                    ) {
                        innings.forEach { inning ->

                            val isSelected = uiState.selectedInning == inning.apiInningIndex

                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .then(if (innings.size <= 2) Modifier.weight(1f) else Modifier.padding(horizontal = 16.dp))
                                    .clickable {
                                        viewModel.handleIntent(
                                            MatchCenterIntent.LoadCommentary(
                                                match.matchId ?: "",
                                                match.eventState,
                                                inning.apiInningIndex
                                            )
                                        )
                                    }, contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = inning.battingTeamShortName ?: "Inning ${inning.number}",
                                    color = if (isSelected) colortheme?.sub_tab?.selected_text.toColor() else colortheme?.sub_tab?.unselected_text.toColor()
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .then(if (innings.size <= 2) Modifier.fillMaxWidth() else Modifier.width(80.dp))
                                            .height(2.dp)
                                            .background(colortheme?.sub_tab?.selected_divider.toColor())
                                    )
                                }
                            }
                        }
                    }
                }
            }

            val commentaryList = match.customMetaData?.commentaryList ?: emptyList()
            if (commentaryList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No commentary available",
                        color = colortheme?.commentary_card?.description.toColor()
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    commentaryList.forEach { item ->
                        if (item.overSummary != null) {
                            OverHeader(item.overSummary, teamName, colortheme?.commentary_card)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (item.isBall == true) {
                            BallItem(item, colortheme?.commentary_card)
                        } else {
                            Text(
                                text = item.commentary ?: item.detail ?: "",
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                                fontSize = 14.sp,
                                color = colortheme?.commentary_card?.description.toColor(),
                                lineHeight = 20.sp
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 0.5.dp,
                            color = colortheme?.commentary_card?.divider.toColor()
                        )
                    }
                }
            }
        }
    } else {
        val commentary = match.customMetaData?.commentary ?: "No commentary available"
        Text(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxSize(),
            textAlign = TextAlign.Start,
            text = commentary,
            fontSize = 14.sp,
            color = Color.DarkGray,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun OverHeader(summary: DomainOverSummary, teamName: String, commentaryCard: CommentaryCard?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(commentaryCard?.commentary_header?.background.toColor())
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Over: ${summary.over} | ${summary.runs} Runs",
            color = commentaryCard?.commentary_header?.over.toColor(),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Text(
            text = "$teamName/Score: ${summary.score}",
            color = commentaryCard?.commentary_header?.score.toColor(),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}

@Composable
fun BallItem(ball: BallDetail, commentaryCard: CommentaryCard?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(
                        CircleShape
                    )
                    .background(
                        if (ball.isWicket == true) commentaryCard?.ball?.wicket_bg.toColor() else getBallColor(
                            ball.runs, commentaryCard?.ball
                        )
                    ), contentAlignment = Alignment.Center
            ) {
                val displayText = when {
                    ball.isWicket == true -> "W"
                    !ball.detail.isNullOrEmpty() -> "${ball.runs}${ball.detail}"
                    else -> ball.runs ?: ""
                }
                Text(
                    text = displayText,
                    color = if (ball.isWicket == true) commentaryCard?.ball?.wicket_text.toColor() else getBallTextColor(
                        ball.runs, commentaryCard?.ball
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = if (displayText.length > 2) 10.sp else 12.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = ball.overLabel ?: "",
                fontSize = 11.sp,
                color = commentaryCard?.over.toColor(),
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${ball.bowlerName} to ${ball.batsmanName}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = commentaryCard?.title.toColor()
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = ball.commentary ?: "",
                fontSize = 13.sp,
                color = commentaryCard?.description.toColor(),
                lineHeight = 18.sp
            )
        }
    }
}

fun getBallColor(runs: String?, ball: Ball?): Color {
    return when (runs) {
        "4" -> ball?.four_bg.toColor()
        "6" -> ball?.six_bg.toColor()
        "W" -> ball?.wicket_bg.toColor()
        else -> ball?.dot_bg.toColor()
    }
}

fun getBallTextColor(runs: String?, ball: Ball?): Color {
    return when (runs) {
        "4" -> ball?.four_text.toColor()
        "6" -> ball?.six_text.toColor()
        "W" -> ball?.wicket_text.toColor()
        else -> ball?.dot_text.toColor()
    }
}

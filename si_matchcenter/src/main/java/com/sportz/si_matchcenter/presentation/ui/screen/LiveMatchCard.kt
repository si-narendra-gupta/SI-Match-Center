package com.sportz.si_matchcenter.presentation.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.business.domain.model.IPLMatch
import com.sportz.si_matchcenter.business.domain.model.Participant
import com.sportz.si_matchcenter.business.domain.model.themecolor.LastBalls
import com.sportz.si_matchcenter.business.domain.model.themecolor.MatchCenterThemeColors
import com.sportz.si_matchcenter.business.domain.model.themecolor.PerformerSection
import com.sportz.si_matchcenter.presentation.ui.theme.toColor

@Composable
fun LiveMatchInfo(match: IPLMatch, themeColors: MatchCenterThemeColors?) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        val gradientColors = remember(themeColors) {
            val start = themeColors?.match_card?.gradient_start?.toColor() ?: Color(0xFF07167A)
            val end = themeColors?.match_card?.gradient_end?.toColor() ?: Color(0xFF950461)
            listOf(start, end)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.verticalGradient(colors = gradientColors))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Header
                MatchCardHeader(match, themeColors)
                // Teams Score Row
                MatchCardScore(match, themeColors)

                when (match.eventState) {
                    EventState.LIVE -> Badge(
                        match.eventStatus,
                        themeColors?.live_badge?.background.toColor(),
                        themeColors?.live_badge?.text.toColor()
                    )

                    else -> Badge(
                        match.eventStatus,
                        themeColors?.result_badge?.background.toColor(),
                        themeColors?.result_badge?.text.toColor()
                    )
                }

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                )
                // Equation / Result
                Text(
                    text = match.eventSubStatus ?: "",
                    color = themeColors?.team?.equation.toColor(),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                // Venue name
                Text(
                    text = match.venueName ?: "",
                    color = themeColors?.team?.venue.toColor(),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(all = 8.dp)
                )

                if (match.eventState == EventState.RESULT && !match.playerOfTheMatch.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(4.dp))
                            .background(themeColors?.player_of_match?.background.toColor())
                            .padding(vertical = 8.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Player of the Match : ",
                            color = themeColors?.player_of_match?.label.toColor(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = match.playerOfTheMatch,
                            color = themeColors?.player_of_match?.player_name.toColor(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                TopPerformers(match, themeColors?.performer_section)

                Spacer(modifier = Modifier.height(4.dp))
                val balls = remember(match.customMetaData?.ballByBall) {
                    match.customMetaData?.ballByBall?.takeLast(6)
                }
                BallByBallList(balls, themeColors?.last_balls)
            }
        }
    }
}

@Composable
fun MatchCardHeader(match: IPLMatch, themeColors: MatchCenterThemeColors?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(themeColors?.header?.header_background.toColor())
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = match.seriesName ?: "Indian T20 League",
                color = themeColors?.header?.subtitle.toColor(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${match.matchNo}",
                    color = themeColors?.header?.match_info_text.toColor(),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(1.dp, 10.dp)
                        .background(themeColors?.header?.divider.toColor())
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = match.beautifiedStartDate ?: "",
                    color = themeColors?.header?.match_info_text.toColor(),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(1.dp, 10.dp)
                        .background(themeColors?.header?.divider.toColor())
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = match.time ?: "",
                    color = themeColors?.header?.match_info_text.toColor(),
                    fontSize = 11.sp
                )
            }
        }

        when (match.eventState) {
            EventState.LIVE -> LiveBadge(
                "Live",
                color = themeColors?.live_badge?.background.toColor(),
                textColor = themeColors?.live_badge?.text.toColor()
            )

            EventState.UPCOMING -> Badge(
                match.matchDisplayStatus,
                color = themeColors?.upcoming_badge?.background.toColor(),
                textColor = themeColors?.upcoming_badge?.text.toColor()
            )

            else -> Badge(
                match.matchDisplayStatus,
                color = themeColors?.result_badge?.background.toColor(),
                textColor = themeColors?.result_badge?.text.toColor()
            )
        }
    }
}

@Composable
fun MatchCardScore(match: IPLMatch, themeColors: MatchCenterThemeColors?) {
    val participants = match.participants ?: emptyList()
    if (participants.isEmpty()) return

    // Identify the two main teams: Home team on the left (Team A), Away team on the right (Team B)
    val teamAId = match.homeTeamId ?: participants.getOrNull(0)?.id
    val teamBId = match.awayTeamId ?: participants.getOrNull(1)?.id

    val teamAInnings = participants.filter { it.id == teamAId }
    val teamBInnings = participants.filter { it.id == teamBId }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        TeamScore(
            modifier = Modifier.weight(1f),
            mainInning = teamAInnings.firstOrNull { !it.isSuperOver },
            superOverInnings = teamAInnings.filter { it.isSuperOver },
            themeColors = themeColors
        )

        // VS circle
        Surface(
            color = themeColors?.team?.vs_background.toColor(),
            shape = CircleShape,
            modifier = Modifier
                .padding(top = 12.dp)
                .size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("vs", color = themeColors?.team?.vs_text.toColor(), fontSize = 10.sp)
            }
        }

        TeamScore(
            modifier = Modifier.weight(1f),
            mainInning = teamBInnings.firstOrNull { !it.isSuperOver },
            superOverInnings = teamBInnings.filter { it.isSuperOver },
            isReverse = true,
            themeColors
        )
    }
}

@Composable
fun LiveBadge(matchDisplayStatus: String?, color: Color, textColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "live_dot")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 0f, animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing), repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )

    Surface(
        color = color, shape = RoundedCornerShape(16.dp), modifier = Modifier.height(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = alpha))
                    .border(1.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = matchDisplayStatus ?: "",
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Badge(matchDisplayStatus: String?, color: Color, textColor: Color) {
    Text(
        text = matchDisplayStatus ?: "",
        color = textColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp
                )
            )
            .background(color)
            .padding(horizontal = 12.dp, vertical = 3.dp)
    )
}

@Composable
fun TeamScore(
    modifier: Modifier = Modifier,
    mainInning: Participant?,
    superOverInnings: List<Participant> = emptyList(),
    isReverse: Boolean = false,
    themeColors: MatchCenterThemeColors?
) {
    val isBatting = mainInning?.highlight == true || superOverInnings.any { it.highlight == true }

    val scoreColor =
        if (isBatting) themeColors?.team?.highlight_color.toColor() else themeColors?.team?.score.toColor()
    val overColor =
        if (isBatting) themeColors?.team?.highlight_color.toColor() else themeColors?.team?.overs.toColor()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = if (isReverse) Arrangement.End else Arrangement.Start
    ) {
        if (!isReverse) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TeamLogo(mainInning, isBatting, themeColors)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mainInning?.shortName ?: "",
                    color = themeColors?.team?.name.toColor(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(horizontalAlignment = if (isReverse) Alignment.End else Alignment.Start) {
            // Main Inning Score
            if (mainInning?.showYetToBat == true) {
                Text(
                    text = "YET TO\n BAT",
                    color = themeColors?.team?.yet_to_bat.toColor(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "${mainInning?.runs ?: "0"}/${mainInning?.wickets ?: "0"}",
                    color = scoreColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "(${mainInning?.overs ?: "0"}) RR:${mainInning?.runRate ?: "0.0"}",
                    color = overColor,
                    fontSize = 10.sp
                )
            }

            // Super Over Scores
            superOverInnings.forEach { soInning ->
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .width(40.dp)
                )
                val scoreColor = if (soInning.highlight == true) {
                    themeColors?.team?.highlight_color.toColor()
                } else {
                    themeColors?.team?.score.toColor()
                }
                val overColor = if (soInning.highlight == true) {
                    themeColors?.team?.highlight_color.toColor()
                } else {
                    themeColors?.team?.overs.toColor()
                }
                val isSecondSuperOver = (soInning.superOverNumber ?: 0) > 1
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${soInning.runs ?: "0"}/${soInning.wickets ?: "0"}",
                        color = scoreColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isSecondSuperOver) 10.sp else 14.sp
                    )
                    Text(
                        text = "(${soInning.overs ?: "0"})",
                        color = overColor,
                        fontSize = if (isSecondSuperOver) 8.sp else 10.sp
                    )
                }
            }
        }
        if (isReverse) {
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TeamLogo(mainInning, isBatting, themeColors)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mainInning?.shortName ?: "",
                    color = themeColors?.team?.name.toColor(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun TopPerformers(match: IPLMatch, performerSection: PerformerSection?) {
    if (match.eventState == EventState.UPCOMING) {
        return
    }
    if (match.topBatters.isNullOrEmpty() && match.topBowlers.isNullOrEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(performerSection?.background.toColor())
    ) {
        // Top Batters
        if (!match.topBatters.isNullOrEmpty()) {
            TopPerformerHeader(
                if (match.eventState == EventState.LIVE) "Batters" else "Top Batters",
                Icons.Default.SportsCricket,
                performerSection
            )
            match.topBatters.forEach { batter ->
                TopPerformerRow(
                    name = batter.name,
                    score = "${batter.runs ?: "0"}(${batter.balls ?: "0"})",
                    showIcon = batter.isCurrent,
                    icon = Icons.Default.SportsCricket,
                    performerSection
                )
            }
        }

        if (!match.topBatters.isNullOrEmpty() && !match.topBowlers.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Top Bowlers
        if (!match.topBowlers.isNullOrEmpty()) {
            TopPerformerHeader(
                if (match.eventState == EventState.LIVE) "Bowlers" else "Top Bowlers",
                Icons.Default.SportsBaseball,
                performerSection
            )
            match.topBowlers.forEach { bowler ->
                TopPerformerRow(
                    name = bowler.name,
                    score = "${bowler.wickets ?: "0"}/${bowler.runs ?: "0"}(${bowler.overs ?: "0"})",
                    showIcon = bowler.isCurrent,
                    icon = Icons.Default.SportsBaseball,
                    performerSection = performerSection
                )
            }
        }
    }
}

@Composable
fun TopPerformerHeader(title: String, icon: ImageVector, performerSection: PerformerSection?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(50.dp))
            .background(performerSection?.header_background.toColor()) // Light orange background
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = performerSection?.icon.toColor()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            color = performerSection?.header_text.toColor(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun TopPerformerRow(
    name: String,
    score: String,
    showIcon: Boolean = false,
    icon: ImageVector? = null,
    performerSection: PerformerSection?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = performerSection?.player_name.toColor(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            if (showIcon && icon != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = performerSection?.icon.toColor()
                )
            }
        }
        Text(
            text = score,
            color = performerSection?.stats.toColor(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TeamLogo(
    participant: Participant?, isBatting: Boolean = false, themeColors: MatchCenterThemeColors?
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(themeColors?.team_logos?.background.toColor())
            .then(
                if (isBatting) Modifier.border(
                    2.dp, themeColors?.team_logos?.border.toColor(), CircleShape
                ) else Modifier
            ), contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = participant?.teamImageUrl,
            contentDescription = participant?.name,
            modifier = Modifier.size(32.dp),
            contentScale = ContentScale.Fit
        )

    }
}

@Composable
fun BallByBallList(balls: List<String>?, lastBalls: LastBalls?) {
    if (balls.isNullOrEmpty()) return
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(lastBalls?.background.toColor())
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            Text(
                text = "Last 6 Balls:",
                color = lastBalls?.label.toColor(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        items(balls) { ball ->
            BallCircle(ball, lastBalls)
        }
    }
}

@Composable
fun BallCircle(ball: String, lastBalls: LastBalls?) {
    val displayBall = if (ball.contains("(") && ball.contains(")")) {
        ball.substringAfter("(").substringBefore(")")
    } else {
        ball
    }
    val backgroundColor = when (displayBall) {
        "4" -> lastBalls?.four_background
        "6" -> lastBalls?.six_background
        "W" -> lastBalls?.wicket_background
        else -> lastBalls?.dot_ball_background
    }
    val textColor = when (displayBall) {
        "4" -> lastBalls?.four_text
        "6" -> lastBalls?.six_text
        "W" -> lastBalls?.wicket_text
        else -> lastBalls?.dot_ball_text
    }
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(backgroundColor.toColor())
            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayBall,
            color = textColor.toColor(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

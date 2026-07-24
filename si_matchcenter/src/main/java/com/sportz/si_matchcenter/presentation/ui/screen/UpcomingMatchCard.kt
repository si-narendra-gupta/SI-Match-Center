package com.sportz.si_matchcenter.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportz.match_center_base.utils.CalendarUtils
import com.sportz.si_matchcenter.business.domain.model.IPLMatch
import com.sportz.si_matchcenter.business.domain.model.Participant
import com.sportz.si_matchcenter.business.domain.model.themecolor.MatchCenterThemeColors
import com.sportz.si_matchcenter.business.domain.model.themecolor.Team
import com.sportz.si_matchcenter.presentation.ui.theme.toColor
import kotlinx.coroutines.delay
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun UpcomingMatchInfo(match: IPLMatch, themeColors: MatchCenterThemeColors?) {

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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                MatchCardHeader(match, themeColors)

                // Teams Score Row
                MatchCardTeam(match, themeColors)

                Badge(
                    match.eventStatus,
                    themeColors?.upcoming_badge?.background.toColor(),
                    themeColors?.upcoming_badge?.text.toColor()
                )

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                )
                // status
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
            }
        }
    }
}


@Composable
fun MatchCardTeam(match: IPLMatch, themeColors: MatchCenterThemeColors?) {
    val participants = match.participants ?: emptyList()
    val homeParticipant =
        participants.find { it.id == match.homeTeamId } ?: participants.getOrNull(0)
    val awayParticipant =
        participants.find { it.id == match.awayTeamId } ?: participants.getOrNull(1)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MatchCardTeamLogoName(
            participant = homeParticipant, themeColors = themeColors
        )

        MatchCountdown(match = match, themeColors?.team)

        MatchCardTeamLogoName(
            participant = awayParticipant, themeColors = themeColors
        )
    }
}

@Composable
fun MatchCountdown(match: IPLMatch, teamTheme: Team?) {

    var days by remember { mutableStateOf("00") }
    var hours by remember { mutableStateOf("00") }
    var minutes by remember { mutableStateOf("00") }
    var seconds by remember { mutableStateOf("00") }

    LaunchedEffect(match.startDate) {
        val targetMillis = CalendarUtils.convertDateToMillis(
            match.startDate, CalendarUtils.MATCH_FULL_DATE_WITH_OFFSET
        )
        if (targetMillis > 0) {
            while (true) {
                val remaining = targetMillis - System.currentTimeMillis()
                if (remaining <= 0) break

                val d = TimeUnit.MILLISECONDS.toDays(remaining)
                val h = TimeUnit.MILLISECONDS.toHours(remaining) % 24
                val m = TimeUnit.MILLISECONDS.toMinutes(remaining) % 60
                val s = TimeUnit.MILLISECONDS.toSeconds(remaining) % 60

                days = String.format(Locale.US, "%02d", d)
                hours = String.format(Locale.US, "%02d", h)
                minutes = String.format(Locale.US, "%02d", m)
                seconds = String.format(Locale.US, "%02d", s)

                delay(1000)
            }
        }
    }

    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CountdownItem(days, "DAYS", teamTheme)
        VerticalDivider(
            modifier = Modifier.height(20.dp), color = teamTheme?.time_left_divider.toColor()
        )
        CountdownItem(hours, "HRS", teamTheme)
        VerticalDivider(
            modifier = Modifier.height(20.dp), color =teamTheme?.time_left_divider.toColor()
        )
        CountdownItem(minutes, "MINS", teamTheme)
        VerticalDivider(
            modifier = Modifier.height(20.dp), color = teamTheme?.time_left_divider.toColor()
        )
        CountdownItem(seconds, "SECS", teamTheme)
    }
}

@Composable
fun CountdownItem(value: String, label: String, teamTheme: Team?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = teamTheme?.time_left.toColor(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = label, color = teamTheme?.time_left_label.toColor(), fontSize = 9.sp
        )
    }
}

@Composable
fun MatchCardTeamLogoName(
    participant: Participant?, themeColors: MatchCenterThemeColors?
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TeamLogo(
            participant, themeColors = themeColors
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = participant?.shortName ?: "",
            color = themeColors?.team?.name.toColor(),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}


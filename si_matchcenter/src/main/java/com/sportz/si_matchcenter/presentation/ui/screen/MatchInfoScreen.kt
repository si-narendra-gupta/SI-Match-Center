package com.sportz.si_matchcenter.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportz.si_matchcenter.business.domain.model.IPLMatch
import com.sportz.si_matchcenter.business.domain.model.themecolor.MatchInfo
import com.sportz.si_matchcenter.presentation.ui.theme.toColor

@Composable
fun MatchInfoScreen(match: IPLMatch, matchInfo: MatchInfo?) {
    val infoItems = remember(match) {
        listOf(
            "Match" to match.matchNo,
            "Venue" to match.venueName,
            "Date & Time" to "${match.beautifiedStartDate} | ${match.time}",
            "Series" to match.seriesName,
            "Weather" to match.weather,
            "Toss" to match.toss,
            "Umpires" to match.umpires,
            "Referee" to match.referee,
            "Player of the Match" to match.playerOfTheMatch
        ).filter { !it.second.isNullOrEmpty() }
    }

    val dividerColor = remember(matchInfo) { matchInfo?.horizontal_divider.toColor() }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = matchInfo?.background.toColor()),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Column {
            infoItems.forEachIndexed { index, item ->
                MatchInfoRow(item.first, item.second, matchInfo)
                if (index < infoItems.lastIndex) {
                    HorizontalDivider(
                        thickness = 0.5.dp, color = dividerColor
                    )
                }
            }
        }
    }
}

@Composable
fun MatchInfoRow(label: String, value: String?, matchInfo: MatchInfo?) {
    if (value.isNullOrEmpty()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = matchInfo?.label.toColor(),
            modifier = Modifier.weight(0.35f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = matchInfo?.value.toColor(),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.65f)
        )
    }
}

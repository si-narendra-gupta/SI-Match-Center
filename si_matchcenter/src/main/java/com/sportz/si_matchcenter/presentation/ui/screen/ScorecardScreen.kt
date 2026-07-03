package com.sportz.si_matchcenter.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sportz.si_matchcenter.business.domain.model.IPLMatch
import com.sportz.si_matchcenter.business.domain.model.Participant
import com.sportz.si_matchcenter.business.domain.model.TeamSquadPlayer
import com.sportz.si_matchcenter.business.domain.model.themecolor.InningTeam
import com.sportz.si_matchcenter.business.domain.model.themecolor.CustomTabDTO
import com.sportz.si_matchcenter.business.domain.model.themecolor.ExtraTotal
import com.sportz.si_matchcenter.business.domain.model.themecolor.FallOfWicket
import com.sportz.si_matchcenter.business.domain.model.themecolor.ImpactPlayer
import com.sportz.si_matchcenter.business.domain.model.themecolor.Scorecard
import com.sportz.si_matchcenter.presentation.ui.theme.toColor

import androidx.compose.runtime.collectAsState
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterViewModel

@Composable
fun ScorecardScreen(match: IPLMatch, colortheme: CustomTabDTO?, viewModel: MatchCenterViewModel) {
    val teams = match.participants ?: return
    val uiState by viewModel.uiState.collectAsState()
    val selectedTeamIndex = uiState?.scorecardTeamIndex ?: 0
    val selectedTeam = teams.getOrNull(selectedTeamIndex) ?: return
    val tabScrollState = rememberScrollState()

    LaunchedEffect(selectedTeamIndex) {
        if (selectedTeamIndex > 0) {
            // Approximate scroll position to bring the selected tab into view
            // Each tab is roughly 80dp wide + 16dp spacing
            val scrollPos = (selectedTeamIndex * 200)
            tabScrollState.animateScrollTo(scrollPos)
        } else {
            tabScrollState.animateScrollTo(0)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
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
                    .then(if (teams.size > 2) Modifier.horizontalScroll(tabScrollState) else Modifier)
            ) {
                teams.forEachIndexed { index, team ->

                    val isSelected = selectedTeamIndex == index

                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .then(if (teams.size <= 2) Modifier.weight(1f) else Modifier.padding(horizontal = 16.dp))
                            .clickable { viewModel.onScorecardTeamSelected(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = team.shortName ?: "",
                            fontSize = 16.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) colortheme?.sub_tab?.selected_text.toColor() else colortheme?.sub_tab?.unselected_text.toColor(),
                            modifier = Modifier.padding(vertical = 8.dp),
                            maxLines = 1
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .then(if (teams.size <= 2) Modifier.fillMaxWidth() else Modifier.width(60.dp))
                                    .height(2.dp)
                                    .background(colortheme?.sub_tab?.selected_divider.toColor())
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Impact Players Legend Header
        ImpactPlayersLegendHeader(colortheme?.scorecard?.impact_player)

        Spacer(modifier = Modifier.height(8.dp))

        // Inning Details Header
        InningHeader(selectedTeam, colortheme?.scorecard?.inning_team)

        Spacer(modifier = Modifier.height(8.dp))

        // Batting Table
        BattingTable(selectedTeam,colortheme?.scorecard?.inning_team)

        // Impact Players Section
        ImpactPlayersSection(
            selectedTeam,
            colortheme?.scorecard
        )

        // Extras and Total
        ExtrasAndTotal(selectedTeam,colortheme?.scorecard?.extra_total)

        Spacer(modifier = Modifier.height(16.dp))

        // Fall of Wickets Header
        if (selectedTeam.fallOfWicketPlayers.isNotEmpty()) {
            FallOfWicketsSection(selectedTeam.fallOfWicketPlayers,colortheme?.scorecard?.fall_of_wicket)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bowling Table
        if (selectedTeam.bowlingScoreBoard.isNotEmpty()) {
            val bowlingTeamParticipant = teams.find { it.id == selectedTeam.bowlingTeamId }
            BowlingTable(
                battingTeam = selectedTeam, bowlingTeam = bowlingTeamParticipant,colortheme?.scorecard?.inning_team
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Impact Players Section For Bowling Team
        val bowlingTeamParticipant = teams.find { it.id == selectedTeam.bowlingTeamId }
        bowlingTeamParticipant?.let {
            ImpactPlayersSection(it, colortheme?.scorecard)
        }
    }
}

@Composable
fun ImpactPlayersLegendHeader(impactPlayer: ImpactPlayer?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = impactPlayer?.background.toColor()),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Impact\nPlayers",
                color = impactPlayer?.header.toColor(),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                lineHeight = 14.sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Player In",
                    color = impactPlayer?.title.toColor(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFF138808), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Player Out",
                    color = impactPlayer?.title.toColor(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFFD32F2F), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FallOfWicketsSection(fallOfWicketPlayers: List<TeamSquadPlayer?>, fallOfWicket: FallOfWicket?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = fallOfWicket?.background.toColor()),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.SportsCricket,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = fallOfWicket?.heading.toColor()
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "FALL OF WICKETS",
                    color = fallOfWicket?.heading.toColor(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "OVERS",
                    color = fallOfWicket?.heading.toColor(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        fallOfWicketPlayers.forEach { player ->

            if (player == null) return@forEach

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${player.fallOfWicket?.score}/${player.fallOfWicket?.wicketNo}",
                        modifier = Modifier.weight(1f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = fallOfWicket?.score.toColor()
                    )
                    Text(
                        text = "${player.name}${if (player.isCaptain) " (c)" else ""}${if (player.isKeeper) " (wk)" else ""}",
                        modifier = Modifier.weight(3f),
                        fontSize = 13.sp,
                        color = fallOfWicket?.player_name.toColor()
                    )
                    Text(
                        text = player.fallOfWicket?.overs ?: "",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        fontSize = 13.sp,
                        color = fallOfWicket?.over.toColor()
                    )
                }
                HorizontalDivider(thickness = 0.5.dp, color = fallOfWicket?.horizontal_divider.toColor())
            }
        }
    }
}

@Composable
fun InningHeader(team: Participant, inningTeam: InningTeam?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = inningTeam?.background.toColor()),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(inningTeam?.team_background.toColor(), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = team.teamImageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = team.name ?: "",
                    color = inningTeam?.team_name.toColor(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Batting", color = inningTeam?.role.toColor(), fontSize = 12.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${team.runs ?: "0"}/${team.wickets ?: "0"} (${team.overs ?: "0"} overs)",
                    color = inningTeam?.score.toColor(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "CRR: ${team.runRate ?: "0.00"}",
                    color = inningTeam?.run_rate.toColor(),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun BattingTable(team: Participant, inningTeam: InningTeam?) {

    val substitutedPlayer = remember(team.squad) {
        team.squad.find { !it.playerInName.isNullOrEmpty() && !it.playerOutName.isNullOrEmpty() }
    }

    val battingPlayers = remember(team.battingScoreBoard) {
        team.battingScoreBoard.filter { it?.battingStats != null }
    }

    val statsLabel = inningTeam?.stats_label.toColor()
    val statsValue = inningTeam?.stats_value.toColor()
    val hoDivider = inningTeam?.horizontal_divider.toColor()
    val playerName = inningTeam?.player_name.toColor()
    val playerEquation = inningTeam?.player_equation.toColor()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(inningTeam?.stats_background.toColor())
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(modifier = Modifier.weight(4f)) {
                Icon(
                    imageVector = Icons.Default.SportsCricket,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = statsLabel
                )
                Text(
                    text = "BATTING",
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = statsLabel
                )
            }

            Text(
                text = "R",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = statsLabel
            )
            Text(
                text = "B",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = statsLabel
            )
            Text(
                text = "4s",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = statsLabel
            )
            Text(
                text = "6s",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = statsLabel
            )
            Text(
                text = "SR",
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.End,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = statsLabel
            )
        }

        battingPlayers.forEach { player ->
            if (player == null) return@forEach
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Column(modifier = Modifier.weight(4f)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = buildString {
                                    append(player.name)
                                    if (player.isCaptain) append(" (c)")
                                    if (player.isKeeper) append(" (wk)")
                                },
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = playerName
                            )

                            substitutedPlayer?.let {

                                val isPlayerIn = player.id == it.playerInId
                                val isPlayerOut = player.id == it.playerOutId

                                when {
                                    isPlayerIn || isPlayerOut -> {
                                        val color = if (isPlayerIn) {
                                            Color(0xFF138808)
                                        } else {
                                            Color(0xFFD32F2F)
                                        }

                                        val icon = if (isPlayerIn) {
                                            Icons.Default.ArrowUpward
                                        } else {
                                            Icons.Default.ArrowDownward
                                        }

                                        Box(
                                            modifier = Modifier
                                                .padding(start = 8.dp)
                                                .size(20.dp)
                                                .background(
                                                    color = color, shape = CircleShape
                                                ), contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        player.battingStats?.howOutShort?.takeIf { it.isNotBlank() }?.let {
                            Text(
                                text = it,
                                fontSize = 11.sp,
                                color = playerEquation,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Text(
                        text = player.battingStats?.runs ?: "0",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = statsValue
                    )
                    Text(
                        text = player.battingStats?.balls ?: "0",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = statsValue
                    )
                    Text(
                        text = player.battingStats?.fours ?: "0",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = statsValue
                    )
                    Text(
                        text = player.battingStats?.sixes ?: "0",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = statsValue
                    )
                    Text(
                        text = player.battingStats?.strikeRate ?: "0.0",
                        modifier = Modifier.weight(1.5f),
                        textAlign = TextAlign.End,
                        fontSize = 14.sp,
                        color = statsValue
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp),
                    thickness = 0.5.dp,
                    color = hoDivider
                )
            }
        }
    }
}

@Composable
fun ImpactPlayersSection(team: Participant, scorecard: Scorecard?) {
    val battingTeam = scorecard?.inning_team
    val impactPlayer = scorecard?.impact_player
    val impactPlayers = team.squad.filter { it.isImpactPlayer }
    if (impactPlayers.isEmpty()) return

    val substitutedPlayer =
        team.squad.find { !it.playerInName.isNullOrEmpty() && !it.playerOutName.isNullOrEmpty() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Impact Players", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = battingTeam?.player_name.toColor()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "(${impactPlayers.joinToString(", ") { it.name ?: "" }})",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = battingTeam?.player_name.toColor()
        )

        if (substitutedPlayer != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = impactPlayer?.background.toColor()),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(Color(0xFF138808), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = substitutedPlayer.playerInName ?: "",
                                color = impactPlayer?.player_name.toColor(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(Color(0xFFD32F2F), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = substitutedPlayer.playerOutName ?: "",
                                color = impactPlayer?.player_name.toColor(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.SyncAlt,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExtrasAndTotal(team: Participant, extraTotal: ExtraTotal?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(extraTotal?.background.toColor(), RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Extras:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = extraTotal?.extra_label.toColor())
            Text(text = team.extraRuns?.displayText ?: "", fontSize = 12.sp, color = extraTotal?.extra_value.toColor())

        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Total",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = extraTotal?.total_label.toColor()
            )
            Text(
                text = "${team.runs ?: "0"}/${team.wickets ?: "0"} (${team.overs ?: "0"} Ov)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color =extraTotal?.total_value.toColor()
            )
        }
    }
}

@Composable
fun BowlingTable(
    battingTeam: Participant, bowlingTeam: Participant?, teamColor: InningTeam?
) {
    val substitutedPlayer = remember(bowlingTeam?.squad) {
        bowlingTeam?.squad?.find { !it.playerInName.isNullOrEmpty() && !it.playerOutName.isNullOrEmpty() }
    }

    val bowlingPlayers = remember(battingTeam.bowlingScoreBoard) {
        battingTeam.bowlingScoreBoard.filter { it?.bowlingStats != null }
    }


    val statsLabel = teamColor?.stats_label.toColor()
    val statsValue = teamColor?.stats_value.toColor()
    val hoDivider = teamColor?.horizontal_divider.toColor()
    val playerName = teamColor?.player_name.toColor()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(teamColor?.team_background.toColor(), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = bowlingTeam?.teamImageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = bowlingTeam?.shortName ?: "",
                color = teamColor?.team_name.toColor(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(teamColor?.stats_background.toColor())
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(4f)
            ) {
                Icon(
                    imageVector = Icons.Default.SportsBaseball,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = statsLabel
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "Bowling",
                    color = statsLabel,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Text(
                color = statsLabel,
                text = "O",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                color = statsLabel,
                text = "M",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                color = statsLabel,
                text = "R",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                color = statsLabel,
                text = "W",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                color =statsLabel,
                text = "ER",
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.End,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        bowlingPlayers.forEach { player ->
            if (player == null) return@forEach
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(4f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = player.name.orEmpty(),
                            modifier = Modifier.weight(1f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = playerName
                        )

                        substitutedPlayer?.let {

                            val isPlayerIn = player.id == it.playerInId
                            val isPlayerOut = player.id == it.playerOutId

                            when {
                                isPlayerIn || isPlayerOut -> {

                                    val color = when {
                                        isPlayerIn -> Color(0xFF138808)
                                        else -> Color(0xFFD32F2F)
                                    }

                                    val icon = when {
                                        isPlayerIn -> Icons.Default.ArrowUpward
                                        else -> Icons.Default.ArrowDownward
                                    }

                                    Box(
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .size(20.dp)
                                            .background(
                                                color = color, shape = CircleShape
                                            ), contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = player.bowlingStats?.overs ?: "0",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = statsValue
                    )

                    Text(
                        text = player.bowlingStats?.maidens ?: "0",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = statsValue
                    )

                    Text(
                        text = player.bowlingStats?.runs ?: "0",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = statsValue
                    )

                    Text(
                        text = player.bowlingStats?.wickets ?: "0",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = statsValue
                    )

                    Text(
                        text = player.bowlingStats?.economyRate ?: "0.0",
                        modifier = Modifier.weight(1.5f),
                        textAlign = TextAlign.End,
                        fontSize = 14.sp,
                        color = statsValue
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp),
                    thickness = 0.5.dp,
                    color = hoDivider
                )
            }
        }
    }
}

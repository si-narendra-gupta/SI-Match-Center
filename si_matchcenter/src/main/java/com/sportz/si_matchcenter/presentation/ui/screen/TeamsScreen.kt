package com.sportz.si_matchcenter.presentation.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.business.domain.model.IPLMatch
import com.sportz.si_matchcenter.business.domain.model.Participant
import com.sportz.si_matchcenter.business.domain.model.TeamSquadPlayer
import com.sportz.si_matchcenter.business.domain.model.themecolor.PlayingTeam
import com.sportz.si_matchcenter.business.domain.model.themecolor.SubTab
import com.sportz.si_matchcenter.presentation.ui.theme.toColor

import androidx.compose.runtime.collectAsState
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterViewModelMatchCenter

@Composable
fun TeamsScreen(
    match: IPLMatch,
    subTab: SubTab?,
    teamsColor: PlayingTeam?,
    viewModel: MatchCenterViewModelMatchCenter
) {
    val teams = match.participants?.distinctBy { it.id } ?: return
    val uiState by viewModel.uiState.collectAsState()
    val selectedTeamIndex = uiState?.teamsTabTeamIndex ?: 0
    val selectedTeam = teams.getOrNull(selectedTeamIndex)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        // Team Selector Tabs
        TeamSelectionTabs(
            teams = teams,
            selectedIndex = selectedTeamIndex,
            onTeamSelected = { viewModel.onTeamsTabTeamSelected(it) },
            subTab
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!selectedTeam?.squad.isNullOrEmpty()) {

            Text(
                text = when (match.eventState) {
                    EventState.UPCOMING -> "SQUAD"
                    else -> "PLAYING XI"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = teamsColor?.header_title.toColor(),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            val squad = selectedTeam.squad
            
            val squadList = when (match.eventState) {
                EventState.UPCOMING -> squad
                else -> squad.filter { it.iSConfirmXI }
            }
            squadList.forEach { player ->
                PlayerCard(player, teamsColor)
                Spacer(modifier = Modifier.height(8.dp))
            }
            val impactPlayer = squad.filter { it.isImpactPlayer }
            if (impactPlayer.isNotEmpty()) {
                Text(
                    text = "IMPACT PLAYERS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = teamsColor?.header_title.toColor(),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                impactPlayer.forEach { player ->
                    PlayerCard(player, teamsColor)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun TeamSelectionTabs(
    teams: List<Participant>, selectedIndex: Int, onTeamSelected: (Int) -> Unit, subTab: SubTab?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        teams.forEachIndexed { index, team ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(Color.Transparent)
                    .padding(bottom = 2.dp), contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = { onTeamSelected(index) },
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(
                        text = team.shortName ?: "",
                        color = if (isSelected) subTab?.selected_text.toColor() else subTab?.unselected_text.toColor(),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp
                    )
                }
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(0.8f)
                            .height(3.dp)
                            .background(subTab?.selected_divider.toColor())
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerCard(
    player: TeamSquadPlayer, teamsColor: PlayingTeam?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = teamsColor?.background.toColor()
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(
            1.dp, teamsColor?.border.toColor()
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(teamsColor?.left_divider.toColor())
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = buildAnnotatedString {
                        player.firstName?.let {
                            withStyle(
                                SpanStyle(
                                    color = teamsColor?.first_name.toColor()
                                )
                            ) {
                                append(it)
                            }
                        }
                        append(" ")

                        player.lastName?.let {
                            withStyle(
                                SpanStyle(
                                    color = teamsColor?.last_name.toColor(),
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(it)
                            }
                        }
                    }, fontSize = 16.sp
                )

                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxHeight(),
                    thickness = 0.5.dp,
                    color = teamsColor?.vertical_divider.toColor()
                )

                Text(
                    text = player.role.orEmpty(),
                    color = teamsColor?.skill.toColor(),
                    fontSize = 12.sp
                )
            }
        }
    }
}

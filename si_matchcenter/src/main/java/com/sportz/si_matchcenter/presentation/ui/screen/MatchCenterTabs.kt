package com.sportz.si_matchcenter.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportz.si_matchcenter.business.domain.model.MatchTabItem
import com.sportz.si_matchcenter.business.domain.model.themecolor.CustomTabDTO
import com.sportz.si_matchcenter.presentation.ui.theme.toColor


@Composable
fun MatchCenterTabSelector(
    tabs: List<MatchTabItem>,
    selectedTab: MatchTabItem?,
    onTabSelected: (MatchTabItem) -> Unit,
    matchDetails: CustomTabDTO?
) {
    val isScrollable = tabs.size > 3
    val scrollState = rememberScrollState()
    val shape = RoundedCornerShape(6.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .clip(shape)
            .border(1.dp, matchDetails?.tab?.border.toColor(), shape)
            .background(matchDetails?.tab?.background.toColor())
            .then(if (isScrollable) Modifier.horizontalScroll(scrollState) else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {

        tabs.forEach { tab ->

            val isSelected = selectedTab?.id == tab.id
            Box(
                modifier = Modifier
                    .then(
                        if (isScrollable) {
                            Modifier.widthIn(min = 100.dp)
                        } else {
                            Modifier.weight(1f)
                        }
                    )
                    .fillMaxHeight()
                    .clip(shape)
                    .background(
                        if (isSelected) matchDetails?.tab?.selected_background.toColor() else Color.Transparent
                    )
                    .clickable {
                        onTabSelected(tab)
                    }, contentAlignment = Alignment.Center
            ) {

                Text(
                    text = tab.title ?: "", style = TextStyle(
                        color = if (isSelected) matchDetails?.tab?.selected_text.toColor() else matchDetails?.tab?.unselected_text.toColor(),
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    )
                )
            }
        }
    }
}

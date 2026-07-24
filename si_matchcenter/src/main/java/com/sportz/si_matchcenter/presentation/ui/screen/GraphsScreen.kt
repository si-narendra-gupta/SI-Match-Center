package com.sportz.si_matchcenter.presentation.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportz.si_matchcenter.business.domain.model.EventState
import com.sportz.si_matchcenter.business.domain.model.IPLMatch
import com.sportz.si_matchcenter.business.domain.model.MatchTabItem
import com.sportz.si_matchcenter.business.domain.model.OverData
import com.sportz.si_matchcenter.business.domain.model.SpiderRegion
import com.sportz.si_matchcenter.business.domain.model.SpiderShot
import com.sportz.si_matchcenter.business.domain.model.themecolor.CustomTabDTO
import com.sportz.si_matchcenter.business.domain.model.themecolor.GraphColor
import com.sportz.si_matchcenter.business.domain.model.themecolor.SpiderColor
import com.sportz.si_matchcenter.business.domain.model.themecolor.Tab
import com.sportz.si_matchcenter.presentation.ui.theme.toColor
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterUiState
import com.sportz.si_matchcenter.presentation.ui.viewmodel.MatchCenterViewModelMatchCenter
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GraphsScreen(
    match: IPLMatch,
    subTabs: List<MatchTabItem>,
    viewModel: MatchCenterViewModelMatchCenter,
    colortheme: CustomTabDTO?
) {

    val uiState by viewModel.uiState.collectAsState()
    val selectedSubTabId = uiState?.selectedSubTabId
    val selectedSubTab = remember(selectedSubTabId, subTabs) {
        subTabs.find { it.id == selectedSubTabId } ?: subTabs.firstOrNull { it.selected } ?: subTabs.firstOrNull()
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colortheme?.screen?.background.toColor())
    ) {
        // Sub-tabs
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
                    .height(40.dp)
                    .then(if (subTabs.size > 2) Modifier.horizontalScroll(scrollState) else Modifier),
                verticalAlignment = Alignment.CenterVertically
            ) {
                subTabs.forEach { tab ->
                    val isSelected = selectedSubTab == tab
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .then(
                                if (subTabs.size <= 2) Modifier.weight(1f) else Modifier.padding(
                                    horizontal = 16.dp
                                )
                            )
                            .clickable { viewModel.selectedSubTab(tab.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.title ?: "",
                            color = if (isSelected) colortheme?.sub_tab?.selected_text.toColor() else colortheme?.sub_tab?.unselected_text.toColor(),
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .then(
                                        if (subTabs.size <= 2) Modifier.fillMaxWidth() else Modifier.width(
                                            80.dp
                                        )
                                    )
                                    .height(2.dp)
                                    .background(colortheme?.sub_tab?.selected_divider.toColor())
                            )
                        }
                    }
                }
            }
        }

        when (selectedSubTab?.id) {
            "manhattan" -> {
                if (uiState?.isManhattanLoading == true) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colortheme?.sub_tab?.selected_divider.toColor())
                    }
                } else {
                    ManhattanTab(uiState?.manhattanData ?: emptyList(), match, colortheme?.graph)
                }
            }

            "innings_progression" -> {
                if (uiState?.isManhattanLoading == true) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colortheme?.sub_tab?.selected_divider.toColor())
                    }
                } else {
                    InningsProgressionTab(
                        uiState?.manhattanData ?: emptyList(), match, colortheme?.graph
                    )
                }
            }

            "spider" -> {
                SpiderTab(match, colortheme?.graph, uiState, viewModel)
            }

            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "${selectedSubTab?.title} Coming Soon")
                }
            }
        }
    }
}

@Composable
fun ManhattanTab(data: List<OverData>, match: IPLMatch, graph: GraphColor?) {
    val team1 = match.participants?.getOrNull(0)?.shortName ?: "T1" // Inning 1
    val team2 = match.participants?.getOrNull(1)?.shortName ?: "T2" // Inning 2

    val team1BarColor = graph?.first_team_bar_indicator.toColor()
    val team2BarColor = graph?.second_team_bar_indicator.toColor()
    val teamNameColor = graph?.team_name.toColor()

    val oversToShow = 4

    // If live, initial over should focus on the current running over (the last one available in data)
    val initialStartOver = remember(data) {
        if (match.eventState == EventState.LIVE && data.isNotEmpty()) {
            val lastOver = if (match.totalInnings >= 2) {
                data.findLast { it.isTeam2Over }?.overNumber ?: data.maxOfOrNull { it.overNumber }
                ?: 1
            } else {
                data.findLast { it.isTeam1Over }?.overNumber ?: data.maxOfOrNull { it.overNumber }
                ?: 1
            }
            (lastOver - oversToShow + 1).coerceAtLeast(1)
        } else {
            1
        }
    }

    var startOver by remember(initialStartOver) { mutableIntStateOf(initialStartOver) }
    val visibleData = data.filter { it.overNumber in startOver until (startOver + oversToShow) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        // Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(team1, team1BarColor, teamNameColor)
            Spacer(modifier = Modifier.width(16.dp))
            LegendItem(team2, team2BarColor, teamNameColor)
        }

        // Manhattan Chart
        ManhattanChart(
            data = visibleData,
            team1Color = team1BarColor,
            team2Color = team2BarColor,
            runsColor = graph?.runs.toColor(),
            wicketBGColor = graph?.wicket_background.toColor(),
            wicketTextColor = graph?.wicket_text.toColor(),
            dataPointColor = graph?.data_point.toColor()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Overs Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = if (startOver > 1) graph?.active_arrow.toColor() else graph?.inactive_arrow.toColor(),
                modifier = Modifier
                    .size(32.dp)
                    .clickable(enabled = startOver > 1) {
                        startOver -= 1
                    })
            Text(
                text = "Overs", style = TextStyle(
                    color = graph?.overs.toColor(), fontSize = 16.sp, fontWeight = FontWeight.Bold
                ), modifier = Modifier.padding(horizontal = 16.dp)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = if (startOver + oversToShow <= data.size) graph?.active_arrow.toColor() else graph?.inactive_arrow.toColor(),
                modifier = Modifier
                    .size(32.dp)
                    .clickable(enabled = startOver + oversToShow <= data.size) {
                        startOver += 1
                    })
        }
    }
}

@Composable
fun LegendItem(name: String, barColor: Color, teamNameColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(barColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name, style = TextStyle(
                color = teamNameColor, fontSize = 14.sp, fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun ManhattanChart(
    data: List<OverData>,
    team1Color: Color,
    team2Color: Color,
    runsColor: Color,
    wicketBGColor: Color = Color.Red,
    wicketTextColor: Color,
    dataPointColor: Color
) {
    val ySteps = 7
    val chartMetrics = remember(data) {
        val maxRuns =
            data.flatMap { listOf(it.team1Runs, it.team2Runs) }.maxOrNull()?.coerceAtLeast(1) ?: 1
        val stepSize = kotlin.math.ceil(maxRuns.toFloat() / ySteps).toInt().coerceAtLeast(1)
        val maxY = stepSize * ySteps
        Triple(maxRuns, stepSize, maxY)
    }
    val stepSize = chartMetrics.second
    val maxY = chartMetrics.third

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val paddingLeft = 35.dp.toPx()
            val paddingBottom = 60.dp.toPx()
            val chartWidth = width - paddingLeft
            val chartHeight = height - paddingBottom

            // Draw X and Y axes
            drawLine(
                color = Color.LightGray,
                start = Offset(paddingLeft, 0f),
                end = Offset(paddingLeft, chartHeight),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = Color.LightGray,
                start = Offset(paddingLeft, chartHeight),
                end = Offset(width, chartHeight),
                strokeWidth = 1.dp.toPx()
            )

            // Draw Y-axis grid lines and labels
            for (i in 1..ySteps) {
                val yVal = i * stepSize
                val yPos = chartHeight - (yVal.toFloat() / maxY * chartHeight)

                // Grid line
                drawLine(
                    color = Color.LightGray,
                    start = Offset(paddingLeft, yPos),
                    end = Offset(width, yPos),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                )

                // Label
                drawContext.canvas.nativeCanvas.drawText(
                    yVal.toString(),
                    paddingLeft - 5.dp.toPx(),
                    yPos + 5.dp.toPx(),
                    android.graphics.Paint().apply {
                        color = dataPointColor.toArgb()
                        textSize = 12.sp.toPx()
                        textAlign = android.graphics.Paint.Align.RIGHT
                        isFakeBoldText = true
                    })
            }

            // Draw X-axis label "Runs"
            drawContext.canvas.nativeCanvas.drawText(
                "Runs", 0.dp.toPx(), height - 10.dp.toPx(), android.graphics.Paint().apply {
                    color = runsColor.toArgb()
                    textSize = 12.sp.toPx()
                    isFakeBoldText = true
                })

            // Draw Bars
            val visibleOvers = data
            val overWidth = chartWidth / visibleOvers.size
            val barWidth = overWidth * 0.25f

            visibleOvers.forEachIndexed { index, over ->
                val xBase = paddingLeft + index * overWidth + overWidth / 2
                val barGap = 2.dp.toPx()

                // Team 1 Bar (Blue, leftmost)
                if (over.isTeam1Over) {
                    drawManhattanBar(
                        x = xBase - barWidth - barGap / 2,
                        yBase = chartHeight,
                        barWidth = barWidth,
                        barHeight = (over.team1Runs.toFloat() / maxY * chartHeight).coerceAtLeast(1f),
                        color = team1Color
                    )
                }

                // Team 2 Bar (Pink, rightmost)
                if (over.isTeam2Over) {
                    drawManhattanBar(
                        x = xBase + barGap / 2,
                        yBase = chartHeight,
                        barWidth = barWidth,
                        barHeight = (over.team2Runs.toFloat() / maxY * chartHeight).coerceAtLeast(1f),
                        color = team2Color
                    )
                }

                // Wicket markers
                val depth = 5.dp.toPx()
                if (over.team1Wickets > 0) {
                    drawWicketMarker(
                        x = xBase - barWidth / 2 - barGap / 2 + depth / 2,
                        y = chartHeight - (over.team1Runs.toFloat() / maxY * chartHeight) - 16.dp.toPx(),
                        wicketBGColor = wicketBGColor,
                        wicketTextColor = wicketTextColor
                    )
                }
                if (over.team2Wickets > 0) {
                    drawWicketMarker(
                        x = xBase + barWidth / 2 + barGap / 2 + depth / 2,
                        y = chartHeight - (over.team2Runs.toFloat() / maxY * chartHeight) - 16.dp.toPx(),
                        wicketBGColor = wicketBGColor,
                        wicketTextColor = wicketTextColor
                    )
                }

                // X-axis over labels
                drawContext.canvas.nativeCanvas.drawText(
                    over.overNumber.toString(),
                    xBase,
                    height - 10.dp.toPx(),
                    android.graphics.Paint().apply {
                        color = dataPointColor.toArgb()
                        textSize = 14.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                        isFakeBoldText = true
                    })
            }
        }
    }
}

@Composable
fun InningsProgressionTab(data: List<OverData>, match: IPLMatch, graph: GraphColor?) {
    val team1 = match.participants?.getOrNull(0)?.shortName ?: "T1"
    val team2 = match.participants?.getOrNull(1)?.shortName ?: "T2"

    val team1BarColor = graph?.first_team_bar_indicator.toColor()
    val team2BarColor = graph?.second_team_bar_indicator.toColor()

    val teamNameColor = graph?.team_name.toColor()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        // Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(team1, team1BarColor, teamNameColor)
            Spacer(modifier = Modifier.width(16.dp))
            LegendItem(team2, team2BarColor, teamNameColor)
        }

        // Progression Chart
        ProgressionChart(
            data = data,
            team1Color = team1BarColor,
            team2Color = team1BarColor,
            wicketBGColor = graph?.wicket_background.toColor(),
            runsColor = graph?.runs.toColor(),
            dataPointColor = graph?.data_point.toColor(),
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Overs",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(
                color = graph?.overs.toColor(), fontSize = 16.sp, fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun ProgressionChart(
    data: List<OverData>,
    team1Color: Color,
    team2Color: Color,
    wicketBGColor: Color = Color.Red,
    wicketTextColor: Color = Color.White,
    runsColor: Color,
    dataPointColor: Color
) {
    // Calculate cumulative runs separately for each team
    val team1Points = mutableListOf<Int>()
    team1Points.add(0)
    var t1Cumulative = 0
    data.filter { it.isTeam1Over }.forEach { over ->
        t1Cumulative += over.team1Runs
        team1Points.add(t1Cumulative)
    }

    val team2Points = mutableListOf<Int>()
    team2Points.add(0)
    var t2Cumulative = 0
    data.filter { it.isTeam2Over }.forEach { over ->
        t2Cumulative += over.team2Runs
        team2Points.add(t2Cumulative)
    }

    val maxRuns = maxOf(t1Cumulative, t2Cumulative).coerceAtLeast(1)
    val ySteps = 5
    // Find a nice step size (e.g., 50, 60, etc.)
    val rawStep = maxRuns.toFloat() / ySteps
    val stepSize =
        if (rawStep <= 0) 10 else (kotlin.math.ceil(rawStep / 10).toInt() * 10).coerceAtLeast(10)
    val maxY = stepSize * ySteps

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val paddingLeft = 35.dp.toPx()
            val paddingBottom = 60.dp.toPx()
            val chartWidth = width - paddingLeft
            val chartHeight = height - paddingBottom

            // Draw X and Y axes
            drawLine(
                color = Color.LightGray,
                start = Offset(paddingLeft, 0f),
                end = Offset(paddingLeft, chartHeight),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = Color.LightGray,
                start = Offset(paddingLeft, chartHeight),
                end = Offset(width, chartHeight),
                strokeWidth = 1.dp.toPx()
            )

            // Draw Y-axis grid lines and labels
            for (i in 0..ySteps) {
                val yVal = i * stepSize
                val yPos = chartHeight - (yVal.toFloat() / maxY * chartHeight)

                drawLine(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    start = Offset(paddingLeft, yPos),
                    end = Offset(width, yPos),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )

                drawContext.canvas.nativeCanvas.drawText(
                    yVal.toString(),
                    paddingLeft - 10.dp.toPx(),
                    yPos + 5.dp.toPx(),
                    android.graphics.Paint().apply {
                        color = dataPointColor.toArgb()
                        textSize = 12.sp.toPx()
                        textAlign = android.graphics.Paint.Align.RIGHT
                        isFakeBoldText = true
                    })
            }

            // Draw X-axis over labels (0, 5, 10, 15, 20)
            val maxOvers = data.size.coerceAtLeast(1)
            val xLabelStep = when {
                maxOvers <= 5 -> 1
                maxOvers <= 10 -> 2
                else -> 5
            }

            val labelPoints = (0..maxOvers step xLabelStep).toMutableList()
            if (!labelPoints.contains(maxOvers)) {
                labelPoints.add(maxOvers)
            }

            labelPoints.forEach { i ->
                val xPos = paddingLeft + (i.toFloat() / maxOvers * chartWidth)
                drawContext.canvas.nativeCanvas.drawText(
                    i.toString(), xPos, height - 10.dp.toPx(), android.graphics.Paint().apply {
                        color = dataPointColor.toArgb()
                        textSize = 14.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                        isFakeBoldText = true
                    })

                // Vertical ticks
                drawLine(
                    color = Color.LightGray,
                    start = Offset(xPos, chartHeight),
                    end = Offset(xPos, chartHeight + 5.dp.toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            }

            drawContext.canvas.nativeCanvas.drawText(
                "Runs", 0.dp.toPx(), height - 10.dp.toPx(), android.graphics.Paint().apply {
                    color = runsColor.toArgb()
                    textSize = 12.sp.toPx()
                    isFakeBoldText = true
                })

            // Draw Paths
            val overStep = chartWidth / maxOvers

            // Team 1 Path
            if (team1Points.size > 1) {
                val path1 = Path().apply {
                    moveTo(paddingLeft, chartHeight)
                    team1Points.forEachIndexed { index, points ->
                        val x = paddingLeft + index * overStep
                        val y = chartHeight - (points.toFloat() / maxY * chartHeight)
                        lineTo(x, y)
                    }
                }

                // Area 1
                val areaPath1 = Path().apply {
                    addPath(path1)
                    lineTo(paddingLeft + (team1Points.size - 1) * overStep, chartHeight)
                    lineTo(paddingLeft, chartHeight)
                    close()
                }
                drawPath(
                    path = areaPath1, brush = Brush.verticalGradient(
                        colors = listOf(
                            team1Color.copy(alpha = 0.4f), team1Color.copy(alpha = 0.05f)
                        ),
                        startY = chartHeight - (t1Cumulative.toFloat() / maxY * chartHeight),
                        endY = chartHeight
                    )
                )
                drawPath(
                    path = path1,
                    color = team1Color,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Team 2 Path
            if (team2Points.size > 1) {
                val path2 = Path().apply {
                    moveTo(paddingLeft, chartHeight)
                    team2Points.forEachIndexed { index, points ->
                        val x = paddingLeft + index * overStep
                        val y = chartHeight - (points.toFloat() / maxY * chartHeight)
                        lineTo(x, y)
                    }
                }

                // Area 2
                val areaPath2 = Path().apply {
                    addPath(path2)
                    lineTo(paddingLeft + (team2Points.size - 1) * overStep, chartHeight)
                    lineTo(paddingLeft, chartHeight)
                    close()
                }
                drawPath(
                    path = areaPath2, brush = Brush.verticalGradient(
                        colors = listOf(
                            team2Color.copy(alpha = 0.4f), team2Color.copy(alpha = 0.05f)
                        ),
                        startY = chartHeight - (t2Cumulative.toFloat() / maxY * chartHeight),
                        endY = chartHeight
                    )
                )
                drawPath(
                    path = path2,
                    color = team2Color,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Wicket markers
            var t1CurrentRuns = 0
            var t2CurrentRuns = 0
            data.forEachIndexed { index, over ->
                val x = paddingLeft + (index + 1) * overStep

                if (over.isTeam1Over && over.team1Wickets > 0) {
                    t1CurrentRuns += over.team1Runs
                    val y = chartHeight - (t1CurrentRuns.toFloat() / maxY * chartHeight)
                    drawWicketMarker(x, y, wicketBGColor, wicketTextColor)
                } else if (over.isTeam1Over) {
                    t1CurrentRuns += over.team1Runs
                }

                if (over.isTeam2Over && over.team2Wickets > 0) {
                    t2CurrentRuns += over.team2Runs
                    val y = chartHeight - (t2CurrentRuns.toFloat() / maxY * chartHeight)
                    drawWicketMarker(x, y, wicketBGColor, wicketTextColor)
                } else if (over.isTeam2Over) {
                    t2CurrentRuns += over.team2Runs
                }
            }
        }
    }
}

fun DrawScope.drawManhattanBar(
    x: Float, yBase: Float, barWidth: Float, barHeight: Float, color: Color
) {
    val depth = 5.dp.toPx()
    val topY = yBase - barHeight

    // Front Face
    drawRect(
        color = color, topLeft = Offset(x, topY), size = Size(barWidth, barHeight)
    )

    // Top Face (for 3D effect)
    val topPath = Path().apply {
        moveTo(x, topY)
        lineTo(x + depth, topY - depth)
        lineTo(x + barWidth + depth, topY - depth)
        lineTo(x + barWidth, topY)
        close()
    }
    drawPath(topPath, color = color.copy(alpha = 0.8f))

    // Side Face (for 3D effect)
    val sidePath = Path().apply {
        moveTo(x + barWidth, topY)
        lineTo(x + barWidth + depth, topY - depth)
        lineTo(x + barWidth + depth, yBase - depth)
        lineTo(x + barWidth, yBase)
        close()
    }
    drawPath(sidePath, color = color.copy(alpha = 0.6f))
}

@Composable
fun SpiderTab(
    match: IPLMatch,
    graph: GraphColor?,
    uiState: MatchCenterUiState?,
    viewModel: MatchCenterViewModelMatchCenter
) {
    val selectedInning = uiState?.selectedInning ?: 1
    val spiderData = uiState?.spiderData
    val isLoading = uiState?.isSpiderLoading ?: false

    val shots = spiderData?.shotsList ?: emptyList()
    val regions = spiderData?.regionsList ?: emptyList()

    Spacer(modifier = Modifier.height(20.dp))

    TeamScoreInfo(match, graph?.tab, selectedInning) { inning ->
        match.matchGameId?.let { viewModel.fetchSpiderData(it, inning) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(360.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = graph?.tab?.selected_background.toColor())
            }
        } else if (spiderData != null) {
            SpiderChart(shots, regions, graph?.spider)
            Spacer(modifier = Modifier.height(32.dp))
            RunLegend(shots, graph?.spider)
            Spacer(modifier = Modifier.height(32.dp))
        } else {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(320.dp), contentAlignment = Alignment.Center) {
                Text(text = "No Spider Data Available", color = graph?.team_name.toColor())
            }
        }
    }
}

@Composable
fun SpiderChart(shots: List<SpiderShot>, regions: List<SpiderRegion>, spider: SpiderColor?) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)      // Takes 85% of screen width
            .aspectRatio(1f)          // Height = Width
            .sizeIn(maxWidth = 320.dp, maxHeight = 320.dp), contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width / 2
            val innerRadius = radius * 0.95f

            // Red outer ring
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        spider?.gradient_end.toColor(),
                        spider?.gradient_start.toColor(),
                        spider?.gradient_end.toColor(),
                    ), center = center
                ),

                radius = radius + 12.dp.toPx(),
                center = center,
                style = Stroke(width = 24.dp.toPx())
            )

            // Outer black thin border for the ring
            drawCircle(
                color = spider?.outer_circle.toColor(),
                radius = radius + 24.dp.toPx(),
                center = center,
                style = Stroke(width = 4.dp.toPx())
            )
            // Inner black thin border for the ring
            drawCircle(
                color = spider?.boundary_circle.toColor(),
                radius = radius,
                center = center,
                style = Stroke(width = 6.dp.toPx())
            )

            // Dashed Circles
            drawCircle(
                color = Color.Gray, radius = innerRadius * 0.6f, center = center, style = Stroke(
                    width = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
            )

            drawCricketPitch(center)

            // Shots
            shots.forEach { shot ->
                // Square leg at 0 degree, anti-clockwise: +x -> -y (top) -> -x -> +y (bottom)
                val angleRad = shot.angle * PI.toFloat() / 180f
                // Adjust length based on runs: 6s touch the boundary circle (radius)

                // Calculate line length
                val length = if (shot.runs < 6) {
                    // 1, 2, 3,4,5  runs -> scale using distance
                    radius * (shot.distance / 5f).coerceIn(0f, 0.97f)
                } else {
                    //6 runs -> always draw to the boundary
                    radius * 1.0f //(if (shot.runs == 6) 1.0f else 0.97f)
                }


                val endX = center.x + cos(angleRad) * length
                val endY = center.y - sin(angleRad) * length // Negative sin for anti-clockwise top

                val color = when (shot.runs) {
                    1 -> spider?.one_bg.toColor()
                    2 -> spider?.two_bg.toColor()
                    3 -> spider?.three_bg.toColor()
                    4 -> spider?.four_bg.toColor()
                    6 -> spider?.six_bg.toColor()
                    else -> Color.Gray
                }

                drawLine(
                    color = color,
                    start = center,
                    end = Offset(endX, endY),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Region Labels (Inside Red Ring)
            val paint = android.graphics.Paint().apply {
                color = spider?.fielding_postion.toColor().toArgb()
                textSize = 10.sp.toPx()
                textAlign = android.graphics.Paint.Align.CENTER
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }

            val regionAngle = 360f / regions.size
            regions.forEachIndexed { index, region ->
                val angle = index * regionAngle + 20f
                val angleRad = angle * PI.toFloat() / 180f

                // Position for region name (Inside the red ring)
                val labelX = center.x + cos(angleRad) * (radius + 12.dp.toPx())
                val labelY = center.y - sin(angleRad) * (radius + 12.dp.toPx())

                drawContext.canvas.nativeCanvas.save()

                var textRotation = -angle + 90
                // If the label is in the bottom half of the circle, flip it so it's right-side up
                if (angle in 180f..360f) {
                    textRotation += 180f
                }

                // Rotate text to follow the circle (tangential)
                drawContext.canvas.nativeCanvas.rotate(textRotation, labelX, labelY)

                // Calculate vertical centering offset using font metrics
                val fontMetrics = paint.fontMetrics
                val verticalCenterOffset = (fontMetrics.ascent + fontMetrics.descent) / 2

                drawContext.canvas.nativeCanvas.drawText(
                    region.name, labelX, labelY - verticalCenterOffset, paint
                )
                drawContext.canvas.nativeCanvas.restore()

                // Run count (Outside the red ring)
                val countX = center.x + cos(angleRad) * (radius + 40.dp.toPx())
                val countY = center.y - sin(angleRad) * (radius + 40.dp.toPx())

                val countPaint = android.graphics.Paint().apply {
                    color = spider?.total_runs.toColor().toArgb()
                    textSize = 18.sp.toPx()
                    textAlign = android.graphics.Paint.Align.CENTER
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                }
                val countFontMetrics = countPaint.fontMetrics
                val countVerticalCenterOffset = (countFontMetrics.ascent + countFontMetrics.descent) / 2

                drawContext.canvas.nativeCanvas.drawText(
                    region.runs.toString(),
                    countX,
                    countY - countVerticalCenterOffset,
                    countPaint
                )
            }
        }
    }
}

@Composable
fun RunLegend(shots: List<SpiderShot>, spider: SpiderColor?) {
    val ones = shots.count { it.runs == 1 }
    val twos = shots.count { it.runs == 2 }
    val threes = shots.count { it.runs == 3 }
    val fours = shots.count { it.runs == 4 }
    val sixes = shots.count { it.runs == 6 }

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        LegendBox(
            "1s",
            ones,
            spider?.one_bg.toColor(),
            labelColor = spider?.runs_label.toColor(),
            scoreColor = spider?.runs.toColor()
        )
        Spacer(modifier = Modifier.width(8.dp))
        LegendBox(
            "2s",
            twos,
            spider?.two_bg.toColor(),
            labelColor = spider?.runs_label.toColor(),
            scoreColor = spider?.runs.toColor()
        )
        Spacer(modifier = Modifier.width(8.dp))
        LegendBox(
            "3s",
            threes,
            spider?.three_bg.toColor(),
            labelColor = spider?.runs_label.toColor(),
            scoreColor = spider?.runs.toColor()
        )
        Spacer(modifier = Modifier.width(8.dp))
        LegendBox(
            "4s",
            fours,
            spider?.four_bg.toColor(),
            labelColor = spider?.runs_label.toColor(),
            scoreColor = spider?.runs.toColor()
        )
        Spacer(modifier = Modifier.width(8.dp))
        LegendBox(
            "6s",
            sixes,
            spider?.six_bg.toColor(),
            labelColor = spider?.runs_label.toColor(),
            scoreColor = spider?.runs.toColor()
        )
    }
}

@Composable
fun LegendBox(label: String, count: Int, color: Color, labelColor: Color, scoreColor: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .border(1.dp, color, RoundedCornerShape(4.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(color)
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Text(text = label, color = labelColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            text = count.toString(),
            modifier = Modifier.padding(horizontal = 6.dp),
            color = scoreColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TeamScoreInfo(
    match: IPLMatch, tab: Tab?, selectedInning: Int, onTeamClick: (Int) -> Unit
) {
    val shape = RoundedCornerShape(6.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .clip(shape)
            .border(1.dp, tab?.border.toColor(), shape)
            .background(tab?.background.toColor()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val team1 = match.participants?.getOrNull(0)
        val team2 = match.participants?.getOrNull(1)

        if (match.totalInnings == 1) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                team1?.let { team ->
                    ScoreChip(
                        inning = 1,
                        team = team.shortName ?: "T1",
                        score = "${team.runs ?: 0}/${team.wickets ?: 0}",
                        overs = "(${team.overs ?: "0.0"} OV)",
                        isSelected = selectedInning == 1,
                        onClick = { onTeamClick(it) },
                        tabColor = tab
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                team1?.let { team ->
                    ScoreChip(
                        inning = 1,
                        team = team.shortName ?: "T1",
                        score = "${team.runs ?: 0}/${team.wickets ?: 0}",
                        overs = "(${team.overs ?: "0.0"} OV)",
                        isSelected = selectedInning == 1,
                        onClick = { onTeamClick(it) },
                        tabColor = tab
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                team2?.let { team ->
                    ScoreChip(
                        inning = 2,
                        team = team.shortName ?: "T2",
                        score = "${team.runs ?: 0}/${team.wickets ?: 0}",
                        overs = "(${team.overs ?: "0.0"} OV)",
                        isSelected = selectedInning == 2,
                        onClick = { onTeamClick(it) },
                        tabColor = tab
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreChip(
    inning: Int,
    team: String,
    score: String,
    overs: String,
    onClick: (Int) -> Unit,
    isSelected: Boolean = false,
    tabColor: Tab?
) {
    val color = if (isSelected) tabColor?.selected_background.toColor() else Color.Transparent
    val textColor =
        if (isSelected) tabColor?.selected_text.toColor() else tabColor?.unselected_text.toColor()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .clickable {
                if (!isSelected) onClick(inning)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = team, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = textColor)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = score, fontSize = 14.sp, color = textColor)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = overs, fontSize = 12.sp, color = textColor)
    }
}

fun DrawScope.drawWicketMarker(x: Float, y: Float, wicketBGColor: Color, wicketTextColor: Color) {
    val radius = 8.dp.toPx()
    drawCircle(
        color = wicketBGColor, radius = radius, center = Offset(x, y)
    )
    drawContext.canvas.nativeCanvas.drawText(
        "W", x, y + 4.dp.toPx(), android.graphics.Paint().apply {
            color = wicketTextColor.toArgb()
            textSize = 10.sp.toPx()
            textAlign = android.graphics.Paint.Align.CENTER
            isFakeBoldText = true
        })
}

private fun DrawScope.drawCricketPitch(center: Offset) {
    val pitchWidth = 14.dp.toPx()
    val pitchHeight = 32.dp.toPx()
    val cornerRadius = 2.dp.toPx()
    val pitchColor = Color(0xFFD2B48C) // Tan color
    val lineColor = Color.White.copy(alpha = 0.6f)
    val strokeWidth = 1.dp.toPx()

    val left = center.x - pitchWidth / 2
    val top = center.y - pitchHeight / 2

    // Main pitch area
    drawRoundRect(
        color = pitchColor,
        topLeft = Offset(left, top),
        size = Size(pitchWidth, pitchHeight),
        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
    )

    val creasePadding = 5.dp.toPx()
    val stumpWidth = 3.dp.toPx()

    // Top Crease Line
    drawLine(
        color = lineColor,
        start = Offset(left, top + creasePadding),
        end = Offset(left + pitchWidth, top + creasePadding),
        strokeWidth = strokeWidth
    )
    // Top Stumps/Return Creases
    drawLine(
        color = lineColor,
        start = Offset(center.x - stumpWidth, top),
        end = Offset(center.x - stumpWidth, top + creasePadding),
        strokeWidth = strokeWidth
    )
    drawLine(
        color = lineColor,
        start = Offset(center.x + stumpWidth, top),
        end = Offset(center.x + stumpWidth, top + creasePadding),
        strokeWidth = strokeWidth
    )

    // Bottom Crease Line
    drawLine(
        color = lineColor,
        start = Offset(left, top + pitchHeight - creasePadding),
        end = Offset(left + pitchWidth, top + pitchHeight - creasePadding),
        strokeWidth = strokeWidth
    )
    // Bottom Stumps/Return Creases
    drawLine(
        color = lineColor,
        start = Offset(center.x - stumpWidth, top + pitchHeight - creasePadding),
        end = Offset(center.x - stumpWidth, top + pitchHeight),
        strokeWidth = strokeWidth
    )
    drawLine(
        color = lineColor,
        start = Offset(center.x + stumpWidth, top + pitchHeight - creasePadding),
        end = Offset(center.x + stumpWidth, top + pitchHeight),
        strokeWidth = strokeWidth
    )
}

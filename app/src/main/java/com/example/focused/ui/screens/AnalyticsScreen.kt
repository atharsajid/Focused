package com.example.focused.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.focused.ui.theme.GrayBg
import com.example.focused.viewmodel.StatsViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavController
import com.example.focused.ui.theme.*
import com.example.focused.viewmodel.DayProgress
import kotlin.math.roundToInt

@Composable
fun AnalyticsScreen(navController: NavController, viewModel: StatsViewModel = viewModel()) {
    val data by viewModel.weeklyData.collectAsState()
    val selectedIndex by viewModel.selectedDayIndex.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black) // Light background for contrast
            .padding(24.dp)
    ) {
        Text(
            "Stats",
            style = TextStyle(fontWeight = FontWeight.Black, fontSize = 20.sp, letterSpacing = 2.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Analytics Card
        Surface(
            color = BgDark,
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Daily Analytics", color = White, fontSize = 14.sp)
                WaveGraph(data, selectedIndex) { viewModel.selectDay(it) }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    data.forEachIndexed { index, day ->
                        Text(
                            day.day.first().toString(),
                            color = if (index == selectedIndex) White else GrayText,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Stats Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Focus Goal Card
            Surface(
                modifier = Modifier.weight(1f).height(140.dp),
                color = BgDark,
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = 0.65f,
                        color = White,
                        trackColor = CardDark,
                        strokeWidth = 8.dp,
                        modifier = Modifier.size(80.dp)
                    )
                    Text("65%", color = White, fontWeight = FontWeight.Bold)
                }
            }

            // Logged Time Card
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Surface(color = White, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().height(60.dp)) {
                    Center { Text("2.4 hrs Focus", color = Black, fontWeight = FontWeight.Bold) }
                }
                Surface(color = AccentYellow, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().height(60.dp)) {
                    Center { Row { Icon(Icons.Default.Add, null); Text("Add Task") } }
                }
            }
        }
    }
}

// Helper for centering content
@Composable
fun Center(content: @Composable () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) { content() }
}

@Composable
fun WaveGraph(
    data: List<DayProgress>,
    selectedIndex: Int,
    onDaySelected: (Int) -> Unit
) {
    val transitionProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        transitionProgress.animateTo(1f, tween(1500, easing = FastOutSlowInEasing))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(top = 20.dp)
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val sectionWidth = size.width / (data.size - 1)
                    val index = (offset.x / sectionWidth).roundToInt().coerceIn(0, data.size - 1)
                    onDaySelected(index)
                }
            }
        ) {
            val width = size.width
            val height = size.height
            val spacing = width / (data.size - 1)
            val maxData = data.maxOf { it.minutes }

            val path = Path()
            val points = data.mapIndexed { index, dayProgress ->
                Offset(
                    x = index * spacing,
                    y = height - (dayProgress.minutes / maxData * height * transitionProgress.value)
                )
            }

            // Draw the smooth curve
            path.moveTo(points[0].x, points[0].y)
            for (i in 0 until points.size - 1) {
                val p0 = points[i]
                val p1 = points[i + 1]
                path.cubicTo(
                    x1 = p0.x + (p1.x - p0.x) / 2, y1 = p0.y,
                    x2 = p0.x + (p1.x - p0.x) / 2, y2 = p1.y,
                    x3 = p1.x, y3 = p1.y
                )
            }

            // Draw the curve line
            drawPath(
                path = path,
                color = White,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw selection indicator (Vertical pill)
            val selectedPoint = points[selectedIndex]
            drawRoundRect(
                color = AccentYellow,
                topLeft = Offset(selectedPoint.x - 10.dp.toPx(), 0f),
                size = Size(20.dp.toPx(), height),
                cornerRadius = CornerRadius(20f, 20f),
                alpha = 0.2f
            )
            drawCircle(
                color = White,
                radius = 6.dp.toPx(),
                center = selectedPoint
            )
        }
    }
}
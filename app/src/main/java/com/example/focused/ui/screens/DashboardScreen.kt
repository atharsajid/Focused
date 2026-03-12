package com.example.focused.ui.screens

import TimerViewModel
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.focused.ui.theme.*
@Composable
fun DashboardScreen(navController: NavController, viewModel: TimerViewModel = viewModel()) {
    val timeLeft = viewModel.timeLeft.value
    val isRunning = viewModel.isRunning.value
    val progress = viewModel.getProgress()

    // Pulse Animation logic (same as before)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = ""
    )

    Column(
        modifier = Modifier.fillMaxSize().background(Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text("Be Focused", color = White, fontWeight = FontWeight.Black, fontSize = 30.sp)

        Spacer(modifier = Modifier.height(50.dp))

        // Timer Circle with Real Progress
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(260.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Background Track
                drawCircle(color = CardDark, style = Stroke(width = 15.dp.toPx()))
                // Live Progress
                drawArc(
                    color = White,
                    startAngle = -90f,
                    sweepAngle = 360 * progress,
                    useCenter = false,
                    style = Stroke(width = 15.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = viewModel.formatTime(),
                    fontSize = 64.sp,
                    color = White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TIME SELECTION DOTS
        TimeSelectionRow(
            onTimeSelected = { viewModel.setTimer(it) },
            currentTime = viewModel.totalTime.value
        )

        Spacer(modifier = Modifier.height(30.dp))

        // PLAY / PAUSE BUTTON
        Column (horizontalAlignment =Alignment.CenterHorizontally) {
            IconButton(
                onClick = {
                    if (isRunning) viewModel.stopTimer() else viewModel.startTimer()
                },
                modifier = Modifier
                    .size(60.dp)
                    .graphicsLayer {
                        scaleX = if (!isRunning) pulseScale else 1f
                        scaleY = if (!isRunning) pulseScale else 1f
                    }
                    .background(White, CircleShape)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Black,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Skip/Reset Button
            Button(
                onClick = { viewModel.setTimer(25) },
                colors = ButtonDefaults.buttonColors(containerColor = CardDark),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Reset", color = White)
            }
        }
    }
}
@Composable
fun TimeSelectionRow(
    currentTime: Long,
    onTimeSelected: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val timeOptions = listOf(5, 15, 25, 60)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        timeOptions.forEach { mins ->
            val isSelected = (currentTime / 60) == mins.toLong()
            SelectableTimeChip(
                label = "${mins}m",
                isSelected = isSelected,
                onClick = { onTimeSelected(mins) }
            )
        }

        // The Functional ADD Icon
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(40.dp)
                .background(CardDark, CircleShape)
                .clickable { showDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = White, modifier = Modifier.size(20.dp))
        }
    }

    if (showDialog) {
        CustomTimePickerDialog(
            onDismiss = { showDialog = false },
            onConfirm = { customMins ->
                onTimeSelected(customMins)
                showDialog = false
            }
        )
    }
}

@Composable
fun SelectableTimeChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) AccentYellow else CardDark,
        animationSpec = tween(300)
    )
    val textColor by animateColorAsState(
        if (isSelected) Black else GrayText,
        animationSpec = tween(300)
    )

    Surface(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Remove the default gray ripple
            ) { onClick() },
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = label,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(initialHour = 0, initialMinute = 25, is24Hour = true)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                // We treat hours * 60 + minutes as total minutes
                val totalMins = (timePickerState.hour * 60) + timePickerState.minute
                onConfirm(if (totalMins > 0) totalMins else 1)
            }) {
                Text("Confirm", color = AccentYellow)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = White) }
        },
        containerColor = CardDark,
        title = { Text("Set Custom Focus Time", color = White, fontSize = 18.sp) },
        text = {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                TimeInput(state = timePickerState) // Modern input style
            }
        }
    )
}
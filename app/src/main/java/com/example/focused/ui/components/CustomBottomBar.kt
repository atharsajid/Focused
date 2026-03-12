package com.example.focused.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.*
import com.example.focused.ui.theme.*
import com.yourpackage.name.ui.navigation.Screen

@Composable
fun CustomBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    // List of navigation items
    val items = listOf(
        Triple(Icons.Default.BarChart, "Stats", Screen.Analytics.route),
        Triple(Icons.Default.Timer, "Timer", Screen.Dashboard.route),
        Triple(Icons.Default.Settings, "Settings", Screen.AppSelection.route)
    )

    // The "Pill" Container
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            .height(64.dp),

        color = BgDark,
        shape = CircleShape,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (icon, label, route) ->
                val isSelected = currentRoute == route

                // Animation for scaling the icon
                val animatedScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1.0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )

                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null // Removes gray ripple for cleaner feel
                        ) { onNavigate(route) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) White else GrayText,
                            modifier = Modifier
                                .size(28.dp)
                                .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale)
                        )
                        if (isSelected) {
                            // Small indicator dot below active icon
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .size(4.dp)
                                    .background(AccentYellow, CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}
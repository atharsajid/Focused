package com.example.focused.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.focused.data.AppInfo
import com.example.focused.ui.theme.AccentYellow
import com.example.focused.ui.theme.Black
import com.example.focused.ui.theme.CardDark
import com.example.focused.ui.theme.GrayText
import com.example.focused.ui.theme.White
import com.example.focused.viewmodel.AppViewModel

@Composable
fun AppSelectionScreen(viewModel: AppViewModel = viewModel()) {
    val apps by viewModel.filteredApps.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black) // Matches Timer screen
            .statusBarsPadding()
    ) {
        // --- Header ---
        Text(
            "BLOCK APPS",
            modifier = Modifier.padding(24.dp),
            style = TextStyle(color = White, fontWeight = FontWeight.Black, fontSize = 24.sp)
        )

        // --- Custom Search Bar ---
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
            placeholder = { Text("Search apps...", color = GrayText) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CardDark,
                unfocusedContainerColor = CardDark,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = White,
                unfocusedTextColor = White
            ),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GrayText) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- App List ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 100.dp)
        ) {
            items(apps, key = { it.packageName }) { app ->
                AppListItem(app) { viewModel.toggleAppSelection(app.packageName) }
            }
        }
    }
}

@Composable
fun AppListItem(app: AppInfo, onToggle: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        color = CardDark,
        shape = RoundedCornerShape(20.dp),
        onClick = onToggle // Makes the whole row clickable
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Icon
            Image(
                painter = rememberAsyncImagePainter(app.icon), // Use Coil library for drawables
                contentDescription = null,
                modifier = Modifier.size(44.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(app.name, color = White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(app.packageName, color = GrayText, fontSize = 11.sp, maxLines = 1)
            }

            // Neo-Minimalist Switch
            Switch(
                checked = app.isSelected,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AccentYellow,
                    checkedTrackColor = AccentYellow.copy(alpha = 0.4f),
                    uncheckedThumbColor = GrayText,
                    uncheckedTrackColor = CardDark
                )
            )
        }
    }
}
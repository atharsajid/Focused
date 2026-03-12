package com.example.focused.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DayProgress(val day: String, val minutes: Float)

class StatsViewModel : ViewModel() {
    private val _weeklyData = MutableStateFlow(listOf(
        DayProgress("Mon", 40f),
        DayProgress("Tue", 90f),
        DayProgress("Wed", 20f),
        DayProgress("Thu", 110f),
        DayProgress("Fri", 50f),
        DayProgress("Sat", 80f),
        DayProgress("Sun", 30f)
    ))
    val weeklyData = _weeklyData.asStateFlow()

    // Currently selected day for the graph interaction
    private val _selectedDayIndex = MutableStateFlow(3) // Default to Thursday
    val selectedDayIndex = _selectedDayIndex.asStateFlow()

    fun selectDay(index: Int) {
        _selectedDayIndex.value = index
    }
}
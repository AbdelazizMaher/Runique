package com.zoksh.analytics.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoksh.analytics.domain.AnalyticsRepository
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AnalyticsDashboardViewModel(
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {
    var state by mutableStateOf(AnalyticsDashboardState())
        private set

    private val dateFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

    init {
        viewModelScope.launch {
            val values = async { analyticsRepository.getAnalyticsValues() }
            val history = async { analyticsRepository.getAnalyticsHistory() }
            
            val analyticsValues = values.await()
            val historyPoints = history.await()

            state = analyticsValues.toAnalyticsDashboardState().copy(
                history = historyPoints,
                selectedDistanceDate = historyPoints.getOrNull(state.selectedDistanceIndex ?: -1)?.dateTimeUtc?.format(dateFormatter) ?: "",
                selectedPaceDate = historyPoints.getOrNull(state.selectedPaceIndex ?: -1)?.dateTimeUtc?.format(dateFormatter) ?: ""
            )
        }
    }

    fun onAction(action: AnalyticsAction) {
        when (action) {
            AnalyticsAction.OnBackClicked -> Unit
            is AnalyticsAction.OnDistancePointSelected -> {
                val date = state.history.getOrNull(action.index ?: -1)?.dateTimeUtc?.format(dateFormatter) ?: ""
                state = state.copy(
                    selectedDistanceIndex = action.index,
                    selectedDistanceDate = date
                )
            }
            is AnalyticsAction.OnPacePointSelected -> {
                val date = state.history.getOrNull(action.index ?: -1)?.dateTimeUtc?.format(dateFormatter) ?: ""
                state = state.copy(
                    selectedPaceIndex = action.index,
                    selectedPaceDate = date
                )
            }
        }
    }
}
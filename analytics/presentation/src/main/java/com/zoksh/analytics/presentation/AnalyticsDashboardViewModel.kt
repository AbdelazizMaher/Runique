package com.zoksh.analytics.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoksh.analytics.domain.AnalyticsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AnalyticsDashboardViewModel(
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {
    var state by mutableStateOf(AnalyticsDashboardState())
        private set

    init {
        viewModelScope.launch {
            val values = async { analyticsRepository.getAnalyticsValues() }
            val history = async { analyticsRepository.getAnalyticsHistory() }
            
            state = values.await().toAnalyticsDashboardState().copy(
                history = history.await()
            )
        }
    }

    fun onAction(action: AnalyticsAction) {
        when (action) {
            AnalyticsAction.OnBackClicked -> Unit
            is AnalyticsAction.OnDistancePointSelected -> {
                state = state.copy(selectedDistanceIndex = action.index)
            }
            is AnalyticsAction.OnPacePointSelected -> {
                state = state.copy(selectedPaceIndex = action.index)
            }
        }
    }
}
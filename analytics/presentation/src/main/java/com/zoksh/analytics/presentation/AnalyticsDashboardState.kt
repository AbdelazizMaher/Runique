package com.zoksh.analytics.presentation

import com.zoksh.analytics.domain.AnalyticsHistoryPoint

data class AnalyticsDashboardState(
    val totalDistanceRun: String = "",
    val totalTimeRun: String = "",
    val fastestEverRun: String = "",
    val avgDistance: String = "",
    val avgPace: String = "",
    val history: List<AnalyticsHistoryPoint> = emptyList(),
    val selectedDistanceIndex: Int? = 5,
    val selectedDistanceDate: String = "",
    val selectedPaceIndex: Int? = 5,
    val selectedPaceDate: String = ""
)
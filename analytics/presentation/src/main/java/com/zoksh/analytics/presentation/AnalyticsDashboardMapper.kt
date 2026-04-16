package com.zoksh.analytics.presentation

import com.zoksh.analytics.domain.AnalyticsValues
import com.zoksh.core.presentation.ui.formatted
import com.zoksh.core.presentation.ui.toFormattedKm
import com.zoksh.core.presentation.ui.toFormattedKmh
import com.zoksh.core.presentation.ui.toFormattedTotalTime
import kotlin.time.Duration.Companion.seconds

fun AnalyticsValues.toAnalyticsDashboardState(): AnalyticsDashboardState {
    return AnalyticsDashboardState(
        totalDistanceRun = (totalDistanceRun / 1000.0).toFormattedKm(),
        totalTimeRun = totalTimeRun.toFormattedTotalTime(),
        fastestEverRun = fastestEverRun.toFormattedKmh(),
        avgDistance = (avgDistancePerRun / 1000.0).toFormattedKm(),
        avgPace = avgPacePerRun.seconds.formatted()
    )
}
package com.zoksh.analytics.presentation

sealed interface AnalyticsAction {
    data object OnBackClicked : AnalyticsAction
    data class OnDistancePointSelected(val index: Int?) : AnalyticsAction
    data class OnPacePointSelected(val index: Int?) : AnalyticsAction
}
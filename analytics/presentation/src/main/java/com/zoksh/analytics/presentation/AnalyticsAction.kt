package com.zoksh.analytics.presentation

sealed interface AnalyticsAction {
    data object OnBackClicked : AnalyticsAction
}
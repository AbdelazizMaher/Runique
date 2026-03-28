package com.zoksh.run.presentation.run_overview

sealed interface RunOverviewAction {
    data object OnStartRunClicked: RunOverviewAction
    data object OnLogoutClicked: RunOverviewAction
    data object OnAnalyticsClicked: RunOverviewAction
}
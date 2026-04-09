package com.zoksh.run.presentation.run_overview

import com.zoksh.run.presentation.run_overview.model.RunUi

sealed interface RunOverviewAction {
    data object OnStartRunClicked: RunOverviewAction
    data object OnLogoutClicked: RunOverviewAction
    data object OnAnalyticsClicked: RunOverviewAction
    data class OnDeleteClick(val run: RunUi): RunOverviewAction
}
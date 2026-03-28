package com.zoksh.run.presentation.active_run

sealed interface ActiveRunAction {
    data object OnToggleRunClicked: ActiveRunAction
    data object OnFinishRunClicked: ActiveRunAction
    data object OnResumeRunClicked: ActiveRunAction
    data object OnBackClicked: ActiveRunAction
}
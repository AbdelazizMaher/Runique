package com.zoksh.run.presentation.active_run

import com.zoksh.core.presentation.ui.UiText

sealed interface ActiveRunEvent {
    data object RunSaved: ActiveRunEvent
    data class Error(val error: UiText): ActiveRunEvent
}
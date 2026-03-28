package com.zoksh.auth.presentation.login

import com.zoksh.auth.presentation.register.RegisterEvent
import com.zoksh.core.presentation.ui.UiText

sealed interface LoginEvent {
    data object LoginSuccess: LoginEvent
    data class Error(val error: UiText): LoginEvent
}
package com.zoksh.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState

data class LoginState(
    val email: TextFieldState = TextFieldState(),
    val isEmailValid: Boolean = false,
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val isLoggingIn: Boolean = false
) {
    val canLogin: Boolean
        get() = isEmailValid && password.text.isNotEmpty() && !isLoggingIn
}

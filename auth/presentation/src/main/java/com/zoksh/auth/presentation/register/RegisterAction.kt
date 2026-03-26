package com.zoksh.auth.presentation.register

sealed interface RegisterAction {
    data object OnPasswordVisibilityChecked : RegisterAction
    data object OnLoginClicked : RegisterAction
    data object OnRegisterClicked : RegisterAction
}
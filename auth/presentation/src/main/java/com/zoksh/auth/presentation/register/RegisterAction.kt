package com.zoksh.auth.presentation.register

sealed interface RegisterAction {
    data object OnTogglePasswordVisibilityChecked : RegisterAction
    data object OnLoginClicked : RegisterAction
    data object OnRegisterClicked : RegisterAction
}
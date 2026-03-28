package com.zoksh.auth.presentation.login

import com.zoksh.auth.presentation.register.RegisterAction

sealed interface LoginAction {
    data object OnTogglePasswordVisibilityChecked : LoginAction
    data object OnLoginClicked : LoginAction
    data object OnRegisterClicked : LoginAction
}
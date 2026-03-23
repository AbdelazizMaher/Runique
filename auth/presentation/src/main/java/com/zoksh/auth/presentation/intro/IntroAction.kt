package com.zoksh.auth.presentation.intro

sealed interface IntroAction {
    data object OnSignupClicked : IntroAction
    data object OnLoginClicked : IntroAction
}
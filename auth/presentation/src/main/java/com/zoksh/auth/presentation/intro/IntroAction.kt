package com.zoksh.auth.presentation.intro

sealed interface IntroAction {
    data object OnSignUpClicked : IntroAction
    data object OnSignInClicked : IntroAction
}
package com.zoksh.auth.presentation.intro

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.zoksh.com.core.presentation.designsystem.RuniqueTheme

@Composable
fun IntroScreenRoute(
    onSignupClicked: () -> Unit,
    onLoginClicked: () -> Unit,
) {
    IntroScreen(
        onAction = { action ->
            when (action) {
                IntroAction.OnLoginClicked -> onLoginClicked()
                IntroAction.OnSignupClicked -> onSignupClicked()
            }
        }
    )
}

@Composable
fun IntroScreen(
    onAction: (IntroAction) -> Unit
) {

}

@Preview
@Composable
fun IntroScreenPreview() {
    RuniqueTheme {
        IntroScreen(
            onAction = {}
        )
    }
}

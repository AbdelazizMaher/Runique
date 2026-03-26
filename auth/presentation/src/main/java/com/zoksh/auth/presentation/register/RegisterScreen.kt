package com.zoksh.auth.presentation.register

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.zoksh.com.core.presentation.designsystem.RuniqueTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRot(
    viewModel: RegisterViewModel = koinViewModel()
) {
    RegisterScreenRotScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun RegisterScreenRotScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    
}

@Preview
@Composable
private fun RegisterScreenRotScreenPreview() {
    RuniqueTheme {
        RegisterScreenRotScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}
        
@file:OptIn(ExperimentalMaterial3Api::class)

package com.zoksh.run.presentation.run_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zoksh.com.core.presentation.designsystem.AnalyticsIcon
import com.zoksh.com.core.presentation.designsystem.LogoIcon
import com.zoksh.com.core.presentation.designsystem.LogoutIcon
import com.zoksh.com.core.presentation.designsystem.RunIcon
import com.zoksh.com.core.presentation.designsystem.RuniqueTheme
import com.zoksh.com.core.presentation.designsystem.components.RuniqueFloatingActionButton
import com.zoksh.com.core.presentation.designsystem.components.RuniqueScaffold
import com.zoksh.com.core.presentation.designsystem.components.RuniqueToolbar
import com.zoksh.com.core.presentation.designsystem.components.util.DropDownItem
import com.zoksh.run.presentation.R
import com.zoksh.run.presentation.run_overview.components.RunListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverviewScreenRoot(
    onStartRunClicked: () -> Unit,
    viewModel: RunOverviewViewModel = koinViewModel()
) {
    RunOverviewScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                RunOverviewAction.OnStartRunClicked -> onStartRunClicked()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun RunOverviewScreen(
    state: RunOverviewState,
    onAction: (RunOverviewAction) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    RuniqueScaffold(
        topAppBar = {
            RuniqueToolbar(
                showBackButton = false,
                title = stringResource(id = R.string.runique),
                scrollBehavior = scrollBehavior,
                menuItems = listOf(
                    DropDownItem(
                        icon = AnalyticsIcon,
                        title = stringResource(id = R.string.analytics)
                    ),
                    DropDownItem(
                        icon = LogoutIcon,
                        title = stringResource(id = R.string.logout)
                    ),
                ),
                onMenuItemClicked = { index ->
                    when (index) {
                        0 -> onAction(RunOverviewAction.OnAnalyticsClicked)
                        1 -> onAction(RunOverviewAction.OnLogoutClicked)
                    }
                },
                startContent = {
                    Icon(
                        imageVector = LogoIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            RuniqueFloatingActionButton(
                icon = RunIcon,
                onClick = {
                    onAction(RunOverviewAction.OnStartRunClicked)
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(horizontal = 16.dp),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.runs,
                key = { it.id }
            ) {
                RunListItem(
                    runUi = it,
                    onDeleteClick = {
                        onAction(RunOverviewAction.OnDeleteClick(it))
                    },
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}

@Preview
@Composable
private fun RunOverviewScreenPreview() {
    RuniqueTheme {
        RunOverviewScreen(
            state = RunOverviewState(),
            onAction = {}
        )
    }
}
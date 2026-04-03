@file:OptIn(ExperimentalMaterial3Api::class)

package com.zoksh.run.presentation.active_run

import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zoksh.com.core.presentation.designsystem.RuniqueTheme
import com.zoksh.com.core.presentation.designsystem.StartIcon
import com.zoksh.com.core.presentation.designsystem.StopIcon
import com.zoksh.com.core.presentation.designsystem.components.RuniqueActionButton
import com.zoksh.com.core.presentation.designsystem.components.RuniqueDialog
import com.zoksh.com.core.presentation.designsystem.components.RuniqueFloatingActionButton
import com.zoksh.com.core.presentation.designsystem.components.RuniqueOutlinedActionButton
import com.zoksh.com.core.presentation.designsystem.components.RuniqueScaffold
import com.zoksh.com.core.presentation.designsystem.components.RuniqueToolbar
import com.zoksh.run.presentation.R
import com.zoksh.run.presentation.active_run.components.RunDataCard
import com.zoksh.run.presentation.active_run.maps.TrackerMap
import com.zoksh.run.presentation.util.hasLocationPermission
import com.zoksh.run.presentation.util.hasNotificationPermission
import com.zoksh.run.presentation.util.shouldShowLocationPermissionRationale
import com.zoksh.run.presentation.util.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel()
) {
    ActiveRunScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ActiveRunScreen(
    state: ActiveRunState,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { perms ->
        val hasCourseLocationPermission = perms[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission = perms[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            perms[android.Manifest.permission.POST_NOTIFICATIONS] == true
        } else true

        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = hasCourseLocationPermission && hasFineLocationPermission,
                showLocationRationale = showLocationRationale
            )
        )
        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = hasNotificationPermission,
                showNotificationRationale = showNotificationRationale
            )
        )
    }
    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationRationale = showLocationRationale
            )
        )
        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationRationale = showNotificationRationale
            )
        )
        if(!showLocationRationale && !showNotificationRationale) {
            permissionLauncher.requestRuniquePermissions(context)
        }
    }
    RuniqueScaffold(
        withGradient = false,
        topAppBar = {
            RuniqueToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClicked = {
                    onAction(ActiveRunAction.OnBackClicked)
                },
            )
        },
        floatingActionButton = {
            RuniqueFloatingActionButton(
                icon = if (state.shouldTrack) {
                    StopIcon
                } else {
                    StartIcon
                },
                onClick = {
                    onAction(ActiveRunAction.OnToggleRunClicked)
                },
                iconSize = 20.dp,
                contentDescription = if(state.shouldTrack) {
                    stringResource(id = R.string.pause_run)
                } else {
                    stringResource(id = R.string.start_run)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TrackerMap(
                isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runData.locations,
                onSnapShot = {},
                modifier = Modifier
                    .fillMaxSize()
            )
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(padding)
                    .fillMaxWidth()
            )
        }
    }

    if (!state.shouldTrack && state.hasStartedRunning) {
        RuniqueDialog(
            title = stringResource(id = R.string.running_is_paused),
            onDismiss = {
                onAction(ActiveRunAction.OnResumeRunClicked)
            },
            description = stringResource(id = R.string.resume_or_finish_run),
            primaryButton = {
                RuniqueActionButton(
                    text = stringResource(id = R.string.resume),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.OnResumeRunClicked)
                    },
                    modifier = Modifier.weight(1f)
                )
            },
            secondaryButton = {
                RuniqueActionButton(
                    text = stringResource(id = R.string.finish),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.OnFinishRunClicked)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        )
    }

    if (state.showLocationRationale || state.showNotificationRationale) {
        RuniqueDialog(
            title = stringResource(id = R.string.permission_required),
            onDismiss = {

            },
            description = when {
                state.showNotificationRationale && state.showLocationRationale -> stringResource(id = R.string.location_notification_rationale)
                state.showLocationRationale -> stringResource(id = R.string.location_rationale)
                else -> stringResource(id = R.string.notification_rationale)
            },
            primaryButton = {
                RuniqueOutlinedActionButton(
                    text = stringResource(id = R.string.okay),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.DismissRationaleDialog)
                        permissionLauncher.requestRuniquePermissions(context)
                    }
                )
            }
        )
    }
}

private fun ActivityResultLauncher<Array<String>>.requestRuniquePermissions(
    context: Context
) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()

    val locationPermissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val notificationPermission = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
    } else arrayOf()

    when {
        !hasLocationPermission && !hasNotificationPermission -> {
            launch(locationPermissions + notificationPermission)
        }
        !hasLocationPermission -> launch(locationPermissions)
        !hasNotificationPermission -> launch(notificationPermission)
    }
}

@Preview
@Composable
private fun ActiveRunScreenPreview() {
    RuniqueTheme {
       ActiveRunScreen(
           state = ActiveRunState(),
           onAction = {}
       )
   }
}
package com.zoksh.run.presentation.active_run

sealed interface ActiveRunAction {
    data object OnToggleRunClicked: ActiveRunAction
    data object OnFinishRunClicked: ActiveRunAction
    data object OnResumeRunClicked: ActiveRunAction
    data object OnBackClicked: ActiveRunAction
    data class SubmitLocationPermissionInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationRationale: Boolean
    ): ActiveRunAction
    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationRationale: Boolean
    ): ActiveRunAction
    data object DismissRationaleDialog: ActiveRunAction
    class OnRunProcessed(val mapPictureBytes: ByteArray): ActiveRunAction
}
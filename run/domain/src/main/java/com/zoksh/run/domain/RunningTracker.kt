@file:OptIn(ExperimentalCoroutinesApi::class)

package com.zoksh.run.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {
    private val isObservingLocation = MutableStateFlow(false)

    val currentLocation = isObservingLocation.flatMapLatest { isObserving ->
        if (isObserving) {
            locationObserver.observeLocation(1000L)
        } else flowOf()
    }.stateIn(
        applicationScope, SharingStarted.Lazily, null
    )

    fun startObserving() {
        isObservingLocation.value = true
    }

    fun stopObserving() {
        isObservingLocation.value = false
    }
}
package com.zoksh.run.presentation.run_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoksh.core.domain.SessionStorage
import com.zoksh.core.domain.run.RunRepository
import com.zoksh.core.domain.run.SyncRunScheduler
import com.zoksh.run.presentation.run_overview.mapper.toRunUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class RunOverviewViewModel(
    private val repository: RunRepository,
    private val syncRunScheduler: SyncRunScheduler,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope
) : ViewModel() {
    var state by mutableStateOf(RunOverviewState())
        private set

    init {
        viewModelScope.launch {
            syncRunScheduler.scheduleSync(
                SyncRunScheduler.SyncType.FetchRuns(30.minutes)
            )
        }

        repository.getRuns().onEach { runs ->
            val runsUi = runs.map { it.toRunUi() }
            state = state.copy(runs = runsUi)
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            repository.syncPendingRuns()
            repository.fetchRuns()
        }
    }

    fun onAction(action: RunOverviewAction) {
        when (action) {
            is RunOverviewAction.OnDeleteClick -> {
                viewModelScope.launch {
                    repository.deleteRun(action.run.id)
                }
            }

            RunOverviewAction.OnLogoutClicked -> logout()
            else -> Unit
        }
    }

    private fun logout() {
        applicationScope.launch {
            syncRunScheduler.cancelAllSync()
            repository.deleteAllRuns()
            repository.logout()
            sessionStorage.set(null)
        }
    }
}
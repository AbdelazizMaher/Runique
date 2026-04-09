package com.zoksh.run.presentation.run_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoksh.core.domain.run.RunRepository
import com.zoksh.run.presentation.run_overview.mapper.toRunUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RunOverviewViewModel(
    private val repository: RunRepository
) : ViewModel() {
    var state by mutableStateOf(RunOverviewState())
        private set

    init {
        repository.getRuns().onEach { runs ->
            val runsUi = runs.map { it.toRunUi() }
            state = state.copy(runs = runsUi)
        }.launchIn(viewModelScope)

        viewModelScope.launch {
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

            RunOverviewAction.OnLogoutClicked -> Unit
            else -> Unit
        }
    }
}
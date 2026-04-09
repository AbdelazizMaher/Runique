package com.zoksh.run.presentation.run_overview

import com.zoksh.run.presentation.run_overview.model.RunUi


data class RunOverviewState(
    val runs: List<RunUi> = emptyList()
)

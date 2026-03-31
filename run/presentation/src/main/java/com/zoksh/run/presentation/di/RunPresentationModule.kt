package com.zoksh.run.presentation.di

import com.zoksh.run.domain.RunningTracker
import com.zoksh.run.presentation.active_run.ActiveRunViewModel
import com.zoksh.run.presentation.run_overview.RunOverviewViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val runPresentationModule = module {
    singleOf(::RunningTracker)

    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}
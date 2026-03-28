package com.zoksh.run.presentation.di

import com.zoksh.run.presentation.active_run.ActiveRunViewModel
import com.zoksh.run.presentation.run_overview.RunOverviewViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val runViewModelModule = module {
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}
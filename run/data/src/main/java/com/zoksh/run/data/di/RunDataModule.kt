package com.zoksh.run.data.di

import com.zoksh.core.domain.run.SyncRunScheduler
import com.zoksh.run.data.CreateRunWorker
import com.zoksh.run.data.DeleteRunWorker
import com.zoksh.run.data.FetchRunsWorker
import com.zoksh.run.data.SyncRunWorkerScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::DeleteRunWorker)
    workerOf(::FetchRunsWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
}
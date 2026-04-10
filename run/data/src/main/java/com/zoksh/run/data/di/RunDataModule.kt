package com.zoksh.run.data.di

import com.zoksh.run.data.CreateRunWorker
import com.zoksh.run.data.DeleteRunWorker
import com.zoksh.run.data.FetchRunsWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::DeleteRunWorker)
    workerOf(::FetchRunsWorker)
}
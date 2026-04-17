package com.zoksh.analytics.data.di

import com.zoksh.analytics.data.RoomAnalyticsRepository
import com.zoksh.analytics.domain.AnalyticsRepository
import com.zoksh.core.database.RunDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsDataModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()
    single {
        get<RunDatabase>().analyticsDao
    }
}
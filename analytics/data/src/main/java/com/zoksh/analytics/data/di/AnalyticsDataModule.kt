package com.zoksh.analytics.data.di

import com.zoksh.analytics.data.RoomAnalyticsRepository
import com.zoksh.analytics.domain.AnalyticsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analytiscModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()
}
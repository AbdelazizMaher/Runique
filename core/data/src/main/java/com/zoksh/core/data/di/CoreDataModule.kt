package com.zoksh.core.data.di

import com.zoksh.core.data.auth.EncryptedSessionStorage
import com.zoksh.core.data.networking.HttpClientFactory
import com.zoksh.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}
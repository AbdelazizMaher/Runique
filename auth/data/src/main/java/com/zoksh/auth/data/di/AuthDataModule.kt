package com.zoksh.auth.data.di

import com.zoksh.auth.data.EmailPatternValidator
import com.zoksh.auth.domain.PatternValidator
import com.zoksh.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
}
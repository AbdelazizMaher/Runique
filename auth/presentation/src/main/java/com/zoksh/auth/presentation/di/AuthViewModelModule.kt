package com.zoksh.auth.presentation.di

import com.zoksh.auth.presentation.login.LoginViewModel
import com.zoksh.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}
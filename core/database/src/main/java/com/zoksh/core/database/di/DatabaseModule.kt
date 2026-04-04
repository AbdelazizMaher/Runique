package com.zoksh.core.database.di

import androidx.room.Room
import com.zoksh.core.database.RoomLocalRunDataSource
import com.zoksh.core.database.RunDatabase
import com.zoksh.core.domain.run.LocalRunDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            RunDatabase::class.java,
            "run.db"
        ).build()
    }
    single { get<RunDatabase>().dao }
    singleOf(::RoomLocalRunDataSource).bind<LocalRunDataSource>()
}
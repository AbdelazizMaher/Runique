package com.zoksh.runique

import android.app.Application
import com.zoksh.auth.data.di.authDataModule
import com.zoksh.auth.presentation.di.authViewModelModule
import com.zoksh.core.data.di.coreDataModule
import com.zoksh.core.database.di.databaseModule
import com.zoksh.run.location.di.locationModule
import com.zoksh.run.network.di.networkModule
import com.zoksh.run.presentation.di.runPresentationModule
import com.zoksh.runique.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            modules(
                authDataModule,
                authViewModelModule,
                runPresentationModule,
                coreDataModule,
                locationModule,
                databaseModule,
                networkModule,
                appModule
            )
        }
    }
}
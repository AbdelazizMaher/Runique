package com.zoksh.analytics.analytics_feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.zoksh.analytics.data.di.analyticsDataModule
import com.zoksh.analytics.presentation.di.analyticsPresentationModule
import org.koin.core.context.loadKoinModules

class AnalyticsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(
            listOf(
                analyticsDataModule,
                analyticsPresentationModule
            )
        )

    }
}
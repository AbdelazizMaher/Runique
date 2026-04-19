package com.zoksh.analytics.analytics_feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.splitcompat.SplitCompat
import com.zoksh.analytics.data.di.analyticsDataModule
import com.zoksh.analytics.presentation.AnalyticsDashboardScreenRoot
import com.zoksh.analytics.presentation.di.analyticsPresentationModule
import com.zoksh.com.core.presentation.designsystem.RuniqueTheme
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
        SplitCompat.installActivity(this)
        enableEdgeToEdge()
        setContent {
            RuniqueTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "analytics_dashboard"
                ) {
                    composable("analytics_dashboard") {
                        AnalyticsDashboardScreenRoot(
                            onBackClicked = {
                                finish()
                            }
                        )
                    }
                }
            }
        }

    }
}
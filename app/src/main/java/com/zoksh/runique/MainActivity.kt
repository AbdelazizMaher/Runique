package com.zoksh.runique

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.zoksh.com.core.presentation.designsystem.RuniqueTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private lateinit var splitInstallManager: SplitInstallManager
    private val splitInstallStateUpdatedListener = SplitInstallStateUpdatedListener { state ->
        when (state.status()) {
            SplitInstallSessionStatus.INSTALLED -> {
                viewModel.setAnalyticsDialogVisibility(false)
                Toast.makeText(
                    applicationContext,
                    R.string.analytics_installed,
                    Toast.LENGTH_LONG
                ).show()
            }
            SplitInstallSessionStatus.INSTALLING -> {
                viewModel.setAnalyticsDialogVisibility(true)
            }
            SplitInstallSessionStatus.DOWNLOADING -> {
                viewModel.setAnalyticsDialogVisibility(true)
            }
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                splitInstallManager.startConfirmationDialogForResult(state, this, 0)
            }
            SplitInstallSessionStatus.FAILED -> {
                viewModel.setAnalyticsDialogVisibility(false)
                Toast.makeText(
                    applicationContext,
                    R.string.error_installation_failed,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
        }
        enableEdgeToEdge()
        splitInstallManager = SplitInstallManagerFactory.create(applicationContext)
        setContent {
            RuniqueTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    if (!viewModel.state.isCheckingAuth) {
                        val navController = rememberNavController()
                        NavigationRoot(
                            navController = navController,
                            viewModel.state.isLoggedIn,
                            onAnalyticsClicked = {
                                installOrStartAnalyticsFeature()
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        splitInstallManager.registerListener(splitInstallStateUpdatedListener)
    }

    override fun onPause() {
        super.onPause()
        splitInstallManager.unregisterListener(splitInstallStateUpdatedListener)
    }

    private fun installOrStartAnalyticsFeature() {
        if (splitInstallManager.installedModules.contains("analytics_feature")) {
            Intent().setClassName(packageName, "com.zoksh.analytics.analytics_feature.AnalyticsActivity")
                .also(::startActivity)
            return
        }

        val request = SplitInstallRequest.newBuilder()
            .addModule("analytics_feature")
            .build()
        splitInstallManager.startInstall(request)
            .addOnFailureListener {
                it.printStackTrace()
                Toast.makeText(
                    applicationContext,
                    R.string.error_couldnt_load_module,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}
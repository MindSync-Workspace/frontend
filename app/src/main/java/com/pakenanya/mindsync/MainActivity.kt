package com.pakenanya.mindsync

import android.os.Bundle
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.padding
import com.pakenanya.mindsync.ui.theme.MindsyncTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.pakenanya.mindsync.data.manager.MindSyncAppPreferences
import com.pakenanya.mindsync.ui.navigation.AppNavigation
import com.pakenanya.mindsync.ui.screen.auth.AuthViewModel
import com.pakenanya.mindsync.ui.screen.onboarding.OnboardingScreen
import com.pakenanya.mindsync.ui.screen.onboarding.OnboardingViewModel
import com.pakenanya.mindsync.ui.screen.splash.SplashScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val onboardingViewModel: OnboardingViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var preferencesManager: MindSyncAppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showSplashScreen by remember { mutableStateOf(true) }
            var initialToken by remember { mutableStateOf<String?>(null) }

            MindsyncTheme {
                if (showSplashScreen) {
                    SplashScreen(
                        onSplashComplete = { token ->
                            initialToken = token
                            showSplashScreen = false
                        }
                    )
                } else {
                    AppContent(
                        onboardingViewModel,
                        authViewModel,
                        initialToken
                    )
                }
            }
        }
    }
}

@Composable
fun AppContent(
    viewModel: OnboardingViewModel,
    authViewModel: AuthViewModel,
    initialToken: String?
) {
    val shouldShowOnboarding by viewModel.shouldShowOnboarding.collectAsState()

    if (shouldShowOnboarding) {
        MindsyncTheme {
            OnboardingScreen(
                onFinished = {
                    viewModel.onOnboardingCompleted()
                }
            )
        }
    } else {
        MindsyncTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                AppNavigation(
                    modifier = Modifier.padding(innerPadding),
                    authViewModel = authViewModel,
                    initialToken = initialToken
                )
            }
        }
    }
}
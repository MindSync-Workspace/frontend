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
import com.pakenanya.mindsync.ui.navigation.AppNavigation
import com.pakenanya.mindsync.ui.screen.onboarding.OnboardingScreen
import com.pakenanya.mindsync.ui.screen.onboarding.OnboardingViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val onboardingViewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppContent(onboardingViewModel)
        }
    }
}

@Composable
fun AppContent(viewModel: OnboardingViewModel) {
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
                AppNavigation(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}
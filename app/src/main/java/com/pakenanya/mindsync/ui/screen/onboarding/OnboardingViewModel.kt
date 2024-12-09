package com.pakenanya.mindsync.ui.screen.onboarding

import android.app.Application
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.SharingStarted
import com.pakenanya.mindsync.data.manager.MindSyncAppPreferences

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {
    private val onboardingManager = MindSyncAppPreferences(application)

    val shouldShowOnboarding: StateFlow<Boolean> = onboardingManager.shouldShowOnboarding
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = true
        )

    fun onOnboardingCompleted() {
        viewModelScope.launch {
            onboardingManager.completeOnboarding()
        }
    }
}
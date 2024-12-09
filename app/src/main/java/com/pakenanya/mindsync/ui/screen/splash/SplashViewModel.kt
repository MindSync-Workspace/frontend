package com.pakenanya.mindsync.ui.screen.splash

import androidx.lifecycle.ViewModel
import com.pakenanya.mindsync.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    init {
        getToken()
    }

    private fun getToken() {
        authRepository.getToken().observeForever {
            _token.value = it

            authRepository.getToken().removeObserver { this }
        }
    }
}
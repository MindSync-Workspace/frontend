package com.pakenanya.mindsync.ui.screen.auth

import androidx.lifecycle.ViewModel
import com.pakenanya.mindsync.data.local.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class AuthScreenUIState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

}
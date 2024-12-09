package com.pakenanya.mindsync.ui.screen.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pakenanya.mindsync.data.manager.MindSyncAppPreferences
import com.pakenanya.mindsync.data.remote.response.UserData
import com.pakenanya.mindsync.data.remote.retrofit.UserRegistrationRequest
import com.pakenanya.mindsync.data.repository.AuthRepository
import com.pakenanya.mindsync.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.pakenanya.mindsync.data.repository.Result

sealed class AuthState {
    data object Authenticated : AuthState()
    data object Unauthenticated : AuthState()
    data object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth,
    private val preferencesManager: MindSyncAppPreferences
): ViewModel() {
    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> = _userData

    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    init {
        getToken()
        getUser()
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email atau password tidak boleh kosong")
            return
        }

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                        task.result.user?.let {
                            userRepository.getUserByEmail(email).observeForever { result ->
                                if (result is Result.Success) {
                                    val user = result.data

                                    val userId = user.id
                                    val username = user.username
                                    val email = user.email
                                    val password = user.password
                                    val isActive = user.isActive

                                    val userData = UserRegistrationRequest(
                                        id = userId,
                                        username = username,
                                        email = email,
                                        password = password,
                                        isActive = isActive
                                    )

                                    userRepository.createUser(userData, false).observeForever { result ->
                                        if (result is Result.Success) {
                                            viewModelScope.launch {
                                                preferencesManager.saveToken(it.uid)
                                            }
                                            getUser()
                                            _authState.value = AuthState.Authenticated
                                        }
                                    }
                                }
                            }
                        }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signUp(fullName: String, email: String, password: String) {
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Semua kolom data tidak boleh kosong")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let {
                        val userData = UserRegistrationRequest(
                            id = 0,
                            username = fullName,
                            email = email,
                            password = password,
                            isActive = true
                        )

                        userRepository.createUser(userData, true).observeForever { result ->
                            if (result is Result.Success) {
                                viewModelScope.launch {
                                    preferencesManager.saveToken(it.uid)
                                }
                                getUser()
                                _authState.value = AuthState.Authenticated
                            }
                        }
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signOut() {
        auth.signOut()
        userRepository.deleteUser().observeForever { result ->
            when (result) {
                is Result.Success -> {
                    Log.e("SignOut", "Successfully")
                }
                is Result.Error -> {
                    Log.e("SignOut", result.message)
                    // Log error jika dibutuhkan
                }
                else -> Unit
            }
        }
        viewModelScope.launch {
            preferencesManager.saveToken("")
        }
        _authState.value = AuthState.Unauthenticated
    }

    private fun getToken() {
        authRepository.getToken().observeForever { token ->
            _token.value = token
        }
    }

    private fun getUser() {
        userRepository.getUser().observeForever { result ->
            if (result is Result.Success) {
                _userData.value = result.data
            }
        }
    }
}

package com.nikunja.aquariusly.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikunja.aquariusly.domain.usecase.RegisterFcmTokenUseCase
import com.nikunja.aquariusly.domain.usecase.SignInWithGoogleUseCase
import com.nikunja.aquariusly.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun setSigningIn(signingIn: Boolean) {
        _state.update { it.copy(isSigningIn = signingIn) }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = signInWithGoogleUseCase(idToken)) {
                is Resource.Success -> {
                    // Register FCM token after successful login
                    registerFcmToken()
                    _state.update { it.copy(isLoading = false, isSigningIn = false, isLoggedIn = true) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, isSigningIn = false, error = result.message) }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun registerFcmToken() {
        viewModelScope.launch {
            registerFcmTokenUseCase()
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

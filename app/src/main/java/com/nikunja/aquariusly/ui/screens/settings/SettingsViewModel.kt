package com.nikunja.aquariusly.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikunja.aquariusly.domain.usecase.GetUserProfileUseCase
import com.nikunja.aquariusly.domain.usecase.SignOutUseCase
import com.nikunja.aquariusly.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = getUserProfileUseCase()) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, profile = result.data) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun toggleDarkMode() {
        _state.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun toggleNotifications() {
        _state.update { it.copy(notificationsEnabled = !it.notificationsEnabled) }
    }

    fun toggleHapticFeedback() {
        _state.update { it.copy(hapticFeedback = !it.hapticFeedback) }
    }

    fun signOut() {
        viewModelScope.launch {
            _state.update { it.copy(isSigningOut = true) }
            
            when (signOutUseCase()) {
                is Resource.Success -> {
                    _state.update { it.copy(isSigningOut = false, isSignedOut = true) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isSigningOut = false, error = "Failed to sign out") }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

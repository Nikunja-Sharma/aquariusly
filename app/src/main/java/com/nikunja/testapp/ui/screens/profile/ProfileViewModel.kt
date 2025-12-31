package com.nikunja.testapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikunja.testapp.domain.usecase.GetCurrentUserUseCase
import com.nikunja.testapp.domain.usecase.SignOutUseCase
import com.nikunja.testapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        val user = getCurrentUserUseCase()
        _state.update { it.copy(user = user) }
    }

    fun signOut() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            when (signOutUseCase()) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, isSignedOut = true) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}

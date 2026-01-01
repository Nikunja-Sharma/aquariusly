package com.nikunja.aquariusly.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikunja.aquariusly.domain.usecase.CheckAuthStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase
) : ViewModel() {
    
    private val _isLoggedIn = MutableStateFlow(checkAuthStatusUseCase())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    fun refreshAuthStatus() {
        viewModelScope.launch {
            _isLoggedIn.value = checkAuthStatusUseCase()
        }
    }
}

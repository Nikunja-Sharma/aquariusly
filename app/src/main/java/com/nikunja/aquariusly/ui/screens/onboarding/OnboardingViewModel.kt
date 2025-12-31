package com.nikunja.aquariusly.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    
    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()
    
    fun onPageChanged(page: Int) {
        _state.update { it.copy(currentPage = page) }
    }
    
    fun onNextClick() {
        viewModelScope.launch {
            val currentPage = _state.value.currentPage
            if (currentPage < onboardingPages.size - 1) {
                _state.update { it.copy(currentPage = currentPage + 1) }
            } else {
                _state.update { it.copy(isCompleted = true) }
            }
        }
    }
    
    fun onSkipClick() {
        viewModelScope.launch {
            _state.update { it.copy(isCompleted = true) }
        }
    }
    
    fun onGetStartedClick() {
        viewModelScope.launch {
            _state.update { it.copy(isCompleted = true) }
        }
    }
}

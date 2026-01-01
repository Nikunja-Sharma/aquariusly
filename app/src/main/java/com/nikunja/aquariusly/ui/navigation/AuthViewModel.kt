package com.nikunja.aquariusly.ui.navigation

import androidx.lifecycle.ViewModel
import com.nikunja.aquariusly.domain.usecase.CheckAuthStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase
) : ViewModel() {
    
    val isLoggedIn: Boolean
        get() = checkAuthStatusUseCase()
}

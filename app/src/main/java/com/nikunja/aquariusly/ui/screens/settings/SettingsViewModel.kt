package com.nikunja.aquariusly.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikunja.aquariusly.domain.usecase.GetUserProfileUseCase
import com.nikunja.aquariusly.domain.usecase.SignOutUseCase
import com.nikunja.aquariusly.domain.usecase.UpdateProfileUseCase
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
    private val updateProfileUseCase: UpdateProfileUseCase,
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
                    _state.update { 
                        it.copy(
                            isLoading = false, 
                            profile = result.data,
                            editedName = result.data?.name ?: ""
                        ) 
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun onNameChange(name: String) {
        _state.update { it.copy(editedName = name, saveSuccess = false) }
    }

    fun saveProfile() {
        val currentName = _state.value.editedName.trim()
        if (currentName.isBlank()) {
            _state.update { it.copy(error = "Name cannot be empty") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null, saveSuccess = false) }
            
            when (val result = updateProfileUseCase(currentName)) {
                is Resource.Success -> {
                    _state.update { 
                        it.copy(
                            isSaving = false, 
                            profile = result.data,
                            saveSuccess = true
                        ) 
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isSaving = false, error = result.message) }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun clearSaveSuccess() {
        _state.update { it.copy(saveSuccess = false) }
    }
}

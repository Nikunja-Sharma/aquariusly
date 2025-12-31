package com.nikunja.testapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikunja.testapp.domain.usecase.GetItemsUseCase
import com.nikunja.testapp.domain.usecase.RefreshItemsUseCase
import com.nikunja.testapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase,
    private val refreshItemsUseCase: RefreshItemsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        observeItems()
        refresh()
    }

    private fun observeItems() {
        viewModelScope.launch {
            getItemsUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update { it.copy(isLoading = false, items = result.data, error = null) }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
                    }
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = refreshItemsUseCase()) {
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}

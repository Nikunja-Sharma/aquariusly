package com.nikunja.aquariusly.ui.screens.modelselection

import androidx.lifecycle.ViewModel
import com.nikunja.aquariusly.domain.model.AIModel
import com.nikunja.aquariusly.domain.model.AIModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ModelSelectionViewModel @Inject constructor() : ViewModel() {
    
    private val _state = MutableStateFlow(ModelSelectionState())
    val state: StateFlow<ModelSelectionState> = _state.asStateFlow()
    
    fun onSearchQueryChange(query: String) {
        _state.update { currentState ->
            val filteredModels = if (query.isBlank()) {
                AIModels.allModels
            } else {
                AIModels.allModels.filter { model ->
                    model.name.contains(query, ignoreCase = true) ||
                    model.description.contains(query, ignoreCase = true) ||
                    model.capabilities.any { it.contains(query, ignoreCase = true) }
                }
            }
            currentState.copy(
                searchQuery = query,
                models = filteredModels
            )
        }
    }
    
    fun onModelSelect(model: AIModel) {
        _state.update { it.copy(selectedModel = model) }
    }
}

package com.nikunja.testapp.ui.screens.modelselection

import com.nikunja.testapp.domain.model.AIModel
import com.nikunja.testapp.domain.model.AIModels

data class ModelSelectionState(
    val models: List<AIModel> = AIModels.allModels,
    val selectedModel: AIModel? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

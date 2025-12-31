package com.nikunja.aquariusly.ui.screens.modelselection

import com.nikunja.aquariusly.domain.model.AIModel
import com.nikunja.aquariusly.domain.model.AIModels

data class ModelSelectionState(
    val models: List<AIModel> = AIModels.allModels,
    val selectedModel: AIModel? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

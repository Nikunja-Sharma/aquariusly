package com.nikunja.aquariusly.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikunja.aquariusly.domain.model.AIModel
import com.nikunja.aquariusly.domain.model.AIModels
import com.nikunja.aquariusly.domain.model.AIProvider
import com.nikunja.aquariusly.domain.model.ChatActionType
import com.nikunja.aquariusly.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    
    private val _state = MutableStateFlow(ChatState(
        chatHistories = generateMockHistories()
    ))
    val state: StateFlow<ChatState> = _state.asStateFlow()
    
    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SendMessage -> sendMessage(event.content)
            is ChatEvent.SelectModel -> selectModel(event.model)
            is ChatEvent.UpdateInput -> updateInput(event.text)
            is ChatEvent.ToggleDrawer -> toggleDrawer()
            is ChatEvent.ToggleActionsPanel -> toggleActionsPanel()
            is ChatEvent.ToggleAction -> toggleAction(event.actionType)
            is ChatEvent.ToggleProviderExpanded -> toggleProviderExpanded(event.provider)
            is ChatEvent.SelectHistory -> selectHistory(event.historyId)
            is ChatEvent.NewChat -> newChat()
            is ChatEvent.ClearError -> clearError()
        }
    }
    
    private fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        viewModelScope.launch {
            val activeActions = _state.value.activeActions
            val model = _state.value.selectedModel
            
            val userMessage = Message(
                id = UUID.randomUUID().toString(),
                content = content.trim(),
                isFromUser = true,
                modelId = model.id
            )
            
            _state.update { 
                it.copy(
                    messages = it.messages + userMessage,
                    inputText = "",
                    isLoading = true,
                    activeActions = emptySet(),
                    isActionsExpanded = false
                )
            }
            
            delay(if (activeActions.isNotEmpty()) 2500 else 1500)
            
            val aiResponse = Message(
                id = UUID.randomUUID().toString(),
                content = generateMockResponse(content, model, activeActions),
                isFromUser = false,
                modelId = model.id
            )
            
            _state.update {
                it.copy(
                    messages = it.messages + aiResponse,
                    isLoading = false
                )
            }
        }
    }
    
    private fun selectModel(model: AIModel) {
        _state.update { 
            it.copy(
                selectedModel = model,
                isDrawerOpen = false
            )
        }
    }
    
    private fun updateInput(text: String) {
        _state.update { it.copy(inputText = text) }
    }
    
    private fun toggleDrawer() {
        _state.update { it.copy(isDrawerOpen = !it.isDrawerOpen) }
    }
    
    private fun toggleActionsPanel() {
        _state.update { it.copy(isActionsExpanded = !it.isActionsExpanded) }
    }
    
    private fun toggleAction(actionType: ChatActionType) {
        _state.update { currentState ->
            val newActions = if (currentState.activeActions.contains(actionType)) {
                currentState.activeActions - actionType
            } else {
                currentState.activeActions + actionType
            }
            currentState.copy(activeActions = newActions)
        }
    }
    
    private fun toggleProviderExpanded(provider: AIProvider) {
        _state.update { currentState ->
            val newExpanded = if (currentState.expandedProviders.contains(provider)) {
                currentState.expandedProviders - provider
            } else {
                currentState.expandedProviders + provider
            }
            currentState.copy(expandedProviders = newExpanded)
        }
    }
    
    private fun selectHistory(historyId: String) {
        val history = _state.value.chatHistories.find { it.id == historyId }
        history?.let {
            val model = AIModels.allModels.find { m -> m.id == it.modelId } ?: AIModels.chatGPT
            _state.update { state ->
                state.copy(
                    selectedHistoryId = historyId,
                    selectedModel = model,
                    messages = emptyList(),
                    isDrawerOpen = false
                )
            }
        }
    }
    
    private fun newChat() {
        _state.update {
            it.copy(
                messages = emptyList(),
                selectedHistoryId = null,
                isDrawerOpen = false
            )
        }
    }
    
    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
    
    private fun generateMockHistories(): List<ChatHistory> {
        return listOf(
            ChatHistory("1", "Code review help", "gpt-4", AIProvider.OPENAI, "Can you review this code?", System.currentTimeMillis() - 3600000),
            ChatHistory("2", "API design question", "gpt-4", AIProvider.OPENAI, "Best practices for REST APIs", System.currentTimeMillis() - 7200000),
            ChatHistory("3", "Writing assistance", "claude-3", AIProvider.ANTHROPIC, "Help me write documentation", System.currentTimeMillis() - 86400000),
            ChatHistory("4", "Research summary", "claude-3", AIProvider.ANTHROPIC, "Summarize this paper", System.currentTimeMillis() - 172800000),
            ChatHistory("5", "Image analysis", "gemini-pro", AIProvider.GOOGLE, "What's in this image?", System.currentTimeMillis() - 259200000),
            ChatHistory("6", "Quick search", "perplexity", AIProvider.PERPLEXITY, "Latest news on AI", System.currentTimeMillis() - 345600000)
        )
    }
    
    private fun generateMockResponse(
        query: String, 
        model: AIModel, 
        actions: Set<ChatActionType>
    ): String {
        val actionContext = when {
            actions.contains(ChatActionType.WEB_SEARCH) -> 
                "Web Search Results\n\nI searched the web and found relevant information:\n\n"
            actions.contains(ChatActionType.DEEP_RESEARCH) -> 
                "Deep Research Report\n\nAfter analyzing multiple sources:\n\n"
            actions.contains(ChatActionType.DEEP_THINK) -> 
                "Extended Reasoning\n\nLet me think through this step by step:\n\n"
            actions.contains(ChatActionType.CODE) -> 
                "Code Analysis\n\n// Here's the solution\n\n"
            actions.contains(ChatActionType.GENERATE_IMAGE) -> 
                "Image Generated\n\n[Image would appear here]\n\n"
            actions.contains(ChatActionType.RAG) -> 
                "Knowledge Base Query\n\nFrom your documents:\n\n"
            else -> ""
        }
        
        return "${actionContext}I'm ${model.name}, and I'd be happy to help! This is a demo response showcasing the Aquariusly interface with ${if (actions.isNotEmpty()) "active tools" else "standard mode"}."
    }
}

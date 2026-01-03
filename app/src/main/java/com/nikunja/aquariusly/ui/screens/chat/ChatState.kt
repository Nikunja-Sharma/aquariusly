package com.nikunja.aquariusly.ui.screens.chat

import com.nikunja.aquariusly.domain.model.AIModel
import com.nikunja.aquariusly.domain.model.AIModels
import com.nikunja.aquariusly.domain.model.AIProvider
import com.nikunja.aquariusly.domain.model.ChatActionType
import com.nikunja.aquariusly.domain.model.Message
import com.nikunja.aquariusly.domain.model.MessageAttachment

data class ChatHistory(
    val id: String,
    val title: String,
    val modelId: String,
    val provider: AIProvider,
    val lastMessage: String,
    val timestamp: Long
)

data class ChatState(
    val messages: List<Message> = emptyList(),
    val inputText: String = "",
    val selectedModel: AIModel = AIModels.chatGPT,
    val isLoading: Boolean = false,
    val isDrawerOpen: Boolean = false,
    val isActionsExpanded: Boolean = false,
    val activeActions: Set<ChatActionType> = emptySet(),
    val attachments: List<MessageAttachment> = emptyList(),
    val chatHistories: List<ChatHistory> = emptyList(),
    val selectedHistoryId: String? = null,
    val expandedProviders: Set<AIProvider> = setOf(AIProvider.OPENAI),
    val error: String? = null
)

sealed class ChatEvent {
    data class SendMessage(val content: String) : ChatEvent()
    data class SelectModel(val model: AIModel) : ChatEvent()
    data class UpdateInput(val text: String) : ChatEvent()
    data object ToggleDrawer : ChatEvent()
    data object ToggleActionsPanel : ChatEvent()
    data class ToggleAction(val actionType: ChatActionType) : ChatEvent()
    data class ToggleProviderExpanded(val provider: AIProvider) : ChatEvent()
    data class SelectHistory(val historyId: String) : ChatEvent()
    data object NewChat : ChatEvent()
    data object ClearError : ChatEvent()
    data class AddAttachment(val attachment: MessageAttachment) : ChatEvent()
    data class RemoveAttachment(val attachment: MessageAttachment) : ChatEvent()
}

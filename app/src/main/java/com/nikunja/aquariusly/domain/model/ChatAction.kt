package com.nikunja.aquariusly.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

enum class ChatActionType {
    WEB_SEARCH,
    DEEP_RESEARCH,
    DEEP_THINK,
    FILE_UPLOAD,
    IMAGE_INPUT,
    GENERATE_IMAGE,
    IMAGE_EDIT,
    CODE,
    RAG,
    MCP,
    CONTEXT,
    VOICE
}

data class ChatAction(
    val type: ChatActionType,
    val label: String,
    val icon: ImageVector,
    val description: String,
    val isPremium: Boolean = false
)

object ChatActions {
    val webSearch = ChatAction(
        type = ChatActionType.WEB_SEARCH,
        label = "Web",
        icon = Icons.Default.Search,
        description = "Search the web for latest info"
    )
    
    val deepResearch = ChatAction(
        type = ChatActionType.DEEP_RESEARCH,
        label = "Research",
        icon = Icons.Default.Star,
        description = "Deep research with multiple sources",
        isPremium = true
    )
    
    val deepThink = ChatAction(
        type = ChatActionType.DEEP_THINK,
        label = "Think",
        icon = Icons.Default.Info,
        description = "Extended reasoning mode",
        isPremium = true
    )
    
    val fileUpload = ChatAction(
        type = ChatActionType.FILE_UPLOAD,
        label = "File",
        icon = Icons.Default.Add,
        description = "Upload documents, PDFs, code"
    )
    
    val imageInput = ChatAction(
        type = ChatActionType.IMAGE_INPUT,
        label = "Vision",
        icon = Icons.Default.MoreVert,
        description = "Analyze images and screenshots"
    )
    
    val generateImage = ChatAction(
        type = ChatActionType.GENERATE_IMAGE,
        label = "Create",
        icon = Icons.Default.Create,
        description = "Generate images with AI",
        isPremium = true
    )
    
    val imageEdit = ChatAction(
        type = ChatActionType.IMAGE_EDIT,
        label = "Edit",
        icon = Icons.Default.Edit,
        description = "Edit and modify images",
        isPremium = true
    )
    
    val code = ChatAction(
        type = ChatActionType.CODE,
        label = "Code",
        icon = Icons.Default.Build,
        description = "Code generation & analysis"
    )
    
    val rag = ChatAction(
        type = ChatActionType.RAG,
        label = "RAG",
        icon = Icons.Default.List,
        description = "Query your knowledge base",
        isPremium = true
    )
    
    val mcp = ChatAction(
        type = ChatActionType.MCP,
        label = "MCP",
        icon = Icons.Default.Settings,
        description = "Connect external tools",
        isPremium = true
    )
    
    val context = ChatAction(
        type = ChatActionType.CONTEXT,
        label = "Context",
        icon = Icons.Default.Info,
        description = "Add custom context"
    )
    
    val voice = ChatAction(
        type = ChatActionType.VOICE,
        label = "Voice",
        icon = Icons.Default.MoreVert,
        description = "Voice input"
    )
    
    val primaryActions = listOf(webSearch, deepResearch, deepThink, code)
    val mediaActions = listOf(fileUpload, imageInput, generateImage, imageEdit)
    val advancedActions = listOf(rag, mcp, context, voice)
    
    val allActions = primaryActions + mediaActions + advancedActions
}

package com.nikunja.aquariusly.ui.screens.chat

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nikunja.aquariusly.domain.model.*
import com.nikunja.aquariusly.ui.components.ChatSidebar
import com.nikunja.aquariusly.ui.theme.UnifiedTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    initialChatId: String? = null,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val colors = UnifiedTheme.colors
    val listState = rememberLazyListState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Handle deep link - open specific chat
    LaunchedEffect(initialChatId) {
        initialChatId?.let { chatId ->
            viewModel.onEvent(ChatEvent.SelectHistory(chatId))
        }
    }
    
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }
    
    LaunchedEffect(state.isDrawerOpen) {
        if (state.isDrawerOpen) drawerState.open() else drawerState.close()
    }
    
    LaunchedEffect(drawerState.currentValue) {
        if (drawerState.currentValue == DrawerValue.Closed && state.isDrawerOpen) {
            viewModel.onEvent(ChatEvent.ToggleDrawer)
        }
    }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ChatSidebar(
                selectedModel = state.selectedModel,
                chatHistories = state.chatHistories,
                selectedHistoryId = state.selectedHistoryId,
                onModelSelect = { viewModel.onEvent(ChatEvent.SelectModel(it)) },
                onSelectHistory = { viewModel.onEvent(ChatEvent.SelectHistory(it)) },
                onNewChat = { viewModel.onEvent(ChatEvent.NewChat) },
                onSettingsClick = onSettingsClick,
                onProfileClick = onProfileClick,
                onClose = { 
                    scope.launch { drawerState.close() }
                    viewModel.onEvent(ChatEvent.ToggleDrawer)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundPrimary)
                .statusBarsPadding()
                .imePadding()
        ) {
            ChatTopBar(
                selectedModel = state.selectedModel,
                onMenuClick = { 
                    scope.launch { drawerState.open() }
                    viewModel.onEvent(ChatEvent.ToggleDrawer)
                },
                onProfileClick = onProfileClick
            )
            
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (state.messages.isEmpty()) {
                    item { EmptyStateContent(selectedModel = state.selectedModel) }
                }
                
                items(items = state.messages, key = { it.id }) { message ->
                    MessageBubble(message = message, model = state.selectedModel)
                }
                
                if (state.isLoading) {
                    item { TypingIndicator(model = state.selectedModel) }
                }
            }
            
            ChatInputSection(
                inputText = state.inputText,
                onInputChange = { viewModel.onEvent(ChatEvent.UpdateInput(it)) },
                onSend = { viewModel.onEvent(ChatEvent.SendMessage(state.inputText)) },
                isLoading = state.isLoading,
                isActionsExpanded = state.isActionsExpanded,
                onToggleActions = { viewModel.onEvent(ChatEvent.ToggleActionsPanel) },
                activeActions = state.activeActions,
                onToggleAction = { viewModel.onEvent(ChatEvent.ToggleAction(it)) },
                accentColor = state.selectedModel.brandColor
            )
        }
    }
}

@Composable
private fun ChatTopBar(
    selectedModel: AIModel,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val colors = UnifiedTheme.colors
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.surfaceElevated)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier.size(22.dp),
                tint = colors.textSecondary
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = colors.surfaceElevated,
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderDefault)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(selectedModel.brandColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedModel.name.first().toString(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = selectedModel.brandColor
                    )
                }
                
                Text(
                    text = selectedModel.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        IconButton(onClick = onProfileClick) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceElevated)
                    .border(1.dp, colors.borderDefault, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(20.dp),
                    tint = colors.textSecondary
                )
            }
        }
    }
}

@Composable
private fun EmptyStateContent(selectedModel: AIModel) {
    val colors = UnifiedTheme.colors
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(selectedModel.brandColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = selectedModel.name.first().toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = selectedModel.brandColor
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "Start a conversation",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = colors.textPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Use tools below to enhance your query",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary
        )
    }
}

@Composable
private fun MessageBubble(message: Message, model: AIModel) {
    val colors = UnifiedTheme.colors
    val isUser = message.isFromUser
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(model.brandColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = model.name.first().toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = model.brandColor
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Surface(
            modifier = Modifier.widthIn(max = 300.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) model.brandColor else colors.surfaceElevated
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isUser) Color.White else colors.textPrimary
            )
        }
    }
}

@Composable
private fun TypingIndicator(model: AIModel) {
    val colors = UnifiedTheme.colors
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(model.brandColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = model.name.first().toString(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = model.brandColor
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Surface(
            shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp),
            color = colors.surfaceElevated
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) { index ->
                    val infiniteTransition = rememberInfiniteTransition(label = "dot$index")
                    val alpha by infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, delayMillis = index * 200),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "alpha"
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(model.brandColor.copy(alpha = alpha))
                    )
                }
            }
        }
    }
}


@Composable
private fun ChatInputSection(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    isLoading: Boolean,
    isActionsExpanded: Boolean,
    onToggleActions: () -> Unit,
    activeActions: Set<ChatActionType>,
    onToggleAction: (ChatActionType) -> Unit,
    accentColor: Color
) {
    val colors = UnifiedTheme.colors
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.backgroundSecondary)
            .navigationBarsPadding()
    ) {
        AnimatedVisibility(
            visible = activeActions.isNotEmpty(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                activeActions.forEach { actionType ->
                    val action = ChatActions.allActions.find { it.type == actionType }
                    action?.let {
                        ActiveActionChip(
                            action = it,
                            onRemove = { onToggleAction(actionType) }
                        )
                    }
                }
            }
        }
        
        AnimatedVisibility(
            visible = isActionsExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            ActionsToolbar(
                activeActions = activeActions,
                onToggleAction = onToggleAction
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onToggleActions,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActionsExpanded) accentColor.copy(alpha = 0.1f)
                        else colors.surfaceElevated
                    )
            ) {
                Icon(
                    imageVector = if (isActionsExpanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = "Tools",
                    modifier = Modifier.size(20.dp),
                    tint = if (isActionsExpanded) accentColor else colors.textSecondary
                )
            }
            
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                color = colors.inputBackground,
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.inputBorder)
            ) {
                BasicTextField(
                    value = inputText,
                    onValueChange = onInputChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = colors.textPrimary
                    ),
                    cursorBrush = SolidColor(accentColor),
                    decorationBox = { innerTextField ->
                        Box {
                            if (inputText.isEmpty()) {
                                Text(
                                    text = "Message...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colors.textMuted
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
            
            IconButton(
                onClick = onSend,
                enabled = inputText.isNotBlank() && !isLoading,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (inputText.isNotBlank() && !isLoading) accentColor
                        else colors.surfaceElevated
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(18.dp),
                    tint = if (inputText.isNotBlank() && !isLoading) Color.White
                    else colors.textMuted
                )
            }
        }
    }
}

@Composable
private fun ActionsToolbar(
    activeActions: Set<ChatActionType>,
    onToggleAction: (ChatActionType) -> Unit
) {
    val colors = UnifiedTheme.colors
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = "Search & Reasoning",
            style = MaterialTheme.typography.labelSmall,
            color = colors.textMuted,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChatActions.primaryActions.forEach { action ->
                ActionButton(
                    action = action,
                    isActive = activeActions.contains(action.type),
                    onClick = { onToggleAction(action.type) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Media & Files",
            style = MaterialTheme.typography.labelSmall,
            color = colors.textMuted,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChatActions.mediaActions.forEach { action ->
                ActionButton(
                    action = action,
                    isActive = activeActions.contains(action.type),
                    onClick = { onToggleAction(action.type) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Advanced Tools",
            style = MaterialTheme.typography.labelSmall,
            color = colors.textMuted,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChatActions.advancedActions.forEach { action ->
                ActionButton(
                    action = action,
                    isActive = activeActions.contains(action.type),
                    onClick = { onToggleAction(action.type) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    action: ChatAction,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = UnifiedTheme.colors
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isActive) colors.accentBlue.copy(alpha = 0.1f) else colors.surfaceElevated,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isActive) colors.accentBlue else colors.borderDefault
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.label,
                modifier = Modifier.size(20.dp),
                tint = if (isActive) colors.accentBlue else colors.textSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = action.label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isActive) colors.accentBlue else colors.textSecondary,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
            )
            Text(
                text = if (action.isPremium) "Pro" else "Free",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                color = if (action.isPremium) colors.accentAmber else colors.accentMint
            )
        }
    }
}

@Composable
private fun ActiveActionChip(
    action: ChatAction,
    onRemove: () -> Unit
) {
    val colors = UnifiedTheme.colors
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.accentBlue.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.accentBlue.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = colors.accentBlue
            )
            Text(
                text = action.label,
                style = MaterialTheme.typography.labelSmall,
                color = colors.accentBlue
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onRemove),
                tint = colors.accentBlue
            )
        }
    }
}

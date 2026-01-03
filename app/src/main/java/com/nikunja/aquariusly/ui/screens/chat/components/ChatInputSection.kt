package com.nikunja.aquariusly.ui.screens.chat.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nikunja.aquariusly.domain.model.ChatAction
import com.nikunja.aquariusly.domain.model.ChatActionType
import com.nikunja.aquariusly.domain.model.ChatActions
import com.nikunja.aquariusly.domain.model.MessageAttachment
import com.nikunja.aquariusly.domain.model.AttachmentType
import com.nikunja.aquariusly.ui.theme.UnifiedTheme

@Composable
fun ChatInputSection(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    isLoading: Boolean,
    isActionsExpanded: Boolean,
    onToggleActions: () -> Unit,
    activeActions: Set<ChatActionType>,
    onToggleAction: (ChatActionType) -> Unit,
    attachments: List<MessageAttachment>,
    onAddAttachment: (MessageAttachment) -> Unit,
    onRemoveAttachment: (MessageAttachment) -> Unit,
    accentColor: Color
) {
    val colors = UnifiedTheme.colors
    val context = LocalContext.current
    
    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val attachment = MessageAttachment(
                id = System.currentTimeMillis().toString(),
                type = AttachmentType.IMAGE,
                uri = it.toString(),
                name = "Image",
                size = 0L
            )
            onAddAttachment(attachment)
        }
    }
    
    // PDF/File picker
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val cursor = context.contentResolver.query(it, null, null, null, null)
            val nameIndex = cursor?.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor?.getColumnIndex(android.provider.OpenableColumns.SIZE)
            cursor?.moveToFirst()
            val name = nameIndex?.let { idx -> cursor.getString(idx) } ?: "File"
            val size = sizeIndex?.let { idx -> cursor.getLong(idx) } ?: 0L
            cursor?.close()
            
            val isPdf = name.endsWith(".pdf", ignoreCase = true)
            val attachment = MessageAttachment(
                id = System.currentTimeMillis().toString(),
                type = if (isPdf) AttachmentType.PDF else AttachmentType.FILE,
                uri = it.toString(),
                name = name,
                size = size
            )
            onAddAttachment(attachment)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.backgroundSecondary)
            .navigationBarsPadding()
    ) {
        // Attachments preview
        AnimatedVisibility(
            visible = attachments.isNotEmpty(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            AttachmentsRow(
                attachments = attachments,
                onRemove = onRemoveAttachment
            )
        }
        
        // Active actions chips
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
        
        // Actions toolbar
        AnimatedVisibility(
            visible = isActionsExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            ActionsToolbar(
                activeActions = activeActions,
                onToggleAction = onToggleAction,
                onImagePick = { imagePickerLauncher.launch("image/*") },
                onFilePick = { filePickerLauncher.launch(arrayOf("application/pdf", "*/*")) }
            )
        }
        
        // Input row
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
            
            val canSend = (inputText.isNotBlank() || attachments.isNotEmpty()) && !isLoading
            IconButton(
                onClick = onSend,
                enabled = canSend,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (canSend) accentColor else colors.surfaceElevated
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(18.dp),
                    tint = if (canSend) Color.White else colors.textMuted
                )
            }
        }
    }
}

@Composable
private fun AttachmentsRow(
    attachments: List<MessageAttachment>,
    onRemove: (MessageAttachment) -> Unit
) {
    val colors = UnifiedTheme.colors
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        attachments.forEach { attachment ->
            AttachmentChip(
                attachment = attachment,
                onRemove = { onRemove(attachment) }
            )
        }
    }
}

@Composable
private fun AttachmentChip(
    attachment: MessageAttachment,
    onRemove: () -> Unit
) {
    val colors = UnifiedTheme.colors
    val icon = when (attachment.type) {
        AttachmentType.IMAGE -> "🖼️"
        AttachmentType.PDF -> "📄"
        AttachmentType.FILE -> "📎"
    }
    
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = colors.surfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderDefault)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = icon, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = attachment.name.take(15) + if (attachment.name.length > 15) "..." else "",
                style = MaterialTheme.typography.labelMedium,
                color = colors.textPrimary
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onRemove),
                tint = colors.textMuted
            )
        }
    }
}

@Composable
private fun ActionsToolbar(
    activeActions: Set<ChatActionType>,
    onToggleAction: (ChatActionType) -> Unit,
    onImagePick: () -> Unit,
    onFilePick: () -> Unit
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
            // Image upload button
            MediaActionButton(
                label = "Image",
                icon = "🖼️",
                onClick = onImagePick,
                modifier = Modifier.weight(1f)
            )
            // PDF/File upload button
            MediaActionButton(
                label = "PDF",
                icon = "📄",
                onClick = onFilePick,
                modifier = Modifier.weight(1f)
            )
            // Generate image action
            ActionButton(
                action = ChatActions.generateImage,
                isActive = activeActions.contains(ChatActionType.GENERATE_IMAGE),
                onClick = { onToggleAction(ChatActionType.GENERATE_IMAGE) },
                modifier = Modifier.weight(1f)
            )
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
private fun MediaActionButton(
    label: String,
    icon: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = UnifiedTheme.colors
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = colors.surfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderDefault)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary
            )
            Text(
                text = "Upload",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                color = colors.accentMint
            )
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

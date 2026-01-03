package com.nikunja.aquariusly.ui.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nikunja.aquariusly.domain.model.AIModel
import com.nikunja.aquariusly.domain.model.Message
import com.nikunja.aquariusly.domain.model.MessageAttachment
import com.nikunja.aquariusly.domain.model.AttachmentType
import com.nikunja.aquariusly.ui.theme.UnifiedTheme

@Composable
fun MessageBubble(
    message: Message,
    model: AIModel
) {
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
        
        Column(
            modifier = Modifier.widthIn(max = 300.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            // Show attachments if any
            message.attachments.forEach { attachment ->
                AttachmentPreview(
                    attachment = attachment,
                    isUser = isUser,
                    accentColor = model.brandColor
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            // Message content
            if (message.content.isNotBlank()) {
                Surface(
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
    }
}

@Composable
private fun AttachmentPreview(
    attachment: MessageAttachment,
    isUser: Boolean,
    accentColor: Color
) {
    val colors = UnifiedTheme.colors
    
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isUser) accentColor.copy(alpha = 0.8f) else colors.surfaceElevated
    ) {
        when (attachment.type) {
            AttachmentType.IMAGE -> {
                AsyncImage(
                    model = attachment.uri,
                    contentDescription = attachment.name,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            AttachmentType.PDF -> {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📄",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Column {
                        Text(
                            text = attachment.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isUser) Color.White else colors.textPrimary,
                            maxLines = 1
                        )
                        Text(
                            text = formatFileSize(attachment.size),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isUser) Color.White.copy(alpha = 0.7f) else colors.textMuted
                        )
                    }
                }
            }
            AttachmentType.FILE -> {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📎",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = attachment.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isUser) Color.White else colors.textPrimary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> "${bytes / (1024 * 1024)} MB"
    }
}

package com.nikunja.aquariusly.ui.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nikunja.aquariusly.domain.model.AIModel
import com.nikunja.aquariusly.ui.theme.UnifiedTheme

@Composable
fun EmptyStateContent(selectedModel: AIModel) {
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

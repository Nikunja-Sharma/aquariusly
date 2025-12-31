package com.nikunja.testapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nikunja.testapp.ui.theme.UnifiedTheme

/**
 * Unified AI Logo - Clean, flat, iconic design
 * Inspired by modern tech logos like Cursor, ChatGPT, Chrome
 * 
 * Design: Stylized "U" with a spark element representing AI
 */
@Composable
fun UnifiedAILogo(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    val colors = UnifiedTheme.colors
    
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        
        val strokeWidth = w * 0.12f
        
        // Main "U" shape - clean rounded bottom
        val uPath = Path().apply {
            // Start from top left
            moveTo(w * 0.2f, w * 0.15f)
            // Down the left side
            lineTo(w * 0.2f, w * 0.55f)
            // Curve at bottom
            cubicTo(
                w * 0.2f, w * 0.85f,
                w * 0.35f, w * 0.95f,
                w * 0.5f, w * 0.95f
            )
            cubicTo(
                w * 0.65f, w * 0.95f,
                w * 0.8f, w * 0.85f,
                w * 0.8f, w * 0.55f
            )
            // Up the right side (shorter - leaves room for spark)
            lineTo(w * 0.8f, w * 0.4f)
        }
        
        // Draw U with gradient stroke
        drawPath(
            path = uPath,
            brush = Brush.verticalGradient(
                colors = listOf(colors.accentBlue, colors.accentViolet),
                startY = 0f,
                endY = h
            ),
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
        
        // Spark/Star element - the AI indicator (top right)
        val sparkCenterX = w * 0.78f
        val sparkCenterY = w * 0.22f
        val sparkSize = w * 0.18f
        
        // Four-pointed star spark
        val sparkPath = Path().apply {
            // Top point
            moveTo(sparkCenterX, sparkCenterY - sparkSize)
            // Right point
            lineTo(sparkCenterX + sparkSize * 0.3f, sparkCenterY - sparkSize * 0.3f)
            lineTo(sparkCenterX + sparkSize, sparkCenterY)
            // Bottom point
            lineTo(sparkCenterX + sparkSize * 0.3f, sparkCenterY + sparkSize * 0.3f)
            lineTo(sparkCenterX, sparkCenterY + sparkSize)
            // Left point
            lineTo(sparkCenterX - sparkSize * 0.3f, sparkCenterY + sparkSize * 0.3f)
            lineTo(sparkCenterX - sparkSize, sparkCenterY)
            // Back to top
            lineTo(sparkCenterX - sparkSize * 0.3f, sparkCenterY - sparkSize * 0.3f)
            close()
        }
        
        drawPath(
            path = sparkPath,
            brush = Brush.linearGradient(
                colors = listOf(colors.accentViolet, colors.accentMint),
                start = Offset(sparkCenterX - sparkSize, sparkCenterY - sparkSize),
                end = Offset(sparkCenterX + sparkSize, sparkCenterY + sparkSize)
            )
        )
    }
}

/**
 * Alternative: Minimal abstract logo
 * Two overlapping rounded squares forming a unified shape
 */
@Composable
fun UnifiedAILogoAlt(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    val colors = UnifiedTheme.colors
    
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val cornerRadius = w * 0.2f
        
        // Back square (slightly offset)
        drawRoundRect(
            color = colors.accentViolet,
            topLeft = Offset(w * 0.15f, w * 0.15f),
            size = Size(w * 0.6f, w * 0.6f),
            cornerRadius = CornerRadius(cornerRadius)
        )
        
        // Front square
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(colors.accentBlue, colors.accentBlue),
                start = Offset(0f, 0f),
                end = Offset(w, w)
            ),
            topLeft = Offset(w * 0.25f, w * 0.25f),
            size = Size(w * 0.6f, w * 0.6f),
            cornerRadius = CornerRadius(cornerRadius)
        )
        
        // Small accent dot
        drawCircle(
            color = colors.accentMint,
            radius = w * 0.08f,
            center = Offset(w * 0.75f, w * 0.25f)
        )
    }
}

/**
 * Chat bubble style logo - represents AI conversation
 */
@Composable
fun UnifiedAILogoBubble(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    val colors = UnifiedTheme.colors
    
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        
        // Main chat bubble shape
        val bubblePath = Path().apply {
            moveTo(w * 0.1f, w * 0.25f)
            // Top edge
            lineTo(w * 0.9f, w * 0.25f)
            // Top right corner
            cubicTo(w * 0.95f, w * 0.25f, w * 0.95f, w * 0.3f, w * 0.95f, w * 0.35f)
            // Right edge
            lineTo(w * 0.95f, w * 0.6f)
            // Bottom right corner
            cubicTo(w * 0.95f, w * 0.7f, w * 0.9f, w * 0.75f, w * 0.8f, w * 0.75f)
            // Bottom edge with tail
            lineTo(w * 0.35f, w * 0.75f)
            lineTo(w * 0.2f, w * 0.9f)
            lineTo(w * 0.25f, w * 0.75f)
            lineTo(w * 0.15f, w * 0.75f)
            // Bottom left corner
            cubicTo(w * 0.05f, w * 0.75f, w * 0.05f, w * 0.7f, w * 0.05f, w * 0.6f)
            // Left edge
            lineTo(w * 0.05f, w * 0.35f)
            // Top left corner
            cubicTo(w * 0.05f, w * 0.3f, w * 0.05f, w * 0.25f, w * 0.1f, w * 0.25f)
            close()
        }
        
        drawPath(
            path = bubblePath,
            brush = Brush.linearGradient(
                colors = listOf(colors.accentBlue, colors.accentViolet),
                start = Offset(0f, 0f),
                end = Offset(w, w)
            )
        )
        
        // Three dots inside (typing indicator style)
        val dotY = w * 0.5f
        val dotRadius = w * 0.06f
        val dotSpacing = w * 0.18f
        
        drawCircle(color = Color.White, radius = dotRadius, center = Offset(w * 0.35f, dotY))
        drawCircle(color = Color.White, radius = dotRadius, center = Offset(w * 0.5f, dotY))
        drawCircle(color = Color.White, radius = dotRadius, center = Offset(w * 0.65f, dotY))
    }
}

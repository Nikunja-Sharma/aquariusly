package com.nikunja.aquariusly.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nikunja.aquariusly.ui.theme.UnifiedTheme

/**
 * Aquariusly Logo - Premium stylized "A" with water wave elements
 * Represents the Aquarius zodiac water bearer theme
 */
@Composable
fun AquariuslyLogo(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp
) {
    val colors = UnifiedTheme.colors
    
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        
        val strokeWidth = w * 0.06f
        val thinStroke = w * 0.03f
        
        // Stylized "A" - Left stroke (blue)
        val leftPath = Path().apply {
            moveTo(w * 0.30f, h * 0.75f)
            lineTo(w * 0.50f, h * 0.20f)
        }
        
        drawPath(
            path = leftPath,
            brush = Brush.linearGradient(
                colors = listOf(colors.accentBlue, colors.accentBlue),
                start = Offset(w * 0.30f, h * 0.75f),
                end = Offset(w * 0.50f, h * 0.20f)
            ),
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
        
        // Stylized "A" - Right stroke (violet)
        val rightPath = Path().apply {
            moveTo(w * 0.50f, h * 0.20f)
            lineTo(w * 0.70f, h * 0.75f)
        }
        
        drawPath(
            path = rightPath,
            brush = Brush.linearGradient(
                colors = listOf(colors.accentViolet, colors.accentViolet),
                start = Offset(w * 0.50f, h * 0.20f),
                end = Offset(w * 0.70f, h * 0.75f)
            ),
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
        
        // Horizontal bar (mint/green)
        val barPath = Path().apply {
            moveTo(w * 0.38f, h * 0.55f)
            lineTo(w * 0.62f, h * 0.55f)
        }
        
        drawPath(
            path = barPath,
            color = colors.accentMint,
            style = Stroke(
                width = strokeWidth * 0.85f,
                cap = StrokeCap.Round
            )
        )
        
        // Water wave 1 (bottom)
        val wave1 = Path().apply {
            moveTo(w * 0.20f, h * 0.82f)
            quadraticTo(w * 0.30f, h * 0.76f, w * 0.40f, h * 0.82f)
            quadraticTo(w * 0.50f, h * 0.88f, w * 0.60f, h * 0.82f)
            quadraticTo(w * 0.70f, h * 0.76f, w * 0.80f, h * 0.82f)
        }
        
        drawPath(
            path = wave1,
            color = colors.accentBlue,
            style = Stroke(
                width = thinStroke,
                cap = StrokeCap.Round
            )
        )
        
        // Water wave 2 (bottom, lighter)
        val wave2 = Path().apply {
            moveTo(w * 0.25f, h * 0.90f)
            quadraticTo(w * 0.35f, h * 0.84f, w * 0.45f, h * 0.90f)
            quadraticTo(w * 0.55f, h * 0.96f, w * 0.65f, h * 0.90f)
            quadraticTo(w * 0.75f, h * 0.84f, w * 0.85f, h * 0.90f)
        }
        
        drawPath(
            path = wave2,
            color = colors.accentBlue.copy(alpha = 0.5f),
            style = Stroke(
                width = thinStroke * 0.8f,
                cap = StrokeCap.Round
            )
        )
        
        // Sparkle accent (top right)
        val sparkleCenter = Offset(w * 0.75f, h * 0.18f)
        val sparkleSize = w * 0.08f
        
        // Vertical line
        drawLine(
            color = colors.accentViolet,
            start = Offset(sparkleCenter.x, sparkleCenter.y - sparkleSize),
            end = Offset(sparkleCenter.x, sparkleCenter.y + sparkleSize),
            strokeWidth = thinStroke * 0.7f,
            cap = StrokeCap.Round
        )
        
        // Horizontal line
        drawLine(
            color = colors.accentViolet,
            start = Offset(sparkleCenter.x - sparkleSize, sparkleCenter.y),
            end = Offset(sparkleCenter.x + sparkleSize, sparkleCenter.y),
            strokeWidth = thinStroke * 0.7f,
            cap = StrokeCap.Round
        )
        
        // Diagonal lines (smaller)
        val diagSize = sparkleSize * 0.7f
        drawLine(
            color = colors.accentViolet.copy(alpha = 0.7f),
            start = Offset(sparkleCenter.x - diagSize, sparkleCenter.y - diagSize),
            end = Offset(sparkleCenter.x + diagSize, sparkleCenter.y + diagSize),
            strokeWidth = thinStroke * 0.5f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = colors.accentViolet.copy(alpha = 0.7f),
            start = Offset(sparkleCenter.x + diagSize, sparkleCenter.y - diagSize),
            end = Offset(sparkleCenter.x - diagSize, sparkleCenter.y + diagSize),
            strokeWidth = thinStroke * 0.5f,
            cap = StrokeCap.Round
        )
    }
}

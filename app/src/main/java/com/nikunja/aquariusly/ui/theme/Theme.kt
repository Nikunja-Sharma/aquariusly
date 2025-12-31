package com.nikunja.aquariusly.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ═══════════════════════════════════════════════════════════════════════════
// UNIFIED AI - Extended Color System
// ═══════════════════════════════════════════════════════════════════════════

data class UnifiedAIColors(
    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val backgroundTertiary: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textMuted: Color,
    val borderDefault: Color,
    val borderSubtle: Color,
    val borderStrong: Color,
    val accentBlue: Color,
    val accentMint: Color,
    val accentViolet: Color,
    val accentAmber: Color,
    val success: Color,
    val successBg: Color,
    val error: Color,
    val errorBg: Color,
    val warning: Color,
    val warningBg: Color,
    val info: Color,
    val infoBg: Color,
    val cardBackground: Color,
    val cardBorder: Color,
    val inputBackground: Color,
    val inputBorder: Color,
    val overlay: Color,
    val skeleton: Color,
    val isDark: Boolean
)

val LightUnifiedColors = UnifiedAIColors(
    backgroundPrimary = BackgroundPrimaryLight,
    backgroundSecondary = BackgroundSecondaryLight,
    backgroundTertiary = BackgroundTertiaryLight,
    surface = SurfaceLight,
    surfaceElevated = SurfaceElevatedLight,
    textPrimary = TextPrimaryLight,
    textSecondary = TextSecondaryLight,
    textTertiary = TextTertiaryLight,
    textMuted = TextMutedLight,
    borderDefault = BorderDefaultLight,
    borderSubtle = BorderSubtleLight,
    borderStrong = BorderStrongLight,
    accentBlue = AccentBlue,
    accentMint = AccentMint,
    accentViolet = AccentViolet,
    accentAmber = AccentAmber,
    success = SuccessLight,
    successBg = SuccessBgLight,
    error = ErrorLight,
    errorBg = ErrorBgLight,
    warning = WarningLight,
    warningBg = WarningBgLight,
    info = InfoLight,
    infoBg = InfoBgLight,
    cardBackground = CardBackgroundLight,
    cardBorder = CardBorderLight,
    inputBackground = InputBackgroundLight,
    inputBorder = InputBorderLight,
    overlay = OverlayLight,
    skeleton = SkeletonLight,
    isDark = false
)

val DarkUnifiedColors = UnifiedAIColors(
    backgroundPrimary = BackgroundPrimaryDark,
    backgroundSecondary = BackgroundSecondaryDark,
    backgroundTertiary = BackgroundTertiaryDark,
    surface = SurfaceDark,
    surfaceElevated = SurfaceElevatedDark,
    textPrimary = TextPrimaryDark,
    textSecondary = TextSecondaryDark,
    textTertiary = TextTertiaryDark,
    textMuted = TextMutedDark,
    borderDefault = BorderDefaultDark,
    borderSubtle = BorderSubtleDark,
    borderStrong = BorderStrongDark,
    accentBlue = AccentBlue,
    accentMint = AccentMint,
    accentViolet = AccentViolet,
    accentAmber = AccentAmber,
    success = SuccessDark,
    successBg = SuccessBgDark,
    error = ErrorDark,
    errorBg = ErrorBgDark,
    warning = WarningDark,
    warningBg = WarningBgDark,
    info = InfoDark,
    infoBg = InfoBgDark,
    cardBackground = CardBackgroundDark,
    cardBorder = CardBorderDark,
    inputBackground = InputBackgroundDark,
    inputBorder = InputBorderDark,
    overlay = OverlayDark,
    skeleton = SkeletonDark,
    isDark = true
)

val LocalUnifiedColors = staticCompositionLocalOf { DarkUnifiedColors }

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundPrimaryLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundPrimaryDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark
)

@Composable
fun AquariuslyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val unifiedColors = if (darkTheme) DarkUnifiedColors else LightUnifiedColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = unifiedColors.backgroundPrimary.toArgb()
            window.navigationBarColor = unifiedColors.backgroundPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalUnifiedColors provides unifiedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

// Legacy theme for backward compatibility
@Composable
fun FlavorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AquariuslyTheme(darkTheme = darkTheme, content = content)
}

// Extension for easy access
object UnifiedTheme {
    val colors: UnifiedAIColors
        @Composable
        get() = LocalUnifiedColors.current
}

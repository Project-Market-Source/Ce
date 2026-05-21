package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CinemanaDarkColorScheme = darkColorScheme(
    primary = CinemanaRed,
    onPrimary = SolidWhite,
    primaryContainer = CinemanaRed,
    onPrimaryContainer = SolidWhite,
    secondary = NeonCoral,
    onSecondary = SolidWhite,
    tertiary = GoldenYellow,
    onTertiary = ObsidianBlack,
    background = ObsidianBlack,
    onBackground = TextPrimary,
    surface = SlateGrey,
    onSurface = TextPrimary,
    surfaceVariant = DarkCharcoal,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
    error = Color(0xFFEF4444),
    onError = SolidWhite
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force Dark mode for theatre-style movie streams
    dynamicColor: Boolean = false, // Preserve our brand colors
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CinemanaDarkColorScheme,
        typography = Typography,
        content = content
    )
}

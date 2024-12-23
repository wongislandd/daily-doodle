package com.wongislandd.nexus.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Primary and secondary colors
val primaryColor = Color(0xFFFF5722) // Vibrant Orange
val secondaryColor = Color(0xFF4CAF50) // Fresh Green

// Accent colors
val accentColor = Color(0xFFFFC107) // Playful Yellow
val errorColor = Color(0xFFD32F2F) // Bright Red for errors

// Background and text colors
val backgroundColor
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFFFF3E0) // Warm Cream for light theme

val textColor
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF212121) // Contrast-rich black text for light theme

val surfaceColor
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else Color(0xFFFFE0B2) // Soft Orange Surface in light theme

val ThemeColors
    @Composable
    get() = if (isSystemInDarkTheme()) {
        darkColors(
            primary = primaryColor,
            onPrimary = Color.White,
            secondary = secondaryColor,
            onSecondary = Color.White,
            background = backgroundColor,
            surface = surfaceColor,
            onSurface = textColor,
            onBackground = textColor,
            error = errorColor,
            onError = Color.White
        )
    } else {
        lightColors(
            primary = primaryColor,
            onPrimary = Color.White,
            secondary = secondaryColor,
            onSecondary = Color.White,
            background = backgroundColor,
            surface = surfaceColor,
            onSurface = textColor,
            onBackground = textColor,
            error = errorColor,
            onError = Color.White
        )
    }

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = ThemeColors,
        content = content
    )
}
package com.traq.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TraqTeal,
    onPrimary = Color.Black,
    primaryContainer = TraqTealDark,
    onPrimaryContainer = TraqTealLight,
    secondary = TraqAmber,
    onSecondary = Color.Black,
    secondaryContainer = TraqAmberDark,
    onSecondaryContainer = TraqAmberLight,
    background = DarkBackground,
    onBackground = Color(0xFFE1E3E6),
    surface = DarkSurface,
    onSurface = Color(0xFFE1E3E6),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFF9AA0A8),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = TraqTeal,
    onPrimary = Color.White,
    primaryContainer = TraqTealLight,
    onPrimaryContainer = TraqTealDark,
    secondary = TraqAmber,
    onSecondary = Color.Black,
    secondaryContainer = TraqAmberLight,
    onSecondaryContainer = TraqAmberDark,
    background = LightBackground,
    onBackground = Color(0xFF1A1F27),
    surface = LightSurface,
    onSurface = Color(0xFF1A1F27),
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF5F6368),
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun TraqTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TraqTypography,
        shapes = TraqShapes,
        content = content
    )
}

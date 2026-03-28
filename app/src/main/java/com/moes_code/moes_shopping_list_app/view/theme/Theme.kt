package com.moes_code.moes_shopping_list_app.view.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * Material 3 Dark Theme for Shopping List App
 * 
 * Uses the custom color palette with proper Material 3 color roles.
 * Optimized for dark mode with subtle surface elevation differences.
 */
private val DarkColorScheme = darkColorScheme(
    // Primary colors
    primary = Colors.moe_blue,
    onPrimary = Colors.moe_white,
    primaryContainer = Colors.moe_blue_dim,
    onPrimaryContainer = Colors.moe_blue_bright,
    
    // Secondary colors (Yellow for actions)
    secondary = Colors.moe_yellow,
    onSecondary = Colors.moe_black,
    secondaryContainer = Colors.moe_yellow_dim,
    onSecondaryContainer = Colors.moe_yellow,
    
    // Tertiary colors (Green for success)
    tertiary = Colors.moe_green,
    onTertiary = Colors.moe_white,
    tertiaryContainer = Colors.moe_green_dim,
    onTertiaryContainer = Colors.moe_green,
    
    // Error colors
    error = Colors.moe_red,
    onError = Colors.moe_white,
    errorContainer = Colors.moe_red_dim,
    onErrorContainer = Colors.moe_red,
    
    // Background and Surface
    background = Colors.moe_black,
    onBackground = Colors.moe_white,
    
    surface = Colors.surface,
    onSurface = Colors.onSurface,
    surfaceVariant = Colors.surfaceVariant,
    onSurfaceVariant = Colors.onSurfaceVariant,
    
    // Surface containers for elevation
    surfaceContainer = Colors.surfaceContainer,
    surfaceContainerLowest = Colors.moe_black,
    surfaceContainerLow = Colors.surface,
    surfaceContainerHigh = Colors.surfaceContainerHigh,
    surfaceContainerHighest = Colors.surfaceElevated,
    
    // Outline
    outline = Colors.outline,
    outlineVariant = Colors.outlineVariant,
    
    // Inverse colors
    inverseSurface = Colors.moe_white,
    inverseOnSurface = Colors.moe_black,
    inversePrimary = Colors.moe_blue_dim,
    
    // Scrim
    scrim = Colors.moe_black
)

@Composable
fun ShoppingListAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

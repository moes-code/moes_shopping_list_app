package com.moes_code.moes_shopping_list_app.view.theme

import androidx.compose.ui.graphics.Color

/**
 * App Color Palette - Material 3 Modern Design
 * 
 * These colors are used to define the MaterialTheme ColorScheme in Theme.kt.
 * UI components should use MaterialTheme.colorScheme instead of referencing
 * these colors directly, except for special cases like swipe backgrounds.
 */
object Colors {
    // === PRIMARY COLORS (Used in ColorScheme definition) ===
    val moe_black = Color(0xFF000000)
    val moe_white = Color(0xFFFFFFFF)
    val moe_blue = Color(0xFF007BFF)
    val moe_yellow = Color(0xFFFFD700)
    val moe_red = Color(0xFFDC3545)
    val moe_green = Color(0xFF28A745)
    
    // === SURFACE VARIANTS (Used in ColorScheme definition) ===
    val surface = Color(0xFF0A0A0A)           // Slightly lighter than pure black
    val surfaceVariant = Color(0xFF141414)    // Card backgrounds
    val surfaceElevated = Color(0xFF1C1C1C)   // Dialogs, elevated surfaces
    val surfaceContainer = Color(0xFF1E1E1E)  // Containers within cards
    val surfaceContainerHigh = Color(0xFF252525) // Higher elevation containers
    
    // === COLOR VARIANTS (Used in ColorScheme definition) ===
    val moe_blue_dim = Color(0xFF004A99)      // Dimmed blue for containers
    val moe_blue_bright = Color(0xFF3399FF)   // Brighter blue for emphasis
    val moe_yellow_dim = Color(0xFF997A00)    // Dimmed yellow
    val moe_red_dim = Color(0xFF8B1F2A)       // Dimmed red for containers
    val moe_green_dim = Color(0xFF1A6B2D)     // Dimmed green
    
    // === OUTLINE COLORS (Used in ColorScheme definition) ===
    val outline = Color(0xFF333333)           // Subtle borders
    val outlineVariant = Color(0xFF404040)    // More visible borders
    
    // === TEXT COLORS (Used in ColorScheme definition) ===
    val onSurface = moe_white
    val onSurfaceVariant = Color(0xFFB3B3B3)  // Secondary text
    
    // === SWIPE ACTION BACKGROUNDS (Used directly - no M3 equivalent) ===
    val swipeDeleteBackground = Color(0xFF2D1215)  // Dark red background
    val swipeEditBackground = Color(0xFF0D1F33)    // Dark blue background
}

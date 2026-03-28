package com.moes_code.moes_shopping_list_app.view.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Material 3 Shapes for Shopping List App
 * 
 * Consistent rounded corners throughout the app.
 */
val AppShapes = Shapes(
    // Extra Small - Chips, small buttons
    extraSmall = RoundedCornerShape(4.dp),
    
    // Small - Text fields, small cards
    small = RoundedCornerShape(8.dp),
    
    // Medium - Cards, dialogs
    medium = RoundedCornerShape(16.dp),
    
    // Large - Large cards, bottom sheets
    large = RoundedCornerShape(24.dp),
    
    // Extra Large - Full screen dialogs
    extraLarge = RoundedCornerShape(32.dp)
)

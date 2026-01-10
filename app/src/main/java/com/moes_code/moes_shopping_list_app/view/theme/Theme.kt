package com.moes_code.moes_shopping_list_app.view.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ShoppingListAppTheme(
    content: @Composable () -> Unit
) {
    val blackColorScheme = darkColorScheme(
        background = Colors.moe_black,
        surface = Colors.moe_black,
        surfaceVariant = Colors.moe_black,
        surfaceContainer = Colors.moe_black,
        surfaceContainerLowest = Colors.moe_black,
        surfaceContainerLow = Colors.moe_black,
        surfaceContainerHigh = Colors.moe_black,
        surfaceContainerHighest = Colors.moe_black
    )

    MaterialTheme(
        colorScheme = blackColorScheme,
        content = content
    )
}
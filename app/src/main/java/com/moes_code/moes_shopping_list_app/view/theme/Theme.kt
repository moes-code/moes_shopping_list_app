package com.moes_code.moes_shopping_list_app.view.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ShoppingListAppTheme(
    content: @Composable () -> Unit
) {
    val blackColorScheme = darkColorScheme(
        background = Colors.fifth,
        surface = Colors.fifth,
        surfaceVariant = Colors.fifth,
        surfaceContainer = Colors.fifth,
        surfaceContainerLowest = Colors.fifth,
        surfaceContainerLow = Colors.fifth,
        surfaceContainerHigh = Colors.fifth,
        surfaceContainerHighest = Colors.fifth
    )

    MaterialTheme(
        colorScheme = blackColorScheme,
        content = content
    )
}
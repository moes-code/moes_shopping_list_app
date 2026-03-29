package com.moes_code.moes_shopping_list_app.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.moes_code.moes_shopping_list_app.view.theme.Colors
import com.moes_code.moes_shopping_list_app.view.theme.Dimensions

/**
 * Reusable swipe background for swipeable items.
 * Shows edit icon when swiping right, delete icon when swiping left.
 *
 * @param direction Current swipe direction
 * @param modifier Modifier to be applied
 * @param shape Shape of the background (should match the foreground item)
 * @param horizontalPadding Padding for the icon from the edge
 * @param iconSize Size of the action icon
 */
@Composable
fun SwipeBackground(
    direction: SwipeDirection,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    horizontalPadding: Dp = Dimensions.swipePaddingItem,
    iconSize: Dp = Dimensions.swipeIconSizeItem
) {
    val colorScheme = MaterialTheme.colorScheme
    
    // Only render content when actively swiping
    if (direction == SwipeDirection.None) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clip(shape)
                .background(colorScheme.surfaceVariant)
        )
        return
    }
    
    val (backgroundColor, icon, alignment) = when (direction) {
        SwipeDirection.Right -> Triple(
            Colors.swipeEditBackground,
            Icons.Default.Edit,
            Alignment.CenterStart
        )
        SwipeDirection.Left -> Triple(
            Colors.swipeDeleteBackground,
            Icons.Default.Delete,
            Alignment.CenterEnd
        )
        SwipeDirection.None -> error("Handled above")
    }
    
    val iconTint = when (direction) {
        SwipeDirection.Right -> colorScheme.primary
        SwipeDirection.Left -> colorScheme.error
        SwipeDirection.None -> error("Handled above")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(shape)
            .background(backgroundColor)
            .padding(horizontal = horizontalPadding),
        contentAlignment = alignment
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}

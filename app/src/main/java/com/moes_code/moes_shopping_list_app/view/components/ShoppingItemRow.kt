package com.moes_code.moes_shopping_list_app.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.moes_code.moes_shopping_list_app.R
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import com.moes_code.moes_shopping_list_app.view.theme.Dimensions

/**
 * A row displaying a shopping item with checkbox, name, quantity, and action buttons.
 * Supports swipe actions for edit/delete.
 */
@Composable
fun ShoppingItemRow(
    item: ShoppingItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleCompleted: () -> Unit,
    swipeResetTrigger: Any? = null
) {
    val colorScheme = MaterialTheme.colorScheme
    
    // Animate border color based on completed state (dimmed when completed)
    val borderColor by animateColorAsState(
        targetValue = if (item.isCompleted) {
            colorScheme.primary.copy(alpha = 0.4f)
        } else {
            colorScheme.primary
        },
        animationSpec = tween(300),
        label = "borderColorAnimation"
    )
    
    // Animate text color based on completed state
    val textColor by animateColorAsState(
        targetValue = if (item.isCompleted) {
            colorScheme.onSurface.copy(alpha = 0.5f)
        } else {
            colorScheme.onSurface
        },
        animationSpec = tween(300),
        label = "textColorAnimation"
    )

    SwipeableItem(
        onSwipeLeft = onDelete,
        onSwipeRight = onEdit,
        resetTrigger = swipeResetTrigger,
        backgroundContent = { direction ->
            SwipeBackground(
                direction = direction,
                shape = MaterialTheme.shapes.medium,
                horizontalPadding = Dimensions.swipePaddingItem,
                iconSize = Dimensions.swipeIconSizeItem
            )
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.background
            ),
            border = BorderStroke(
                width = Dimensions.itemBorderWidth,
                color = borderColor
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimensions.itemPaddingHorizontal,
                        vertical = Dimensions.itemPaddingVertical
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox for completed state
                Checkbox(
                    checked = item.isCompleted,
                    onCheckedChange = { onToggleCompleted() },
                    modifier = Modifier.scale(Dimensions.CHECKBOX_SCALE),
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorScheme.tertiary,
                        uncheckedColor = colorScheme.outline,
                        checkmarkColor = colorScheme.onTertiary
                    )
                )
                
                // Item name and quantity with animation
                AnimatedContent(
                    targetState = item.isCompleted,
                    transitionSpec = {
                        fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) togetherWith
                            fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
                    },
                    modifier = Modifier.weight(1f),
                    label = "completedStateAnimation"
                ) { isCompleted ->
                    Text(
                        text = "${item.name} (${item.quantity})",
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
        }
    }
}

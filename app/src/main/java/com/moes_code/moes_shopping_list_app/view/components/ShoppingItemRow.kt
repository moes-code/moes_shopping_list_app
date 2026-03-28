package com.moes_code.moes_shopping_list_app.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import com.moes_code.moes_shopping_list_app.view.theme.Colors

@Composable
fun ShoppingItemRow(
    item: ShoppingItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleCompleted: () -> Unit,
    swipeResetTrigger: Any? = null
) {
    val colorScheme = MaterialTheme.colorScheme
    
    // Animate background color based on completed state
    val backgroundColor by animateColorAsState(
        targetValue = if (item.isCompleted) {
            colorScheme.surfaceContainer.copy(alpha = 0.5f)
        } else {
            colorScheme.surfaceContainer
        },
        animationSpec = tween(300),
        label = "backgroundColorAnimation"
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
        enableSwipeLeft = true,
        enableSwipeRight = true,
        resetTrigger = swipeResetTrigger,
        backgroundContent = { direction ->
            ItemSwipeBackground(direction = direction)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(backgroundColor)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox for completed state
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggleCompleted() },
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
            
            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Edit Button
                FilledTonalIconButton(
                    onClick = onEdit,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = colorScheme.primaryContainer,
                        contentColor = colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Item",
                        modifier = Modifier.size(18.dp)
                    )
                }
                
                // Delete Button
                FilledTonalIconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = colorScheme.errorContainer,
                        contentColor = colorScheme.onErrorContainer
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Item",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemSwipeBackground(direction: SwipeDirection) {
    val colorScheme = MaterialTheme.colorScheme
    
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
        SwipeDirection.None -> Triple(
            colorScheme.surfaceContainer,
            Icons.Default.Delete,
            Alignment.CenterEnd
        )
    }
    
    val iconTint = when (direction) {
        SwipeDirection.Right -> colorScheme.primary
        SwipeDirection.Left -> colorScheme.error
        SwipeDirection.None -> colorScheme.error
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .padding(horizontal = 16.dp),
        contentAlignment = alignment
    ) {
        if (direction != SwipeDirection.None) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

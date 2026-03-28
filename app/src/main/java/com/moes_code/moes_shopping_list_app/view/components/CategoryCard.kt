package com.moes_code.moes_shopping_list_app.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import com.moes_code.moes_shopping_list_app.view.theme.Colors

@Composable
fun CategoryCard(
    category: Category,
    items: List<ShoppingItem>,
    onAddItem: () -> Unit,
    onEditCategory: () -> Unit,
    onEditItem: (ShoppingItem) -> Unit,
    onDeleteCategory: () -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit,
    onToggleItemCompleted: (ShoppingItem) -> Unit,
    modifier: Modifier = Modifier,
    swipeResetTrigger: Any? = null
) {
    var isExpanded by remember { mutableStateOf(true) }

    SwipeableItem(
        onSwipeLeft = onDeleteCategory,
        onSwipeRight = onEditCategory,
        modifier = modifier,
        enableSwipeLeft = true,
        enableSwipeRight = true,
        resetTrigger = swipeResetTrigger,
        backgroundContent = { direction ->
            CategorySwipeBackground(direction = direction)
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category name with icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Add Item Button
                        FilledTonalIconButton(
                            onClick = onAddItem,
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add Item",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        // Edit Category Button
                        FilledTonalIconButton(
                            onClick = onEditCategory,
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit Category",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        // Delete Category Button
                        FilledTonalIconButton(
                            onClick = onDeleteCategory,
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete Category",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Divider
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 1.dp
                )

                // Items List
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        if (items.isNotEmpty()) {
                            items.forEachIndexed { index, item ->
                                ShoppingItemRow(
                                    item = item,
                                    onEdit = { onEditItem(item) },
                                    onDelete = { onDeleteItem(item) },
                                    onToggleCompleted = { onToggleItemCompleted(item) },
                                    swipeResetTrigger = swipeResetTrigger
                                )
                                if (index < items.lastIndex) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        } else {
                            EmptyItemsPlaceholder()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategorySwipeBackground(direction: SwipeDirection) {
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
            MaterialTheme.colorScheme.surfaceVariant,
            Icons.Default.Delete,
            Alignment.CenterEnd
        )
    }
    
    val iconTint = when (direction) {
        SwipeDirection.Right -> MaterialTheme.colorScheme.primary
        SwipeDirection.Left -> MaterialTheme.colorScheme.error
        SwipeDirection.None -> MaterialTheme.colorScheme.error
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.large)
            .background(backgroundColor)
            .padding(horizontal = 24.dp),
        contentAlignment = alignment
    ) {
        if (direction != SwipeDirection.None) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun EmptyItemsPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No items yet. Tap + to add one!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

package com.moes_code.moes_shopping_list_app.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.moes_code.moes_shopping_list_app.R
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import com.moes_code.moes_shopping_list_app.view.theme.Dimensions

/**
 * A card displaying a category with its items.
 * Supports swipe actions for edit/delete and expandable/collapsible items list.
 */
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
    
    // Animate rotation for expand icon (0° when expanded, 180° when collapsed)
    val expandIconRotation by animateFloatAsState(
        targetValue = if (isExpanded) 0f else 180f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "expandIconRotation"
    )

    SwipeableItem(
        onSwipeLeft = onDeleteCategory,
        onSwipeRight = onEditCategory,
        modifier = modifier,
        resetTrigger = swipeResetTrigger,
        backgroundContent = { direction ->
            SwipeBackground(
                direction = direction,
                shape = MaterialTheme.shapes.large,
                horizontalPadding = Dimensions.swipePaddingCategory,
                iconSize = Dimensions.swipeIconSizeCategory
            )
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(
                width = Dimensions.categoryBorderWidth,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier.padding(Dimensions.categoryPadding)
            ) {
                // Header Row
                CategoryHeader(
                    categoryName = category.name,
                    isExpanded = isExpanded,
                    expandIconRotation = expandIconRotation,
                    onExpandClick = { isExpanded = !isExpanded },
                    onAddItem = onAddItem,
                    onEditCategory = onEditCategory,
                    onDeleteCategory = onDeleteCategory
                )

                // Divider
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimensions.categoryDividerPadding),
                    color = MaterialTheme.colorScheme.outline
                )

                // Items List (expandable)
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
                    CategoryItemsList(
                        items = items,
                        onEditItem = onEditItem,
                        onDeleteItem = onDeleteItem,
                        onToggleItemCompleted = onToggleItemCompleted,
                        swipeResetTrigger = swipeResetTrigger
                    )
                }
            }
        }
    }
}

/**
 * Header row for the category card containing expand button, title, and action buttons.
 */
@Composable
private fun CategoryHeader(
    categoryName: String,
    isExpanded: Boolean,
    expandIconRotation: Float,
    onExpandClick: () -> Unit,
    onAddItem: () -> Unit,
    onEditCategory: () -> Unit,
    onDeleteCategory: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Expand button + Category name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Expand/Collapse button (transparent)
            IconButton(
                onClick = onExpandClick,
                modifier = Modifier.size(Dimensions.expandButtonSize)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(
                        if (isExpanded) R.string.content_description_collapse 
                        else R.string.content_description_expand
                    ),
                    tint = colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(Dimensions.expandIconSize)
                        .rotate(expandIconRotation)
                )
            }
            
            Spacer(modifier = Modifier.width(Dimensions.expandButtonTitleSpacing))
            
            // Category name
            Text(
                text = categoryName,
                style = MaterialTheme.typography.titleLarge,
                color = colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        // Action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimensions.categoryButtonSpacing)
        ) {
            // Add Item Button
            ActionIconButton(
                onClick = onAddItem,
                icon = Icons.Default.Add,
                contentDescription = stringResource(R.string.action_add_item),
                containerColor = colorScheme.secondaryContainer,
                contentColor = colorScheme.onSecondaryContainer,
                buttonSize = Dimensions.categoryButtonSize,
                iconSize = Dimensions.categoryIconSize
            )
        }
    }
}

/**
 * List of shopping items within a category.
 */
@Composable
private fun CategoryItemsList(
    items: List<ShoppingItem>,
    onEditItem: (ShoppingItem) -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit,
    onToggleItemCompleted: (ShoppingItem) -> Unit,
    swipeResetTrigger: Any?
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
                    Spacer(modifier = Modifier.height(Dimensions.itemSpacingInCategory))
                }
            }
        } else {
            EmptyItemsPlaceholder()
        }
    }
}

/**
 * Placeholder shown when a category has no items.
 */
@Composable
private fun EmptyItemsPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.emptyPlaceholderPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.empty_items_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

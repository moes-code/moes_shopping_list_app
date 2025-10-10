package com.moes_code.moes_shopping_list_app.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    category: Category,
    items: List<ShoppingItem>,
    onAddItem: () -> Unit,
    onEditItem: (ShoppingItem) -> Unit,
    onDeleteCategory: () -> Unit,
    onDeleteItem: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    IconButton(onClick = onAddItem) {
                        Icon(Icons.Default.Add, contentDescription = "Add Item")
                    }
                    IconButton(onClick = onDeleteCategory) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Category",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            if (items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                items.forEach { item ->
                    ShoppingItemRow(
                        item = item,
                        onEdit = { onEditItem(item) },
                        onDelete = { onDeleteItem(item.id) }
                    )
                }
            } else {
                Text(
                    text = "No items in this category",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
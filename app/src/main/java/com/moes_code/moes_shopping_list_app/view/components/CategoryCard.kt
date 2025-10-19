package com.moes_code.moes_shopping_list_app.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.model.ShoppingItem
import com.moes_code.moes_shopping_list_app.view.theme.Colors

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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Colors.primary
        )
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
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Colors.secondary
                )
                Row {
                    IconButton(onClick = onAddItem) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Item",
                            tint = Colors.third,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    IconButton(onClick = onDeleteCategory) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Category",
                            tint = Colors.fourth,
                            modifier = Modifier.size(28.dp)
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
                    fontSize = 18.sp,
                    color = Colors.secondary
                )
            }
        }
    }
}
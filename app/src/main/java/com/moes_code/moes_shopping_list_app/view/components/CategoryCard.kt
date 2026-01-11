package com.moes_code.moes_shopping_list_app.view.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
    onEditCategory: () -> Unit,
    onEditItem: (ShoppingItem) -> Unit,
    onDeleteCategory: () -> Unit,
    onDeleteItem: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Colors.moe_blue,
                shape = RoundedCornerShape(16.dp)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Colors.moe_black
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
                    color = Colors.moe_white
                )
                Row {
                    IconButton(onClick = onAddItem) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Item",
                            tint = Colors.moe_yellow,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    IconButton(onClick = onEditCategory) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Category",
                            tint = Colors.moe_blue,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    IconButton(onClick = onDeleteCategory) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Category",
                            tint = Colors.moe_red,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Colors.moe_blue,
                thickness = 1.dp
            )

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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No items",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    color = Colors.moe_white,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}
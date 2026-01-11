package com.moes_code.moes_shopping_list_app.view.components.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moes_code.moes_shopping_list_app.model.Category
import com.moes_code.moes_shopping_list_app.view.theme.Colors

@Composable
fun EditCategoryDialog(
    category: Category,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var categoryName by remember { mutableStateOf(category.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Colors.moe_blue,
                shape = RoundedCornerShape(16.dp)
            ),
        title = { Text("Edit Category",
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        ) },
        text = {
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category Name",
                    fontSize = 18.sp,
                ) },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = Colors.moe_white
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Colors.moe_blue,
                    unfocusedBorderColor = Colors.moe_blue,
                    focusedLabelColor = Colors.moe_white,
                    unfocusedLabelColor = Colors.moe_white,
                    focusedTextColor = Colors.moe_white,
                    unfocusedTextColor = Colors.moe_white,
                    cursorColor = Colors.moe_white
                )
            )
        },

        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Cancel Button
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Colors.moe_red
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 2.dp,
                            color = Colors.moe_red,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Cancel",
                        tint = Colors.moe_red,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Add Button
                TextButton(
                    onClick = {
                        if (categoryName.isNotBlank()) {
                            onConfirm(categoryName)
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Colors.moe_yellow
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 2.dp,
                            color = Colors.moe_yellow,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Category",
                        tint = Colors.moe_yellow,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        dismissButton = {},
    )
}
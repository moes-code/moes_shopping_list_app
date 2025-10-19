package com.moes_code.moes_shopping_list_app.view.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.moes_code.moes_shopping_list_app.view.theme.Colors

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Category",
            fontSize = 28.sp
        ) },
        text = {
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category Name",
                    fontSize = 18.sp
                ) },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Colors.primary,
                    focusedLabelColor = Colors.primary,
                    cursorColor = Colors.primary
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (categoryName.isNotBlank()) {
                        onConfirm(categoryName)
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Colors.third
                )
            ) {
                Text("Add",
                    fontSize = 18.sp)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Colors.fourth
                )
                ) {
                Text("Cancel",
                    fontSize = 18.sp)
            }
        },
    )
}
package com.moes_code.moes_shopping_list_app.view.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moes_code.moes_shopping_list_app.view.theme.Colors

@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Item",
            fontSize = 28.sp) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name",
                        fontSize = 18.sp) },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 18.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Colors.moe_blue,
                        focusedLabelColor = Colors.moe_blue,
                        cursorColor = Colors.moe_blue
                    )
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity",
                        fontSize = 18.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 18.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Colors.moe_blue,
                        focusedLabelColor = Colors.moe_blue,
                        cursorColor = Colors.moe_blue
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (itemName.isNotBlank() && quantity.toIntOrNull()?.let { it > 0 } == true) {
                        onConfirm(itemName, quantity.toInt())
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Colors.moe_yellow
                )
            ) {
                Text("Add",
                    fontSize = 18.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Colors.moe_red
                )
                ) {
                Text("Cancel",
                    fontSize = 18.sp)
            }
        }
    )
}
package com.moes_code.moes_shopping_list_app.view.components.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.moes_code.moes_shopping_list_app.model.ShoppingItem

@Composable
fun EditItemDialog(
    item: ShoppingItem,
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var itemName by remember { mutableStateOf(item.name) }
    var quantity by remember { mutableStateOf(item.quantity.toString()) }

    val isQuantityValid = quantity.isNotEmpty() && quantity.toIntOrNull()?.let { it > 0 } == true
    val isFormValid = itemName.isNotBlank() && isQuantityValid
    val hasChanges = itemName != item.name || quantity != item.quantity.toString()
    val colorScheme = MaterialTheme.colorScheme

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = colorScheme.surfaceContainerHighest,
        titleContentColor = colorScheme.onSurface,
        textContentColor = colorScheme.onSurface,
        title = {
            Text(
                "Edit Item",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Item Name Field
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { 
                        Text(
                            "Item Name",
                            style = MaterialTheme.typography.bodyMedium
                        ) 
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    shape = MaterialTheme.shapes.small,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.outline,
                        focusedLabelColor = colorScheme.primary,
                        unfocusedLabelColor = colorScheme.onSurfaceVariant,
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,
                        cursorColor = colorScheme.primary,
                        focusedContainerColor = colorScheme.surfaceContainer,
                        unfocusedContainerColor = colorScheme.surfaceContainer
                    )
                )
                
                // Quantity Field
                Column {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { newValue ->
                            quantity = newValue.filter { it.isDigit() }
                        },
                        label = { 
                            Text(
                                "Quantity",
                                style = MaterialTheme.typography.bodyMedium
                            ) 
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = !isQuantityValid && quantity.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        shape = MaterialTheme.shapes.small,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isQuantityValid) colorScheme.primary else colorScheme.error,
                            unfocusedBorderColor = if (isQuantityValid || quantity.isEmpty()) colorScheme.outline else colorScheme.error,
                            focusedLabelColor = if (isQuantityValid) colorScheme.primary else colorScheme.error,
                            unfocusedLabelColor = colorScheme.onSurfaceVariant,
                            focusedTextColor = colorScheme.onSurface,
                            unfocusedTextColor = colorScheme.onSurface,
                            cursorColor = colorScheme.primary,
                            errorBorderColor = colorScheme.error,
                            errorLabelColor = colorScheme.error,
                            focusedContainerColor = colorScheme.surfaceContainer,
                            unfocusedContainerColor = colorScheme.surfaceContainer,
                            errorContainerColor = colorScheme.surfaceContainer
                        )
                    )
                    
                    // Error message
                    AnimatedVisibility(
                        visible = !isQuantityValid && quantity.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = "Quantity must be greater than 0",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.error,
                            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Cancel Button
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancel")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Save Button
                FilledTonalButton(
                    onClick = {
                        if (isFormValid) {
                            onConfirm(itemName, quantity.toInt())
                        }
                    },
                    enabled = isFormValid && hasChanges,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary,
                        disabledContainerColor = colorScheme.primary.copy(alpha = 0.3f),
                        disabledContentColor = colorScheme.onPrimary.copy(alpha = 0.5f)
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save")
                }
            }
        }
    )
}

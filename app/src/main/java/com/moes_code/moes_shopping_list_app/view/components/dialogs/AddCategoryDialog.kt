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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.unit.dp

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    val isFormValid = categoryName.isNotBlank()
    val colorScheme = MaterialTheme.colorScheme

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = colorScheme.surfaceContainerHighest,
        titleContentColor = colorScheme.onSurface,
        textContentColor = colorScheme.onSurface,
        title = {
            Text(
                "Add Category",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { 
                        Text(
                            "Category Name",
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
                
                // Helper text
                AnimatedVisibility(
                    visible = categoryName.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = "Enter a name for your category",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
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
                
                // Add Button
                FilledTonalButton(
                    onClick = {
                        if (isFormValid) {
                            onConfirm(categoryName)
                        }
                    },
                    enabled = isFormValid,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = colorScheme.secondary,
                        contentColor = colorScheme.onSecondary,
                        disabledContainerColor = colorScheme.secondary.copy(alpha = 0.3f),
                        disabledContentColor = colorScheme.onSecondary.copy(alpha = 0.5f)
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add")
                }
            }
        }
    )
}

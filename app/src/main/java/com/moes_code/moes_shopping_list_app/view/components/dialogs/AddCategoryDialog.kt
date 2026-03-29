package com.moes_code.moes_shopping_list_app.view.components.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moes_code.moes_shopping_list_app.R
import com.moes_code.moes_shopping_list_app.view.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    val isFormValid = categoryName.isNotBlank()
    val colorScheme = MaterialTheme.colorScheme

    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = colorScheme.background,
            contentColor = colorScheme.onSurface,
            border = BorderStroke(
                width = Dimensions.dialogBorderWidth,
                color = colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier.padding(Dimensions.dialogPadding)
            ) {
                // Title
                Text(
                    text = stringResource(R.string.dialog_title_add_category),
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                
                // Content
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { 
                        Text(
                            stringResource(R.string.label_category_name),
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
                        focusedContainerColor = colorScheme.background,
                        unfocusedContainerColor = colorScheme.background
                    )
                )
                
                // Helper text
                AnimatedVisibility(
                    visible = categoryName.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = stringResource(R.string.helper_enter_category_name),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.padding(vertical = 12.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // Cancel Button
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = colorScheme.onSurface
                        )
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.dialogButtonSpacing))
                        Text(stringResource(R.string.button_cancel))
                    }
                    
                    Spacer(modifier = Modifier.width(Dimensions.dialogButtonSpacing))
                    
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
                        Spacer(modifier = Modifier.width(Dimensions.dialogButtonSpacing))
                        Text(stringResource(R.string.button_add))
                    }
                }
            }
        }
    }
}

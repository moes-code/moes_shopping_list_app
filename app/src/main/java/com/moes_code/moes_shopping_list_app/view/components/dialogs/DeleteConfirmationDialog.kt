package com.moes_code.moes_shopping_list_app.view.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DeleteConfirmationDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = colorScheme.surfaceContainerHighest,
        titleContentColor = colorScheme.onSurface,
        textContentColor = colorScheme.onSurfaceVariant,
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                message,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Cancel Button (Keep/Safe action)
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colorScheme.tertiary
                    )
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Keep")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Delete Button (Destructive action)
                FilledTonalButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = colorScheme.error,
                        contentColor = colorScheme.onError
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete")
                }
            }
        }
    )
}

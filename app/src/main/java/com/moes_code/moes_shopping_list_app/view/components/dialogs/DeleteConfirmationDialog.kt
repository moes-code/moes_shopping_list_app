package com.moes_code.moes_shopping_list_app.view.components.dialogs

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moes_code.moes_shopping_list_app.R
import com.moes_code.moes_shopping_list_app.view.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmationDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
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
                modifier = Modifier.padding(Dimensions.dialogPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
                
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                
                // Message
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.padding(vertical = 12.dp))
                
                // Buttons
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
                        Spacer(modifier = Modifier.width(Dimensions.dialogButtonSpacing))
                        Text(stringResource(R.string.button_keep))
                    }
                    
                    Spacer(modifier = Modifier.width(Dimensions.dialogButtonSpacing))
                    
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
                        Spacer(modifier = Modifier.width(Dimensions.dialogButtonSpacing))
                        Text(stringResource(R.string.button_delete))
                    }
                }
            }
        }
    }
}

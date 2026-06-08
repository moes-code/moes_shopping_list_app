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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moes_code.moes_shopping_list_app.R
import com.moes_code.moes_shopping_list_app.view.theme.Colors
import com.moes_code.moes_shopping_list_app.view.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeTutorialDialog(
    onDismiss: () -> Unit
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
                // Title
                Text(
                    text = stringResource(R.string.tutorial_swipe_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.padding(vertical = 12.dp))

                // Edit gesture row
                GestureRow(
                    icon = Icons.Default.Edit,
                    label = stringResource(R.string.tutorial_swipe_edit),
                    arrow = Icons.AutoMirrored.Filled.ArrowForward,
                    backgroundColor = Colors.swipeEditBackground,
                    iconTint = colorScheme.primary
                )

                Spacer(modifier = Modifier.padding(vertical = 8.dp))

                // Delete gesture row
                GestureRow(
                    icon = Icons.Default.Delete,
                    label = stringResource(R.string.tutorial_swipe_delete),
                    arrow = Icons.AutoMirrored.Filled.ArrowBack,
                    backgroundColor = Colors.swipeDeleteBackground,
                    iconTint = colorScheme.error
                )

                Spacer(modifier = Modifier.padding(vertical = 12.dp))

                // Got it button
                FilledTonalButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = colorScheme.secondary,
                        contentColor = colorScheme.onSecondary
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        stringResource(R.string.tutorial_swipe_got_it),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun GestureRow(
    icon: ImageVector,
    label: String,
    arrow: ImageVector,
    backgroundColor: Color,
    iconTint: Color
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        contentColor = iconTint
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = iconTint
                )
            }
            Icon(
                imageVector = arrow,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

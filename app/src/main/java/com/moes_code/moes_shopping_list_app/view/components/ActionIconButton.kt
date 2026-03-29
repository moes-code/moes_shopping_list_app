package com.moes_code.moes_shopping_list_app.view.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.moes_code.moes_shopping_list_app.view.theme.Dimensions

/**
 * Reusable action icon button with consistent styling.
 * Used for Add, Edit, Delete actions throughout the app.
 *
 * @param onClick Callback when button is clicked
 * @param icon The icon to display
 * @param contentDescription Accessibility description
 * @param containerColor Background color of the button
 * @param contentColor Color of the icon
 * @param modifier Modifier to be applied to the button
 * @param buttonSize Size of the button (default: itemButtonSize)
 * @param iconSize Size of the icon (default: itemIconSize)
 */
@Composable
fun ActionIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    buttonSize: Dp = Dimensions.itemButtonSize,
    iconSize: Dp = Dimensions.itemIconSize
) {
    FilledTonalIconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier.size(buttonSize)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize)
        )
    }
}

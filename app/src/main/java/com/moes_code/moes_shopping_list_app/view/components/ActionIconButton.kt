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

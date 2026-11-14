package com.example.ds.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ContainedContainerColor = Color(0xFF0061A4)
private val ContainedContentColor = Color(0xFFFFFFFF)
private val DisabledContainerColor = Color(0xFFE0E2E5)
private val DisabledContentColor = Color(0xFF8E9196)
private val ButtonCornerRadius = 8.dp
private val ButtonMinHeight = 48.dp
private val ButtonHorizontalPadding = 16.dp
private val ButtonVerticalPadding = 12.dp

/**
 * Simple contained button for triggering primary actions.
 *
 * Uses hardcoded visual values until `DsTheme` and token bindings are implemented.
 *
 * @param text Label text displayed inside the button.
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier Modifier applied to the button container.
 * @param enabled Controls whether the button is interactive.
 */
@Composable
fun DsButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val containerColor = if (enabled) ContainedContainerColor else DisabledContainerColor
    val contentColor = if (enabled) ContainedContentColor else DisabledContentColor
    val shape = RoundedCornerShape(ButtonCornerRadius)
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = ButtonMinHeight)
            .clip(shape)
            .background(color = containerColor, shape = shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .semantics { role = Role.Button }
            .padding(horizontal = ButtonHorizontalPadding, vertical = ButtonVerticalPadding),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicText(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    color = contentColor,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DsButtonPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        DsButton(text = "Primary Action", onClick = {})
        DsButton(text = "Disabled", onClick = {}, enabled = false)
    }
}

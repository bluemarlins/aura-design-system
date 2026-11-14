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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ds.foundation.theme.AuraColorMode
import com.example.ds.foundation.theme.AuraTheme

private val ButtonCornerRadius = 8.dp
private val ButtonMinHeight = 48.dp
private val ButtonHorizontalPadding = 16.dp
private val ButtonVerticalPadding = 12.dp

/**
 * Simple contained button for triggering primary actions.
 *
 * Colors bind to [AuraTheme.colors]; dimension and typography tokens will be
 * migrated once those foundation specs are implemented.
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
    val colors = AuraTheme.colors
    val containerColor = if (enabled) colors.brand.primary else colors.interaction.disabledContainer
    val contentColor = if (enabled) colors.brand.onPrimary else colors.content.disabled
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

@Preview(name = "Light", showBackground = true)
@Composable
private fun DsButtonLightPreview() {
    AuraTheme(colorMode = AuraColorMode.Light) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DsButton(text = "Primary Action", onClick = {})
            DsButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}

@Preview(name = "Dark", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DsButtonDarkPreview() {
    AuraTheme(colorMode = AuraColorMode.Dark) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DsButton(text = "Primary Action", onClick = {})
            DsButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}

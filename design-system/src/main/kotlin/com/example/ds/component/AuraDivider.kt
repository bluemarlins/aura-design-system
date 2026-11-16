package com.example.ds.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ds.foundation.theme.AuraColorMode
import com.example.ds.foundation.theme.AuraTheme

/**
 * Axis orientation for [AuraDivider].
 */
enum class AuraDividerOrientation {
    Horizontal,
    Vertical,
}

/**
 * Semantic contrast strength for [AuraDivider].
 */
enum class AuraDividerTone {
    Subtle,
    Default,
    Strong,
}

/**
 * Hairline separator implementing the `aura.component.divider` specification.
 *
 * Uses `AuraTheme.colors.border` tones without Material 3 dependency.
 */
@Composable
fun AuraDivider(
    modifier: Modifier = Modifier,
    orientation: AuraDividerOrientation = AuraDividerOrientation.Horizontal,
    tone: AuraDividerTone = AuraDividerTone.Subtle,
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp,
    endIndent: Dp = 0.dp,
) {
    val colors = AuraTheme.colors
    val motion = AuraTheme.motion
    val targetColor: Color = when (tone) {
        AuraDividerTone.Subtle -> colors.border.subtle
        AuraDividerTone.Default -> colors.border.default
        AuraDividerTone.Strong -> colors.border.strong
    }
    val dividerColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = if (motion.reduceMotion) snap() else tween(durationMillis = motion.stateChangeDurationMillis),
        label = "AuraDividerColor",
    )

    val layoutModifier = when (orientation) {
        AuraDividerOrientation.Horizontal -> modifier
            .fillMaxWidth()
            .padding(start = startIndent, end = endIndent)
            .height(thickness)
        AuraDividerOrientation.Vertical -> modifier
            .fillMaxHeight()
            .padding(top = startIndent, bottom = endIndent)
            .width(thickness)
    }

    Box(
        modifier = layoutModifier.background(dividerColor),
    )
}

@Preview(showBackground = true)
@Composable
private fun AuraDividerPreview() {
    AuraTheme(colorMode = AuraColorMode.Light) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AuraTheme.colors.background.primary)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AuraText("Subtle", style = AuraTheme.typography.labelMedium)
            AuraDivider(tone = AuraDividerTone.Subtle)
            AuraText("Default (Indented)", style = AuraTheme.typography.labelMedium)
            AuraDivider(tone = AuraDividerTone.Default, startIndent = 16.dp, endIndent = 16.dp)
            AuraText("Strong", style = AuraTheme.typography.labelMedium)
            AuraDivider(tone = AuraDividerTone.Strong)
            Row(
                modifier = Modifier.height(24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AuraText("Left", style = AuraTheme.typography.bodySmall)
                AuraDivider(orientation = AuraDividerOrientation.Vertical, tone = AuraDividerTone.Default)
                AuraText("Right", style = AuraTheme.typography.bodySmall)
            }
        }
    }
}

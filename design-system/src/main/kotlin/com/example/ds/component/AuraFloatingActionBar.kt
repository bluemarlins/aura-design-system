package com.example.ds.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ds.foundation.motion.AuraMotionProgressKey
import com.example.ds.foundation.theme.AuraColorMode
import com.example.ds.foundation.theme.AuraMaterial
import com.example.ds.foundation.theme.AuraTheme

/**
 * Floating glass action dock (Layer 2 `chrome`) for task screens — editors, wizards, and detail
 * screens whose primary action must stay reachable while the form scrolls underneath.
 *
 * It is the task-screen counterpart of [AuraBottomIsland]: same floating margin, same glass
 * recipe, but it carries one primary action (plus an optional secondary) instead of navigation.
 * Place it in the `bottomBar` slot of [FloatingGlassScaffold] with
 * `bottomIslandHeight = AuraFloatingActionBarDefaults.Height` so the last content clears it.
 *
 * The dock rides above the soft keyboard (`imePadding`) so "Save" stays one tap away while typing.
 * When [visible] turns `false` it slides below the screen edge on the floating-bar motion tokens.
 */
@Composable
fun AuraFloatingActionBar(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    material: AuraMaterial = AuraMaterial.Glass,
    supportingText: String? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = AuraTheme.colors
    val motion = AuraTheme.motion
    val reducedMotion = rememberAuraReducedMotion()

    val shown by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = when {
            reducedMotion -> snap()
            visible -> motion.floatingBarAppearSpec()
            else -> motion.floatingBarDisappearSpec()
        },
        label = "AuraFloatingActionBarVisibility",
    )
    if (shown <= 0.001f && !visible) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .padding(
                horizontal = AuraFloatingActionBarDefaults.HorizontalMargin,
                vertical = AuraFloatingActionBarDefaults.BottomMargin,
            )
            .graphicsLayer {
                translationY = (1f - shown) * (AuraFloatingActionBarDefaults.Height + 32.dp).toPx()
                alpha = shown
            }
            .semantics {
                isTraversalGroup = true
                this[AuraMotionProgressKey] = shown
            },
        contentAlignment = Alignment.BottomCenter,
    ) {
        GlassSurface(
            modifier = Modifier
                .widthIn(max = AuraFloatingActionBarDefaults.MaxWidth)
                .fillMaxWidth()
                .testTag(AuraFloatingActionBarTestTag),
            material = material,
            shape = RoundedCornerShape(AuraFloatingActionBarDefaults.CornerRadius),
            cornerRadiusHint = AuraFloatingActionBarDefaults.CornerRadius,
            elevation = AuraTheme.elevation.level2,
        ) {
            Column(
                modifier = Modifier.padding(AuraFloatingActionBarDefaults.InnerPadding),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                if (!supportingText.isNullOrBlank()) {
                    AuraText(
                        text = supportingText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        style = AuraTheme.typography.bodySmall,
                        color = colors.content.secondary,
                        textAlign = TextAlign.Center,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    content = content,
                )
            }
        }
    }
}

object AuraFloatingActionBarDefaults {
    /** Dock height with a single `Large` action row; pass to `FloatingGlassScaffold(bottomIslandHeight = ...)`. */
    val Height: Dp = 72.dp

    /** Extra height to add to [Height] when `supportingText` is shown. */
    val SupportingTextHeight: Dp = 24.dp
    val HorizontalMargin: Dp = 16.dp
    val BottomMargin: Dp = 12.dp
    val InnerPadding: Dp = 8.dp
    val CornerRadius: Dp = 28.dp
    val MaxWidth: Dp = 560.dp
}

const val AuraFloatingActionBarTestTag = "aura_floating_action_bar"

@Preview(name = "Floating Action Bar", showBackground = true)
@Composable
private fun AuraFloatingActionBarPreview() {
    AuraTheme(colorMode = AuraColorMode.Light) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AuraTheme.colors.background.primary),
        ) {
            AuraFloatingActionBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                supportingText = "Add a name and a rating to save",
            ) {
                AuraButton(
                    text = "Cancel",
                    onClick = {},
                    variant = AuraButtonVariant.Ghost,
                    size = AuraButtonSize.Large,
                )
                AuraButton(
                    text = "Save",
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    size = AuraButtonSize.Large,
                    fullWidth = true,
                )
            }
        }
    }
}

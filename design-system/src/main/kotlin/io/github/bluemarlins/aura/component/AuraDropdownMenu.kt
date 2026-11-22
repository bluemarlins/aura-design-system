package io.github.bluemarlins.aura.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import io.github.bluemarlins.aura.foundation.motion.AuraMotionProfile
import io.github.bluemarlins.aura.foundation.motion.AuraMotionProgressKey
import io.github.bluemarlins.aura.foundation.theme.AuraTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * One choice in an [AuraDropdownMenu].
 *
 * [selected] draws a check mark and exposes the selected state to accessibility services, so a
 * section of mutually exclusive items reads as a single-choice group (e.g. a filter or sort order).
 */
data class AuraMenuItem(
    val id: String,
    val label: String,
    val selected: Boolean = false,
    val enabled: Boolean = true,
)

/**
 * A titled group of [AuraMenuItem]s. Sections are separated by a divider; [title] is optional.
 */
data class AuraMenuSection(
    val title: String? = null,
    val items: List<AuraMenuItem>,
)

private val MenuCornerRadius = 20.dp
private val MenuMinWidth = 200.dp
private val MenuMaxWidth = 280.dp
private val MenuAnchorGap = 6.dp
private val MenuWindowMargin = 12.dp

/**
 * Anchored overflow / filter menu implementing the `aura.component.dropdown-menu` specification.
 *
 * Place it in the same parent as its trigger (for example inside a `Box` with an [AuraIconButton]).
 * The menu opens below the trigger, aligned to its end edge, and flips above it when there is not
 * enough room. Tapping outside, pressing Back, or choosing an item (when [dismissOnItemClick]) calls
 * [onDismissRequest].
 */
@Composable
fun AuraDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    sections: List<AuraMenuSection>,
    onItemClick: (AuraMenuItem) -> Unit,
    modifier: Modifier = Modifier,
    dismissOnItemClick: Boolean = true,
) {
    val colors = AuraTheme.colors
    val motion = AuraTheme.motion.forTrack(AuraMotionProfile.Expressive)
    val reducedMotion = rememberAuraReducedMotion()
    val density = LocalDensity.current

    // Stays composed through the Hide animation (MOT-RULE-010).
    var mounted by remember { mutableStateOf(expanded) }
    val scale = remember { Animatable(if (reducedMotion) 1f else motion.popoverEnterScale) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(expanded, reducedMotion) {
        if (expanded) {
            mounted = true
            if (alpha.value == 0f) {
                scale.snapTo(if (reducedMotion) 1f else motion.popoverEnterScale)
            }
            coroutineScope {
                if (reducedMotion) {
                    launch { scale.snapTo(1f) }
                    launch { alpha.animateTo(1f, motion.reducedFadeSpec()) }
                } else {
                    launch { scale.animateTo(1f, motion.spatialSpring()) }
                    launch { alpha.animateTo(1f, motion.enterSpec()) }
                }
            }
        } else if (mounted) {
            coroutineScope {
                if (reducedMotion) {
                    launch { alpha.animateTo(0f, motion.reducedFadeSpec()) }
                } else {
                    launch { scale.animateTo(motion.popoverEnterScale, motion.exitSpec()) }
                    launch { alpha.animateTo(0f, motion.exitSpec()) }
                }
            }
            mounted = false
        }
    }

    if (!mounted) return

    val positionProvider = remember(density) {
        DropdownMenuPositionProvider(
            gapPx = with(density) { MenuAnchorGap.roundToPx() },
            marginPx = with(density) { MenuWindowMargin.roundToPx() },
        )
    }
    val shape = RoundedCornerShape(MenuCornerRadius)

    Popup(
        popupPositionProvider = positionProvider,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = true),
    ) {
        Column(
            modifier = modifier
                .semantics { this[AuraMotionProgressKey] = alpha.value }
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    this.alpha = alpha.value
                    transformOrigin = TransformOrigin(1f, 0f)
                }
                .widthIn(min = MenuMinWidth, max = MenuMaxWidth)
                .shadow(elevation = AuraTheme.elevation.level3.elevation, shape = shape)
                .clip(shape)
                .background(colors.surface.floating, shape)
                .border(width = 1.dp, color = colors.border.subtle, shape = shape)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
        ) {
            sections.forEachIndexed { index, section ->
                if (index > 0) {
                    AuraDivider(modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp))
                }
                MenuSection(
                    section = section,
                    onItemClick = { item ->
                        onItemClick(item)
                        if (dismissOnItemClick) onDismissRequest()
                    },
                )
            }
        }
    }
}

@Composable
private fun MenuSection(
    section: AuraMenuSection,
    onItemClick: (AuraMenuItem) -> Unit,
) {
    val colors = AuraTheme.colors
    val typography = AuraTheme.typography
    section.title?.let { title ->
        AuraText(
            text = title,
            style = typography.labelMedium,
            color = colors.content.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 2.dp)
                .semantics { heading() },
        )
    }
    section.items.forEach { item ->
        MenuItemRow(item = item, onClick = { onItemClick(item) })
    }
}

@Composable
private fun MenuItemRow(
    item: AuraMenuItem,
    onClick: () -> Unit,
) {
    val colors = AuraTheme.colors
    val typography = AuraTheme.typography
    val interaction = remember(item.id) { MutableInteractionSource() }
    val contentColor = when {
        !item.enabled -> colors.content.disabled
        item.selected -> colors.brand.primary
        else -> colors.content.primary
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(horizontal = 8.dp)
            .clip(AuraTheme.shapes.medium)
            .background(if (item.selected) colors.interaction.selectedContainer else Color.Transparent)
            .fluidPressScale(interaction)
            .clickable(
                interactionSource = interaction,
                indication = null,
                enabled = item.enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .semantics { selected = item.selected }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AuraText(
            text = item.label,
            style = typography.bodyLarge,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        if (item.selected) {
            MenuCheckGlyph(color = contentColor, size = 18.dp)
        } else {
            Spacer(modifier = Modifier.width(18.dp))
        }
    }
}

@Composable
private fun MenuCheckGlyph(color: Color, size: Dp) {
    Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            val path = Path().apply {
                moveTo(w * 0.18f, h * 0.52f)
                lineTo(w * 0.42f, h * 0.76f)
                lineTo(w * 0.84f, h * 0.28f)
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
        }
    }
}

/**
 * Opens below the anchor aligned to its end edge, flips above when the window has no room below,
 * and keeps the menu [marginPx] inside the window horizontally.
 */
internal class DropdownMenuPositionProvider(
    private val gapPx: Int,
    private val marginPx: Int,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset = dropdownMenuOffset(anchorBounds, windowSize, layoutDirection, popupContentSize, gapPx, marginPx)
}

internal fun dropdownMenuOffset(
    anchorBounds: IntRect,
    windowSize: IntSize,
    layoutDirection: LayoutDirection,
    popupContentSize: IntSize,
    gapPx: Int,
    marginPx: Int,
): IntOffset {
    val preferredX = if (layoutDirection == LayoutDirection.Ltr) {
        anchorBounds.right - popupContentSize.width
    } else {
        anchorBounds.left
    }
    val maxX = (windowSize.width - popupContentSize.width - marginPx).coerceAtLeast(marginPx)
    val x = preferredX.coerceIn(marginPx, maxX)

    val below = anchorBounds.bottom + gapPx
    val above = anchorBounds.top - gapPx - popupContentSize.height
    val fitsBelow = below + popupContentSize.height <= windowSize.height - marginPx
    val y = when {
        fitsBelow -> below
        above >= marginPx -> above
        else -> (windowSize.height - popupContentSize.height - marginPx).coerceAtLeast(marginPx)
    }
    return IntOffset(x, y)
}

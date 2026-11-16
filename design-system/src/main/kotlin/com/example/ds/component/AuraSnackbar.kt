package com.example.ds.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ds.foundation.color.AuraColors
import com.example.ds.foundation.theme.AuraColorMode
import com.example.ds.foundation.theme.AuraMaterial
import com.example.ds.foundation.theme.AuraTheme
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

/**
 * Semantic tone variants for [AuraSnackbar].
 */
enum class AuraSnackbarVariant {
    Neutral,
    Success,
    Warning,
    Error,
}

/**
 * Display duration presets for [AuraSnackbarHostState.showSnackbar].
 */
enum class AuraSnackbarDuration(val durationMillis: Long) {
    Short(3_000L),
    Long(5_000L),
    Indefinite(kotlin.Long.MAX_VALUE),
}

/**
 * Outcome returned when an [AuraSnackbar] finishes displaying.
 */
enum class AuraSnackbarResult {
    Dismissed,
    ActionPerformed,
}

/**
 * Immutable payload describing a queued [AuraSnackbar].
 */
@Immutable
data class AuraSnackbarVisuals(
    val message: String,
    val actionLabel: String? = null,
    val duration: AuraSnackbarDuration = AuraSnackbarDuration.Short,
    val variant: AuraSnackbarVariant = AuraSnackbarVariant.Neutral,
)

/**
 * Active snackbar handle passed to [AuraSnackbarHost]'s slot composable.
 */
@Stable
interface AuraSnackbarData {
    val visuals: AuraSnackbarVisuals
    fun performAction()
    fun dismiss()
}

/**
 * Queueing state holder for [AuraSnackbarHost].
 */
@Stable
class AuraSnackbarHostState {
    private val mutex = Mutex()

    var currentSnackbarData: AuraSnackbarData? by mutableStateOf(null)
        private set

    suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: AuraSnackbarDuration = AuraSnackbarDuration.Short,
        variant: AuraSnackbarVariant = AuraSnackbarVariant.Neutral,
    ): AuraSnackbarResult = showSnackbar(
        AuraSnackbarVisuals(
            message = message,
            actionLabel = actionLabel,
            duration = duration,
            variant = variant,
        ),
    )

    suspend fun showSnackbar(visuals: AuraSnackbarVisuals): AuraSnackbarResult = mutex.withLock {
        try {
            suspendCancellableCoroutine { continuation ->
                currentSnackbarData = AuraSnackbarDataImpl(visuals, continuation)
            }
        } finally {
            currentSnackbarData = null
        }
    }

    fun dismissCurrent() {
        currentSnackbarData?.dismiss()
    }

    private class AuraSnackbarDataImpl(
        override val visuals: AuraSnackbarVisuals,
        private val continuation: CancellableContinuation<AuraSnackbarResult>,
    ) : AuraSnackbarData {
        override fun performAction() {
            if (continuation.isActive) {
                continuation.resume(AuraSnackbarResult.ActionPerformed)
            }
        }

        override fun dismiss() {
            if (continuation.isActive) {
                continuation.resume(AuraSnackbarResult.Dismissed)
            }
        }
    }
}

/**
 * Remembers an [AuraSnackbarHostState] across recompositions.
 */
@Composable
fun rememberAuraSnackbarHostState(): AuraSnackbarHostState = remember { AuraSnackbarHostState() }

/**
 * Animated host container for [AuraSnackbarHostState] implementing `aura.component.snackbar`.
 */
@Composable
fun AuraSnackbarHost(
    hostState: AuraSnackbarHostState,
    modifier: Modifier = Modifier,
    material: AuraMaterial = AuraMaterial.Solid,
    snackbar: @Composable (AuraSnackbarData) -> Unit = { data ->
        AuraSnackbar(
            message = data.visuals.message,
            actionLabel = data.visuals.actionLabel,
            onActionClick = { data.performAction() },
            variant = data.visuals.variant,
            material = material,
        )
    },
) {
    val currentData = hostState.currentSnackbarData
    val motion = AuraTheme.motion
    val layout = AuraTheme.layout

    LaunchedEffect(currentData) {
        if (currentData != null && currentData.visuals.duration != AuraSnackbarDuration.Indefinite) {
            delay(currentData.visuals.duration.durationMillis)
            currentData.dismiss()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = layout.screenMarginHorizontal, vertical = 12.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AnimatedContent(
            targetState = currentData,
            transitionSpec = {
                val enter = if (motion.reduceMotion) {
                    fadeIn(animationSpec = snap())
                } else {
                    fadeIn(animationSpec = motion.enterSpec()) +
                        slideInVertically(animationSpec = motion.spatialSpring()) { it / 2 }
                }
                val exit = if (motion.reduceMotion) {
                    fadeOut(animationSpec = snap())
                } else {
                    fadeOut(animationSpec = motion.exitSpec()) +
                        slideOutVertically(animationSpec = motion.exitSpec()) { it / 2 }
                }
                enter togetherWith exit
            },
            label = "AuraSnackbarHost",
        ) { data ->
            if (data != null) {
                snackbar(data)
            }
        }
    }
}

private data class AuraSnackbarPalette(
    val container: Color,
    val content: Color,
    val border: Color,
    val actionContent: Color,
)

private fun resolveAuraSnackbarPalette(
    variant: AuraSnackbarVariant,
    colors: AuraColors,
): AuraSnackbarPalette = when (variant) {
    AuraSnackbarVariant.Neutral -> AuraSnackbarPalette(
        container = colors.surface.inverse,
        content = colors.content.inverse,
        border = colors.border.subtle,
        actionContent = colors.content.inverse,
    )
    AuraSnackbarVariant.Success -> AuraSnackbarPalette(
        container = colors.status.success.solid,
        content = colors.status.success.onSolid,
        border = colors.status.success.solid,
        actionContent = colors.status.success.onSolid,
    )
    AuraSnackbarVariant.Warning -> AuraSnackbarPalette(
        container = colors.status.warning.solid,
        content = colors.status.warning.onSolid,
        border = colors.status.warning.solid,
        actionContent = colors.status.warning.onSolid,
    )
    AuraSnackbarVariant.Error -> AuraSnackbarPalette(
        container = colors.status.error.solid,
        content = colors.status.error.onSolid,
        border = colors.status.error.solid,
        actionContent = colors.status.error.onSolid,
    )
}

/**
 * Transient feedback banner implementing the `aura.component.snackbar` specification.
 */
@Composable
fun AuraSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    variant: AuraSnackbarVariant = AuraSnackbarVariant.Neutral,
    material: AuraMaterial = AuraMaterial.Solid,
    shape: Shape = AuraTheme.shapes.medium,
) {
    val colors = AuraTheme.colors
    val typography = AuraTheme.typography
    val elevation = AuraTheme.elevation.level3
    val effectiveMaterial = AuraTheme.effectiveMaterial(material)
    val palette = resolveAuraSnackbarPalette(variant = variant, colors = colors)

    val cardModifier = modifier
        .widthIn(max = 560.dp)
        .fillMaxWidth()
        .defaultMinSize(minHeight = 48.dp)
        .semantics {
            liveRegion = LiveRegionMode.Polite
        }

    val snackbarContent: @Composable (Color, Color) -> Unit = { textColor, actionColor ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = if (actionLabel != null && onActionClick != null) 8.dp else 16.dp,
                    top = 10.dp,
                    bottom = 10.dp,
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AuraText(
                text = message,
                style = typography.bodyMedium,
                color = textColor,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            if (!actionLabel.isNullOrBlank() && onActionClick != null) {
                AuraSnackbarAction(
                    label = actionLabel,
                    contentColor = actionColor,
                    onClick = onActionClick,
                )
            }
        }
    }

    if (effectiveMaterial != AuraMaterial.Solid) {
        AuraSnackbarGlassContainer(
            modifier = cardModifier,
            shape = shape,
        ) {
            snackbarContent(colors.content.primary, colors.brand.primary)
        }
    } else {
        Box(
            modifier = cardModifier
                .shadow(
                    elevation = elevation.elevation,
                    shape = shape,
                    clip = false,
                )
                .clip(shape)
                .background(palette.container)
                .border(width = 1.dp, color = palette.border, shape = shape),
        ) {
            snackbarContent(palette.content, palette.actionContent)
        }
    }
}

@Composable
private fun AuraSnackbarAction(
    label: String,
    contentColor: Color,
    onClick: () -> Unit,
) {
    val motion = AuraTheme.motion
    val typography = AuraTheme.typography
    val shapes = AuraTheme.shapes
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && !motion.reduceMotion) motion.pressedScaleGhost else 1f,
        animationSpec = if (motion.reduceMotion) snap() else if (isPressed) motion.pressInSpring() else motion.pressOutSpring(),
        label = "AuraSnackbarActionScale",
    )

    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(shapes.small)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        AuraText(
            text = label,
            style = typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Suppress("DEPRECATION")
@Composable
private fun AuraSnackbarGlassContainer(
    modifier: Modifier,
    shape: Shape,
    content: @Composable () -> Unit,
) {
    GlassSurface(
        modifier = modifier,
        glassStyle = GlassStyle.FROSTED,
        shape = shape,
        cornerRadiusHint = 12.dp,
        showShadow = true,
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun AuraSnackbarPreview() {
    AuraTheme(colorMode = AuraColorMode.Light) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AuraTheme.colors.background.primary)
                .padding(16.dp),
        ) {
            AuraSnackbar(
                message = "Tasting note saved to your cellar.",
                actionLabel = "Undo",
                onActionClick = {},
            )
        }
    }
}

package com.example.ds.component

import android.os.SystemClock
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ds.foundation.color.AuraColors
import com.example.ds.foundation.theme.AuraColorMode
import com.example.ds.foundation.theme.AuraTheme

private val MinTouchTargetWidth = 48.dp

/**
 * Visual hierarchy variants for [AuraButton].
 */
enum class AuraButtonVariant {
    /** Single highest-emphasis CTA per screen or modal context. */
    Primary,

    /** Tonal accent button for complementary actions or inside cards. */
    Secondary,

    /** Bordered neutral button for cancel or alternative actions. */
    Outline,

    /** Borderless low-emphasis action for toolbars and inline headers. */
    Ghost,

    /** High-alert confirmation button reserved for irreversible destructive actions. */
    Destructive,
}

/**
 * Visual size scale for [AuraButton].
 *
 * Physical touch targets always enforce a minimum of 48dp even for [Small].
 */
enum class AuraButtonSize {
    Small,
    Medium,
    Large,
}

private data class AuraButtonSizeSpec(
    val visualHeight: Dp,
    val minTouchHeight: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val cornerRadius: Dp,
    val fontSize: TextUnit,
    val lineHeight: TextUnit,
    val iconSize: Dp,
    val iconGap: Dp,
    val spinnerSize: Dp,
    val spinnerStrokeWidth: Dp,
)

private fun AuraButtonSize.toSpec(): AuraButtonSizeSpec = when (this) {
    AuraButtonSize.Small -> AuraButtonSizeSpec(
        visualHeight = 36.dp,
        minTouchHeight = 48.dp,
        horizontalPadding = 12.dp,
        verticalPadding = 6.dp,
        cornerRadius = 8.dp,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        iconSize = 16.dp,
        iconGap = 6.dp,
        spinnerSize = 16.dp,
        spinnerStrokeWidth = 2.dp,
    )
    AuraButtonSize.Medium -> AuraButtonSizeSpec(
        visualHeight = 48.dp,
        minTouchHeight = 48.dp,
        horizontalPadding = 16.dp,
        verticalPadding = 10.dp,
        cornerRadius = 10.dp,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        iconSize = 18.dp,
        iconGap = 8.dp,
        spinnerSize = 18.dp,
        spinnerStrokeWidth = 2.dp,
    )
    AuraButtonSize.Large -> AuraButtonSizeSpec(
        visualHeight = 56.dp,
        minTouchHeight = 56.dp,
        horizontalPadding = 20.dp,
        verticalPadding = 12.dp,
        cornerRadius = 12.dp,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        iconSize = 20.dp,
        iconGap = 8.dp,
        spinnerSize = 20.dp,
        spinnerStrokeWidth = 2.5.dp,
    )
}

private data class AuraButtonResolvedColors(
    val container: Color,
    val content: Color,
    val border: Color?,
)

private fun resolveAuraButtonColors(
    variant: AuraButtonVariant,
    enabled: Boolean,
    isPressed: Boolean,
    loading: Boolean,
    colors: AuraColors,
): AuraButtonResolvedColors {
    // Precedence: Loading > Disabled > Pressed > Rest
    if (!enabled && !loading) {
        val container = when (variant) {
            AuraButtonVariant.Outline,
            AuraButtonVariant.Ghost,
            -> Color.Transparent

            AuraButtonVariant.Primary,
            AuraButtonVariant.Secondary,
            AuraButtonVariant.Destructive,
            -> colors.interaction.disabledContainer
        }
        val border = if (variant == AuraButtonVariant.Outline) colors.border.subtle else null
        return AuraButtonResolvedColors(
            container = container,
            content = colors.content.disabled,
            border = border,
        )
    }

    val pressedActive = isPressed && !loading
    return when (variant) {
        AuraButtonVariant.Primary -> AuraButtonResolvedColors(
            container = if (pressedActive) colors.brand.primaryPressed else colors.brand.primary,
            content = colors.brand.onPrimary,
            border = null,
        )

        AuraButtonVariant.Secondary -> AuraButtonResolvedColors(
            container = if (pressedActive) {
                colors.interaction.pressedOverlay.compositeOver(colors.brand.subtle)
            } else {
                colors.brand.subtle
            },
            content = colors.brand.onSubtle,
            border = null,
        )

        AuraButtonVariant.Outline -> AuraButtonResolvedColors(
            container = if (pressedActive) colors.interaction.pressedOverlay else Color.Transparent,
            content = colors.content.primary,
            border = colors.border.default,
        )

        AuraButtonVariant.Ghost -> AuraButtonResolvedColors(
            container = if (pressedActive) colors.interaction.pressedOverlay else Color.Transparent,
            content = colors.brand.primary,
            border = null,
        )

        AuraButtonVariant.Destructive -> AuraButtonResolvedColors(
            container = if (pressedActive) {
                colors.interaction.pressedOverlay.compositeOver(colors.status.error.solid)
            } else {
                colors.status.error.solid
            },
            content = colors.status.error.onSolid,
            border = null,
        )
    }
}

/**
 * Labeled action button implementing the `aura.component.button` specification.
 *
 * Colors bind strictly to [AuraTheme.colors]; dimension and typography tokens will
 * migrate to `AuraDimensions` and `AuraTypography` once those foundation layers land.
 *
 * @param text Action verb label displayed inside the button.
 * @param onClick Callback invoked when clicked (throttled by [debounceIntervalMs]).
 * @param modifier Modifier applied to the outer touch-target container.
 * @param variant Visual hierarchy variant (`Primary`, `Secondary`, `Outline`, `Ghost`, `Destructive`).
 * @param size Visual height and typography scale (`Small`, `Medium`, `Large`).
 * @param enabled Controls whether the button accepts user input.
 * @param loading Displays a centered spinner while preserving button width and blocking clicks.
 * @param fullWidth Expands the button to fill available horizontal width.
 * @param debounceIntervalMs Minimum interval in milliseconds between accepted clicks (`0L` disables).
 * @param leadingIcon Optional icon composable rendered before [text].
 * @param trailingIcon Optional icon composable rendered after [text].
 */
@Composable
fun AuraButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AuraButtonVariant = AuraButtonVariant.Primary,
    size: AuraButtonSize = AuraButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    debounceIntervalMs: Long = 400L,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val colors = AuraTheme.colors
    val sizeSpec = size.toSpec()
    val shape = RoundedCornerShape(sizeSpec.cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val resolvedColors = resolveAuraButtonColors(
        variant = variant,
        enabled = enabled,
        isPressed = isPressed,
        loading = loading,
        colors = colors,
    )

    var lastClickUptimeMs by remember { mutableLongStateOf(-1L) }
    val debouncedOnClick: () -> Unit = {
        if (debounceIntervalMs <= 0L) {
            onClick()
        } else {
            val now = SystemClock.uptimeMillis()
            if (lastClickUptimeMs < 0L || now - lastClickUptimeMs >= debounceIntervalMs) {
                lastClickUptimeMs = now
                onClick()
            }
        }
    }

    val widthModifier = if (fullWidth) Modifier.fillMaxWidth() else Modifier

    Box(
        modifier = modifier
            .then(widthModifier)
            .defaultMinSize(minWidth = MinTouchTargetWidth, minHeight = sizeSpec.minTouchHeight)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && !loading,
                role = Role.Button,
                onClick = debouncedOnClick,
            )
            .semantics {
                role = Role.Button
                if (loading) {
                    stateDescription = "In progress"
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        val borderModifier = if (resolvedColors.border != null) {
            Modifier.border(width = 1.dp, color = resolvedColors.border, shape = shape)
        } else {
            Modifier
        }

        Box(
            modifier = Modifier
                .then(widthModifier)
                .heightIn(min = sizeSpec.visualHeight)
                .clip(shape)
                .background(color = resolvedColors.container, shape = shape)
                .then(borderModifier)
                .padding(
                    horizontal = sizeSpec.horizontalPadding,
                    vertical = sizeSpec.verticalPadding,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                // Hide content visually while loading so measured width stays fixed.
                modifier = Modifier.alpha(if (loading) 0f else 1f),
                horizontalArrangement = Arrangement.spacedBy(sizeSpec.iconGap, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leadingIcon != null) {
                    Box(
                        modifier = Modifier.size(sizeSpec.iconSize),
                        contentAlignment = Alignment.Center,
                    ) {
                        leadingIcon()
                    }
                }
                BasicText(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = resolvedColors.content,
                        fontSize = sizeSpec.fontSize,
                        lineHeight = sizeSpec.lineHeight,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                if (trailingIcon != null) {
                    Box(
                        modifier = Modifier.size(sizeSpec.iconSize),
                        contentAlignment = Alignment.Center,
                    ) {
                        trailingIcon()
                    }
                }
            }

            if (loading) {
                AuraLoadingSpinner(
                    color = resolvedColors.content,
                    size = sizeSpec.spinnerSize,
                    strokeWidth = sizeSpec.spinnerStrokeWidth,
                )
            }
        }
    }
}

@Composable
private fun AuraLoadingSpinner(
    color: Color,
    size: Dp,
    strokeWidth: Dp,
) {
    val transition = rememberInfiniteTransition(label = "AuraButtonSpinner")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "SpinnerRotation",
    )

    Canvas(modifier = Modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val inset = strokePx / 2f
        val arcSize = Size(
            width = this.size.width - strokePx,
            height = this.size.height - strokePx,
        )
        drawCircle(
            color = color.copy(alpha = 0.24f),
            radius = (this.size.minDimension - strokePx) / 2f,
            style = Stroke(width = strokePx),
        )
        drawArc(
            color = color,
            startAngle = angle,
            sweepAngle = 100f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = strokePx, cap = StrokeCap.Round),
        )
    }
}

@Preview(name = "Light Variants", showBackground = true)
@Composable
private fun AuraButtonLightPreview() {
    AuraTheme(colorMode = AuraColorMode.Light) {
        Column(
            modifier = Modifier
                .background(AuraTheme.colors.background.primary)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AuraButton(text = "Primary", onClick = {}, variant = AuraButtonVariant.Primary)
            AuraButton(text = "Secondary", onClick = {}, variant = AuraButtonVariant.Secondary)
            AuraButton(text = "Outline", onClick = {}, variant = AuraButtonVariant.Outline)
            AuraButton(text = "Ghost", onClick = {}, variant = AuraButtonVariant.Ghost)
            AuraButton(text = "Destructive", onClick = {}, variant = AuraButtonVariant.Destructive)
            AuraButton(text = "Loading", onClick = {}, loading = true)
            AuraButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}

@Preview(name = "Dark Variants", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun AuraButtonDarkPreview() {
    AuraTheme(colorMode = AuraColorMode.Dark) {
        Column(
            modifier = Modifier
                .background(AuraTheme.colors.background.primary)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AuraButton(text = "Primary", onClick = {}, variant = AuraButtonVariant.Primary)
            AuraButton(text = "Secondary", onClick = {}, variant = AuraButtonVariant.Secondary)
            AuraButton(text = "Outline", onClick = {}, variant = AuraButtonVariant.Outline)
            AuraButton(text = "Ghost", onClick = {}, variant = AuraButtonVariant.Ghost)
            AuraButton(text = "Destructive", onClick = {}, variant = AuraButtonVariant.Destructive)
            AuraButton(text = "Loading", onClick = {}, loading = true)
            AuraButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}

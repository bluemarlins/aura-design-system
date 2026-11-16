package com.example.ds.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ds.foundation.color.AuraColors
import com.example.ds.foundation.theme.AuraColorMode
import com.example.ds.foundation.theme.AuraTheme

/**
 * Semantic tone variants for [AuraLinearProgressBar] and [AuraStepProgressBar].
 */
enum class AuraProgressVariant {
    Brand,
    Success,
    Warning,
    Error,
    Info,
}

private fun resolveAuraProgressColor(
    variant: AuraProgressVariant,
    colors: AuraColors,
): Color = when (variant) {
    AuraProgressVariant.Brand -> colors.brand.primary
    AuraProgressVariant.Success -> colors.status.success.solid
    AuraProgressVariant.Warning -> colors.status.warning.solid
    AuraProgressVariant.Error -> colors.status.error.solid
    AuraProgressVariant.Info -> colors.status.info.solid
}

/**
 * Continuous linear progress bar implementing the `aura.component.progress-bar` specification.
 */
@Composable
fun AuraLinearProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    variant: AuraProgressVariant = AuraProgressVariant.Brand,
    height: Dp = 6.dp,
    shape: Shape = AuraTheme.shapes.small,
    trackColor: Color = AuraTheme.colors.surface.sunken,
    indicatorColor: Color = Color.Unspecified,
    contentDescription: String? = null,
) {
    val colors = AuraTheme.colors
    val motion = AuraTheme.motion
    val clampedProgress = progress.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = clampedProgress,
        animationSpec = if (motion.reduceMotion) snap() else tween(durationMillis = motion.stateChangeDurationMillis),
        label = "AuraLinearProgress",
    )
    val resolvedIndicator = indicatorColor.takeOrElse {
        resolveAuraProgressColor(variant = variant, colors = colors)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(trackColor)
            .semantics(mergeDescendants = true) {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = clampedProgress,
                    range = 0f..1f,
                )
                if (!contentDescription.isNullOrBlank()) {
                    this.contentDescription = contentDescription
                }
            },
    ) {
        if (animatedProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(shape)
                    .background(resolvedIndicator),
            )
        }
    }
}

/**
 * Segmented step progress indicator implementing the `aura.component.progress-bar` specification.
 */
@Composable
fun AuraStepProgressBar(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
    variant: AuraProgressVariant = AuraProgressVariant.Brand,
    height: Dp = 4.dp,
    segmentSpacing: Dp = 4.dp,
    shape: Shape = AuraTheme.shapes.extraSmall,
    activeColor: Color = Color.Unspecified,
    inactiveColor: Color = AuraTheme.colors.surface.sunken,
    contentDescription: String? = null,
) {
    val colors = AuraTheme.colors
    val motion = AuraTheme.motion
    val safeTotal = totalSteps.coerceAtLeast(0)
    val safeCurrent = currentStep.coerceIn(0, safeTotal)
    val resolvedActive = activeColor.takeOrElse {
        resolveAuraProgressColor(variant = variant, colors = colors)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = safeCurrent.toFloat(),
                    range = 0f..safeTotal.coerceAtLeast(1).toFloat(),
                )
                if (!contentDescription.isNullOrBlank()) {
                    this.contentDescription = contentDescription
                }
            },
        horizontalArrangement = Arrangement.spacedBy(segmentSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(safeTotal) { index ->
            val targetColor = if (index < safeCurrent) resolvedActive else inactiveColor
            val segmentColor by animateColorAsState(
                targetValue = targetColor,
                animationSpec = if (motion.reduceMotion) snap() else tween(durationMillis = motion.stateChangeDurationMillis),
                label = "AuraStepSegmentColor_$index",
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(height)
                    .clip(shape)
                    .background(segmentColor),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuraProgressBarPreview() {
    AuraTheme(colorMode = AuraColorMode.Light) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AuraTheme.colors.background.primary)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AuraLinearProgressBar(progress = 0.65f, variant = AuraProgressVariant.Brand)
            AuraLinearProgressBar(progress = 0.85f, variant = AuraProgressVariant.Success)
            AuraStepProgressBar(currentStep = 3, totalSteps = 5)
        }
    }
}

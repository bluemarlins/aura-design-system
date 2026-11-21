package io.github.bluemarlins.aura.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.foundation.motion.AuraMotionProgressKey
import io.github.bluemarlins.aura.foundation.theme.AuraColorMode
import io.github.bluemarlins.aura.foundation.theme.AuraTheme

/**
 * Loading placeholder that holds the shape of content that has not arrived yet.
 *
 * Use it instead of rendering an empty state before data has loaded: an empty state claims
 * "there is nothing here", which is false while the first query is still running.
 *
 * The block pulses between `surface.sunken` and `surface.raised` on
 * `AuraTheme.motion.skeletonPulseDurationMillis`; with Reduced Motion it holds a static
 * `surface.sunken` fill.
 */
@Composable
fun AuraSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = AuraTheme.shapes.small,
    contentDescription: String? = null,
) {
    val colors = AuraTheme.colors
    val motion = AuraTheme.motion
    val reducedMotion = rememberAuraReducedMotion()

    val pulse = if (reducedMotion) {
        0f
    } else {
        val transition = rememberInfiniteTransition(label = "AuraSkeletonPulse")
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = motion.skeletonPulseDurationMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "AuraSkeletonPulseFraction",
        ).value
    }

    val fill = lerp(colors.surface.sunken, colors.surface.raised, pulse * 0.6f)

    Box(
        modifier = modifier
            .testTag(AuraSkeletonTestTag)
            .semantics {
                this[AuraMotionProgressKey] = pulse
                if (contentDescription != null) this.contentDescription = contentDescription
            }
            .background(color = fill, shape = shape),
    )
}

/**
 * A stack of [lines] text-line skeletons; the last line is shorter so the block reads as a paragraph.
 */
@Composable
fun AuraSkeletonText(
    modifier: Modifier = Modifier,
    lines: Int = 2,
    lineHeight: Dp = 14.dp,
    lineSpacing: Dp = 8.dp,
    contentDescription: String? = null,
) {
    val count = lines.coerceAtLeast(1)
    Column(
        modifier = modifier.semantics(mergeDescendants = true) {
            if (contentDescription != null) this.contentDescription = contentDescription
        },
        verticalArrangement = Arrangement.spacedBy(lineSpacing),
    ) {
        repeat(count) { index ->
            val isLast = index == count - 1 && count > 1
            AuraSkeleton(
                modifier = Modifier
                    .fillMaxWidth(if (isLast) 0.6f else 1f)
                    .height(lineHeight),
                shape = AuraTheme.shapes.extraSmall,
            )
        }
    }
}

const val AuraSkeletonTestTag = "aura_skeleton"

@Preview(name = "Skeleton", showBackground = true)
@Composable
private fun AuraSkeletonPreview() {
    AuraTheme(colorMode = AuraColorMode.Light) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AuraSkeleton(modifier = Modifier.fillMaxWidth().height(96.dp), shape = AuraTheme.shapes.extraLarge)
            AuraSkeletonText(lines = 3)
        }
    }
}

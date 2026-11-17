package com.example.ds.foundation.motion

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Aura-original interaction motion track.
 *
 * - [Standard]: Productive, restrained spatial transitions with near-zero overshoot for
 *   task-heavy workflows, inputs, and dense records.
 * - [Expressive]: Elastic spatial transitions and fluid choreography reserved for hero
 *   navigation chrome and transient modal overlays.
 *
 * Defined independently of Android Material Design (`material3`).
 */
enum class AuraMotionProfile {
    Standard,
    Expressive,
}

/**
 * Tier 2 — Aura semantic interaction motion tokens.
 *
 * Defines spring physics, asymmetric press/release durations, subtle tactile micro-interaction
 * amplitudes, and dual-track spatial/choreography timings independent of Material Design.
 */
@Immutable
data class AuraMotion(
    val pressInDampingRatio: Float = 0.85f,
    val pressInStiffness: Float = 800f,
    val pressOutDampingRatio: Float = 0.65f,
    val pressOutStiffness: Float = 500f,
    val iconDampingRatio: Float = 0.75f,
    val iconStiffness: Float = 600f,
    val pressInDurationMillis: Int = 90,
    val pressOutDurationMillis: Int = 180,
    val minPressDurationMillis: Long = 110L,
    val stateChangeDurationMillis: Int = 150,
    val loadingCrossfadeDurationMillis: Int = 120,
    val spinnerRotationDurationMillis: Int = 900,
    val spinnerSweepDurationMillis: Int = 650,
    val pressedScaleDefault: Float = 0.96f,
    val pressedScaleLarge: Float = 0.975f,
    val pressedScaleGhost: Float = 0.95f,
    val leadingIconPressedScale: Float = 0.92f,
    val trailingIconNudge: Dp = 2.dp,
    val profile: AuraMotionProfile = AuraMotionProfile.Standard,
    val enterDurationMillis: Int = 280,
    val exitDurationMillis: Int = 200,
    val spatialDampingRatio: Float = 0.90f,
    val spatialStiffness: Float = 400f,
    val elasticDampingRatio: Float = 0.58f,
    val elasticStiffness: Float = 320f,
    val staggerDelayMillis: Int = 40,
    val staggerBudgetMillis: Int = 400,
    val scrimFadeDurationMillis: Int = 180,
    val adaptiveBarDampingRatio: Float = 0.85f,
    val adaptiveBarStiffness: Float = 380f,
    val floatingBarAppearDurationMillis: Int = 250,
    val floatingBarDisappearDurationMillis: Int = 180,
    val reduceMotion: Boolean = false,
) {
    fun <T> pressInSpring(): SpringSpec<T> = spring(
        dampingRatio = pressInDampingRatio,
        stiffness = pressInStiffness,
    )

    fun <T> pressOutSpring(): SpringSpec<T> = spring(
        dampingRatio = pressOutDampingRatio,
        stiffness = pressOutStiffness,
    )

    fun <T> iconSpring(): SpringSpec<T> = spring(
        dampingRatio = iconDampingRatio,
        stiffness = iconStiffness,
    )

    fun <T> spatialSpring(): SpringSpec<T> = spring(
        dampingRatio = spatialDampingRatio,
        stiffness = spatialStiffness,
    )

    fun <T> elasticStretchSpring(): SpringSpec<T> = spring(
        dampingRatio = elasticDampingRatio,
        stiffness = elasticStiffness,
    )

    fun <T> enterSpec(): FiniteAnimationSpec<T> = if (reduceMotion) {
        snap()
    } else {
        tween(durationMillis = enterDurationMillis, easing = FastOutSlowInEasing)
    }

    fun <T> exitSpec(): FiniteAnimationSpec<T> = if (reduceMotion) {
        snap()
    } else {
        tween(durationMillis = exitDurationMillis, easing = FastOutSlowInEasing)
    }

    /** Non-overshooting damped spring for Docked <-> Floating adaptive app bar morphing. */
    fun <T> adaptiveBarMorphSpring(): FiniteAnimationSpec<T> = if (reduceMotion) {
        snap()
    } else {
        spring(
            dampingRatio = adaptiveBarDampingRatio,
            stiffness = adaptiveBarStiffness,
        )
    }

    /** Fast-entry smooth-settle curve for floating bar appearance (`250ms`, ease-out cubic). */
    fun <T> floatingBarAppearSpec(): FiniteAnimationSpec<T> = if (reduceMotion) {
        snap()
    } else {
        tween(
            durationMillis = floatingBarAppearDurationMillis,
            easing = CubicBezierEasing(0.22f, 1.0f, 0.36f, 1.0f),
        )
    }

    /** Quick-exit curve for floating bar dismissal (`180ms`, ease-in cubic). */
    fun <T> floatingBarDisappearSpec(): FiniteAnimationSpec<T> = if (reduceMotion) {
        snap()
    } else {
        tween(
            durationMillis = floatingBarDisappearDurationMillis,
            easing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f),
        )
    }

    fun staggerDelay(index: Int): Int =
        if (reduceMotion || index <= 0) 0 else minOf(index * staggerDelayMillis, staggerBudgetMillis)
}

object AuraMotionDefaults {
    val Standard = AuraMotion()

    val Expressive = AuraMotion(
        profile = AuraMotionProfile.Expressive,
        enterDurationMillis = 380,
        exitDurationMillis = 260,
        spatialDampingRatio = 0.75f,
        spatialStiffness = 350f,
        staggerDelayMillis = 60,
        staggerBudgetMillis = 400,
        scrimFadeDurationMillis = 220,
    )

    fun forProfile(profile: AuraMotionProfile): AuraMotion = when (profile) {
        AuraMotionProfile.Standard -> Standard
        AuraMotionProfile.Expressive -> Expressive
    }
}

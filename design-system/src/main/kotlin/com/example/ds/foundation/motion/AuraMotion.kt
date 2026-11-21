package com.example.ds.foundation.motion

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
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
 * Aura easing curves (Tier 1). Arrivals decelerate, departures accelerate (`P4 · Asymmetry`).
 */
object AuraEasing {
    /** `cubic-bezier(0.4, 0, 0.2, 1)`: color, border, state tint, crossfade. */
    val Standard: Easing = FastOutSlowInEasing

    /** `cubic-bezier(0.22, 1, 0.36, 1)`: every entrance. */
    val Decelerate: Easing = CubicBezierEasing(0.22f, 1.0f, 0.36f, 1.0f)

    /** `cubic-bezier(0.4, 0, 1, 1)`: every exit. */
    val Accelerate: Easing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)
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
    /** Selection travel (thumbs, indices, values): `0.6%` overshoot, `~192ms` settle. */
    val selectionDampingRatio: Float = 0.85f,
    val selectionStiffness: Float = 600f,
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
    /** `fluidPressScale` depth on Glass chrome (Light mass class). */
    val glassPressedScale: Float = 0.95f,
    /** Anisotropic gel response on Glass press: horizontal stretch / vertical squash per unit of compression. */
    val gelStretchX: Float = 0.14f,
    val gelSquashY: Float = 0.06f,
    /** Peak radial touch glow alpha on Glass press. */
    val pressGlowAlpha: Float = 0.24f,
    /** Switch thumb width while pressed; Slider thumb while dragging. */
    val grabScale: Float = 1.18f,
    /** Rating star under the finger while scrubbing. */
    val scrubScale: Float = 1.12f,
    /** Selected Bottom Island tab icon. */
    val selectedIconScale: Float = 1.08f,
    /** Badge scale before its content appears. */
    val presenceEnterScale: Float = 0.90f,
    /** Dialog scale at the start of Show and the end of Hide. */
    val overlayEnterScale: Float = 0.94f,
    val overlayExitScale: Float = 0.97f,
    /** Anchored popover scale at the start of Show. */
    val popoverEnterScale: Float = 0.92f,
    /** Label/icon scale while a button swaps to its loading spinner. */
    val loadingContentScale: Float = 0.92f,
    /** Spinner scale before it appears. */
    val spinnerEnterScale: Float = 0.75f,
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
    /** One half-cycle of the `AuraSkeleton` loading pulse (rest → highlight). */
    val skeletonPulseDurationMillis: Int = 900,
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

    /** Non-overshooting travel for selection state changes. Use instead of [pressOutSpring] for thumbs and values. */
    fun <T> selectionSpring(): FiniteAnimationSpec<T> = if (reduceMotion) {
        snap()
    } else {
        spring(
            dampingRatio = selectionDampingRatio,
            stiffness = selectionStiffness,
        )
    }

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
        tween(durationMillis = enterDurationMillis, easing = AuraEasing.Decelerate)
    }

    fun <T> exitSpec(): FiniteAnimationSpec<T> = if (reduceMotion) {
        snap()
    } else {
        tween(durationMillis = exitDurationMillis, easing = AuraEasing.Accelerate)
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
            easing = AuraEasing.Decelerate,
        )
    }

    /** Quick-exit curve for floating bar dismissal (`180ms`, ease-in cubic). */
    fun <T> floatingBarDisappearSpec(): FiniteAnimationSpec<T> = if (reduceMotion) {
        snap()
    } else {
        tween(
            durationMillis = floatingBarDisappearDurationMillis,
            easing = AuraEasing.Accelerate,
        )
    }

    /** Opacity-only fade that replaces spatial Show/Hide/Morph under Reduced Motion (`MOT-RULE-007`). */
    fun <T> reducedFadeSpec(): FiniteAnimationSpec<T> =
        tween(durationMillis = loadingCrossfadeDurationMillis, easing = AuraEasing.Standard)

    /**
     * Binds a component to its declared `motion_track`. Spatial, stagger, and scrim tokens come from
     * [track]; tactile tokens and [reduceMotion] stay as configured on this instance.
     */
    fun forTrack(track: AuraMotionProfile): AuraMotion {
        if (profile == track) return this
        val target = AuraMotionDefaults.forProfile(track)
        return copy(
            profile = target.profile,
            enterDurationMillis = target.enterDurationMillis,
            exitDurationMillis = target.exitDurationMillis,
            spatialDampingRatio = target.spatialDampingRatio,
            spatialStiffness = target.spatialStiffness,
            staggerDelayMillis = target.staggerDelayMillis,
            staggerBudgetMillis = target.staggerBudgetMillis,
            scrimFadeDurationMillis = target.scrimFadeDurationMillis,
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

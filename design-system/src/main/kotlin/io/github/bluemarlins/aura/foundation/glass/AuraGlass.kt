package io.github.bluemarlins.aura.foundation.glass

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.foundation.color.AuraPalette
import io.github.bluemarlins.aura.foundation.theme.AuraColorMode

/**
 * Tier 2 — Aura-original optical tokens for `Glass` / `LiquidGlass` surfaces.
 *
 * Single source of truth for backdrop blur, saturation, tint, specular stroke, and liquid lens
 * values consumed by `GlassSurface` and `LiquidMetaballBridge` via `AuraTheme.glass`.
 *
 * [frostedTint] and [liquidTint] must keep `content.primary` at `>= 4.5:1` over both a pure white
 * and a pure black backdrop (`VIS-RULE-003`); `AuraGlassTest` enforces this for the defaults.
 *
 * Defined independently of Android Material Design (`material3`).
 */
@Immutable
data class AuraGlass(
    /** Translucent wash over the blurred backdrop for `Glass`. */
    val frostedTint: Color,
    /** Translucent wash over the refracted backdrop for `LiquidGlass`. */
    val liquidTint: Color,
    /** Fill of the `LiquidGlass` metaball neck between two pills. */
    val liquidBridgeTint: Color,
    /** Specular stroke alpha at the top-start (lit) corner. */
    val strokeStartAlpha: Float,
    /** Specular stroke alpha at the bottom-end (shaded) corner. */
    val strokeEndAlpha: Float,
    /** Opacity of `surface.floating` when a glass surface falls back to `Solid`. */
    val solidFallbackAlpha: Float = 0.98f,
    /** Backdrop gaussian blur radius for `Glass`. */
    val blurRadius: Dp = 26.dp,
    /** Softer backdrop blur radius under the `LiquidGlass` lens. */
    val liquidBlurRadius: Dp = 10.dp,
    /** Backdrop saturation multiplier for `Glass`. */
    val saturation: Float = 1.2f,
    /** Backdrop saturation multiplier for `LiquidGlass`. */
    val liquidSaturation: Float = 1.32f,
    /** Width of the convex edge bevel where `LiquidGlass` refraction ramps in. */
    val bevelWidth: Dp = 11.dp,
    /** Maximum backdrop displacement at the `LiquidGlass` edge. */
    val refraction: Dp = 7.5.dp,
    /** Chromatic RGB channel separation at the `LiquidGlass` edge. */
    val dispersion: Dp = 1.45.dp,
    /**
     * Opacity of the monochrome frosted micro-grain drawn over a `Glass` (Glass Morphism) sheet.
     * Breaks up gradient banding and gives the acrylic its etched texture (`0f` disables it).
     */
    val noiseAlpha: Float = 0.018f,
    /**
     * Linear gain applied to the blurred `Glass` backdrop. Dark mode keeps it below `1f` so bright
     * highlights passing under smoked glass never bloom into hotspots.
     */
    val highlightGain: Float = 1f,
)

object AuraGlassDefaults {
    val Light = AuraGlass(
        frostedTint = AuraPalette.Neutral0.copy(alpha = 0.72f),
        liquidTint = AuraPalette.Neutral0.copy(alpha = 0.54f),
        liquidBridgeTint = AuraPalette.Neutral0.copy(alpha = 0.68f),
        strokeStartAlpha = 0.55f,
        strokeEndAlpha = 0.08f,
        blurRadius = 26.dp,
        saturation = 1.35f,
        noiseAlpha = 0.018f,
        highlightGain = 1f,
    )

    val Dark = AuraGlass(
        frostedTint = AuraPalette.GlassDarkTintBase.copy(alpha = 0.68f),
        liquidTint = AuraPalette.GlassDarkTintBase.copy(alpha = 0.66f),
        liquidBridgeTint = AuraPalette.GlassDarkBridgeBase.copy(alpha = 0.62f),
        strokeStartAlpha = 0.34f,
        strokeEndAlpha = 0.06f,
        blurRadius = 28.dp,
        saturation = 1.22f,
        noiseAlpha = 0.022f,
        highlightGain = 0.9f,
    )

    fun forColorMode(colorMode: AuraColorMode): AuraGlass = when (colorMode) {
        AuraColorMode.Light -> Light
        AuraColorMode.Dark -> Dark
    }
}

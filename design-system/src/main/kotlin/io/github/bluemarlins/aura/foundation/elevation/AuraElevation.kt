package io.github.bluemarlins.aura.foundation.elevation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Single rung on Aura's Z-axis elevation ladder.
 *
 * Pairs physical Z elevation with key/ambient shadow opacity multipliers and protective
 * scrim alpha so floating layers separate cleanly without ad-hoc drop-shadow literals.
 */
@Immutable
data class AuraElevationLevel(
    val elevation: Dp,
    val keyShadowAlpha: Float,
    val ambientShadowAlpha: Float,
    val scrimAlpha: Float,
)

/**
 * Tier 2 — Aura-original 5-step depth & scrim elevation ladder (`Level0`–`Level4`).
 *
 * Defined independently of Android Material Design (`material3`).
 */
@Immutable
data class AuraElevation(
    val level0: AuraElevationLevel = AuraElevationLevel(
        elevation = 0.dp,
        keyShadowAlpha = 0f,
        ambientShadowAlpha = 0f,
        scrimAlpha = 0f,
    ),
    val level1: AuraElevationLevel = AuraElevationLevel(
        elevation = 2.dp,
        keyShadowAlpha = 0.06f,
        ambientShadowAlpha = 0.04f,
        scrimAlpha = 0.16f,
    ),
    val level2: AuraElevationLevel = AuraElevationLevel(
        elevation = 6.dp,
        keyShadowAlpha = 0.14f,
        ambientShadowAlpha = 0.08f,
        scrimAlpha = 0.28f,
    ),
    val level3: AuraElevationLevel = AuraElevationLevel(
        elevation = 12.dp,
        keyShadowAlpha = 0.14f,
        ambientShadowAlpha = 0.08f,
        scrimAlpha = 0.40f,
    ),
    val level4: AuraElevationLevel = AuraElevationLevel(
        elevation = 24.dp,
        keyShadowAlpha = 0.20f,
        ambientShadowAlpha = 0.12f,
        scrimAlpha = 0.56f,
    ),
)

object AuraElevationDefaults {
    val Standard = AuraElevation()
}

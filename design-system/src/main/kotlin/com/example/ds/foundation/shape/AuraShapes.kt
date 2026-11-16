package com.example.ds.foundation.shape

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

/**
 * Tier 2 — Aura semantic corner shape tokens.
 *
 * Exposes standardized corner radii across Aura components and consumer app surfaces:
 * - [extraSmall]: `4.dp` (step progress segments, compact tags)
 * - [small]: `8.dp` (progress bars, thumbnails, compact controls)
 * - [medium]: `12.dp` (snackbars, inline media cards, standard fields)
 * - [large]: `16.dp` (cards, grouped panels, previews)
 * - [extraLarge]: `24.dp` (dialogs, bottom sheets, spatial containers)
 * - [full]: `CircleShape` (capsules, pills, circular controls)
 */
@Immutable
data class AuraShapes(
    val extraSmall: CornerBasedShape = RoundedCornerShape(4.dp),
    val small: CornerBasedShape = RoundedCornerShape(8.dp),
    val medium: CornerBasedShape = RoundedCornerShape(12.dp),
    val large: CornerBasedShape = RoundedCornerShape(16.dp),
    val extraLarge: CornerBasedShape = RoundedCornerShape(24.dp),
    val full: CornerBasedShape = CircleShape,
)

object AuraShapesDefaults {
    val Standard = AuraShapes()
}

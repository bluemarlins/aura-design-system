package com.example.ds.foundation.motion

import androidx.compose.ui.semantics.SemanticsPropertyKey

/**
 * Internal semantics hooks exposing live animated scale and normalized progress values
 * to deterministic `mainClock` motion tests without altering public component signatures.
 */
internal val AuraMotionScaleKey = SemanticsPropertyKey<Float>("AuraMotionScale")
internal val AuraMotionProgressKey = SemanticsPropertyKey<Float>("AuraMotionProgress")

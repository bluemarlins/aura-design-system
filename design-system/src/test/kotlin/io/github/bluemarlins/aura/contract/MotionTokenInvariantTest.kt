package io.github.bluemarlins.aura.contract

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sqrt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import io.github.bluemarlins.aura.foundation.motion.AuraMotionDefaults
import io.github.bluemarlins.aura.foundation.motion.AuraMotionProfile

class MotionTokenInvariantTest {
    @Test
    fun `standard and expressive profiles satisfy asymmetric timing and stagger bounds`() {
        val profiles = listOf(
            AuraMotionDefaults.Standard,
            AuraMotionDefaults.Expressive,
        )

        profiles.forEach { motion ->
            assertTrue(
                "${motion.profile}: enterDurationMillis must exceed exitDurationMillis",
                motion.enterDurationMillis > motion.exitDurationMillis,
            )
            assertTrue(
                "${motion.profile}: enter must be at least 30% longer than exit",
                motion.enterDurationMillis >= (motion.exitDurationMillis * 1.3f).toInt(),
            )
            assertTrue(
                "${motion.profile}: pressInDurationMillis must be shorter than pressOutDurationMillis",
                motion.pressInDurationMillis < motion.pressOutDurationMillis,
            )
            assertTrue(
                "${motion.profile}: spatialDampingRatio must be within (0, 1]",
                motion.spatialDampingRatio > 0f && motion.spatialDampingRatio <= 1f,
            )
            assertTrue(
                "${motion.profile}: staggerDelayMillis must stay within 20..100ms",
                motion.staggerDelayMillis in 20..100,
            )
            assertTrue(
                "${motion.profile}: staggerBudgetMillis must stay <= 400ms",
                motion.staggerBudgetMillis <= 400,
            )
            assertEquals(0, motion.staggerDelay(0))
            assertEquals(3 * motion.staggerDelayMillis, motion.staggerDelay(3))
            assertTrue(motion.staggerDelay(100) <= motion.staggerBudgetMillis)
            assertEquals(0, motion.copy(reduceMotion = true).staggerDelay(5))
        }
    }

    @Test
    fun `standard keeps near-zero overshoot and expressive allows lower damping`() {
        val standard = AuraMotionDefaults.Standard
        val expressive = AuraMotionDefaults.Expressive

        assertEquals(AuraMotionProfile.Standard, standard.profile)
        assertEquals(AuraMotionProfile.Expressive, expressive.profile)
        assertTrue(
            "Standard spatial damping must stay >= 0.85f (0-3% overshoot)",
            standard.spatialDampingRatio >= 0.85f,
        )
        assertTrue(
            "Expressive spatial damping should be lower than Standard",
            expressive.spatialDampingRatio < standard.spatialDampingRatio,
        )
        assertEquals(standard, AuraMotionDefaults.forProfile(AuraMotionProfile.Standard))
        assertEquals(expressive, AuraMotionDefaults.forProfile(AuraMotionProfile.Expressive))
    }

    /** Overshoot of a unit-mass damped spring: `e^(-ζπ/√(1-ζ²))`, `0` when critically damped. */
    private fun overshoot(dampingRatio: Float): Double =
        if (dampingRatio >= 1f) 0.0 else exp(-dampingRatio * PI / sqrt(1.0 - dampingRatio * dampingRatio))

    @Test
    fun `standard travel springs stay within the 3 percent overshoot budget`() {
        val standard = AuraMotionDefaults.Standard
        listOf(
            "selection" to standard.selectionDampingRatio,
            "spatial" to standard.spatialDampingRatio,
            "adaptiveBarMorph" to standard.adaptiveBarDampingRatio,
            "pressIn" to standard.pressInDampingRatio,
        ).forEach { (name, ratio) ->
            assertTrue("$name overshoot must be <= 3% (was ${overshoot(ratio)})", overshoot(ratio) <= 0.03)
        }
        assertTrue(
            "Expressive spatial overshoot must stay <= 3%",
            overshoot(AuraMotionDefaults.Expressive.spatialDampingRatio) <= 0.03,
        )
        assertTrue(
            "selectionSpring must settle faster than spatialSpring",
            standard.selectionStiffness > standard.spatialStiffness,
        )
    }

    @Test
    fun `press amplitudes follow the mass classes`() {
        val motion = AuraMotionDefaults.Standard
        listOf(
            motion.pressedScaleGhost,
            motion.pressedScaleDefault,
            motion.pressedScaleLarge,
            motion.glassPressedScale,
        ).forEach { scale ->
            assertTrue("pressed scale $scale must stay within [0.94, 0.98]", scale in 0.94f..0.98f)
        }
        assertTrue(motion.pressedScaleGhost <= motion.pressedScaleDefault)
        assertTrue(motion.pressedScaleDefault < motion.pressedScaleLarge)
        assertEquals(motion.pressedScaleGhost, motion.glassPressedScale)
        assertTrue(
            "Hide must travel less than Show (overlayExitScale > overlayEnterScale)",
            motion.overlayExitScale > motion.overlayEnterScale,
        )
    }

    @Test
    fun `forTrack swaps spatial tokens and keeps tactile tokens and reduceMotion`() {
        val custom = AuraMotionDefaults.Standard.copy(pressedScaleDefault = 0.97f, reduceMotion = true)
        val bound = custom.forTrack(AuraMotionProfile.Expressive)
        val expressive = AuraMotionDefaults.Expressive

        assertEquals(AuraMotionProfile.Expressive, bound.profile)
        assertEquals(expressive.enterDurationMillis, bound.enterDurationMillis)
        assertEquals(expressive.exitDurationMillis, bound.exitDurationMillis)
        assertEquals(expressive.spatialDampingRatio, bound.spatialDampingRatio)
        assertEquals(expressive.spatialStiffness, bound.spatialStiffness)
        assertEquals(expressive.scrimFadeDurationMillis, bound.scrimFadeDurationMillis)
        assertEquals(0.97f, bound.pressedScaleDefault)
        assertTrue(bound.reduceMotion)
        assertEquals(expressive, expressive.forTrack(AuraMotionProfile.Expressive))
    }
}

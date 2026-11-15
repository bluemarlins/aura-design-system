package com.example.ds.contract

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import com.example.ds.foundation.motion.AuraMotionDefaults
import com.example.ds.foundation.motion.AuraMotionProfile

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
}

package io.github.bluemarlins.aura.foundation.glass

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import io.github.bluemarlins.aura.foundation.color.AuraColorSchemes
import io.github.bluemarlins.aura.foundation.theme.AuraColorMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AuraGlassTest {
    @Test
    fun `forColorMode selects the matching glass scheme`() {
        assertEquals(AuraGlassDefaults.Light, AuraGlassDefaults.forColorMode(AuraColorMode.Light))
        assertEquals(AuraGlassDefaults.Dark, AuraGlassDefaults.forColorMode(AuraColorMode.Dark))
    }

    @Test
    fun `light and dark glass schemes keep optical invariants`() {
        listOf(AuraGlassDefaults.Light, AuraGlassDefaults.Dark).forEach { glass ->
            assertTrue("frostedTint must be translucent", glass.frostedTint.alpha > 0f && glass.frostedTint.alpha < 1f)
            assertTrue(
                "liquidTint must be clearer than frostedTint",
                glass.liquidTint.alpha < glass.frostedTint.alpha,
            )
            assertTrue(
                "specular stroke must fade from the lit corner",
                glass.strokeStartAlpha > glass.strokeEndAlpha,
            )
            assertTrue(
                "liquid lens must blur less than frosted glass",
                glass.liquidBlurRadius < glass.blurRadius,
            )
            assertTrue("solid fallback must be near-opaque", glass.solidFallbackAlpha >= 0.95f)
            assertTrue(
                "Glass Morphism blur must stay in the 26–28dp diffusion band",
                glass.blurRadius.value in 26f..28f,
            )
            assertTrue(
                "Glass Morphism vibrancy must stay in the 1.20–1.35x band",
                glass.saturation in 1.2f..1.35f,
            )
            assertTrue(
                "frosted micro-grain must stay a subtle 1.5–2.5% texture",
                glass.noiseAlpha in 0.015f..0.025f,
            )
            assertTrue("highlight gain may only dim the backdrop", glass.highlightGain in 0.8f..1f)
        }
    }

    @Test
    fun `glass morphism tokens follow the mode separated spec`() {
        val light = AuraGlassDefaults.Light
        val dark = AuraGlassDefaults.Dark
        assertEquals(26f, light.blurRadius.value, 0.001f)
        assertEquals(28f, dark.blurRadius.value, 0.001f)
        assertEquals(1.35f, light.saturation, 0.001f)
        assertEquals(1.22f, dark.saturation, 0.001f)
        assertEquals(0.018f, light.noiseAlpha, 0.0001f)
        assertEquals(0.022f, dark.noiseAlpha, 0.0001f)
        assertTrue("dark glass clamps highlights", dark.highlightGain < light.highlightGain)
    }

    @Test
    fun `glass tints keep primary content at WCAG AA over worst-case backdrops`() {
        fun contrast(a: Color, b: Color): Float {
            val lighter = maxOf(a.luminance(), b.luminance())
            val darker = minOf(a.luminance(), b.luminance())
            return (lighter + 0.05f) / (darker + 0.05f)
        }

        listOf(
            "Light" to (AuraGlassDefaults.Light to AuraColorSchemes.Light.content.primary),
            "Dark" to (AuraGlassDefaults.Dark to AuraColorSchemes.Dark.content.primary),
        ).forEach { (mode, pair) ->
            val (glass, text) = pair
            listOf("frostedTint" to glass.frostedTint, "liquidTint" to glass.liquidTint).forEach { (name, tint) ->
                // Blur and saturation leave a uniform backdrop unchanged, so pure white / black are the extremes.
                listOf("white" to Color.White, "black" to Color.Black).forEach { (backdropName, backdrop) ->
                    val ratio = contrast(text, tint.compositeOver(backdrop))
                    assertTrue(
                        "$mode $name over $backdropName backdrop: content.primary contrast $ratio must be >= 4.5",
                        ratio >= 4.5f,
                    )
                }
            }
        }
    }
}

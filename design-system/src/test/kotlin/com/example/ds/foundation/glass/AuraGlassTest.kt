package com.example.ds.foundation.glass

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import com.example.ds.foundation.color.AuraColorSchemes
import com.example.ds.foundation.theme.AuraColorMode
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
        }
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

@file:Suppress("DEPRECATION")

package com.example.ds.foundation.theme

import com.example.ds.component.GlassStyle
import com.example.ds.component.toMaterial
import org.junit.Assert.assertEquals
import org.junit.Test

class MaterialResolutionTest {
    @Test
    fun `resolveEffectiveMaterial enforces full SDK and reduceTransparency matrix`() {
        data class Case(
            val requested: AuraMaterial,
            val sdkInt: Int,
            val reduceTransparency: Boolean,
            val expected: AuraMaterial,
        )

        val matrix = listOf(
            // reduceTransparency = true always resolves to Solid across all SDKs
            Case(AuraMaterial.Solid, 26, true, AuraMaterial.Solid),
            Case(AuraMaterial.Glass, 26, true, AuraMaterial.Solid),
            Case(AuraMaterial.LiquidGlass, 26, true, AuraMaterial.Solid),
            Case(AuraMaterial.Solid, 31, true, AuraMaterial.Solid),
            Case(AuraMaterial.Glass, 31, true, AuraMaterial.Solid),
            Case(AuraMaterial.LiquidGlass, 31, true, AuraMaterial.Solid),
            Case(AuraMaterial.Solid, 33, true, AuraMaterial.Solid),
            Case(AuraMaterial.Glass, 33, true, AuraMaterial.Solid),
            Case(AuraMaterial.LiquidGlass, 33, true, AuraMaterial.Solid),
            Case(AuraMaterial.LiquidGlass, 35, true, AuraMaterial.Solid),

            // Pre-RenderEffect Android (API 26..30): Glass/LiquidGlass degrade to Solid
            Case(AuraMaterial.Solid, 26, false, AuraMaterial.Solid),
            Case(AuraMaterial.Glass, 26, false, AuraMaterial.Solid),
            Case(AuraMaterial.LiquidGlass, 26, false, AuraMaterial.Solid),
            Case(AuraMaterial.Solid, 30, false, AuraMaterial.Solid),
            Case(AuraMaterial.Glass, 30, false, AuraMaterial.Solid),
            Case(AuraMaterial.LiquidGlass, 30, false, AuraMaterial.Solid),

            // API 31..32 (RenderEffect without AGSL RuntimeShader): LiquidGlass degrades to Glass
            Case(AuraMaterial.Solid, 31, false, AuraMaterial.Solid),
            Case(AuraMaterial.Glass, 31, false, AuraMaterial.Glass),
            Case(AuraMaterial.LiquidGlass, 31, false, AuraMaterial.Glass),
            Case(AuraMaterial.Solid, 32, false, AuraMaterial.Solid),
            Case(AuraMaterial.Glass, 32, false, AuraMaterial.Glass),
            Case(AuraMaterial.LiquidGlass, 32, false, AuraMaterial.Glass),

            // API 33+ (AGSL RuntimeShader + RenderEffect): full material fidelity
            Case(AuraMaterial.Solid, 33, false, AuraMaterial.Solid),
            Case(AuraMaterial.Glass, 33, false, AuraMaterial.Glass),
            Case(AuraMaterial.LiquidGlass, 33, false, AuraMaterial.LiquidGlass),
            Case(AuraMaterial.Solid, 35, false, AuraMaterial.Solid),
            Case(AuraMaterial.Glass, 35, false, AuraMaterial.Glass),
            Case(AuraMaterial.LiquidGlass, 35, false, AuraMaterial.LiquidGlass),
        )

        matrix.forEach { case ->
            val actual = resolveEffectiveMaterial(
                requested = case.requested,
                sdkInt = case.sdkInt,
                reduceTransparency = case.reduceTransparency,
            )
            assertEquals(
                "Mismatch for $case",
                case.expected,
                actual,
            )
        }
    }

    @Test
    fun `theme ceiling caps the requested material before SDK resolution`() {
        fun resolve(requested: AuraMaterial, ceiling: AuraMaterial, sdkInt: Int = 35) =
            resolveEffectiveMaterial(requested = requested, sdkInt = sdkInt, ceiling = ceiling)

        assertEquals(AuraMaterial.LiquidGlass, resolve(AuraMaterial.LiquidGlass, AuraMaterial.LiquidGlass))
        assertEquals(AuraMaterial.Glass, resolve(AuraMaterial.LiquidGlass, AuraMaterial.Glass))
        assertEquals(AuraMaterial.Solid, resolve(AuraMaterial.LiquidGlass, AuraMaterial.Solid))
        assertEquals(AuraMaterial.Solid, resolve(AuraMaterial.Glass, AuraMaterial.Solid))
        // A ceiling never raises a lower request
        assertEquals(AuraMaterial.Glass, resolve(AuraMaterial.Glass, AuraMaterial.LiquidGlass))
        assertEquals(AuraMaterial.Solid, resolve(AuraMaterial.Solid, AuraMaterial.LiquidGlass))
        // Ceiling and SDK downgrade compose
        assertEquals(AuraMaterial.Solid, resolve(AuraMaterial.LiquidGlass, AuraMaterial.Glass, sdkInt = 30))
    }

    @Test
    @Suppress("DEPRECATION")
    fun `deprecated GlassStyle maps cleanly to AuraMaterial`() {
        assertEquals(AuraMaterial.Glass, GlassStyle.FROSTED.toMaterial())
        assertEquals(AuraMaterial.LiquidGlass, GlassStyle.LIQUID.toMaterial())
    }
}

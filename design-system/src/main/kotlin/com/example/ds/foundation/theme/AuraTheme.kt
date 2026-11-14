package com.example.ds.foundation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.ds.foundation.color.AuraColorSchemes
import com.example.ds.foundation.color.AuraColors

/**
 * Color axis for [AuraTheme].
 */
enum class AuraColorMode {
    Light,
    Dark,
}

/**
 * Surface material axis for [AuraTheme].
 *
 * Skeleton starts with [Solid]. Future releases will extend this axis with
 * `Glass` (blur + tint) and `LiquidGlass` (blur + refraction + physics) without
 * altering semantic color roles.
 */
enum class AuraMaterial {
    Solid,
}

private val LocalAuraColors = staticCompositionLocalOf { AuraColorSchemes.Light }
private val LocalAuraColorMode = staticCompositionLocalOf { AuraColorMode.Light }
private val LocalAuraMaterial = staticCompositionLocalOf { AuraMaterial.Solid }

/**
 * Root theme provider for the Aura design system.
 *
 * Uses two independent axes ([colorMode] and [material]) so surface treatments
 * can evolve independently of Light/Dark color semantics.
 */
@Composable
fun AuraTheme(
    colorMode: AuraColorMode = if (isSystemInDarkTheme()) AuraColorMode.Dark else AuraColorMode.Light,
    material: AuraMaterial = AuraMaterial.Solid,
    content: @Composable () -> Unit,
) {
    val colors = when (colorMode) {
        AuraColorMode.Light -> AuraColorSchemes.Light
        AuraColorMode.Dark -> AuraColorSchemes.Dark
    }

    CompositionLocalProvider(
        LocalAuraColors provides colors,
        LocalAuraColorMode provides colorMode,
        LocalAuraMaterial provides material,
        content = content,
    )
}

object AuraTheme {
    val colors: AuraColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraColors.current

    val colorMode: AuraColorMode
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraColorMode.current

    val material: AuraMaterial
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraMaterial.current
}

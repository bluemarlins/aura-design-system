package com.example.ds.foundation.theme

import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.example.ds.component.LocalAuraContentColor
import com.example.ds.component.LocalAuraTextStyle
import com.example.ds.foundation.color.AuraColorSchemes
import com.example.ds.foundation.color.AuraColors
import com.example.ds.foundation.elevation.AuraElevation
import com.example.ds.foundation.elevation.AuraElevationDefaults
import com.example.ds.foundation.layout.AuraLayout
import com.example.ds.foundation.layout.AuraLayoutDefaults
import com.example.ds.foundation.layout.AuraWindowInfo
import com.example.ds.foundation.layout.rememberAuraWindowInfo
import com.example.ds.foundation.motion.AuraMotion
import com.example.ds.foundation.motion.AuraMotionDefaults
import com.example.ds.foundation.motion.AuraMotionProfile
import com.example.ds.foundation.motion.rememberAuraReducedMotion
import com.example.ds.foundation.shape.AuraShapes
import com.example.ds.foundation.shape.AuraShapesDefaults
import com.example.ds.foundation.typography.AuraTypography
import com.example.ds.foundation.typography.AuraTypographyDefaults

/**
 * Color axis for [AuraTheme].
 */
enum class AuraColorMode {
    Light,
    Dark,
}

/**
 * Aura-original optical surface materiality axis for [AuraTheme].
 *
 * - [Solid]: High-legibility opaque surface (default for Layer 0/1 content and accessibility fallbacks).
 * - [Glass]: Frosted translucent backdrop blur with adaptive surface tint and specular stroke (Layer 2/3).
 * - [LiquidGlass]: AGSL edge bevel refraction, chromatic dispersion, and fluid physics (Layer 2/3 hero chrome).
 *
 * Defined independently of Android Material Design (`material3`).
 */
enum class AuraMaterial {
    Solid,
    Glass,
    LiquidGlass,
}

/**
 * Pure deterministic resolver determining the active [AuraMaterial] for the current runtime environment.
 *
 * - Forces [AuraMaterial.Solid] whenever [reduceTransparency] is `true`.
 * - Requires API 33+ for [AuraMaterial.LiquidGlass] AGSL shader refraction (degrades to [AuraMaterial.Glass] on API 31–32).
 * - Requires API 31+ for [AuraMaterial.Glass] `RenderEffect` backdrop blur (degrades to [AuraMaterial.Solid] on API 26–30).
 */
fun resolveEffectiveMaterial(
    requested: AuraMaterial,
    sdkInt: Int = Build.VERSION.SDK_INT,
    reduceTransparency: Boolean = false,
): AuraMaterial = when {
    reduceTransparency -> AuraMaterial.Solid
    requested == AuraMaterial.LiquidGlass && sdkInt >= Build.VERSION_CODES.TIRAMISU -> AuraMaterial.LiquidGlass
    requested == AuraMaterial.LiquidGlass && sdkInt >= Build.VERSION_CODES.S -> AuraMaterial.Glass
    requested == AuraMaterial.Glass && sdkInt >= Build.VERSION_CODES.S -> AuraMaterial.Glass
    else -> AuraMaterial.Solid
}

/**
 * Evaluates whether translucent glass effects should fall back to [AuraMaterial.Solid].
 *
 * Returns `true` when Android battery saver (`PowerManager.isPowerSaveMode`) or high-contrast text
 * accessibility (`high_text_contrast_enabled`) is active.
 */
@Composable
fun rememberAuraReduceTransparency(): Boolean {
    if (LocalInspectionMode.current) return false
    val context = LocalContext.current
    return remember(context) {
        val powerSave = runCatching {
            val pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
            pm?.isPowerSaveMode == true
        }.getOrDefault(false)
        val highContrast = runCatching {
            Settings.Secure.getInt(context.contentResolver, "high_text_contrast_enabled", 0) == 1
        }.getOrDefault(false)
        powerSave || highContrast
    }
}

private val LocalAuraColors = staticCompositionLocalOf { AuraColorSchemes.Light }
private val LocalAuraMotion = staticCompositionLocalOf { AuraMotionDefaults.Standard }
private val LocalAuraElevation = staticCompositionLocalOf { AuraElevationDefaults.Standard }
private val LocalAuraColorMode = staticCompositionLocalOf { AuraColorMode.Light }
private val LocalAuraMaterial = staticCompositionLocalOf { AuraMaterial.Solid }
private val LocalAuraReduceTransparency = staticCompositionLocalOf { false }
private val LocalAuraWindowInfo = staticCompositionLocalOf {
    AuraWindowInfo.fromDimensions(smallestWidthDp = 360, widthDp = 360, heightDp = 800)
}
private val LocalAuraLayout = staticCompositionLocalOf { AuraLayoutDefaults.Compact }
private val LocalAuraTypography = staticCompositionLocalOf { AuraTypographyDefaults.Standard }
private val LocalAuraShapes = staticCompositionLocalOf { AuraShapesDefaults.Standard }

/**
 * Root theme provider for the Aura design system.
 *
 * Uses two independent styling axes ([colorMode] and [material]) alongside dual-track
 * [motionProfile] / [motion] physics, Z-axis [elevation] ladder tokens, responsive
 * [windowInfo]/[layout] breakpoints, and [typography] roles.
 */
@Composable
fun AuraTheme(
    colorMode: AuraColorMode = if (isSystemInDarkTheme()) AuraColorMode.Dark else AuraColorMode.Light,
    material: AuraMaterial = AuraMaterial.Solid,
    motionProfile: AuraMotionProfile = AuraMotionProfile.Standard,
    reduceTransparency: Boolean = rememberAuraReduceTransparency(),
    motion: AuraMotion = AuraMotionDefaults.forProfile(motionProfile),
    elevation: AuraElevation = AuraElevationDefaults.Standard,
    windowInfo: AuraWindowInfo = rememberAuraWindowInfo(),
    layout: AuraLayout = AuraLayoutDefaults.fromWindowInfo(windowInfo),
    typography: AuraTypography = AuraTypographyDefaults.scaled(layout.typographyScale),
    shapes: AuraShapes = AuraShapesDefaults.Standard,
    content: @Composable () -> Unit,
) {
    val colors = when (colorMode) {
        AuraColorMode.Light -> AuraColorSchemes.Light
        AuraColorMode.Dark -> AuraColorSchemes.Dark
    }
    val systemReducedMotion = rememberAuraReducedMotion()
    val effectiveMotion = if (systemReducedMotion && !motion.reduceMotion) {
        motion.copy(reduceMotion = true)
    } else {
        motion
    }

    CompositionLocalProvider(
        LocalAuraColors provides colors,
        LocalAuraMotion provides effectiveMotion,
        LocalAuraElevation provides elevation,
        LocalAuraColorMode provides colorMode,
        LocalAuraMaterial provides material,
        LocalAuraReduceTransparency provides reduceTransparency,
        LocalAuraWindowInfo provides windowInfo,
        LocalAuraLayout provides layout,
        LocalAuraTypography provides typography,
        LocalAuraShapes provides shapes,
        LocalAuraContentColor provides colors.content.primary,
        LocalAuraTextStyle provides typography.bodyMedium,
        content = content,
    )
}

object AuraTheme {
    val colors: AuraColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraColors.current

    val typography: AuraTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraTypography.current

    val shapes: AuraShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraShapes.current

    val motion: AuraMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraMotion.current

    val elevation: AuraElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraElevation.current

    val window: AuraWindowInfo
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraWindowInfo.current

    val layout: AuraLayout
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraLayout.current

    val colorMode: AuraColorMode
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraColorMode.current

    val material: AuraMaterial
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraMaterial.current

    val reduceTransparency: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraReduceTransparency.current

    @Composable
    @ReadOnlyComposable
    fun effectiveMaterial(requested: AuraMaterial = material): AuraMaterial =
        resolveEffectiveMaterial(
            requested = requested,
            reduceTransparency = reduceTransparency,
        )
}

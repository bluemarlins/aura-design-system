package com.example.ds.preview.ui

import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraBadge
import com.example.ds.component.AuraBadgeSize
import com.example.ds.component.AuraBadgeVariant
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraSwitch
import com.example.ds.component.AuraText
import com.example.ds.component.GlassBackdropState
import com.example.ds.component.GlassSurface
import com.example.ds.component.fluidPressScale
import com.example.ds.component.glassBackdropSource
import com.example.ds.component.rememberGlassBackdropState
import com.example.ds.foundation.glass.AuraGlassDefaults
import com.example.ds.foundation.theme.AuraColorMode
import com.example.ds.foundation.theme.AuraMaterial
import com.example.ds.foundation.theme.AuraTheme
import com.example.ds.foundation.theme.resolveEffectiveMaterial
import kotlin.math.roundToInt

private data class MaterialPrinciple(
    val material: AuraMaterial,
    val title: String,
    val optics: String,
    val role: String,
    val limit: String,
    val fallback: String,
)

private val MaterialPrinciples = listOf(
    MaterialPrinciple(
        material = AuraMaterial.Solid,
        title = "Solid",
        optics = "Near-opaque surface.floating fill (98%) with a crisp 1dp border. No blur; only a faint trace of strong shapes behind it remains.",
        role = "Long-lived content, forms, cards, and every accessibility fallback.",
        limit = "Unlimited.",
        fallback = "None needed. This is the fallback.",
    ),
    MaterialPrinciple(
        material = AuraMaterial.Glass,
        title = "Glass Morphism",
        optics = "Live backdrop blur with saturation boost, a translucent tint wash, and a diagonal specular stroke.",
        role = "Floating navigation chrome, search capsules, transient sheets and dialogs.",
        limit = "At most 3 live-blur surfaces per viewport.",
        fallback = "Solid below API 31, under Reduced Transparency, or under a Solid theme ceiling.",
    ),
    MaterialPrinciple(
        material = AuraMaterial.LiquidGlass,
        title = "Liquid Glass",
        optics = "Convex edge bevel that refracts the backdrop, chromatic dispersion at the rim, inner glow, and a lighter blur.",
        role = "One interactive hero surface, such as the bottom island or the active pill.",
        limit = "At most 1 per screen.",
        fallback = "Glass on API 31-32 or under a Glass theme ceiling. Solid wherever Glass falls back.",
    ),
)

/**
 * Side-by-side comparison of the two theme axes: color mode (Light / Dark) and
 * surface material (Solid / Glass / LiquidGlass), over the same moving backdrop.
 * Three materials in two color modes give the six specimens shown.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialComparisonPreviewScreen(onBack: () -> Unit) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography

    var reduceTransparency by remember { mutableStateOf(false) }
    var animateBackdrop by remember { mutableStateOf(true) }

    val transition = rememberInfiniteTransition(label = "MaterialComparisonBackdrop")
    val animatedPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "MaterialComparisonBackdropPhase",
    )
    val phase: () -> Float = { if (animateBackdrop) animatedPhase else 0.18f }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(text = "Material Comparison", style = t.titleLarge)
                },
                navigationIcon = {
                    AuraIconButton(
                        onClick = onBack,
                        variant = AuraIconButtonVariant.Ghost,
                        contentDescription = "Back",
                    ) {
                        AuraIcon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = c.background.primary,
                    titleContentColor = c.content.primary,
                    navigationIconContentColor = c.content.primary,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AuraTheme.layout.screenMarginHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            AuraCard(
                modifier = Modifier.fillMaxWidth(),
                variant = AuraCardVariant.Outlined,
                padding = AuraCardPadding.Medium,
            ) {
                AuraText(text = "Conditions", style = t.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                AuraText(
                    text = "Three materials in Light and Dark: six specimens over the same backdrop. Tap a surface to feel the press spring.",
                    style = t.bodySmall,
                    color = c.content.secondary,
                )
                Spacer(modifier = Modifier.height(12.dp))
                AuraSwitch(
                    checked = reduceTransparency,
                    onCheckedChange = { reduceTransparency = it },
                    label = "Simulate Reduce Transparency",
                    description = "Battery saver or accessibility settings force every surface to Solid.",
                )
                AuraSwitch(
                    checked = animateBackdrop,
                    onCheckedChange = { animateBackdrop = it },
                    label = "Animate backdrop",
                    description = "Moving shapes show how each material samples what is behind it.",
                )
            }

            MaterialPrinciples.forEach { principle ->
                MaterialComparisonRow(
                    principle = principle,
                    reduceTransparency = reduceTransparency,
                    phase = phase,
                )
            }

            ColorModeTokenCard()
        }
    }
}

@Composable
private fun MaterialComparisonRow(
    principle: MaterialPrinciple,
    reduceTransparency: Boolean,
    phase: () -> Float,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val effective = resolveEffectiveMaterial(
        requested = principle.material,
        reduceTransparency = reduceTransparency,
    )

    AuraCard(
        modifier = Modifier.fillMaxWidth(),
        variant = AuraCardVariant.Outlined,
        padding = AuraCardPadding.Medium,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AuraText(text = principle.title, style = t.titleMedium)
            AuraBadge(
                label = if (effective == principle.material) {
                    "Renders ${effective.name}"
                } else {
                    "Falls back to ${effective.name}"
                },
                variant = if (effective == principle.material) AuraBadgeVariant.Brand else AuraBadgeVariant.Warning,
                size = AuraBadgeSize.Small,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            listOf(AuraColorMode.Light, AuraColorMode.Dark).forEach { colorMode ->
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    MaterialSpecimen(
                        colorMode = colorMode,
                        material = principle.material,
                        reduceTransparency = reduceTransparency,
                        phase = phase,
                    )
                    AuraText(
                        text = specimenReadout(colorMode = colorMode, effective = effective),
                        style = t.monoSmall,
                        color = c.content.secondary,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        PrincipleLine(label = "Optics", value = principle.optics)
        PrincipleLine(label = "Role", value = principle.role)
        PrincipleLine(label = "Limit", value = principle.limit)
        PrincipleLine(label = "Fallback", value = principle.fallback)
    }
}

@Composable
private fun PrincipleLine(label: String, value: String) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AuraText(
            text = label,
            style = t.labelMedium,
            color = c.content.secondary,
            modifier = Modifier.weight(0.26f),
        )
        AuraText(
            text = value,
            style = t.bodySmall,
            modifier = Modifier.weight(0.74f),
        )
    }
}

/** One surface of [material] in [colorMode], floating over its own recorded backdrop. */
@Composable
private fun MaterialSpecimen(
    colorMode: AuraColorMode,
    material: AuraMaterial,
    reduceTransparency: Boolean,
    phase: () -> Float,
) {
    AuraTheme(
        colorMode = colorMode,
        reduceTransparency = reduceTransparency,
    ) {
        val c = AuraTheme.colors
        val t = AuraTheme.typography
        val backdrop = rememberGlassBackdropState()
        val interactionSource = remember { MutableInteractionSource() }
        val stageShape = RoundedCornerShape(20.dp)
        val surfaceShape = RoundedCornerShape(18.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp)
                .clip(stageShape),
        ) {
            SpecimenBackdrop(
                state = backdrop,
                phase = phase,
                modifier = Modifier.fillMaxSize(),
            )
            GlassSurface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(12.dp)
                    .fluidPressScale(interactionSource)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {},
                    ),
                material = material,
                shape = surfaceShape,
                cornerRadiusHint = 18.dp,
                backdropState = backdrop,
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                    AuraText(
                        text = colorMode.name,
                        style = t.titleSmall,
                        color = c.content.primary,
                    )
                    AuraText(
                        text = "Primary content",
                        style = t.bodySmall,
                        color = c.content.primary,
                    )
                    AuraText(
                        text = "Secondary content",
                        style = t.bodySmall,
                        color = c.content.secondary,
                    )
                }
            }
        }
    }
}

/**
 * High-contrast moving backdrop: a color gradient, fine stripes (shows blur), and pure white and
 * black discs (the worst cases for text contrast on a translucent surface).
 */
@Composable
private fun SpecimenBackdrop(
    state: GlassBackdropState,
    phase: () -> Float,
    modifier: Modifier = Modifier,
) {
    val c = AuraTheme.colors
    val gradient = listOf(c.brand.primary, c.status.warning.solid, c.status.success.solid)

    LaunchedEffect(state) {
        snapshotFlow { phase() }.collect { state.invalidateBackdrop() }
    }

    Box(modifier = modifier.glassBackdropSource(state)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val p = phase()
            drawRect(brush = Brush.linearGradient(colors = gradient))

            val stripeWidth = 3.dp.toPx()
            val stripeGap = 14.dp.toPx()
            val stripeShift = p * stripeGap * 4f
            var x = -stripeGap * 4f + stripeShift
            while (x < size.width) {
                drawRect(
                    color = Color.White.copy(alpha = 0.55f),
                    topLeft = Offset(x, 0f),
                    size = Size(stripeWidth, size.height),
                )
                x += stripeGap
            }

            val radius = size.height * 0.26f
            val travel = size.width + radius * 2f
            fun discX(offset: Float): Float = ((p + offset) % 1f) * travel - radius
            drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(discX(0f), size.height * 0.34f),
            )
            drawCircle(
                color = Color.Black,
                radius = radius,
                center = Offset(discX(0.5f), size.height * 0.66f),
            )
        }
    }
}

private fun specimenReadout(colorMode: AuraColorMode, effective: AuraMaterial): String {
    val glass = AuraGlassDefaults.forColorMode(colorMode)
    return when (effective) {
        AuraMaterial.Solid -> "fill ${percent(glass.solidFallbackAlpha)}\nno blur"
        AuraMaterial.Glass -> "tint ${percent(glass.frostedTint.alpha)}\nblur ${glass.blurRadius.value.roundToInt()}dp"
        AuraMaterial.LiquidGlass -> "tint ${percent(glass.liquidTint.alpha)}\nblur ${glass.liquidBlurRadius.value.roundToInt()}dp + lens"
    }
}

private fun percent(fraction: Float): String = "${(fraction * 100f).roundToInt()}%"

/** Light vs Dark values of the color-mode-dependent glass tokens, read live from [AuraGlassDefaults]. */
@Composable
private fun ColorModeTokenCard() {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val light = AuraGlassDefaults.Light
    val dark = AuraGlassDefaults.Dark

    AuraCard(
        modifier = Modifier.fillMaxWidth(),
        variant = AuraCardVariant.Outlined,
        padding = AuraCardPadding.Medium,
    ) {
        AuraText(text = "Light vs Dark", style = t.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        AuraText(
            text = "Color mode and material are independent axes. Blur, saturation, and lens geometry are shared; " +
                "only the tint and the specular stroke change. Light washes with white, Dark with charcoal, and both " +
                "keep primary content at 4.5:1 over any backdrop.",
            style = t.bodySmall,
            color = c.content.secondary,
        )
        Spacer(modifier = Modifier.height(12.dp))
        TokenRow(label = "Token", light = "Light", dark = "Dark", header = true)
        TokenRow(
            label = "frostedTint",
            light = "white ${percent(light.frostedTint.alpha)}",
            dark = "charcoal ${percent(dark.frostedTint.alpha)}",
        )
        TokenRow(
            label = "liquidTint",
            light = "white ${percent(light.liquidTint.alpha)}",
            dark = "charcoal ${percent(dark.liquidTint.alpha)}",
        )
        TokenRow(
            label = "stroke start",
            light = percent(light.strokeStartAlpha),
            dark = percent(dark.strokeStartAlpha),
        )
        TokenRow(
            label = "stroke end",
            light = percent(light.strokeEndAlpha),
            dark = percent(dark.strokeEndAlpha),
        )
        Spacer(modifier = Modifier.height(8.dp))
        AuraText(
            text = "This device: API ${Build.VERSION.SDK_INT}. Glass needs API 31, LiquidGlass needs API 33.",
            style = t.monoSmall,
            color = c.content.secondary,
        )
    }
}

@Composable
private fun TokenRow(
    label: String,
    light: String,
    dark: String,
    header: Boolean = false,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val style = if (header) t.labelMedium else t.monoSmall
    val color = if (header) c.content.secondary else c.content.primary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
    ) {
        AuraText(text = label, style = style, color = color, modifier = Modifier.weight(1f))
        AuraText(text = light, style = style, color = color, modifier = Modifier.weight(1f))
        AuraText(text = dark, style = style, color = color, modifier = Modifier.weight(1f))
    }
}

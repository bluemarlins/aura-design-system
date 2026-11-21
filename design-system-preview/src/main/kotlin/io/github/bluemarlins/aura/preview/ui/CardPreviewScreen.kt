package io.github.bluemarlins.aura.preview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.component.AuraAdaptiveTwoPane
import io.github.bluemarlins.aura.component.AuraCard
import io.github.bluemarlins.aura.component.AuraCardPadding
import io.github.bluemarlins.aura.component.AuraCardVariant
import io.github.bluemarlins.aura.component.AuraChipButton
import io.github.bluemarlins.aura.component.AuraChipSize
import io.github.bluemarlins.aura.component.AuraChipVariant
import io.github.bluemarlins.aura.component.AuraIcon
import io.github.bluemarlins.aura.component.AuraIconButton
import io.github.bluemarlins.aura.component.AuraIconButtonVariant
import io.github.bluemarlins.aura.component.AuraScaffold
import io.github.bluemarlins.aura.component.AuraSwitch
import io.github.bluemarlins.aura.component.AuraSwitchSize
import io.github.bluemarlins.aura.component.AuraText
import io.github.bluemarlins.aura.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CardPreviewScreen(onBack: () -> Unit) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout
    val window = AuraTheme.window

    var selectedVariant by remember { mutableStateOf(AuraCardVariant.Outlined) }
    var selectedPadding by remember { mutableStateOf(AuraCardPadding.Medium) }
    var interactive by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }
    var tapCount by remember { mutableIntStateOf(0) }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(text = "AuraCard", style = t.titleLarge)
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
        AuraAdaptiveTwoPane(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            primaryPane = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(layout.sectionSpacing),
                ) {
                    AuraText(text = "Interactive Specimen", style = t.titleMedium)

                    val useGlassStage = selectedVariant == AuraCardVariant.Glass
                    Box(
                        modifier = if (useGlassStage) {
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(c.surface.inverse, c.brand.primaryPressed),
                                    ),
                                )
                                .padding(16.dp)
                        } else {
                            Modifier.fillMaxWidth()
                        },
                    ) {
                        AuraCard(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = if (interactive) {
                                {
                                    tapCount++
                                    selected = !selected
                                }
                            } else {
                                null
                            },
                            variant = selectedVariant,
                            padding = selectedPadding,
                            enabled = enabled,
                            selected = selected,
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AuraText(
                                    text = "${selectedVariant.name} · ${selectedPadding.name}",
                                    style = t.titleSmall,
                                )
                                AuraText(
                                    text = "Taps: $tapCount",
                                    style = t.monoSmall,
                                    color = c.brand.primary,
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            AuraText(
                                text = if (interactive) {
                                    "Interactive mode uses pressedScaleLarge (0.975f) + Role.Button semantics."
                                } else {
                                    "Passive mode stays visually calm with zero press scale or tap target."
                                },
                                style = t.bodySmall,
                                color = c.content.secondary,
                            )
                        }
                    }

                    AuraText(text = "Surface & Emphasis Matrix", style = t.titleMedium)

                    AuraCard(variant = AuraCardVariant.Elevated, padding = AuraCardPadding.Medium) {
                        AuraText(text = "My Finance · Net Worth (Elevated)", style = t.labelLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        AuraText(text = "₩ 148,250,000", style = t.headlineSmall)
                        AuraText(
                            text = "+4.2% vs last month · surface.raised + 4.dp elevation",
                            style = t.bodySmall,
                            color = c.status.success.solid,
                        )
                    }

                    AuraCard(variant = AuraCardVariant.Outlined, padding = AuraCardPadding.Medium, onClick = {}) {
                        AuraText(text = "My Drink Diary · 2019 Barolo DOCG (Outlined)", style = t.labelLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        AuraText(
                            text = "14.0% ABV · Dried rose, tart cherry, firm tannin structure.",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                    }

                    AuraCard(variant = AuraCardVariant.Filled, padding = AuraCardPadding.Compact) {
                        AuraText(text = "My Planner · Quiet Reflection Callout (Filled)", style = t.labelMedium)
                        AuraText(
                            text = "Soft surface.sunken separation avoids visual heaviness next to diary forms.",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(c.surface.inverse, c.brand.primaryPressed),
                                ),
                            )
                            .padding(16.dp),
                    ) {
                        AuraCard(
                            variant = AuraCardVariant.Glass,
                            padding = AuraCardPadding.Medium,
                            onClick = {},
                        ) {
                            AuraText(text = "Home Launcher · Frosted Glass Widget", style = t.labelLarge)
                            AuraText(
                                text = "surface.floating over custom gradient or photo wallpapers.",
                                style = t.bodySmall,
                                color = c.content.secondary,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            },
            secondaryPane = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    AuraText(text = "Variant", style = t.titleSmall)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        AuraCardVariant.entries.forEach { variant ->
                            AuraChipButton(
                                label = variant.name,
                                selected = selectedVariant == variant,
                                variant = AuraChipVariant.Filter,
                                size = AuraChipSize.Small,
                                onClick = { selectedVariant = variant },
                            )
                        }
                    }

                    AuraText(text = "Padding Preset", style = t.titleSmall)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        AuraCardPadding.entries.forEach { padding ->
                            AuraChipButton(
                                label = padding.name,
                                selected = selectedPadding == padding,
                                variant = AuraChipVariant.Filter,
                                size = AuraChipSize.Small,
                                onClick = { selectedPadding = padding },
                            )
                        }
                    }

                    AuraSwitch(
                        checked = interactive,
                        onCheckedChange = { interactive = it },
                        label = "Interactive (onClick != null)",
                        size = AuraSwitchSize.Small,
                    )
                    AuraSwitch(
                        checked = selected,
                        onCheckedChange = { selected = it },
                        label = "Selected Highlight",
                        size = AuraSwitchSize.Small,
                    )
                    AuraSwitch(
                        checked = enabled,
                        onCheckedChange = { enabled = it },
                        label = "Enabled",
                        size = AuraSwitchSize.Small,
                    )

                    AuraCard(variant = AuraCardVariant.Filled, padding = AuraCardPadding.Compact) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            AuraText(text = "Design Rules Check", style = t.labelLarge)
                            AuraText(
                                text = "Profile: ${window.widthClass.name} / ${window.displayRatio.name} (${window.widthDp}×${window.heightDp} dp)",
                                style = t.bodySmall,
                                color = c.content.secondary,
                            )
                            AuraText(
                                text = "CARD-RULE-004: Interactive cards compress gently at 0.975f while passive containers remain still.",
                                style = t.bodySmall,
                                color = c.content.secondary,
                            )
                        }
                    }
                }
            },
        )
    }
}

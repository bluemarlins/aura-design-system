package com.example.ds.preview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.ViewInAr
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
import com.example.ds.component.AuraAdaptiveTwoPane
import com.example.ds.component.AuraChipButton
import com.example.ds.component.AuraChipSize
import com.example.ds.component.AuraFab
import com.example.ds.component.AuraFabSize
import com.example.ds.component.AuraFabVariant
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraSwitch
import com.example.ds.component.AuraSwitchSize
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme

private enum class FabPresetMode(val title: String, val ctaLabel: String) {
    Planner("My Planner", "New Diary"),
    Finance("My Finance", "Add Asset"),
    DrinkDiary("My Drink Diary", "Log Bottle"),
    Launcher("Home Launcher", "3D Widget"),
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FabPreviewScreen(onBack: () -> Unit) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout
    val window = AuraTheme.window

    var selectedVariant by remember { mutableStateOf(AuraFabVariant.Primary) }
    var selectedSize by remember { mutableStateOf(AuraFabSize.Regular) }
    var selectedPreset by remember { mutableStateOf(FabPresetMode.Planner) }
    var expanded by remember { mutableStateOf(true) }
    var showText by remember { mutableStateOf(true) }
    var enabled by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }
    var photoBackdrop by remember { mutableStateOf(false) }
    var creationCount by remember { mutableIntStateOf(0) }

    val activeIcon = when (selectedPreset) {
        FabPresetMode.Planner -> Icons.Filled.EditNote
        FabPresetMode.Finance -> Icons.Filled.AccountBalanceWallet
        FabPresetMode.DrinkDiary -> Icons.Filled.LocalBar
        FabPresetMode.Launcher -> Icons.Filled.ViewInAr
    }
    val activeText = if (showText) selectedPreset.ctaLabel else null

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = "AuraFab",
                        style = t.titleLarge,
                    )
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
        floatingActionButton = {
            AuraFab(
                onClick = { creationCount++ },
                text = activeText,
                expanded = expanded,
                variant = selectedVariant,
                size = selectedSize,
                enabled = enabled,
                loading = loading,
                contentDescription = selectedPreset.ctaLabel,
            ) {
                AuraIcon(imageVector = activeIcon, contentDescription = null)
            }
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

                    val useGradientStage = photoBackdrop || selectedVariant == AuraFabVariant.Glass
                    val stageModifier = if (useGradientStage) {
                        Modifier.background(
                            Brush.linearGradient(
                                colors = listOf(c.surface.inverse, c.brand.primaryPressed, c.status.info.solid),
                            ),
                        )
                    } else {
                        Modifier.background(c.surface.raised)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(176.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, c.border.subtle, RoundedCornerShape(16.dp))
                            .then(stageModifier),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            AuraFab(
                                onClick = { creationCount++ },
                                text = activeText,
                                expanded = expanded,
                                variant = selectedVariant,
                                size = selectedSize,
                                enabled = enabled,
                                loading = loading,
                                contentDescription = selectedPreset.ctaLabel,
                            ) {
                                AuraIcon(imageVector = activeIcon, contentDescription = null)
                            }
                            AuraText(
                                text = "${selectedVariant.name} · ${selectedSize.name} · Creations: $creationCount",
                                style = t.labelMedium,
                                color = if (useGradientStage) c.brand.onPrimary else c.content.secondary,
                            )
                        }
                    }

                    AuraText(text = "All Variants Comparison", style = t.titleMedium)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(c.surface.raised)
                            .border(1.dp, c.border.subtle, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            AuraFabVariant.entries.forEach { variant ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    AuraFab(
                                        onClick = { selectedVariant = variant },
                                        variant = variant,
                                        size = AuraFabSize.Regular,
                                        contentDescription = "${variant.name} FAB",
                                    ) {
                                        AuraIcon(imageVector = Icons.Filled.Add, contentDescription = null)
                                    }
                                    AuraText(
                                        text = variant.name,
                                        style = t.labelSmall,
                                        color = c.content.secondary,
                                    )
                                }
                            }
                        }
                    }

                    AuraText(text = "Sizes (Small · Regular · Large)", style = t.titleMedium)

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        AuraFabSize.entries.forEach { fabSize ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                AuraFab(
                                    onClick = { selectedSize = fabSize },
                                    variant = AuraFabVariant.Secondary,
                                    size = fabSize,
                                    contentDescription = "${fabSize.name} FAB",
                                ) {
                                    AuraIcon(imageVector = Icons.Filled.Add, contentDescription = null)
                                }
                                AuraText(
                                    text = fabSize.name,
                                    style = t.labelSmall,
                                    color = c.content.secondary,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(72.dp))
                }
            },
            secondaryPane = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    AuraText(text = "App Creation Preset", style = t.titleSmall)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        FabPresetMode.entries.forEach { preset ->
                            AuraChipButton(
                                label = preset.title,
                                selected = selectedPreset == preset,
                                onClick = { selectedPreset = preset },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    AuraText(text = "Variant", style = t.titleSmall)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        AuraFabVariant.entries.forEach { variant ->
                            AuraChipButton(
                                label = variant.name,
                                selected = selectedVariant == variant,
                                onClick = { selectedVariant = variant },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    AuraText(text = "Size", style = t.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraFabSize.entries.forEach { size ->
                            AuraChipButton(
                                label = size.name,
                                selected = selectedSize == size,
                                onClick = { selectedSize = size },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    FabSwitchRow("Extended Mode (text != null)", showText) { showText = it }
                    FabSwitchRow("Expanded State (scroll toggle)", expanded) { expanded = it }
                    FabSwitchRow("Enabled", enabled) { enabled = it }
                    FabSwitchRow("Loading Spinner", loading) { loading = it }
                    FabSwitchRow("Gradient / Photo Backdrop", photoBackdrop) { photoBackdrop = it }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(c.surface.sunken)
                            .border(1.dp, c.border.subtle, RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        AuraText(text = "Usage Rules", style = t.labelLarge)
                        AuraText(
                            text = "Profile: ${window.widthClass.name} / ${window.displayRatio.name} (${window.widthDp}×${window.heightDp} dp)",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                        AuraText(
                            text = "FAB-RULE-001: Keep at most 1 AuraFab per screen anchored in AuraScaffold's floatingActionButton slot.",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                    }

                    Spacer(modifier = Modifier.height(72.dp))
                }
            },
        )
    }
}

@Composable
private fun FabSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    AuraSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        label = label,
        size = AuraSwitchSize.Small,
    )
}

package io.github.bluemarlins.aura.preview.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.component.AuraAdaptiveTwoPane
import io.github.bluemarlins.aura.component.AuraCard
import io.github.bluemarlins.aura.component.AuraCardPadding
import io.github.bluemarlins.aura.component.AuraCardVariant
import io.github.bluemarlins.aura.component.AuraChipButton
import io.github.bluemarlins.aura.component.AuraChipSize
import io.github.bluemarlins.aura.component.AuraIcon
import io.github.bluemarlins.aura.component.AuraIconButton
import io.github.bluemarlins.aura.component.AuraIconButtonVariant
import io.github.bluemarlins.aura.component.AuraScaffold
import io.github.bluemarlins.aura.component.AuraSwitch
import io.github.bluemarlins.aura.component.AuraSwitchSize
import io.github.bluemarlins.aura.component.AuraText
import io.github.bluemarlins.aura.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchPreviewScreen(onBack: () -> Unit) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout
    val window = AuraTheme.window

    var checked by remember { mutableStateOf(true) }
    var selectedSize by remember { mutableStateOf(AuraSwitchSize.Medium) }
    var enabled by remember { mutableStateOf(true) }
    var showLabel by remember { mutableStateOf(true) }
    var showDescription by remember { mutableStateOf(true) }

    var launcherParallax by remember { mutableStateOf(true) }
    var launcherBlur by remember { mutableStateOf(false) }
    var plannerAllDay by remember { mutableStateOf(false) }
    var financeAlerts by remember { mutableStateOf(true) }
    var drinkInStockOnly by remember { mutableStateOf(true) }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(text = "AuraSwitch", style = t.titleLarge)
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

                    AuraCard(
                        variant = AuraCardVariant.Elevated,
                        padding = AuraCardPadding.Large,
                    ) {
                        AuraSwitch(
                            checked = checked,
                            onCheckedChange = { checked = it },
                            size = selectedSize,
                            enabled = enabled,
                            label = if (showLabel) "3D Wallpaper Depth" else null,
                            description = if (showLabel && showDescription) {
                                "Applies tilt-reactive depth parallax immediately (SW-RULE-001)."
                            } else {
                                null
                            },
                        )
                    }

                    AuraText(text = "State Matrix", style = t.titleMedium)

                    AuraCard(variant = AuraCardVariant.Outlined, padding = AuraCardPadding.Medium) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AuraText("Standalone (Medium)", style = t.bodyMedium, color = c.content.secondary)
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    AuraSwitch(checked = false, onCheckedChange = {})
                                    AuraSwitch(checked = true, onCheckedChange = {})
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AuraText("Standalone (Small)", style = t.bodyMedium, color = c.content.secondary)
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    AuraSwitch(checked = false, onCheckedChange = {}, size = AuraSwitchSize.Small)
                                    AuraSwitch(checked = true, onCheckedChange = {}, size = AuraSwitchSize.Small)
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AuraText("Disabled States", style = t.bodyMedium, color = c.content.secondary)
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    AuraSwitch(checked = false, onCheckedChange = {}, enabled = false)
                                    AuraSwitch(checked = true, onCheckedChange = {}, enabled = false)
                                }
                            }
                        }
                    }

                    AuraText(text = "Domain Presets (4-App Patterns)", style = t.titleMedium)

                    AuraCard(variant = AuraCardVariant.Outlined, padding = AuraCardPadding.Medium) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            AuraSwitch(
                                checked = launcherParallax,
                                onCheckedChange = { launcherParallax = it },
                                label = "Home Launcher · 3D Parallax",
                                description = "Immediate gyroscope offset on desktop dock",
                            )
                            AuraSwitch(
                                checked = launcherBlur,
                                onCheckedChange = { launcherBlur = it },
                                label = "Home Launcher · Frosted Surface",
                                description = "Render translucent Glass cards over wallpaper",
                            )
                            AuraSwitch(
                                checked = plannerAllDay,
                                onCheckedChange = { plannerAllDay = it },
                                label = "My Planner · All-day Schedule",
                                description = "Pin schedule item above hourly blocks",
                            )
                            AuraSwitch(
                                checked = financeAlerts,
                                onCheckedChange = { financeAlerts = it },
                                label = "My Finance · Price Alerts",
                                description = "Notify on 5% daily threshold move",
                            )
                            AuraSwitch(
                                checked = drinkInStockOnly,
                                onCheckedChange = { drinkInStockOnly = it },
                                size = AuraSwitchSize.Small,
                                label = "My Drink Diary · In-cellar Only",
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            },
            secondaryPane = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    AuraText(text = "Size", style = t.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraSwitchSize.entries.forEach { size ->
                            AuraChipButton(
                                label = size.name,
                                selected = selectedSize == size,
                                onClick = { selectedSize = size },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    AuraSwitch(
                        checked = checked,
                        onCheckedChange = { checked = it },
                        label = "Checked State",
                        size = AuraSwitchSize.Small,
                    )
                    AuraSwitch(
                        checked = enabled,
                        onCheckedChange = { enabled = it },
                        label = "Enabled",
                        size = AuraSwitchSize.Small,
                    )
                    AuraSwitch(
                        checked = showLabel,
                        onCheckedChange = { showLabel = it },
                        label = "Show Row Label",
                        size = AuraSwitchSize.Small,
                    )
                    AuraSwitch(
                        checked = showDescription,
                        onCheckedChange = { showDescription = it },
                        enabled = showLabel,
                        label = "Show Description",
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
                                text = "SW-RULE-001: Flip takes effect immediately; thumb elongates softly under press (pressedScaleDefault = 0.96f).",
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

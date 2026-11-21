package io.github.bluemarlins.aura.preview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.component.AuraAdaptiveTwoPane
import io.github.bluemarlins.aura.component.AuraBadge
import io.github.bluemarlins.aura.component.AuraBadgeSize
import io.github.bluemarlins.aura.component.AuraBadgeVariant
import io.github.bluemarlins.aura.component.AuraBadgedBox
import io.github.bluemarlins.aura.component.AuraButton
import io.github.bluemarlins.aura.component.AuraButtonSize
import io.github.bluemarlins.aura.component.AuraButtonVariant
import io.github.bluemarlins.aura.component.AuraCard
import io.github.bluemarlins.aura.component.AuraCardPadding
import io.github.bluemarlins.aura.component.AuraCardVariant
import io.github.bluemarlins.aura.component.AuraChipButton
import io.github.bluemarlins.aura.component.AuraChipSize
import io.github.bluemarlins.aura.component.AuraChipVariant
import io.github.bluemarlins.aura.component.AuraDialog
import io.github.bluemarlins.aura.component.AuraDialogVariant
import io.github.bluemarlins.aura.component.AuraIcon
import io.github.bluemarlins.aura.component.AuraIconButton
import io.github.bluemarlins.aura.component.AuraIconButtonVariant
import io.github.bluemarlins.aura.component.AuraScaffold
import io.github.bluemarlins.aura.component.AuraSwitch
import io.github.bluemarlins.aura.component.AuraSwitchSize
import io.github.bluemarlins.aura.component.AuraText
import io.github.bluemarlins.aura.component.GlassStyle
import io.github.bluemarlins.aura.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DialogBadgePreviewScreen(
    onBack: () -> Unit,
    initialFocusBadge: Boolean = false,
) {
    var activeDialogVariant by remember { mutableStateOf<AuraDialogVariant?>(null) }
    var dialogGlassStyle by remember { mutableStateOf(GlassStyle.LIQUID) }
    var lastDialogAction by remember { mutableStateOf("Tap any button below to inspect modal dialog.") }

    var unreadCount by remember { mutableIntStateOf(6) }
    var badgeCountOverflow by remember { mutableStateOf(false) }

    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val title = if (initialFocusBadge) "AuraBadge" else "AuraDialog"

    val dialogPane: @Composable () -> Unit = {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AuraText(text = "AuraDialog · Modal Containment", style = t.titleMedium)

            AuraCard(
                variant = AuraCardVariant.Outlined,
                padding = AuraCardPadding.Medium,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AuraText(text = "Interactive Modal Triggers", style = t.titleSmall)
                AuraText(
                    text = lastDialogAction,
                    style = t.bodySmall,
                    color = c.content.secondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                )
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AuraButton(
                        text = "Open Standard Dialog",
                        onClick = { activeDialogVariant = AuraDialogVariant.Standard },
                        variant = AuraButtonVariant.Primary,
                        fullWidth = true,
                    )
                    AuraButton(
                        text = "Open Destructive Dialog",
                        onClick = { activeDialogVariant = AuraDialogVariant.Destructive },
                        variant = AuraButtonVariant.Destructive,
                        fullWidth = true,
                    )
                    AuraButton(
                        text = "Open Glass (${dialogGlassStyle.name}) Dialog",
                        onClick = { activeDialogVariant = AuraDialogVariant.Glass },
                        variant = AuraButtonVariant.Secondary,
                        fullWidth = true,
                    )
                }
            }

            AuraText(text = "Glass Dialog Material", style = t.labelLarge)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GlassStyle.entries.forEach { style ->
                    AuraChipButton(
                        label = style.name,
                        selected = dialogGlassStyle == style,
                        variant = AuraChipVariant.Filter,
                        size = AuraChipSize.Small,
                        onClick = { dialogGlassStyle = style },
                    )
                }
            }
        }
    }

    val badgePane: @Composable () -> Unit = {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AuraText(text = "AuraBadge · Status & Counters", style = t.titleMedium)

            AuraCard(
                variant = AuraCardVariant.Filled,
                padding = AuraCardPadding.Medium,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AuraText(text = "Semantic Status Pills (Medium)", style = t.labelLarge)
                FlowRow(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AuraBadge(label = "Ready", variant = AuraBadgeVariant.Success)
                    AuraBadge(label = "Brand", variant = AuraBadgeVariant.Brand)
                    AuraBadge(label = "Info", variant = AuraBadgeVariant.Info)
                    AuraBadge(label = "Low Stock", variant = AuraBadgeVariant.Warning)
                    AuraBadge(label = "Overdue", variant = AuraBadgeVariant.Error)
                    AuraBadge(label = "Planned", variant = AuraBadgeVariant.Neutral)
                }
            }

            AuraCard(
                variant = AuraCardVariant.Outlined,
                padding = AuraCardPadding.Medium,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AuraText(text = "AuraBadgedBox & Numeric Clamping", style = t.labelLarge)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AuraBadgedBox(
                        badge = {
                            AuraBadge(
                                count = if (badgeCountOverflow) 142 else unreadCount,
                                maxCount = 99,
                                variant = AuraBadgeVariant.Error,
                                size = AuraBadgeSize.Small,
                            )
                        },
                    ) {
                        AuraIconButton(
                            onClick = { unreadCount += 1 },
                            variant = AuraIconButtonVariant.Tonal,
                            contentDescription = "Notifications",
                        ) {
                            AuraIcon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = null,
                            )
                        }
                    }

                    AuraBadgedBox(
                        badge = {
                            AuraBadge(
                                variant = AuraBadgeVariant.Success,
                                size = AuraBadgeSize.Dot,
                            )
                        },
                    ) {
                        AuraIconButton(
                            onClick = { unreadCount = 0 },
                            variant = AuraIconButtonVariant.Outlined,
                            contentDescription = "Presence dot sample",
                        ) {
                            AuraIcon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = null,
                            )
                        }
                    }

                    AuraButton(
                        text = "+1 Count",
                        onClick = { unreadCount += 1 },
                        size = AuraButtonSize.Small,
                        variant = AuraButtonVariant.Outline,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(c.surface.sunken)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AuraText(text = "Simulate Overflow (142 -> 99+)", style = t.bodySmall)
                    AuraSwitch(
                        checked = badgeCountOverflow,
                        onCheckedChange = { badgeCountOverflow = it },
                        size = AuraSwitchSize.Small,
                    )
                }
            }
        }
    }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = { AuraText(text = title, style = t.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = c.background.primary,
                    titleContentColor = c.content.primary,
                    navigationIconContentColor = c.content.primary,
                ),
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
            )
        },
    ) { innerPadding ->
        AuraAdaptiveTwoPane(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            primaryPane = if (initialFocusBadge) badgePane else dialogPane,
            secondaryPane = if (initialFocusBadge) dialogPane else badgePane,
        )
    }

    val currentVariant = activeDialogVariant
    AuraDialog(
        visible = currentVariant != null,
        title = when (currentVariant) {
            AuraDialogVariant.Destructive -> "Delete tasting entry?"
            AuraDialogVariant.Glass -> "Save Spatial Preset?"
            AuraDialogVariant.Standard, null -> "Archive cellar bottle?"
        },
        variant = currentVariant ?: AuraDialogVariant.Standard,
        glassStyle = dialogGlassStyle,
        message = when (currentVariant) {
            AuraDialogVariant.Destructive -> "Removing this 1998 Barolo entry erases its score and aroma sliders permanently."
            AuraDialogVariant.Glass -> "Applies Liquid Glass optics across top navigation pill and bottom split island."
            AuraDialogVariant.Standard, null -> "Move this bottle from Active Shelf into Finished Cellar Archive?"
        },
        confirmText = when (currentVariant) {
            AuraDialogVariant.Destructive -> "Delete Entry"
            AuraDialogVariant.Glass -> "Apply Preset"
            AuraDialogVariant.Standard, null -> "Archive"
        },
        dismissText = "Cancel",
        onConfirm = {
            lastDialogAction = "Confirmed ${currentVariant?.name} dialog!"
            activeDialogVariant = null
        },
        onDismissRequest = {
            lastDialogAction = "Dismissed ${currentVariant?.name} dialog."
            activeDialogVariant = null
        },
    )
}

package com.example.ds.preview.ui

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraAdaptiveTwoPane
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraCheckbox
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraSwitch
import com.example.ds.component.AuraSwitchSize
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckboxPreviewScreen(onBack: () -> Unit) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout
    val window = AuraTheme.window

    var checked by remember { mutableStateOf(true) }
    var indeterminate by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var strikethrough by remember { mutableStateOf(false) }

    val plannerTasks = remember {
        mutableStateListOf(true, false, false)
    }
    val plannerLabels = listOf(
        "Review Q3 investment memo",
        "Record tasting notes for 2019 Barolo",
        "Adjust Home Launcher grid preset",
    )

    val assetSelections = remember {
        mutableStateListOf(true, false, true)
    }
    val assetLabels = listOf(
        "KRW Deposit Reserve",
        "S&P 500 Core ETF",
        "Dividend Cash Account",
    )
    val allAssetsChecked = assetSelections.all { it }
    val anyAssetChecked = assetSelections.any { it }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(text = "AuraCheckbox", style = t.titleLarge)
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

                    AuraCard(variant = AuraCardVariant.Elevated, padding = AuraCardPadding.Large) {
                        AuraCheckbox(
                            checked = checked,
                            onCheckedChange = {
                                indeterminate = false
                                checked = it
                            },
                            indeterminate = indeterminate,
                            enabled = enabled,
                            isError = isError,
                            label = "Include Unrealized Gains",
                            description = "Supports Unchecked, Checked, and Indeterminate tri-state semantics.",
                            strikethroughWhenChecked = strikethrough,
                        )
                        if (isError) {
                            AuraText(
                                text = "Required confirmation before exporting tax report.",
                                style = t.bodySmall,
                                color = c.status.error.solid,
                                modifier = Modifier.padding(start = 32.dp, top = 4.dp),
                            )
                        }
                    }

                    AuraText(text = "My Finance · Tri-state Batch Selection", style = t.titleMedium)

                    AuraCard(variant = AuraCardVariant.Outlined, padding = AuraCardPadding.Medium) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            AuraCheckbox(
                                checked = allAssetsChecked,
                                indeterminate = anyAssetChecked && !allAssetsChecked,
                                onCheckedChange = {
                                    val target = !allAssetsChecked
                                    for (i in assetSelections.indices) {
                                        assetSelections[i] = target
                                    }
                                },
                                label = "All Portfolio Accounts",
                                description = if (anyAssetChecked && !allAssetsChecked) {
                                    "Partial selection (${assetSelections.count { it }} of ${assetSelections.size})"
                                } else {
                                    "${assetSelections.count { it }} of ${assetSelections.size} selected"
                                },
                            )
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                assetLabels.forEachIndexed { index, label ->
                                    AuraCheckbox(
                                        checked = assetSelections[index],
                                        onCheckedChange = { assetSelections[index] = it },
                                        label = label,
                                    )
                                }
                            }
                        }
                    }

                    AuraText(text = "My Planner · Strikethrough Checklist", style = t.titleMedium)

                    AuraCard(variant = AuraCardVariant.Outlined, padding = AuraCardPadding.Medium) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            plannerLabels.forEachIndexed { index, task ->
                                AuraCheckbox(
                                    checked = plannerTasks[index],
                                    onCheckedChange = { plannerTasks[index] = it },
                                    label = task,
                                    strikethroughWhenChecked = true,
                                )
                            }
                        }
                    }

                    AuraText(text = "Standalone Visual Matrix", style = t.titleMedium)

                    AuraCard(variant = AuraCardVariant.Filled, padding = AuraCardPadding.Compact) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            AuraCheckbox(checked = false, onCheckedChange = {})
                            AuraCheckbox(checked = true, onCheckedChange = {})
                            AuraCheckbox(checked = false, indeterminate = true, onCheckedChange = {})
                            AuraCheckbox(checked = false, isError = true, onCheckedChange = {})
                            AuraCheckbox(checked = true, enabled = false, onCheckedChange = {})
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            },
            secondaryPane = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AuraSwitch(
                        checked = checked,
                        onCheckedChange = {
                            indeterminate = false
                            checked = it
                        },
                        label = "Checked",
                        size = AuraSwitchSize.Small,
                    )
                    AuraSwitch(
                        checked = indeterminate,
                        onCheckedChange = { indeterminate = it },
                        label = "Indeterminate Mode",
                        size = AuraSwitchSize.Small,
                    )
                    AuraSwitch(
                        checked = enabled,
                        onCheckedChange = { enabled = it },
                        label = "Enabled",
                        size = AuraSwitchSize.Small,
                    )
                    AuraSwitch(
                        checked = isError,
                        onCheckedChange = { isError = it },
                        label = "Error State (INP-RULE-005)",
                        size = AuraSwitchSize.Small,
                    )
                    AuraSwitch(
                        checked = strikethrough,
                        onCheckedChange = { strikethrough = it },
                        label = "Strikethrough When Checked",
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
                                text = "CB-RULE-001: Parent Select All renders an animated horizontal bar via ToggleableState.Indeterminate.",
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

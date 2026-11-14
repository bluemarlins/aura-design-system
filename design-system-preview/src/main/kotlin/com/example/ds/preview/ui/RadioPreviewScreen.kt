package com.example.ds.preview.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraAdaptiveTwoPane
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraRadio
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraSwitch
import com.example.ds.component.AuraSwitchSize
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioPreviewScreen(onBack: () -> Unit) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout
    val window = AuraTheme.window

    var singleSelected by remember { mutableStateOf(true) }
    var enabled by remember { mutableStateOf(true) }
    var showDescription by remember { mutableStateOf(true) }

    var currencyIndex by remember { mutableIntStateOf(0) }
    val currencyOptions = listOf(
        "Korean Won (KRW)" to "Primary ledger reporting in ₩ without decimals.",
        "US Dollar (USD)" to "Format US brokerage balances with $ and 2 decimals.",
        "Euro (EUR)" to "Track European wine cellar valuations in €.",
    )

    var gridIndex by remember { mutableIntStateOf(0) }
    val gridOptions = listOf(
        "4 × 5 Standard" to "Balanced desktop rhythm on smartphones and narrow panes",
        "4 × 6 Dense" to "Fits more utility apps per Home Launcher page",
        "5 × 5 Spacious" to "Wide breathing room on Tablet & Foldable displays",
    )

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(text = "AuraRadio", style = t.titleLarge)
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
                        AuraRadio(
                            selected = singleSelected,
                            onClick = { singleSelected = true },
                            enabled = enabled,
                            label = "Preferred Portfolio Currency",
                            description = if (showDescription) {
                                "Mutually exclusive selection (RD-RULE-002 keeps selected state on re-tap)."
                            } else {
                                null
                            },
                        )
                    }

                    AuraText(text = "My Finance · Base Currency", style = t.titleMedium)

                    AuraCard(variant = AuraCardVariant.Outlined, padding = AuraCardPadding.Medium) {
                        Column(
                            modifier = Modifier.selectableGroup(),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            currencyOptions.forEachIndexed { index, (title, desc) ->
                                AuraRadio(
                                    selected = currencyIndex == index,
                                    onClick = { currencyIndex = index },
                                    label = title,
                                    description = desc,
                                )
                            }
                        }
                    }

                    AuraText(text = "Home Launcher · Icon Grid Preset", style = t.titleMedium)

                    AuraCard(variant = AuraCardVariant.Outlined, padding = AuraCardPadding.Medium) {
                        Column(
                            modifier = Modifier.selectableGroup(),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            gridOptions.forEachIndexed { index, (title, desc) ->
                                AuraRadio(
                                    selected = gridIndex == index,
                                    onClick = { gridIndex = index },
                                    label = title,
                                    description = desc,
                                )
                            }
                        }
                    }

                    AuraText(text = "Standalone State Matrix", style = t.titleMedium)

                    AuraCard(variant = AuraCardVariant.Filled, padding = AuraCardPadding.Compact) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            AuraRadio(selected = false, onClick = {})
                            AuraRadio(selected = true, onClick = {})
                            AuraRadio(selected = false, enabled = false, onClick = {})
                            AuraRadio(selected = true, enabled = false, onClick = {})
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
                        checked = singleSelected,
                        onCheckedChange = { singleSelected = it },
                        label = "Selected State",
                        size = AuraSwitchSize.Small,
                    )
                    AuraSwitch(
                        checked = enabled,
                        onCheckedChange = { enabled = it },
                        label = "Enabled",
                        size = AuraSwitchSize.Small,
                    )
                    AuraSwitch(
                        checked = showDescription,
                        onCheckedChange = { showDescription = it },
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
                                text = "RD-RULE-001: Wrap related radio rows in Modifier.selectableGroup() for screen-reader list indexing.",
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

package com.example.ds.preview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraAdaptiveTwoPane
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraChipButton
import com.example.ds.component.AuraChipSize
import com.example.ds.component.AuraChipVariant
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraNumberField
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraSwitch
import com.example.ds.component.AuraSwitchSize
import com.example.ds.component.AuraText
import com.example.ds.component.AuraTextFieldVariant
import com.example.ds.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NumberFieldPreviewScreen(
    onBack: () -> Unit,
) {
    var playgroundValue by remember { mutableStateOf("250000") }
    var selectedVariant by remember { mutableStateOf(AuraTextFieldVariant.Outlined) }
    var showSteppers by remember { mutableStateOf(true) }
    var formatThousands by remember { mutableStateOf(true) }
    var allowDecimals by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }

    var financeRate by remember { mutableStateOf("4.35") }
    var drinkVintage by remember { mutableStateOf("2018") }
    var drinkVolume by remember { mutableStateOf("750") }

    val c = AuraTheme.colors
    val t = AuraTheme.typography

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = { AuraText(text = "AuraNumberField", style = t.titleLarge) },
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
            primaryPane = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AuraText(text = "Interactive Playground", style = t.titleMedium)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(c.surface.default)
                            .padding(20.dp),
                    ) {
                        AuraNumberField(
                            value = playgroundValue,
                            onValueChange = { playgroundValue = it },
                            modifier = Modifier.fillMaxWidth(),
                            variant = selectedVariant,
                            enabled = enabled,
                            label = "Portfolio Contribution",
                            placeholder = "0",
                            prefixText = "₩",
                            suffixText = "원",
                            supportingText = "Tabular numerals prevent horizontal jitter while stepping.",
                            errorMessage = if (hasError) "Amount must be at least ₩10,000." else null,
                            showSteppers = showSteppers,
                            step = if (allowDecimals) 0.5 else 10000.0,
                            minValue = 0.0,
                            maxValue = 50_000_000.0,
                            allowDecimals = allowDecimals,
                            formatThousands = formatThousands,
                        )
                    }

                    AuraText(text = "Variant", style = t.labelLarge)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        AuraTextFieldVariant.entries.forEach { variant ->
                            AuraChipButton(
                                label = variant.name,
                                selected = selectedVariant == variant,
                                variant = AuraChipVariant.Filter,
                                size = AuraChipSize.Small,
                                onClick = { selectedVariant = variant },
                            )
                        }
                    }

                    NumberToggleRow(
                        title = "Show Steppers (- / +)",
                        checked = showSteppers,
                        onCheckedChange = { showSteppers = it },
                    )
                    NumberToggleRow(
                        title = "Format Thousands (,)",
                        checked = formatThousands,
                        onCheckedChange = { formatThousands = it },
                    )
                    NumberToggleRow(
                        title = "Allow Decimals",
                        checked = allowDecimals,
                        onCheckedChange = { allowDecimals = it },
                    )
                    NumberToggleRow(
                        title = "Error State",
                        checked = hasError,
                        onCheckedChange = { hasError = it },
                    )
                    NumberToggleRow(
                        title = "Enabled",
                        checked = enabled,
                        onCheckedChange = { enabled = it },
                    )
                }
            },
            secondaryPane = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AuraText(text = "Domain Examples", style = t.titleMedium)

                    AuraCard(
                        variant = AuraCardVariant.Elevated,
                        padding = AuraCardPadding.Medium,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        AuraText(text = "My Finance · Dividend Yield", style = t.labelLarge, color = c.brand.primary)
                        AuraNumberField(
                            value = financeRate,
                            onValueChange = { financeRate = it },
                            variant = AuraTextFieldVariant.Filled,
                            suffixText = "%",
                            showSteppers = true,
                            step = 0.25,
                            minValue = 0.0,
                            maxValue = 25.0,
                            supportingText = "Annualized coupon rate step: 0.25%.",
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }

                    AuraCard(
                        variant = AuraCardVariant.Outlined,
                        padding = AuraCardPadding.Medium,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        AuraText(text = "My Drink Diary · Cellar Metadata", style = t.labelLarge, color = c.brand.primary)
                        AuraNumberField(
                            value = drinkVintage,
                            onValueChange = { drinkVintage = it },
                            label = "Harvest Vintage",
                            showSteppers = true,
                            allowDecimals = false,
                            minValue = 1950.0,
                            maxValue = 2030.0,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                        AuraNumberField(
                            value = drinkVolume,
                            onValueChange = { drinkVolume = it },
                            variant = AuraTextFieldVariant.Paperless,
                            label = "Bottle Volume",
                            suffixText = "ml",
                            allowDecimals = false,
                            modifier = Modifier.padding(top = 12.dp),
                        )
                    }
                }
            },
        )
    }
}

@Composable
private fun NumberToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(c.surface.default)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AuraText(text = title, style = t.bodyMedium, color = c.content.primary)
        AuraSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            size = AuraSwitchSize.Small,
        )
    }
}

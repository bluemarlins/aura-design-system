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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraAdaptiveTwoPane
import com.example.ds.component.AuraChipButton
import com.example.ds.component.AuraChipSize
import com.example.ds.component.AuraChipVariant
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraText
import com.example.ds.component.AuraTextField
import com.example.ds.component.AuraTextFieldVariant
import com.example.ds.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TextFieldPreviewScreen(
    onBack: () -> Unit,
) {
    var playgroundValue by remember { mutableStateOf("") }
    var selectedVariant by remember { mutableStateOf(AuraTextFieldVariant.Outlined) }
    var singleLine by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }
    var readOnly by remember { mutableStateOf(false) }
    var showAffixes by remember { mutableStateOf(false) }
    var useMaxLength by remember { mutableStateOf(true) }
    var prefixInput by remember { mutableStateOf("₩") }
    var suffixInput by remember { mutableStateOf("원") }

    var plannerDiary by remember {
        mutableStateOf("Morning walk along the river, then sketched the new schedule layout.")
    }
    var financeAmount by remember { mutableStateOf("15,000,000") }
    var drinkAbv by remember { mutableStateOf("46.0") }

    val c = AuraTheme.colors
    val t = AuraTheme.typography

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = "AuraTextField",
                        style = t.titleLarge,
                    )
                },
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
                    AuraText(
                        text = "Interactive Playground",
                        style = t.titleMedium,
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(c.surface.default)
                            .padding(20.dp),
                    ) {
                        AuraTextField(
                            value = playgroundValue,
                            onValueChange = { playgroundValue = it },
                            modifier = Modifier.fillMaxWidth(),
                            variant = selectedVariant,
                            label = "Title / Entry",
                            placeholder = if (singleLine) "Enter text..." else "Write multi-line notes...",
                            supportingText = "Helper guidance appears below the container.",
                            errorText = if (hasError) "This field requires a valid value." else null,
                            prefixText = if (showAffixes) prefixInput.ifBlank { null } else null,
                            suffixText = if (showAffixes) suffixInput.ifBlank { null } else null,
                            maxLength = if (useMaxLength) 80 else null,
                            enabled = enabled,
                            readOnly = readOnly,
                            singleLine = singleLine,
                            minLines = if (singleLine) 1 else 3,
                        )
                    }

                    AuraText(
                        text = "Variant",
                        style = t.labelLarge,
                    )
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

                    FieldToggleRow(
                        title = "Single Line",
                        checked = singleLine,
                        onCheckedChange = { singleLine = it },
                    )
                    FieldToggleRow(
                        title = "Error State",
                        checked = hasError,
                        onCheckedChange = { hasError = it },
                    )
                    FieldToggleRow(
                        title = "Enabled",
                        checked = enabled,
                        onCheckedChange = { enabled = it },
                    )
                    FieldToggleRow(
                        title = "Read Only",
                        checked = readOnly,
                        onCheckedChange = { readOnly = it },
                    )
                    FieldToggleRow(
                        title = "Max Length (80)",
                        checked = useMaxLength,
                        onCheckedChange = { useMaxLength = it },
                    )
                    FieldToggleRow(
                        title = "Prefix / Suffix Affixes",
                        checked = showAffixes,
                        onCheckedChange = { showAffixes = it },
                    )

                    if (showAffixes) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            AuraTextField(
                                value = prefixInput,
                                onValueChange = { prefixInput = it },
                                label = "Prefix",
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                            )
                            AuraTextField(
                                value = suffixInput,
                                onValueChange = { suffixInput = it },
                                label = "Suffix",
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            },
            secondaryPane = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AuraText(
                        text = "Domain Presets (4-App Patterns)",
                        style = t.titleMedium,
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = c.surface.default,
                        ),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                        ) {
                            AuraTextField(
                                value = plannerDiary,
                                onValueChange = { plannerDiary = it },
                                modifier = Modifier.fillMaxWidth(),
                                variant = AuraTextFieldVariant.Paperless,
                                label = "My Planner · Daily Diary",
                                placeholder = "Write your reflections...",
                                supportingText = "Paperless mode removes heavy boxes for long-form writing.",
                                singleLine = false,
                                minLines = 3,
                                maxLength = 300,
                            )

                            AuraTextField(
                                value = financeAmount,
                                onValueChange = { financeAmount = it },
                                modifier = Modifier.fillMaxWidth(),
                                variant = AuraTextFieldVariant.Outlined,
                                label = "My Finance · Asset Amount",
                                prefixText = "₩",
                                suffixText = "원",
                                supportingText = "Clear boundary + explicit currency affixes.",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            )

                            AuraTextField(
                                value = drinkAbv,
                                onValueChange = { drinkAbv = it },
                                modifier = Modifier.fillMaxWidth(),
                                variant = AuraTextFieldVariant.Filled,
                                label = "My Drink Diary · Alcohol Strength",
                                suffixText = "% ABV",
                                supportingText = "Compact filled style inside tasting metadata cards.",
                                maxLength = 120,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            )
                        }
                    }
                }
            },
        )
    }
}

@Composable
private fun FieldToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AuraText(
            text = title,
            style = AuraTheme.typography.bodyLarge,
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

package com.example.ds.preview.ui

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

    val auraColors = AuraTheme.colors

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AuraTextField") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "Interactive Playground",
                style = MaterialTheme.typography.titleMedium,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(auraColors.surface.default)
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

            Text(
                text = "Variant",
                style = MaterialTheme.typography.labelLarge,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                AuraTextFieldVariant.entries.forEach { variant ->
                    FilterChip(
                        selected = selectedVariant == variant,
                        onClick = { selectedVariant = variant },
                        label = { Text(variant.name) },
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
                    OutlinedTextField(
                        value = prefixInput,
                        onValueChange = { prefixInput = it },
                        label = { Text("Prefix") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    OutlinedTextField(
                        value = suffixInput,
                        onValueChange = { suffixInput = it },
                        label = { Text("Suffix") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Domain Presets (4-App Patterns)",
                style = MaterialTheme.typography.titleMedium,
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = auraColors.surface.default,
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
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

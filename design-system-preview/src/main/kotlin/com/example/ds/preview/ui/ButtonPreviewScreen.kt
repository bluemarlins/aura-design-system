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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraButton
import com.example.ds.component.AuraButtonSize
import com.example.ds.component.AuraButtonVariant
import com.example.ds.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ButtonPreviewScreen(
    onBack: () -> Unit,
) {
    var label by remember { mutableStateOf("Save Entry") }
    var selectedVariant by remember { mutableStateOf(AuraButtonVariant.Primary) }
    var selectedSize by remember { mutableStateOf(AuraButtonSize.Medium) }
    var enabled by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }
    var fullWidth by remember { mutableStateOf(false) }
    var clickCount by remember { mutableIntStateOf(0) }

    val auraColors = AuraTheme.colors

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AuraButton") },
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
                    .padding(24.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AuraButton(
                        text = label.ifBlank { "Button" },
                        onClick = { clickCount++ },
                        variant = selectedVariant,
                        size = selectedSize,
                        enabled = enabled,
                        loading = loading,
                        fullWidth = fullWidth,
                    )
                    Text(
                        text = "Click count (400ms debounced): $clickCount",
                        style = MaterialTheme.typography.bodySmall,
                        color = auraColors.content.secondary,
                    )
                }
            }

            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text("Label") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = "Variant",
                style = MaterialTheme.typography.labelLarge,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                AuraButtonVariant.entries.forEach { variant ->
                    FilterChip(
                        selected = selectedVariant == variant,
                        onClick = { selectedVariant = variant },
                        label = { Text(variant.name) },
                    )
                }
            }

            Text(
                text = "Size",
                style = MaterialTheme.typography.labelLarge,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AuraButtonSize.entries.forEach { size ->
                    FilterChip(
                        selected = selectedSize == size,
                        onClick = { selectedSize = size },
                        label = { Text(size.name) },
                    )
                }
            }

            ToggleRow(title = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
            ToggleRow(title = "Loading", checked = loading, onCheckedChange = { loading = it })
            ToggleRow(title = "Full Width", checked = fullWidth, onCheckedChange = { fullWidth = it })

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Variant Matrix",
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
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AuraButtonVariant.entries.forEach { variant ->
                        MatrixRow(title = variant.name) {
                            AuraButton(
                                text = variant.name,
                                variant = variant,
                                onClick = {},
                            )
                        }
                    }
                    MatrixRow(title = "Loading") {
                        AuraButton(
                            text = "Submitting",
                            loading = true,
                            onClick = {},
                        )
                    }
                    MatrixRow(title = "Disabled") {
                        AuraButton(
                            text = "Unavailable",
                            enabled = false,
                            onClick = {},
                        )
                    }
                }
            }

            Text(
                text = "Size & Icon Matrix",
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
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AuraButtonSize.entries.forEach { size ->
                        MatrixRow(title = size.name) {
                            AuraButton(
                                text = "${size.name} Action",
                                size = size,
                                onClick = {},
                            )
                        }
                    }
                    MatrixRow(title = "Leading Icon") {
                        AuraButton(
                            text = "Add Note",
                            variant = AuraButtonVariant.Secondary,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = null,
                                    tint = auraColors.brand.onSubtle,
                                    modifier = Modifier.size(18.dp),
                                )
                            },
                            onClick = {},
                        )
                    }
                    MatrixRow(title = "Trailing Icon") {
                        AuraButton(
                            text = "Next Step",
                            variant = AuraButtonVariant.Primary,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = auraColors.brand.onPrimary,
                                    modifier = Modifier.size(18.dp),
                                )
                            },
                            onClick = {},
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ToggleRow(
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

@Composable
private fun MatrixRow(
    title: String,
    content: @Composable () -> Unit,
) {
    val auraColors = AuraTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = auraColors.content.secondary,
        )
        Box {
            content()
        }
    }
}

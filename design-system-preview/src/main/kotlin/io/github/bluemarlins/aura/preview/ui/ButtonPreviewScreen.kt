package io.github.bluemarlins.aura.preview.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
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
import io.github.bluemarlins.aura.component.AuraButton
import io.github.bluemarlins.aura.component.AuraButtonSize
import io.github.bluemarlins.aura.component.AuraButtonVariant
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
import io.github.bluemarlins.aura.component.AuraTextField
import io.github.bluemarlins.aura.foundation.theme.AuraTheme

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

    val c = AuraTheme.colors
    val t = AuraTheme.typography

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = "AuraButton",
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
                            AuraText(
                                text = "Click count (400ms debounced): $clickCount",
                                style = t.bodySmall,
                                color = c.content.secondary,
                            )
                        }
                    }

                    AuraTextField(
                        value = label,
                        onValueChange = { label = it },
                        label = "Label",
                        placeholder = "Enter button action label",
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    AuraText(
                        text = "Variant",
                        style = t.labelLarge,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        AuraButtonVariant.entries.forEach { variant ->
                            AuraChipButton(
                                label = variant.name,
                                selected = selectedVariant == variant,
                                variant = AuraChipVariant.Filter,
                                size = AuraChipSize.Small,
                                onClick = { selectedVariant = variant },
                            )
                        }
                    }

                    AuraText(
                        text = "Size",
                        style = t.labelLarge,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraButtonSize.entries.forEach { size ->
                            AuraChipButton(
                                label = size.name,
                                selected = selectedSize == size,
                                variant = AuraChipVariant.Filter,
                                size = AuraChipSize.Small,
                                onClick = { selectedSize = size },
                            )
                        }
                    }

                    ToggleRow(title = "Enabled", checked = enabled, onCheckedChange = { enabled = it })
                    ToggleRow(title = "Loading", checked = loading, onCheckedChange = { loading = it })
                    ToggleRow(title = "Full Width", checked = fullWidth, onCheckedChange = { fullWidth = it })
                }
            },
            secondaryPane = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AuraText(
                        text = "Variant Matrix",
                        style = t.titleMedium,
                    )

                    AuraCard(
                        modifier = Modifier.fillMaxWidth(),
                        variant = AuraCardVariant.Outlined,
                        padding = AuraCardPadding.Medium,
                    ) {
                        Column(
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

                    AuraText(
                        text = "Size & Icon Matrix",
                        style = t.titleMedium,
                    )

                    AuraCard(
                        modifier = Modifier.fillMaxWidth(),
                        variant = AuraCardVariant.Outlined,
                        padding = AuraCardPadding.Medium,
                    ) {
                        Column(
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
                                        AuraIcon(
                                            imageVector = Icons.Filled.Add,
                                            contentDescription = null,
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
                                        AuraIcon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp),
                                        )
                                    },
                                    onClick = {},
                                )
                            }
                        }
                    }
                }
            },
        )
    }
}

@Composable
private fun ToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    AuraSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        label = title,
        size = AuraSwitchSize.Small,
    )
}

@Composable
private fun MatrixRow(
    title: String,
    content: @Composable () -> Unit,
) {
    val c = AuraTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AuraText(
            text = title,
            style = AuraTheme.typography.bodyMedium,
            color = c.content.secondary,
        )
        Box {
            content()
        }
    }
}

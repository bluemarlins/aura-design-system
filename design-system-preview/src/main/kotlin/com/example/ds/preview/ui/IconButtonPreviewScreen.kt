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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Tune
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
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonShape
import com.example.ds.component.AuraIconButtonSize
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraSwitch
import com.example.ds.component.AuraSwitchSize
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme

private enum class ToggleMode(val label: String, val value: Boolean?) {
    Stateless("Stateless (null)", null),
    Unselected("Selected = false", false),
    Selected("Selected = true", true),
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun IconButtonPreviewScreen(onBack: () -> Unit) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout
    val window = AuraTheme.window

    var selectedVariant by remember { mutableStateOf(AuraIconButtonVariant.Filled) }
    var selectedSize by remember { mutableStateOf(AuraIconButtonSize.Medium) }
    var selectedShape by remember { mutableStateOf(AuraIconButtonShape.Circle) }
    var toggleMode by remember { mutableStateOf(ToggleMode.Stateless) }
    var enabled by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }
    var photoBackdrop by remember { mutableStateOf(false) }
    var clickCount by remember { mutableIntStateOf(0) }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = "AuraIconButton",
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
                    AuraText(
                        text = "Interactive Stage",
                        style = t.titleMedium,
                    )

                    val stageModifier = if (photoBackdrop) {
                        Modifier.background(
                            Brush.linearGradient(
                                colors = listOf(c.brand.primary, c.status.info.solid, c.surface.inverse),
                            ),
                        )
                    } else {
                        Modifier.background(c.surface.raised)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(172.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, c.border.subtle, RoundedCornerShape(16.dp))
                            .then(stageModifier),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            AuraIconButton(
                                onClick = {
                                    clickCount++
                                    toggleMode = when (toggleMode) {
                                        ToggleMode.Stateless -> ToggleMode.Stateless
                                        ToggleMode.Unselected -> ToggleMode.Selected
                                        ToggleMode.Selected -> ToggleMode.Unselected
                                    }
                                },
                                variant = selectedVariant,
                                size = selectedSize,
                                shape = selectedShape,
                                enabled = enabled,
                                loading = loading,
                                selected = toggleMode.value,
                                contentDescription = "Sample ${selectedVariant.name} action",
                            ) {
                                val icon = when {
                                    selectedVariant == AuraIconButtonVariant.Destructive -> Icons.Filled.DeleteOutline
                                    toggleMode == ToggleMode.Selected -> Icons.Filled.Favorite
                                    toggleMode == ToggleMode.Unselected -> Icons.Filled.FavoriteBorder
                                    selectedVariant == AuraIconButtonVariant.Filled -> Icons.Filled.Add
                                    else -> Icons.Filled.Tune
                                }
                                AuraIcon(imageVector = icon, contentDescription = null)
                            }

                            AuraText(
                                text = "${selectedVariant.name} · ${selectedSize.name} · ${selectedShape.name} · Taps: $clickCount",
                                style = t.labelMedium,
                                color = if (photoBackdrop) c.brand.onPrimary else c.content.secondary,
                            )
                        }
                    }

                    AuraText(
                        text = "All Variants Matrix",
                        style = t.titleMedium,
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(c.surface.raised)
                            .border(1.dp, c.border.subtle, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        AuraText(
                            text = "Resting & Toggle Comparison (Tap any icon to test tactile spring)",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            AuraIconButtonVariant.entries.forEach { variant ->
                                var itemSelected by remember(variant) { mutableStateOf(false) }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    AuraIconButton(
                                        onClick = { itemSelected = !itemSelected },
                                        variant = variant,
                                        selected = if (variant == AuraIconButtonVariant.Filled) null else itemSelected,
                                        contentDescription = "${variant.name} specimen",
                                    ) {
                                        val vec = when (variant) {
                                            AuraIconButtonVariant.Filled -> Icons.Filled.Add
                                            AuraIconButtonVariant.Tonal -> if (itemSelected) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder
                                            AuraIconButtonVariant.Outlined -> if (itemSelected) Icons.Filled.Star else Icons.Filled.StarBorder
                                            AuraIconButtonVariant.Ghost -> Icons.Filled.Share
                                            AuraIconButtonVariant.Floating -> Icons.Filled.Edit
                                            AuraIconButtonVariant.Destructive -> Icons.Filled.DeleteOutline
                                        }
                                        AuraIcon(imageVector = vec, contentDescription = null)
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

                    AuraText(
                        text = "Toolbar & Floating Context Pairing",
                        style = t.titleMedium,
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(c.surface.floating)
                            .border(1.dp, c.border.subtle, RoundedCornerShape(24.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AuraIconButton(
                                    onClick = {},
                                    variant = AuraIconButtonVariant.Ghost,
                                    contentDescription = "Navigate back",
                                ) {
                                    AuraIcon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                                }
                                AuraText(
                                    text = "Diary Entry",
                                    style = t.titleSmall,
                                    modifier = Modifier.padding(start = 4.dp),
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AuraIconButton(
                                    onClick = {},
                                    variant = AuraIconButtonVariant.Ghost,
                                    contentDescription = "Filter notes",
                                ) {
                                    AuraIcon(Icons.Filled.FilterList, contentDescription = null)
                                }
                                AuraIconButton(
                                    onClick = {},
                                    variant = AuraIconButtonVariant.Filled,
                                    size = AuraIconButtonSize.Small,
                                    contentDescription = "Save entry",
                                ) {
                                    AuraIcon(Icons.Filled.Check, contentDescription = null)
                                }
                                AuraIconButton(
                                    onClick = {},
                                    variant = AuraIconButtonVariant.Ghost,
                                    contentDescription = "More options",
                                ) {
                                    AuraIcon(Icons.Filled.MoreVert, contentDescription = null)
                                }
                            }
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
                    AuraText(text = "Variant", style = t.titleSmall)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        AuraIconButtonVariant.entries.forEach { variant ->
                            AuraChipButton(
                                label = variant.name,
                                selected = selectedVariant == variant,
                                onClick = { selectedVariant = variant },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    AuraText(text = "Size (Min Hit Target: 48.dp)", style = t.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraIconButtonSize.entries.forEach { size ->
                            AuraChipButton(
                                label = size.name,
                                selected = selectedSize == size,
                                onClick = { selectedSize = size },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    AuraText(text = "Shape", style = t.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraIconButtonShape.entries.forEach { shape ->
                            AuraChipButton(
                                label = shape.name,
                                selected = selectedShape == shape,
                                onClick = { selectedShape = shape },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    AuraText(text = "Selection State", style = t.titleSmall)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        ToggleMode.entries.forEach { mode ->
                            AuraChipButton(
                                label = mode.label,
                                selected = toggleMode == mode,
                                onClick = { toggleMode = mode },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    IconSwitchRow("Enabled", enabled) { enabled = it }
                    IconSwitchRow("Loading Spinner", loading) { loading = it }
                    IconSwitchRow("Gradient / Photo Backdrop", photoBackdrop) { photoBackdrop = it }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(c.surface.sunken)
                            .border(1.dp, c.border.subtle, RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        AuraText(
                            text = "Responsive Viewport Readout",
                            style = t.labelLarge,
                        )
                        AuraText(
                            text = "Profile: ${window.widthClass.name} / ${window.displayRatio.name} (${window.widthDp}×${window.heightDp} dp)",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                        AuraText(
                            text = "IBTN-RULE-003 guarantees >= 48.dp touch bounds even when Small (32.dp) visual frame is active.",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                    }
                }
            },
        )
    }
}

@Composable
private fun IconSwitchRow(
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

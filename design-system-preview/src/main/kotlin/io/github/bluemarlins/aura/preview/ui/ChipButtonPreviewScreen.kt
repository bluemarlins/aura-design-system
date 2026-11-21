package io.github.bluemarlins.aura.preview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalBar
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
import io.github.bluemarlins.aura.component.AuraAdaptiveTwoPane
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
import io.github.bluemarlins.aura.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChipButtonPreviewScreen(onBack: () -> Unit) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout

    var selectedVariant by remember { mutableStateOf(AuraChipVariant.Filter) }
    var selectedSize by remember { mutableStateOf(AuraChipSize.Medium) }
    var isSelected by remember { mutableStateOf(true) }
    var enabled by remember { mutableStateOf(true) }
    var showLeadingIcon by remember { mutableStateOf(true) }
    var showTrailingIcon by remember { mutableStateOf(false) }
    var clickCount by remember { mutableIntStateOf(0) }

    var activeFinanceFilters by remember { mutableStateOf(setOf("All")) }
    var activeTastingTags by remember { mutableStateOf(setOf("Smoky", "Oak")) }
    var activeGlassLayer by remember { mutableStateOf("Clock") }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = "AuraChipButton",
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
                    AuraText(text = "Interactive Specimen", style = t.titleMedium)

                    val useGlassBackdrop = selectedVariant == AuraChipVariant.Glass
                    val stageBg = if (useGlassBackdrop) {
                        Modifier.background(
                            Brush.horizontalGradient(
                                colors = listOf(c.brand.primary, c.status.info.solid, c.surface.inverse),
                            ),
                        )
                    } else {
                        Modifier.background(c.surface.raised)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(148.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, c.border.subtle, RoundedCornerShape(16.dp))
                            .then(stageBg),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val lead: (@Composable () -> Unit)? = if (showLeadingIcon) {
                                {
                                    AuraIcon(
                                        imageVector = if (isSelected) Icons.Filled.Check else Icons.Filled.Tune,
                                        contentDescription = null,
                                    )
                                }
                            } else {
                                null
                            }
                            val trail: (@Composable () -> Unit)? = if (showTrailingIcon) {
                                {
                                    AuraIcon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = null,
                                    )
                                }
                            } else {
                                null
                            }

                            AuraChipButton(
                                label = "${selectedVariant.name} Chip",
                                selected = isSelected,
                                onClick = {
                                    isSelected = !isSelected
                                    clickCount++
                                },
                                variant = selectedVariant,
                                size = selectedSize,
                                enabled = enabled,
                                leadingIcon = lead,
                                trailingIcon = trail,
                            )

                            AuraText(
                                text = "Selected: $isSelected · Taps: $clickCount",
                                style = t.labelMedium,
                                color = if (useGlassBackdrop) c.brand.onPrimary else c.content.secondary,
                            )
                        }
                    }

                    AuraText(text = "My Finance · Scrollable Filter Strip", style = t.titleMedium)

                    val financeCategories = listOf("All", "Dining", "Transit", "Housing", "Subscriptions", "Income")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(financeCategories) { category ->
                            val isCatSelected = category in activeFinanceFilters
                            AuraChipButton(
                                label = category,
                                selected = isCatSelected,
                                variant = AuraChipVariant.Filter,
                                leadingIcon = if (isCatSelected) {
                                    { AuraIcon(Icons.Filled.Check, contentDescription = null) }
                                } else {
                                    null
                                },
                                onClick = {
                                    activeFinanceFilters = if (category == "All") {
                                        setOf("All")
                                    } else {
                                        val next = (activeFinanceFilters - "All").let { cur ->
                                            if (category in cur) cur - category else cur + category
                                        }
                                        next.ifEmpty { setOf("All") }
                                    }
                                },
                            )
                        }
                    }

                    AuraText(text = "My Drink Diary · Tasting Note Tags", style = t.titleMedium)

                    val tastingNotes = listOf("Smoky", "Oak", "Dark Berry", "Vanilla", "Peat", "Citrus")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        tastingNotes.forEach { note ->
                            val active = note in activeTastingTags
                            AuraChipButton(
                                label = note,
                                selected = active,
                                variant = AuraChipVariant.Tag,
                                size = AuraChipSize.Small,
                                leadingIcon = {
                                    AuraIcon(Icons.Filled.LocalBar, contentDescription = null)
                                },
                                onClick = {
                                    activeTastingTags = if (active) {
                                        activeTastingTags - note
                                    } else {
                                        activeTastingTags + note
                                    }
                                },
                            )
                        }
                    }

                    AuraText(text = "Home Launcher & Planner · Glass + Assist", style = t.titleMedium)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(c.surface.inverse, c.brand.primaryPressed),
                                ),
                            )
                            .padding(16.dp),
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                        ) {
                            listOf("Clock", "Depth", "Widgets").forEach { layer ->
                                AuraChipButton(
                                    label = layer,
                                    selected = activeGlassLayer == layer,
                                    variant = AuraChipVariant.Glass,
                                    size = AuraChipSize.Small,
                                    onClick = { activeGlassLayer = layer },
                                )
                            }
                            AuraChipButton(
                                label = "Today",
                                variant = AuraChipVariant.Assist,
                                size = AuraChipSize.Small,
                                leadingIcon = {
                                    AuraIcon(Icons.Filled.CalendarToday, contentDescription = null)
                                },
                                onClick = { activeGlassLayer = "Today" },
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
                    AuraText(text = "Variant", style = t.titleSmall)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        AuraChipVariant.entries.forEach { variant ->
                            AuraChipButton(
                                label = variant.name,
                                selected = selectedVariant == variant,
                                onClick = { selectedVariant = variant },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    AuraText(text = "Size (48.dp Touch Wrapper)", style = t.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraChipSize.entries.forEach { size ->
                            AuraChipButton(
                                label = size.name,
                                selected = selectedSize == size,
                                onClick = { selectedSize = size },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    ChipSwitchRow("Selected", isSelected) { isSelected = it }
                    ChipSwitchRow("Enabled", enabled) { enabled = it }
                    ChipSwitchRow("Leading Icon", showLeadingIcon) { showLeadingIcon = it }
                    ChipSwitchRow("Trailing Icon", showTrailingIcon) { showTrailingIcon = it }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(c.surface.sunken)
                            .border(1.dp, c.border.subtle, RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        AuraText(text = "Design Rules Check", style = t.labelLarge)
                        AuraText(
                            text = "CHIP-RULE-001: Single-line label with ellipsis; never wrap paragraphs inside chips.",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                        AuraText(
                            text = "CHIP-RULE-004: Small (28.dp) & Medium (34.dp) pills sit centered in >= 48.dp touch targets.",
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
private fun ChipSwitchRow(
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

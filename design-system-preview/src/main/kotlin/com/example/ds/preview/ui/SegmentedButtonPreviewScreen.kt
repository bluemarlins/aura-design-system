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
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
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
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraSegmentedButton
import com.example.ds.component.AuraSegmentedSize
import com.example.ds.component.AuraSegmentedVariant
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SegmentedButtonPreviewScreen(onBack: () -> Unit) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout
    val window = AuraTheme.window

    var selectedVariant by remember { mutableStateOf(AuraSegmentedVariant.Surface) }
    var selectedSize by remember { mutableStateOf(AuraSegmentedSize.Medium) }
    var fullWidth by remember { mutableStateOf(true) }
    var showIcons by remember { mutableStateOf(true) }
    var enabled by remember { mutableStateOf(true) }

    var plannerIndex by remember { mutableIntStateOf(1) }
    var financeRangeIndex by remember { mutableIntStateOf(1) }
    var financeCurrencyIndex by remember { mutableIntStateOf(0) }
    var drinkCategoryIndex by remember { mutableIntStateOf(0) }
    var launcherModeIndex by remember { mutableIntStateOf(0) }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = "AuraSegmentedButton",
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

                    val useGlassStage = selectedVariant == AuraSegmentedVariant.Glass
                    val stageBg = if (useGlassStage) {
                        Modifier.background(
                            Brush.linearGradient(
                                colors = listOf(c.surface.inverse, c.brand.primaryPressed, c.status.info.solid),
                            ),
                        )
                    } else {
                        Modifier.background(c.surface.raised)
                    }

                    val plannerOptions = listOf("Day", "Week", "Month")
                    val plannerIcons: List<(@Composable () -> Unit)?>? = if (showIcons) {
                        listOf(
                            { AuraIcon(Icons.Filled.CalendarViewDay, contentDescription = null) },
                            { AuraIcon(Icons.Filled.CalendarViewWeek, contentDescription = null) },
                            { AuraIcon(Icons.Filled.CalendarViewMonth, contentDescription = null) },
                        )
                    } else {
                        null
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, c.border.subtle, RoundedCornerShape(16.dp))
                            .then(stageBg)
                            .padding(20.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            AuraSegmentedButton(
                                options = plannerOptions,
                                selectedIndex = plannerIndex,
                                onOptionSelected = { plannerIndex = it },
                                variant = selectedVariant,
                                size = selectedSize,
                                enabled = enabled,
                                fullWidth = fullWidth,
                                optionIcons = plannerIcons,
                            )
                            AuraText(
                                text = "My Planner View: ${plannerOptions[plannerIndex]} · ${selectedVariant.name}",
                                style = t.labelMedium,
                                color = if (useGlassStage) c.brand.onPrimary else c.content.secondary,
                            )
                        }
                    }

                    AuraText(text = "My Finance · Timeframe & Currency Switchers", style = t.titleMedium)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(c.surface.raised)
                            .border(1.dp, c.border.subtle, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        val ranges = listOf("1W", "1M", "1Y", "All")
                        val currencies = listOf("KRW", "USD")
                        val balanceText = when (currencies[financeCurrencyIndex]) {
                            "KRW" -> when (ranges[financeRangeIndex]) {
                                "1W" -> "₩ 48,250,000 (+1.4%)"
                                "1M" -> "₩ 48,250,000 (+4.8%)"
                                "1Y" -> "₩ 48,250,000 (+16.2%)"
                                else -> "₩ 48,250,000 (+32.0%)"
                            }
                            else -> when (ranges[financeRangeIndex]) {
                                "1W" -> "$ 35,120 (+1.4%)"
                                "1M" -> "$ 35,120 (+4.8%)"
                                "1Y" -> "$ 35,120 (+16.2%)"
                                else -> "$ 35,120 (+32.0%)"
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AuraText(
                                text = balanceText,
                                style = t.titleMedium,
                            )
                            AuraSegmentedButton(
                                options = currencies,
                                selectedIndex = financeCurrencyIndex,
                                onOptionSelected = { financeCurrencyIndex = it },
                                variant = AuraSegmentedVariant.Outlined,
                                size = AuraSegmentedSize.Small,
                                fullWidth = false,
                            )
                        }

                        AuraSegmentedButton(
                            options = ranges,
                            selectedIndex = financeRangeIndex,
                            onOptionSelected = { financeRangeIndex = it },
                            variant = AuraSegmentedVariant.Surface,
                            size = AuraSegmentedSize.Small,
                        )
                    }

                    AuraText(text = "My Drink Diary · Category Switcher", style = t.titleMedium)

                    val drinkOptions = listOf("All", "Wine", "Whiskey")
                    AuraSegmentedButton(
                        options = drinkOptions,
                        selectedIndex = drinkCategoryIndex,
                        onOptionSelected = { drinkCategoryIndex = it },
                        variant = AuraSegmentedVariant.Outlined,
                    )

                    AuraText(text = "Home Launcher · Glass Canvas Switcher", style = t.titleMedium)

                    val launcherOptions = listOf("Icons", "Widgets", "3D")
                    val launcherIcons: List<(@Composable () -> Unit)?> = listOf(
                        { AuraIcon(Icons.Filled.Apps, contentDescription = null) },
                        { AuraIcon(Icons.Filled.Widgets, contentDescription = null) },
                        { AuraIcon(Icons.Filled.ViewInAr, contentDescription = null) },
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(c.surface.inverse, c.brand.primaryPressed),
                                ),
                            )
                            .padding(16.dp),
                    ) {
                        AuraSegmentedButton(
                            options = launcherOptions,
                            selectedIndex = launcherModeIndex,
                            onOptionSelected = { launcherModeIndex = it },
                            variant = AuraSegmentedVariant.Glass,
                            optionIcons = launcherIcons,
                        )
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
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraSegmentedVariant.entries.forEach { variant ->
                            AuraChipButton(
                                label = variant.name,
                                selected = selectedVariant == variant,
                                onClick = { selectedVariant = variant },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    AuraText(text = "Size (48.dp Touch Height)", style = t.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraSegmentedSize.entries.forEach { size ->
                            AuraChipButton(
                                label = size.name,
                                selected = selectedSize == size,
                                onClick = { selectedSize = size },
                                size = AuraChipSize.Small,
                            )
                        }
                    }

                    SegmentedSwitchRow("Full Width", fullWidth) { fullWidth = it }
                    SegmentedSwitchRow("Option Icons", showIcons) { showIcons = it }
                    SegmentedSwitchRow("Enabled", enabled) { enabled = it }

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
                            text = "Profile: ${window.widthClass.name} / ${window.displayRatio.name} (${window.widthDp}×${window.heightDp} dp)",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                        AuraText(
                            text = "SEG-RULE-001: Reserve AuraSegmentedButton for 2–5 fixed mutually exclusive choices with immediate effect.",
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
private fun SegmentedSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AuraText(
            text = label,
            style = AuraTheme.typography.bodyMedium,
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

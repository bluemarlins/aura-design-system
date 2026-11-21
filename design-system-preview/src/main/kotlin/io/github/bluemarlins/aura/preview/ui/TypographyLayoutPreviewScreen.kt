package io.github.bluemarlins.aura.preview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.component.AuraAdaptiveGrid
import io.github.bluemarlins.aura.component.AuraAdaptiveTwoPane
import io.github.bluemarlins.aura.component.AuraChipButton
import io.github.bluemarlins.aura.component.AuraChipSize
import io.github.bluemarlins.aura.component.AuraChipVariant
import io.github.bluemarlins.aura.component.AuraIcon
import io.github.bluemarlins.aura.component.AuraIconButton
import io.github.bluemarlins.aura.component.AuraIconButtonVariant
import io.github.bluemarlins.aura.component.AuraScaffold
import io.github.bluemarlins.aura.component.AuraText
import io.github.bluemarlins.aura.foundation.layout.AuraLayoutDefaults
import io.github.bluemarlins.aura.foundation.layout.AuraWindowInfo
import io.github.bluemarlins.aura.foundation.theme.AuraTheme
import java.util.Locale

private enum class WindowPreset(
    val label: String,
    val simulatedWindow: AuraWindowInfo?,
) {
    Auto("Auto (Device)", null),
    Cover("Cover 344×820", AuraWindowInfo.fromDimensions(344, 344, 820)),
    Phone("Phone 412×915", AuraWindowInfo.fromDimensions(412, 412, 915)),
    FoldSquare("Square 710×760", AuraWindowInfo.fromDimensions(690, 710, 760)),
    PhoneLandscape("Land 915×412", AuraWindowInfo.fromDimensions(412, 915, 412)),
    Tablet("Tablet 1280×800", AuraWindowInfo.fromDimensions(840, 1280, 800)),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypographyLayoutPreviewScreen(
    onBack: () -> Unit,
) {
    val c = AuraTheme.colors
    val liveWindow = AuraTheme.window
    var selectedPreset by remember { mutableStateOf(WindowPreset.Auto) }
    val activeWindow = selectedPreset.simulatedWindow ?: liveWindow
    val activeLayout = AuraLayoutDefaults.fromWindowInfo(activeWindow)

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = "AuraTypography, Shapes & Layout",
                        style = AuraTheme.typography.titleLarge,
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
                LayoutTelemetryPane(
                    selectedPreset = selectedPreset,
                    onSelectPreset = { selectedPreset = it },
                    activeWindow = activeWindow,
                )
            },
            secondaryPane = {
                AuraTheme(
                    colorMode = AuraTheme.colorMode,
                    windowInfo = activeWindow,
                    layout = activeLayout,
                ) {
                    TypographySpecimensPane()
                }
            },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LayoutTelemetryPane(
    selectedPreset: WindowPreset,
    onSelectPreset: (WindowPreset) -> Unit,
    activeWindow: AuraWindowInfo,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val simulatedLayout = AuraLayoutDefaults.fromWindowInfo(activeWindow)
    val cardShape = RoundedCornerShape(16.dp)

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AuraText(
            text = "2-Axis Breakpoint Simulator",
            style = t.titleMedium,
        )
        AuraText(
            text = "Combines Smallest Width (sw) and Display Ratio (width / height) to classify cover screens, ~1:1 unfolded foldables, landscape phones, and tablets.",
            style = t.bodySmall,
            color = c.content.secondary,
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            WindowPreset.entries.forEach { preset ->
                AuraChipButton(
                    label = preset.label,
                    selected = selectedPreset == preset,
                    variant = AuraChipVariant.Filter,
                    size = AuraChipSize.Small,
                    onClick = { onSelectPreset(preset) },
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface.default, cardShape)
                .border(1.dp, c.border.subtle, cardShape)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AuraText(
                text = if (selectedPreset == WindowPreset.Auto) "Live Device Window" else "Simulated Window (${selectedPreset.label})",
                style = t.titleSmall,
            )
            TelemetryRow("smallestWidth", "${activeWindow.smallestWidthDp}dp (${activeWindow.smallestWidthClass})")
            TelemetryRow("viewport", "${activeWindow.widthDp} × ${activeWindow.heightDp} dp (${activeWindow.widthClass})")
            TelemetryRow(
                "displayRatio",
                String.format(Locale.US, "%.2f · %s", activeWindow.aspectRatio, activeWindow.displayRatio),
            )
            TelemetryRow("margins (H / V)", "${simulatedLayout.screenMarginHorizontal.value.toInt()}dp / ${simulatedLayout.screenMarginVertical.value.toInt()}dp")
            TelemetryRow("gridColumns / gutter", "${simulatedLayout.gridColumns} cols · ${simulatedLayout.gridGutter.value.toInt()}dp")
            TelemetryRow("supportsTwoPane", "${simulatedLayout.supportsTwoPane} (primary=${(simulatedLayout.primaryPaneFraction * 100).toInt()}%)")
            TelemetryRow("topBarHeight", "${simulatedLayout.topBarHeight.value.toInt()}dp")
            TelemetryRow("typographyScale", String.format(Locale.US, "%.2fx", simulatedLayout.typographyScale))
        }

        AuraTheme(
            colorMode = AuraTheme.colorMode,
            windowInfo = activeWindow,
            layout = simulatedLayout,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AuraText(
                    text = "Simulated Grid Preview (${simulatedLayout.gridColumns} Columns)",
                    style = AuraTheme.typography.titleSmall,
                )
                AuraAdaptiveGrid(
                    items = (1..6).map { "Slot $it" },
                    columns = simulatedLayout.gridColumns,
                    horizontalSpacing = simulatedLayout.gridGutter,
                    verticalSpacing = simulatedLayout.gridGutter,
                ) { title ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(c.brand.subtle, RoundedCornerShape(10.dp))
                            .border(1.dp, c.border.subtle, RoundedCornerShape(10.dp))
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        AuraText(
                            text = title,
                            style = AuraTheme.typography.labelMedium,
                            color = c.brand.onSubtle,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TelemetryRow(label: String, value: String) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AuraText(
            text = label,
            style = t.bodySmall,
            color = c.content.secondary,
        )
        AuraText(
            text = value,
            style = t.monoSmall,
            color = c.content.primary,
        )
    }
}

@Composable
private fun TypographySpecimensPane() {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val cardShape = RoundedCornerShape(16.dp)

    val specimens = listOf(
        Triple("displayLarge", t.displayLarge, "₩128,450,000"),
        Triple("displayMedium", t.displayMedium, "+14.8% YTD"),
        Triple("headlineLarge", t.headlineLarge, "Daily Planner"),
        Triple("headlineMedium", t.headlineMedium, "Springbank 15Y"),
        Triple("headlineSmall", t.headlineSmall, "Tasting Summary"),
        Triple("titleLarge", t.titleLarge, "Portfolio Overview"),
        Triple("titleMedium", t.titleMedium, "Monthly Budget"),
        Triple("titleSmall", t.titleSmall, "Cask & Vintage"),
        Triple("bodyLarge", t.bodyLarge, "Warm oak, salted caramel, and lingering peat smoke."),
        Triple("bodyMedium", t.bodyMedium, "Clean blue-led clarity inspired by One UI surface hierarchy."),
        Triple("bodySmall", t.bodySmall, "Updated 2 minutes ago · WCAG AA verified"),
        Triple("labelLarge", t.labelLarge, "Save Tasting Note"),
        Triple("labelMedium", t.labelMedium, "Foundation & Tokens"),
        Triple("labelSmall", t.labelSmall, "READY · SOLID"),
        Triple("monoMedium", t.monoMedium, "00:42:18.09 · 46.0%"),
        Triple("monoSmall", t.monoSmall, "#FF2F6DD8 · tnum"),
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AuraText(
            text = "AuraTypography Scale (${String.format(Locale.US, "%.2fx", AuraTheme.layout.typographyScale)} Headings)",
            style = t.titleMedium,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface.default, cardShape)
                .border(1.dp, c.border.subtle, cardShape)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            specimens.forEach { (tokenName, style, sample) ->
                TypographySpecimenRow(
                    tokenName = tokenName,
                    style = style,
                    sample = sample,
                )
            }
        }

        val shapes = AuraTheme.shapes
        val shapeTokens = listOf(
            Triple("extraSmall", "4.dp", shapes.extraSmall),
            Triple("small", "8.dp", shapes.small),
            Triple("medium", "12.dp", shapes.medium),
            Triple("large", "16.dp", shapes.large),
            Triple("extraLarge", "24.dp", shapes.extraLarge),
            Triple("full", "CircleShape", shapes.full),
        )
        AuraText(
            text = "AuraShapes Corner Tokens",
            style = t.titleMedium,
            modifier = Modifier.padding(top = 8.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface.default, cardShape)
                .border(1.dp, c.border.subtle, cardShape)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            shapeTokens.forEach { (name, radiusLabel, shape) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(c.brand.subtle, shape)
                            .border(1.dp, c.brand.primary, shape)
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        AuraText(
                            text = "AuraTheme.shapes.$name",
                            style = t.monoSmall,
                            color = c.brand.onSubtle,
                        )
                    }
                    AuraText(
                        text = radiusLabel,
                        style = t.monoSmall,
                        color = c.content.tertiary,
                    )
                }
            }
        }
    }
}

@Composable
private fun TypographySpecimenRow(
    tokenName: String,
    style: TextStyle,
    sample: String,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AuraText(
                text = tokenName,
                style = t.monoSmall,
                color = c.brand.primary,
            )
            AuraText(
                text = "${style.fontSize.value.toInt()}sp / ${style.lineHeight.value.toInt()}sp",
                style = t.monoSmall,
                color = c.content.tertiary,
            )
        }
        AuraText(
            text = sample,
            style = style,
            color = c.content.primary,
        )
    }
}

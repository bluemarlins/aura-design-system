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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraAdaptiveTwoPane
import com.example.ds.component.AuraBadge
import com.example.ds.component.AuraBadgeSize
import com.example.ds.component.AuraBadgeVariant
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraChipButton
import com.example.ds.component.AuraChipSize
import com.example.ds.component.AuraChipVariant
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraSearchBar
import com.example.ds.component.AuraSearchBarSurface
import com.example.ds.component.AuraSwitch
import com.example.ds.component.AuraSwitchSize
import com.example.ds.component.AuraText
import com.example.ds.component.GlassStyle
import com.example.ds.component.LocalGlassBackdropState
import com.example.ds.component.glassBackdropSource
import com.example.ds.component.rememberGlassBackdropState
import com.example.ds.foundation.theme.AuraTheme

private data class SampleCellarBottle(
    val title: String,
    val region: String,
    val badge: String,
)

private val SampleCellarList = listOf(
    SampleCellarBottle("Barolo Riserva 2018", "Piedmont · Nebbiolo", "94 pts"),
    SampleCellarBottle("Lagavulin 16 Year", "Islay · Peated Single Malt", "43%"),
    SampleCellarBottle("Chablis Premier Cru", "Burgundy · Chardonnay", "Crisp"),
    SampleCellarBottle("Yamazaki 12 Year", "Kyoto · Japanese Malt", "Archive"),
    SampleCellarBottle("Rioja Gran Reserva", "Spain · Tempranillo", "Ready"),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchBarPreviewScreen(
    onBack: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var surface by remember { mutableStateOf(AuraSearchBarSurface.Solid) }
    var glassStyle by remember { mutableStateOf(GlassStyle.LIQUID) }
    var filterCount by remember { mutableIntStateOf(2) }
    var enabled by remember { mutableStateOf(true) }
    var lastSubmitted by remember { mutableStateOf("—") }

    val c = AuraTheme.colors
    val t = AuraTheme.typography

    val matchingBottles = remember(query) {
        if (query.isBlank()) {
            SampleCellarList
        } else {
            SampleCellarList.filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.region.contains(query, ignoreCase = true)
            }
        }
    }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = { AuraText(text = "AuraSearchBar", style = t.titleLarge) },
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
                    AuraText(text = "Interactive Surface Playground", style = t.titleMedium)

                    val backdropState = rememberGlassBackdropState()
                    CompositionLocalProvider(LocalGlassBackdropState provides backdropState) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp)),
                        ) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .glassBackdropSource(backdropState)
                                    .background(
                                        if (surface == AuraSearchBarSurface.Glass) {
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    c.brand.primary.copy(alpha = 0.85f),
                                                    c.status.info.solid.copy(alpha = 0.80f),
                                                    c.status.warning.solid.copy(alpha = 0.72f),
                                                ),
                                            )
                                        } else {
                                            Brush.linearGradient(
                                                colors = listOf(c.surface.default, c.surface.default),
                                            )
                                        },
                                    ),
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                AuraSearchBar(
                                    query = query,
                                    onQueryChange = { query = it },
                                    onSearch = { lastSubmitted = it.ifBlank { "(empty)" } },
                                    placeholder = "Search bottles, regions, notes...",
                                    surface = surface,
                                    glassStyle = glassStyle,
                                    enabled = enabled,
                                    activeFilterCount = filterCount,
                                    onFilterClick = { filterCount = (filterCount + 1) % 5 },
                                )
                                AuraText(
                                    text = "Last IME Submit: $lastSubmitted · Tap filter icon to cycle count",
                                    style = t.monoSmall,
                                    color = if (surface == AuraSearchBarSurface.Glass) Color.White else c.content.secondary,
                                )
                            }
                        }
                    }

                    AuraText(text = "Surface Material", style = t.labelLarge)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraSearchBarSurface.entries.forEach { mode ->
                            AuraChipButton(
                                label = mode.name,
                                selected = surface == mode,
                                variant = AuraChipVariant.Filter,
                                size = AuraChipSize.Small,
                                onClick = { surface = mode },
                            )
                        }
                    }

                    if (surface == AuraSearchBarSurface.Glass) {
                        AuraText(text = "Glass Optics", style = t.labelLarge)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GlassStyle.entries.forEach { preset ->
                                AuraChipButton(
                                    label = preset.name,
                                    selected = glassStyle == preset,
                                    variant = AuraChipVariant.Filter,
                                    size = AuraChipSize.Small,
                                    onClick = { glassStyle = preset },
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(c.surface.default)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AuraText(text = "Enabled", style = t.bodyMedium)
                        AuraSwitch(
                            checked = enabled,
                            onCheckedChange = { enabled = it },
                            size = AuraSwitchSize.Small,
                        )
                    }
                }
            },
            secondaryPane = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AuraText(text = "Live Cellar Filter", style = t.titleMedium)
                        AuraBadge(
                            label = "${matchingBottles.size} results",
                            variant = AuraBadgeVariant.Brand,
                            size = AuraBadgeSize.Small,
                        )
                    }

                    matchingBottles.forEach { item ->
                        AuraCard(
                            variant = AuraCardVariant.Outlined,
                            padding = AuraCardPadding.Compact,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    AuraText(text = item.title, style = t.titleSmall)
                                    AuraText(text = item.region, style = t.bodySmall, color = c.content.secondary)
                                }
                                AuraBadge(
                                    label = item.badge,
                                    variant = AuraBadgeVariant.Info,
                                    size = AuraBadgeSize.Small,
                                )
                            }
                        }
                    }
                }
            },
        )
    }
}

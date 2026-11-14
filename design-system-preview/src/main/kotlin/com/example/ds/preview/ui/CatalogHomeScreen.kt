package com.example.ds.preview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.example.ds.component.AuraAdaptiveGrid
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraChipButton
import com.example.ds.component.AuraChipVariant
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme
import com.example.ds.preview.catalog.CatalogCategory
import com.example.ds.preview.catalog.CatalogItem
import com.example.ds.preview.catalog.CatalogStatus
import com.example.ds.preview.catalog.ComponentCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogHomeScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onSelectItem: (CatalogItem) -> Unit,
) {
    var selectedCategory by remember { mutableStateOf<CatalogCategory?>(null) }
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout
    val window = AuraTheme.window

    val filteredItems = remember(selectedCategory) {
        if (selectedCategory == null) {
            ComponentCatalog.items
        } else {
            ComponentCatalog.items.filter { it.category == selectedCategory }
        }
    }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = "Aura Preview",
                        style = t.titleLarge,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = c.background.primary,
                    titleContentColor = c.content.primary,
                    actionIconContentColor = c.content.primary,
                ),
                actions = {
                    AuraIconButton(
                        onClick = onToggleTheme,
                        variant = AuraIconButtonVariant.Ghost,
                        contentDescription = if (isDarkTheme) "Switch to light theme" else "Switch to dark theme",
                    ) {
                        AuraIcon(
                            imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            val widthConstraint = if (layout.maxContentWidth.isSpecified) {
                Modifier.widthIn(max = layout.maxContentWidth)
            } else {
                Modifier
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(widthConstraint),
            ) {
                LazyRow(
                    contentPadding = PaddingValues(
                        horizontal = layout.screenMarginHorizontal,
                        vertical = 2.dp,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {
                        AuraChipButton(
                            label = "All",
                            selected = selectedCategory == null,
                            variant = AuraChipVariant.Filter,
                            onClick = { selectedCategory = null },
                        )
                    }
                    items(CatalogCategory.entries) { category ->
                        AuraChipButton(
                            label = category.label,
                            selected = selectedCategory == category,
                            variant = AuraChipVariant.Filter,
                            onClick = {
                                selectedCategory = if (selectedCategory == category) null else category
                            },
                        )
                    }
                }

                AuraText(
                    text = "${window.widthClass} · ${window.displayRatio} (${layout.gridColumns} col)",
                    style = t.monoSmall,
                    color = c.content.tertiary,
                    modifier = Modifier.padding(
                        horizontal = layout.screenMarginHorizontal,
                        vertical = 2.dp,
                    ),
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = layout.screenMarginHorizontal,
                            vertical = layout.screenMarginVertical,
                        ),
                ) {
                    AuraAdaptiveGrid(items = filteredItems) { item ->
                        CatalogItemCard(
                            item = item,
                            onClick = { onSelectItem(item) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogItemCard(
    item: CatalogItem,
    onClick: () -> Unit,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography

    AuraCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        variant = AuraCardVariant.Outlined,
        padding = AuraCardPadding.Medium,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AuraText(
                text = item.name,
                style = t.titleMedium,
                modifier = Modifier.weight(1f),
            )
            StatusBadge(status = item.status)
        }
        Spacer(modifier = Modifier.height(4.dp))
        AuraText(
            text = item.category.label,
            style = t.labelMedium,
            color = c.brand.primary,
        )
        Spacer(modifier = Modifier.height(6.dp))
        AuraText(
            text = item.description,
            style = t.bodyMedium,
            color = c.content.secondary,
        )
    }
}

@Composable
private fun StatusBadge(status: CatalogStatus) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val containerColor = when (status) {
        CatalogStatus.Ready -> c.brand.subtle
        CatalogStatus.Planned -> c.surface.sunken
    }
    val contentColor = when (status) {
        CatalogStatus.Ready -> c.brand.onSubtle
        CatalogStatus.Planned -> c.content.secondary
    }

    Box(
        modifier = Modifier
            .background(containerColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        AuraText(
            text = status.label,
            style = t.labelSmall,
            color = contentColor,
        )
    }
}

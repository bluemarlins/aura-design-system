package com.example.ds.preview.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    val filteredItems = remember(selectedCategory) {
        if (selectedCategory == null) {
            ComponentCatalog.items
        } else {
            ComponentCatalog.items.filter { it.category == selectedCategory }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DS Preview") },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = if (isDarkTheme) "Switch to light theme" else "Switch to dark theme",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All") },
                    )
                }
                items(CatalogCategory.entries) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = if (selectedCategory == category) null else category
                        },
                        label = { Text(category.label) },
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    CatalogItemCard(
                        item = item,
                        onClick = { onSelectItem(item) },
                    )
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                StatusBadge(status = item.status)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.category.label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun StatusBadge(status: CatalogStatus) {
    val containerColor = when (status) {
        CatalogStatus.Ready -> MaterialTheme.colorScheme.primaryContainer
        CatalogStatus.Planned -> MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val contentColor = when (status) {
        CatalogStatus.Ready -> MaterialTheme.colorScheme.onPrimaryContainer
        CatalogStatus.Planned -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = containerColor,
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = status.label,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

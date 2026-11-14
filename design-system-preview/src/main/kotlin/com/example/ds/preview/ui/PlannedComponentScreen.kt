package com.example.ds.preview.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme
import com.example.ds.preview.catalog.CatalogItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannedComponentScreen(
    item: CatalogItem?,
    onBack: () -> Unit,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = item?.name ?: "Component",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            val widthConstraint = if (layout.maxContentWidth.isSpecified) {
                Modifier.widthIn(max = layout.maxContentWidth)
            } else {
                Modifier
            }
            Column(
                modifier = Modifier
                    .then(widthConstraint)
                    .padding(horizontal = layout.screenMarginHorizontal, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AuraText(
                    text = item?.name ?: "Unknown Component",
                    style = t.headlineSmall,
                    textAlign = TextAlign.Center,
                )
                if (item != null) {
                    AuraText(
                        text = item.category.label,
                        style = t.labelLarge,
                        color = c.brand.primary,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    AuraText(
                        text = item.description,
                        style = t.bodyMedium,
                        color = c.content.secondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
                AuraText(
                    text = "Planned — not yet implemented in :design-system.",
                    style = t.bodySmall,
                    color = c.content.tertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 24.dp),
                )
            }
        }
    }
}

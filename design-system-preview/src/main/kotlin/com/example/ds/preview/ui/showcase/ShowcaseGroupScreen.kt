package com.example.ds.preview.ui.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraAdaptiveGrid
import com.example.ds.component.AuraBadge
import com.example.ds.component.AuraBadgeVariant
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowcaseGroupScreen(
    group: ShowcaseGroup,
    onBack: () -> Unit,
    onOpenComponent: (catalogId: String) -> Unit,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = { AuraText(text = group.title, style = t.titleLarge) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = layout.screenMarginHorizontal,
                    vertical = layout.screenMarginVertical,
                ),
            verticalArrangement = Arrangement.spacedBy(layout.sectionSpacing),
        ) {
            AuraText(
                text = group.subtitle,
                style = t.bodyMedium,
                color = c.content.secondary,
            )
            val numbered = group.comparisons.mapIndexed { index, comparison -> index + 1 to comparison }
            AuraAdaptiveGrid(items = numbered) { (number, comparison) ->
                ShowcaseComparisonCard(
                    number = number,
                    comparison = comparison,
                    onOpenComponent = onOpenComponent,
                )
            }
        }
    }
}

@Composable
private fun ShowcaseComparisonCard(
    number: Int,
    comparison: ShowcaseComparison,
    onOpenComponent: (catalogId: String) -> Unit,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val target = comparison.targetCatalogId

    AuraCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = target?.let { id -> { onOpenComponent(id) } },
        variant = AuraCardVariant.Outlined,
        padding = AuraCardPadding.Medium,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .background(c.surface.sunken, AuraTheme.shapes.medium)
                .padding(12.dp),
            contentAlignment = Alignment.Center,
        ) {
            val specimen = comparison.specimen
            if (comparison.status == ShowcaseStatus.Ready && specimen != null) {
                specimen()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    AuraBadge(label = "Planned", variant = AuraBadgeVariant.Neutral)
                    AuraText(
                        text = "Component not implemented yet",
                        style = t.bodySmall,
                        color = c.content.secondary,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AuraText(
                text = number.toString().padStart(2, '0'),
                style = t.titleMedium,
                color = c.brand.primary,
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                comparison.ko.forEachIndexed { index, name ->
                    if (index > 0) {
                        AuraText(text = "vs", style = t.labelMedium, color = c.content.tertiary)
                    }
                    AuraText(text = name, style = t.titleMedium)
                }
            }
        }
        AuraText(
            text = comparison.en.joinToString(" vs "),
            style = t.labelMedium,
            color = c.content.secondary,
        )

        Spacer(modifier = Modifier.height(10.dp))
        GuideLine(label = "Use", text = comparison.useWhen, labelColor = c.brand.primary)
        Spacer(modifier = Modifier.height(4.dp))
        GuideLine(label = "Avoid", text = comparison.avoidWhen, labelColor = c.content.secondary)
    }
}

@Composable
private fun GuideLine(
    label: String,
    text: String,
    labelColor: androidx.compose.ui.graphics.Color,
) {
    val t = AuraTheme.typography
    val c = AuraTheme.colors
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AuraText(text = label, style = t.labelMedium, color = labelColor)
        AuraText(
            text = text,
            style = t.bodySmall,
            color = c.content.secondary,
            modifier = Modifier.weight(1f),
        )
    }
}

package com.example.ds.preview.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraDivider
import com.example.ds.component.AuraDividerOrientation
import com.example.ds.component.AuraDividerTone
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DividerPreviewScreen(onBack: () -> Unit) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = { AuraText(text = "AuraDivider", style = t.titleLarge) },
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
            AuraCard(
                variant = AuraCardVariant.Outlined,
                padding = AuraCardPadding.Large,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AuraText(text = "Horizontal Tones", style = t.titleMedium)
                    AuraText(text = "Subtle (Default)", style = t.bodySmall, color = c.content.secondary)
                    AuraDivider(tone = AuraDividerTone.Subtle)
                    AuraText(text = "Default (With 16.dp Indent)", style = t.bodySmall, color = c.content.secondary)
                    AuraDivider(tone = AuraDividerTone.Default, startIndent = 16.dp, endIndent = 16.dp)
                    AuraText(text = "Strong", style = t.bodySmall, color = c.content.secondary)
                    AuraDivider(tone = AuraDividerTone.Strong)
                }
            }

            AuraCard(
                variant = AuraCardVariant.Outlined,
                padding = AuraCardPadding.Large,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AuraText(text = "Vertical Orientation", style = t.titleMedium)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AuraText(text = "4.5 ★", style = t.titleSmall)
                        AuraDivider(
                            orientation = AuraDividerOrientation.Vertical,
                            tone = AuraDividerTone.Default,
                        )
                        AuraText(text = "12 Notes", style = t.titleSmall)
                        AuraDivider(
                            orientation = AuraDividerOrientation.Vertical,
                            tone = AuraDividerTone.Default,
                        )
                        AuraText(text = "Repurchase", style = t.titleSmall)
                    }
                }
            }
        }
    }
}

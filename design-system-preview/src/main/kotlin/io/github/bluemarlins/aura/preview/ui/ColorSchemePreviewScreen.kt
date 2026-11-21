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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import io.github.bluemarlins.aura.component.AuraAdaptiveGrid
import io.github.bluemarlins.aura.component.AuraButton
import io.github.bluemarlins.aura.component.AuraIcon
import io.github.bluemarlins.aura.component.AuraIconButton
import io.github.bluemarlins.aura.component.AuraIconButtonVariant
import io.github.bluemarlins.aura.component.AuraScaffold
import io.github.bluemarlins.aura.component.AuraText
import io.github.bluemarlins.aura.foundation.color.AuraColors
import io.github.bluemarlins.aura.foundation.theme.AuraColorMode
import io.github.bluemarlins.aura.foundation.theme.AuraTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSchemePreviewScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onBack: () -> Unit,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = "AuraColors (${if (c.isDark) "Dark" else "Light"})",
                        style = t.titleLarge,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = c.background.primary,
                    titleContentColor = c.content.primary,
                    navigationIconContentColor = c.content.primary,
                    actionIconContentColor = c.content.primary,
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
        ColorSchemeShowcaseContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}

@Composable
private fun ColorSchemeShowcaseContent(modifier: Modifier = Modifier) {
    val c = AuraTheme.colors
    val layout = AuraTheme.layout
    val widthConstraint = if (layout.maxContentWidth.isSpecified) {
        Modifier.widthIn(max = layout.maxContentWidth)
    } else {
        Modifier
    }

    Box(
        modifier = modifier.background(c.background.primary),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(widthConstraint)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = layout.screenMarginHorizontal,
                    vertical = layout.screenMarginVertical,
                ),
            verticalArrangement = Arrangement.spacedBy(layout.sectionSpacing),
        ) {
            AuraAdaptiveGrid(
                items = listOf(0, 1),
                columns = minOf(layout.gridColumns, 2),
            ) { index ->
                if (index == 0) {
                    SurfaceDepthDemo()
                } else {
                    ComponentSampleCard()
                }
            }
            AuraAdaptiveGrid(items = c.roleGroups()) { (group, roles) ->
                RoleGroup(group = group, roles = roles)
            }
        }
    }
}

@Composable
private fun SurfaceDepthDemo() {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val cardShape = RoundedCornerShape(20.dp)
    val innerShape = RoundedCornerShape(14.dp)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionHeader("Surface Depth Hierarchy (One UI + Floating)")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface.default, cardShape)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AuraText(
                text = "surface.default over background.primary",
                style = t.titleSmall,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(c.surface.raised, innerShape)
                    .border(1.dp, c.border.subtle, innerShape)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AuraText(
                    text = "surface.raised (nested card / sheet)",
                    style = t.labelMedium,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(c.surface.sunken, RoundedCornerShape(10.dp))
                        .padding(10.dp),
                ) {
                    AuraText(
                        text = "surface.sunken (input field / recessed well)",
                        style = t.bodySmall,
                        color = c.content.secondary,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = if (c.isDark) 0.dp else 8.dp, shape = innerShape)
                    .background(c.surface.floating, innerShape)
                    .border(1.dp, c.border.default, innerShape)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    AuraText(
                        text = "surface.floating",
                        style = t.titleSmall,
                    )
                    AuraText(
                        text = "Bottom bar / popover — future LiquidGlass target",
                        style = t.labelSmall,
                        color = c.content.tertiary,
                    )
                }
                Pill("Glass next", c.brand.subtle, c.brand.onSubtle)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(c.surface.inverse, RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            ) {
                AuraText(
                    text = "surface.inverse / content.inverse (Toast / Snackbar)",
                    style = t.bodySmall,
                    color = c.content.inverse,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ComponentSampleCard() {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val shape = RoundedCornerShape(20.dp)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionHeader("Brand, Interaction & Status Preview")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface.default, shape)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            AuraText(text = "Aura Preview Card", style = t.titleMedium)
            AuraText(
                text = "Secondary copy explains context against surface.default.",
                style = t.bodyMedium,
                color = c.content.secondary,
            )
            AuraText(
                text = "Tertiary caption · disabled hint",
                style = t.bodySmall,
                color = c.content.tertiary,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AuraButton(text = "Primary CTA", onClick = {})
                AuraButton(text = "Disabled", onClick = {}, enabled = false)
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Pill("brand.subtle", c.brand.subtle, c.brand.onSubtle)
                Pill("selected", c.interaction.selectedContainer, c.interaction.onSelected)
                Pill("disabled", c.interaction.disabledContainer, c.content.disabled)
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                with(c.status) {
                    Pill("Error", error.solid, error.onSolid)
                    Pill("Success", success.solid, success.onSolid)
                    Pill("Warning", warning.solid, warning.onSolid)
                    Pill("Info", info.solid, info.onSolid)
                }
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                with(c.status) {
                    Pill("Error subtle", error.subtle, error.onSubtle)
                    Pill("Success subtle", success.subtle, success.onSubtle)
                    Pill("Warning subtle", warning.subtle, warning.onSubtle)
                    Pill("Info subtle", info.subtle, info.onSubtle)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    AuraText(
        text = title,
        style = AuraTheme.typography.titleMedium,
    )
}

@Composable
private fun Pill(label: String, container: Color, content: Color) {
    AuraText(
        text = label,
        color = content,
        style = AuraTheme.typography.labelSmall,
        modifier = Modifier
            .background(container, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}

@Composable
private fun RoleGroup(group: String, roles: List<Pair<String, Color>>) {
    val c = AuraTheme.colors
    val cardShape = RoundedCornerShape(16.dp)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeader(group)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface.default, cardShape)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            roles.forEach { (name, color) ->
                SwatchRow(token = "$group.$name", color = color)
            }
        }
    }
}

@Composable
private fun SwatchRow(token: String, color: Color) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val shape = RoundedCornerShape(8.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color, shape)
                .border(1.dp, c.border.default, shape),
        )
        Column {
            AuraText(text = token, style = t.labelMedium)
            AuraText(
                text = color.toHexLabel(),
                style = t.monoSmall,
                color = c.content.secondary,
            )
        }
    }
}

private fun Color.toHexLabel(): String = "#%08X".format(toArgb())

private fun AuraColors.StatusRole.swatches(): List<Pair<String, Color>> = listOf(
    "solid" to solid,
    "onSolid" to onSolid,
    "subtle" to subtle,
    "onSubtle" to onSubtle,
)

private fun AuraColors.roleGroups(): List<Pair<String, List<Pair<String, Color>>>> = listOf(
    "background" to listOf("primary" to background.primary, "secondary" to background.secondary),
    "surface" to listOf(
        "default" to surface.default,
        "raised" to surface.raised,
        "sunken" to surface.sunken,
        "floating" to surface.floating,
        "inverse" to surface.inverse,
    ),
    "content" to listOf(
        "primary" to content.primary,
        "secondary" to content.secondary,
        "tertiary" to content.tertiary,
        "disabled" to content.disabled,
        "inverse" to content.inverse,
    ),
    "brand" to listOf(
        "primary" to brand.primary,
        "onPrimary" to brand.onPrimary,
        "primaryPressed" to brand.primaryPressed,
        "subtle" to brand.subtle,
        "onSubtle" to brand.onSubtle,
    ),
    "border" to listOf(
        "subtle" to border.subtle,
        "default" to border.default,
        "strong" to border.strong,
        "focus" to border.focus,
    ),
    "status.error" to status.error.swatches(),
    "status.success" to status.success.swatches(),
    "status.warning" to status.warning.swatches(),
    "status.info" to status.info.swatches(),
    "interaction" to listOf(
        "pressedOverlay" to interaction.pressedOverlay,
        "selectedContainer" to interaction.selectedContainer,
        "onSelected" to interaction.onSelected,
        "disabledContainer" to interaction.disabledContainer,
        "scrim" to interaction.scrim,
    ),
)

@Preview(name = "Aura Light", widthDp = 380, heightDp = 2400)
@Composable
private fun AuraColorSchemeLightPreview() {
    AuraTheme(colorMode = AuraColorMode.Light) {
        ColorSchemeShowcaseContent()
    }
}

@Preview(name = "Aura Dark", widthDp = 380, heightDp = 2400)
@Composable
private fun AuraColorSchemeDarkPreview() {
    AuraTheme(colorMode = AuraColorMode.Dark) {
        ColorSchemeShowcaseContent()
    }
}

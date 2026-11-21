@file:Suppress("DEPRECATION")

package io.github.bluemarlins.aura.preview.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.component.AuraBadge
import io.github.bluemarlins.aura.component.AuraBadgeSize
import io.github.bluemarlins.aura.component.AuraBadgeVariant
import io.github.bluemarlins.aura.component.AuraBottomIsland
import io.github.bluemarlins.aura.component.AuraBottomIslandItem
import io.github.bluemarlins.aura.component.AuraButton
import io.github.bluemarlins.aura.component.AuraButtonSize
import io.github.bluemarlins.aura.component.AuraButtonVariant
import io.github.bluemarlins.aura.component.AuraCard
import io.github.bluemarlins.aura.component.AuraCardPadding
import io.github.bluemarlins.aura.component.AuraCardVariant
import io.github.bluemarlins.aura.component.AuraChipButton
import io.github.bluemarlins.aura.component.AuraChipVariant
import io.github.bluemarlins.aura.component.AuraDialog
import io.github.bluemarlins.aura.component.AuraDialogVariant
import io.github.bluemarlins.aura.component.AuraFab
import io.github.bluemarlins.aura.component.AuraIcon
import io.github.bluemarlins.aura.component.AuraIconButton
import io.github.bluemarlins.aura.component.AuraIconButtonVariant
import io.github.bluemarlins.aura.component.AuraScaffold
import io.github.bluemarlins.aura.component.AuraSegmentedButton
import io.github.bluemarlins.aura.component.AuraSegmentedSize
import io.github.bluemarlins.aura.component.AuraSwitch
import io.github.bluemarlins.aura.component.AuraText
import io.github.bluemarlins.aura.component.GlassStyle
import io.github.bluemarlins.aura.component.GlassSurface
import io.github.bluemarlins.aura.foundation.motion.AuraMotionDefaults
import io.github.bluemarlins.aura.foundation.motion.AuraMotionProfile
import io.github.bluemarlins.aura.foundation.theme.AuraColorMode
import io.github.bluemarlins.aura.foundation.theme.AuraMaterial
import io.github.bluemarlins.aura.foundation.theme.AuraTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractionPrinciplesPreviewScreen(
    onBack: () -> Unit,
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    initialProfile: AuraMotionProfile = AuraTheme.motion.profile,
    onProfileChange: (AuraMotionProfile) -> Unit = {},
    initialMaterial: AuraMaterial = AuraTheme.material,
    onMaterialChange: (AuraMaterial) -> Unit = {},
    initialReduceMotion: Boolean = AuraTheme.motion.reduceMotion,
    onReduceMotionChange: (Boolean) -> Unit = {},
    initialReduceTransparency: Boolean = AuraTheme.reduceTransparency,
    onReduceTransparencyChange: (Boolean) -> Unit = {},
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography

    var selectedProfile by remember(initialProfile) { mutableStateOf(initialProfile) }
    var selectedMaterial by remember(initialMaterial) { mutableStateOf(initialMaterial) }
    var simReduceMotion by remember(initialReduceMotion) { mutableStateOf(initialReduceMotion) }
    var simReduceTransparency by remember(initialReduceTransparency) { mutableStateOf(initialReduceTransparency) }

    val workbenchMotion = remember(selectedProfile, simReduceMotion) {
        AuraMotionDefaults.forProfile(selectedProfile).copy(reduceMotion = simReduceMotion)
    }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = "Motion & Visual Interaction",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AuraTheme.layout.screenMarginHorizontal, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            AuraCard(
                modifier = Modifier.fillMaxWidth(),
                variant = AuraCardVariant.Outlined,
                padding = AuraCardPadding.Medium,
            ) {
                AuraText(
                    text = "Global Axis Controls",
                    style = t.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                AuraText(
                    text = "Motion Profile",
                    style = t.labelMedium,
                    color = c.content.secondary,
                )
                Spacer(modifier = Modifier.height(6.dp))
                val profileOptions = listOf(AuraMotionProfile.Standard, AuraMotionProfile.Expressive)
                AuraSegmentedButton(
                    options = profileOptions.map { it.name },
                    selectedIndex = profileOptions.indexOf(selectedProfile),
                    onOptionSelected = { index ->
                        val next = profileOptions[index]
                        selectedProfile = next
                        onProfileChange(next)
                    },
                    size = AuraSegmentedSize.Small,
                )

                Spacer(modifier = Modifier.height(12.dp))
                AuraText(
                    text = "Material Axis",
                    style = t.labelMedium,
                    color = c.content.secondary,
                )
                Spacer(modifier = Modifier.height(6.dp))
                val materialOptions = listOf(AuraMaterial.Solid, AuraMaterial.Glass, AuraMaterial.LiquidGlass)
                AuraSegmentedButton(
                    options = materialOptions.map { it.name },
                    selectedIndex = materialOptions.indexOf(selectedMaterial),
                    onOptionSelected = { index ->
                        val next = materialOptions[index]
                        selectedMaterial = next
                        onMaterialChange(next)
                    },
                    size = AuraSegmentedSize.Small,
                )

                Spacer(modifier = Modifier.height(12.dp))
                AuraSwitch(
                    checked = simReduceMotion,
                    onCheckedChange = {
                        simReduceMotion = it
                        onReduceMotionChange(it)
                    },
                    label = "Simulate Reduce Motion",
                    description = "Forces snap() physics and disables spatial bounce.",
                )
                AuraSwitch(
                    checked = simReduceTransparency,
                    onCheckedChange = {
                        simReduceTransparency = it
                        onReduceTransparencyChange(it)
                    },
                    label = "Simulate Reduce Transparency",
                    description = "Forces Glass/LiquidGlass layers onto Solid floating surfaces.",
                )
            }

            AuraTheme(
                colorMode = AuraTheme.colorMode,
                material = selectedMaterial,
                motionProfile = selectedProfile,
                reduceTransparency = simReduceTransparency,
                motion = workbenchMotion,
            ) {
                WorkbenchContent(selectedMaterial = selectedMaterial)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WorkbenchContent(
    selectedMaterial: AuraMaterial,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val m = AuraTheme.motion
    val elevation = AuraTheme.elevation
    val effectiveMaterial = AuraTheme.effectiveMaterial()

    var buttonLoading by remember { mutableStateOf(false) }
    var chipSelected by remember { mutableStateOf(true) }
    var switchState by remember { mutableStateOf(true) }
    var fabExpanded by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var isSplitIsland by remember { mutableStateOf(true) }
    var splitTapCount by remember { mutableIntStateOf(0) }
    var showSpatialDialog by remember { mutableStateOf(false) }
    var replayNonce by remember { mutableIntStateOf(0) }
    var cascadeStep by remember { mutableIntStateOf(3) }

    LaunchedEffect(replayNonce, m.reduceMotion, m.staggerDelayMillis) {
        if (replayNonce > 0) {
            cascadeStep = -1
            for (index in 0..3) {
                val incrementDelay = if (m.reduceMotion) {
                    0L
                } else {
                    val prevDelay = if (index == 0) 0 else m.staggerDelay(index - 1)
                    (m.staggerDelay(index) - prevDelay).toLong().coerceAtLeast(0L)
                }
                if (incrementDelay > 0L) delay(incrementDelay)
                cascadeStep = index
            }
        }
    }

    val glassStyle = if (selectedMaterial == AuraMaterial.LiquidGlass) {
        GlassStyle.LIQUID
    } else {
        GlassStyle.FROSTED
    }

    if (showSpatialDialog) {
        AuraDialog(
            visible = true,
            title = "Spatial Entrance (${m.profile.name})",
            message = "Enters with spatialSpring(damping=${m.spatialDampingRatio}, stiffness=${m.spatialStiffness}) and scrimFade=${m.scrimFadeDurationMillis}ms.",
            variant = if (selectedMaterial == AuraMaterial.Solid) {
                AuraDialogVariant.Standard
            } else {
                AuraDialogVariant.Glass
            },
            glassStyle = glassStyle,
            onConfirm = { showSpatialDialog = false },
            onDismissRequest = { showSpatialDialog = false },
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // 1. Tactile Micro-Physics
        AuraCard(
            modifier = Modifier.fillMaxWidth(),
            variant = AuraCardVariant.Outlined,
            padding = AuraCardPadding.Medium,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AuraText(text = "1. Tactile Micro-Physics", style = t.titleMedium)
                AuraBadge(
                    label = m.profile.name,
                    variant = if (m.profile == AuraMotionProfile.Expressive) {
                        AuraBadgeVariant.Brand
                    } else {
                        AuraBadgeVariant.Neutral
                    },
                    size = AuraBadgeSize.Small,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            AuraText(
                text = "pressIn(${m.pressInDampingRatio}/${m.pressInStiffness}) · pressOut(${m.pressOutDampingRatio}/${m.pressOutStiffness}) · reduceMotion=${m.reduceMotion}",
                style = t.monoSmall,
                color = c.content.secondary,
            )
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                AuraButton(
                    text = "Primary Action",
                    onClick = {},
                    loading = buttonLoading,
                    variant = AuraButtonVariant.Primary,
                    size = AuraButtonSize.Medium,
                )
                AuraButton(
                    text = "Ghost Scale",
                    onClick = {},
                    variant = AuraButtonVariant.Ghost,
                    size = AuraButtonSize.Medium,
                )
                AuraIconButton(
                    onClick = { chipSelected = !chipSelected },
                    variant = AuraIconButtonVariant.Tonal,
                    contentDescription = "Favorite interaction",
                ) {
                    AuraIcon(imageVector = Icons.Filled.Star, contentDescription = null)
                }
                AuraChipButton(
                    label = if (chipSelected) "Chip Active" else "Chip Idle",
                    selected = chipSelected,
                    variant = AuraChipVariant.Filter,
                    onClick = { chipSelected = !chipSelected },
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AuraSwitch(
                checked = buttonLoading,
                onCheckedChange = { buttonLoading = it },
                label = "Button Loading Crossfade",
                description = "Preserves width & retains In progress semantics.",
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AuraSwitch(
                    checked = switchState,
                    onCheckedChange = { switchState = it },
                )
                AuraFab(
                    onClick = { fabExpanded = !fabExpanded },
                    text = "Compose",
                    expanded = fabExpanded,
                    contentDescription = "Toggle FAB expansion",
                ) {
                    AuraIcon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }
        }

        // 2. Spatial & Liquid Physics (Metaball Split + Dialog Spatial Entry)
        AuraCard(
            modifier = Modifier.fillMaxWidth(),
            variant = AuraCardVariant.Outlined,
            padding = AuraCardPadding.Medium,
        ) {
            AuraText(text = "2. Spatial & Liquid Physics", style = t.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            AuraText(
                text = "spatial(${m.spatialDampingRatio}/${m.spatialStiffness}) · elastic(${m.elasticDampingRatio}/${m.elasticStiffness})",
                style = t.monoSmall,
                color = c.content.secondary,
            )
            Spacer(modifier = Modifier.height(10.dp))
            AuraSwitch(
                checked = isSplitIsland,
                onCheckedChange = { isSplitIsland = it },
                label = "Split Island (Metaball)",
                description = "Detaches auxiliary pill with elasticStretchSpring() · taps=$splitTapCount",
            )
            Spacer(modifier = Modifier.height(12.dp))
            val islandItems = remember {
                listOf(
                    AuraBottomIslandItem(id = "home", label = "Home", icon = Icons.Filled.Home),
                    AuraBottomIslandItem(id = "search", label = "Search", icon = Icons.Filled.Search),
                    AuraBottomIslandItem(id = "profile", label = "Profile", icon = Icons.Filled.Person),
                )
            }
            AuraBottomIsland(
                items = islandItems,
                selectedIndex = selectedTab,
                onItemSelected = { selectedTab = it },
                glassStyle = glassStyle,
                splitActionIcon = Icons.Filled.Add,
                splitActionLabel = "New action",
                isActionSplit = isSplitIsland,
                onSplitActionClick = { splitTapCount++ },
            )
            Spacer(modifier = Modifier.height(8.dp))
            AuraButton(
                text = "Open Spatial AuraDialog",
                onClick = { showSpatialDialog = true },
                variant = AuraButtonVariant.Secondary,
                size = AuraButtonSize.Medium,
                fullWidth = true,
            )
        }

        // 3. Materiality & Optical Fallback
        AuraCard(
            modifier = Modifier.fillMaxWidth(),
            variant = AuraCardVariant.Outlined,
            padding = AuraCardPadding.Medium,
        ) {
            AuraText(text = "3. Materiality & Optical Fallback", style = t.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            AuraText(
                text = "requested=$selectedMaterial · effective=$effectiveMaterial · reduceTransparency=${AuraTheme.reduceTransparency}",
                style = t.monoSmall,
                color = c.content.secondary,
            )
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(136.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                c.brand.primary.copy(alpha = 0.28f),
                                c.status.success.solid.copy(alpha = 0.22f),
                                c.brand.subtle,
                            ),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = (-72).dp, y = (-18).dp)
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(c.brand.primary.copy(alpha = 0.45f)),
                )
                Box(
                    modifier = Modifier
                        .offset(x = 68.dp, y = 20.dp)
                        .size(84.dp)
                        .clip(CircleShape)
                        .background(c.status.warning.solid.copy(alpha = 0.35f)),
                )
                if (selectedMaterial == AuraMaterial.Solid && !AuraTheme.reduceTransparency) {
                    AuraCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        variant = AuraCardVariant.Elevated,
                        padding = AuraCardPadding.Medium,
                    ) {
                        AuraText(
                            text = "Solid Opaque Surface",
                            style = t.titleSmall,
                        )
                        AuraText(
                            text = "Canvas/Content baseline — zero backdrop refraction.",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                    }
                } else {
                    GlassSurface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        glassStyle = glassStyle,
                        shape = RoundedCornerShape(18.dp),
                        cornerRadiusHint = 18.dp,
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            AuraText(
                                text = "Effective: $effectiveMaterial",
                                style = t.titleSmall,
                            )
                            AuraText(
                                text = if (AuraTheme.reduceTransparency) {
                                    "Solid fallback active for 4.5:1 high contrast & power saving."
                                } else {
                                    "Chrome/Spatial optical layer sampling underlying backdrop."
                                },
                                style = t.bodySmall,
                                color = c.content.secondary,
                            )
                        }
                    }
                }
            }
        }

        // 4. Z-Axis Elevation Ladder
        AuraCard(
            modifier = Modifier.fillMaxWidth(),
            variant = AuraCardVariant.Outlined,
            padding = AuraCardPadding.Medium,
        ) {
            AuraText(text = "4. Z-Axis Elevation Ladder", style = t.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            AuraText(
                text = "Five discrete steps (level0..level4) binding shadow & scrim tokens.",
                style = t.bodySmall,
                color = c.content.secondary,
            )
            Spacer(modifier = Modifier.height(12.dp))
            val levels = listOf(
                "level0 (Canvas)" to elevation.level0,
                "level1 (Raised Card)" to elevation.level1,
                "level2 (Floating Chrome)" to elevation.level2,
                "level3 (Drawer / FAB)" to elevation.level3,
                "level4 (Modal / Dialog)" to elevation.level4,
            )
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                levels.forEach { (label, level) ->
                    val shape = RoundedCornerShape(12.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = level.elevation,
                                shape = shape,
                                ambientColor = Color.Black.copy(alpha = level.ambientShadowAlpha),
                                spotColor = Color.Black.copy(alpha = level.keyShadowAlpha),
                            )
                            .clip(shape)
                            .background(c.surface.floating, shape)
                            .border(1.dp, c.border.subtle, shape)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AuraText(text = label, style = t.labelLarge)
                            AuraText(
                                text = "${level.elevation} · scrim=${level.scrimAlpha}",
                                style = t.monoSmall,
                                color = c.content.secondary,
                            )
                        }
                    }
                }
            }
        }

        // 5. Staggered Choreography Cascade
        AuraCard(
            modifier = Modifier.fillMaxWidth(),
            variant = AuraCardVariant.Outlined,
            padding = AuraCardPadding.Medium,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AuraText(text = "5. Staggered Choreography", style = t.titleMedium)
                AuraButton(
                    text = "Replay Cascade",
                    onClick = { replayNonce++ },
                    variant = AuraButtonVariant.Secondary,
                    size = AuraButtonSize.Small,
                    leadingIcon = {
                        AuraIcon(imageVector = Icons.Filled.PlayArrow, contentDescription = null)
                    },
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            AuraText(
                text = "staggerDelay=${m.staggerDelayMillis}ms · budget=${m.staggerBudgetMillis}ms · enter=${m.enterDurationMillis}ms",
                style = t.monoSmall,
                color = c.content.secondary,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val stages = listOf(
                    "01 · Header & Primary Context",
                    "02 · Hero Interactive Card",
                    "03 · Secondary Metadata Row",
                    "04 · Chrome Anchor Action",
                )
                stages.forEachIndexed { index, stageTitle ->
                    val isVisible = cascadeStep >= index
                    val alpha by animateFloatAsState(
                        targetValue = if (isVisible) 1f else 0f,
                        animationSpec = if (isVisible) m.enterSpec() else snap(),
                        label = "CascadeAlpha$index",
                    )
                    val offsetY by animateFloatAsState(
                        targetValue = if (isVisible) 0f else 18f,
                        animationSpec = if (isVisible) m.spatialSpring() else snap(),
                        label = "CascadeOffset$index",
                    )
                    val shape = RoundedCornerShape(12.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                this.alpha = alpha
                                translationY = offsetY
                            }
                            .clip(shape)
                            .background(c.surface.sunken, shape)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AuraText(text = stageTitle, style = t.bodyMedium)
                        AuraText(
                            text = "+${if (m.reduceMotion) 0 else m.staggerDelay(index)}ms",
                            style = t.monoSmall,
                            color = c.brand.primary,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun InteractionPrinciplesLightPreview() {
    AuraTheme(
        colorMode = AuraColorMode.Light,
        material = AuraMaterial.LiquidGlass,
        motionProfile = AuraMotionProfile.Standard,
    ) {
        InteractionPrinciplesPreviewScreen(onBack = {})
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun InteractionPrinciplesDarkPreview() {
    AuraTheme(
        colorMode = AuraColorMode.Dark,
        material = AuraMaterial.LiquidGlass,
        motionProfile = AuraMotionProfile.Expressive,
    ) {
        InteractionPrinciplesPreviewScreen(
            onBack = {},
            isDarkTheme = true,
        )
    }
}

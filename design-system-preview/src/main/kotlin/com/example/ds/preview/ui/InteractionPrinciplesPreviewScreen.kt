@file:Suppress("DEPRECATION")

package com.example.ds.preview.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraBadge
import com.example.ds.component.AuraBadgeSize
import com.example.ds.component.AuraBadgeVariant
import com.example.ds.component.AuraBottomIsland
import com.example.ds.component.AuraBottomIslandItem
import com.example.ds.component.AuraButton
import com.example.ds.component.AuraButtonSize
import com.example.ds.component.AuraButtonVariant
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraFab
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraSegmentedButton
import com.example.ds.component.AuraSegmentedSize
import com.example.ds.component.AuraSwitch
import com.example.ds.component.AuraText
import com.example.ds.component.GlassStyle
import com.example.ds.component.GlassSurface
import com.example.ds.foundation.motion.AuraMotionDefaults
import com.example.ds.foundation.motion.AuraMotionProfile
import com.example.ds.foundation.theme.AuraMaterial
import com.example.ds.foundation.theme.AuraTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractionPrinciplesPreviewScreen(
    onBack: () -> Unit,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography

    var selectedProfile by remember { mutableStateOf(AuraMotionProfile.Standard) }
    var selectedMaterial by remember { mutableStateOf(AuraMaterial.LiquidGlass) }
    var simReduceMotion by remember { mutableStateOf(false) }
    var simReduceTransparency by remember { mutableStateOf(false) }

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
                    onOptionSelected = { index -> selectedProfile = profileOptions[index] },
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
                    onOptionSelected = { index -> selectedMaterial = materialOptions[index] },
                    size = AuraSegmentedSize.Small,
                )

                Spacer(modifier = Modifier.height(12.dp))
                AuraSwitch(
                    checked = simReduceMotion,
                    onCheckedChange = { simReduceMotion = it },
                    label = "Simulate Reduce Motion",
                    description = "Forces snap() physics and disables spatial bounce.",
                )
                AuraSwitch(
                    checked = simReduceTransparency,
                    onCheckedChange = { simReduceTransparency = it },
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
                WorkbenchContent(
                    selectedMaterial = selectedMaterial,
                )
            }
        }
    }
}

@Composable
private fun WorkbenchContent(
    selectedMaterial: AuraMaterial,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val m = AuraTheme.motion
    val elevation = AuraTheme.elevation
    val effectiveMaterial = AuraTheme.effectiveMaterial()

    var switchState by remember { mutableStateOf(true) }
    var fabExpanded by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableIntStateOf(0) }
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
                text = "spatial(damping=${m.spatialDampingRatio}, stiffness=${m.spatialStiffness}) · pressOut(damping=${m.pressOutDampingRatio}, stiffness=${m.pressOutStiffness}) · reduceMotion=${m.reduceMotion}",
                style = t.monoSmall,
                color = c.content.secondary,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AuraButton(
                    text = "Primary Action",
                    onClick = {},
                    variant = AuraButtonVariant.Primary,
                    size = AuraButtonSize.Medium,
                )
                AuraButton(
                    text = "Secondary",
                    onClick = {},
                    variant = AuraButtonVariant.Secondary,
                    size = AuraButtonSize.Medium,
                )
            }
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

        // 2. Materiality & Optical Fallback
        AuraCard(
            modifier = Modifier.fillMaxWidth(),
            variant = AuraCardVariant.Outlined,
            padding = AuraCardPadding.Medium,
        ) {
            AuraText(text = "2. Materiality & Fallback", style = t.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            AuraText(
                text = "requested=$selectedMaterial · effective=$effectiveMaterial · reduceTransparency=${AuraTheme.reduceTransparency}",
                style = t.monoSmall,
                color = c.content.secondary,
            )
            Spacer(modifier = Modifier.height(12.dp))

            val glassStyle = if (selectedMaterial == AuraMaterial.LiquidGlass) {
                GlassStyle.LIQUID
            } else {
                GlassStyle.FROSTED
            }
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

            Spacer(modifier = Modifier.height(14.dp))
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
            )
        }

        // 3. Z-Axis Elevation Ladder
        AuraCard(
            modifier = Modifier.fillMaxWidth(),
            variant = AuraCardVariant.Outlined,
            padding = AuraCardPadding.Medium,
        ) {
            AuraText(text = "3. Z-Axis Elevation Ladder", style = t.titleMedium)
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

        // 4. Staggered Choreography Cascade
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
                AuraText(text = "4. Staggered Choreography", style = t.titleMedium)
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

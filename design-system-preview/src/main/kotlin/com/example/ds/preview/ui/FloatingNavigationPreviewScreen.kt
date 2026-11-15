package com.example.ds.preview.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraBottomIslandItem
import com.example.ds.component.AuraButton
import com.example.ds.component.AuraButtonSize
import com.example.ds.component.AuraButtonVariant
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraChipButton
import com.example.ds.component.AuraChipSize
import com.example.ds.component.AuraChipVariant
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraMoreMenuAction
import com.example.ds.component.AuraSegmentedButton
import com.example.ds.component.AuraSegmentedSize
import com.example.ds.component.AuraSegmentedVariant
import com.example.ds.component.AuraSwitch
import com.example.ds.component.AuraSwitchSize
import com.example.ds.component.AuraText
import com.example.ds.component.FloatingBottomIsland
import com.example.ds.component.FloatingGlassScaffold
import com.example.ds.component.FloatingTopPillBar
import com.example.ds.component.GlassStyle
import com.example.ds.component.GlassSurface
import com.example.ds.component.MorphingMorePopover
import com.example.ds.foundation.theme.AuraColorMode
import com.example.ds.foundation.theme.AuraTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private data class SpatialFeedCardSpec(
    val title: String,
    val subtitle: String,
    val tag: String,
    val accentStart: Color,
    val accentEnd: Color,
    val metric: String,
)

@Composable
fun FloatingNavigationPreviewScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onBack: () -> Unit,
    initialTabIndex: Int = 0,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var glassStyle by remember { mutableStateOf(GlassStyle.LIQUID) }
    var metaballSplitProgress by remember { mutableFloatStateOf(0f) }
    var autoMergeMetaballOnScroll by remember { mutableStateOf(false) }
    var showQuickHud by remember { mutableStateOf(true) }
    var isActionSplit by remember { mutableStateOf(true) }
    var splitActionUseSearch by remember { mutableStateOf(false) }
    var hideTitleOnScrollDown by remember { mutableStateOf(true) }
    var isBookmarked by remember { mutableStateOf(false) }
    var moreMenuExpanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(initialTabIndex) }
    var isScrollingDown by remember { mutableStateOf(false) }
    var lastEventBanner by remember { mutableStateOf("Ready · Scroll canvas behind pills & test Liquid/Frosted") }

    val thresholdPx = remember(density) { with(density) { 64.dp.toPx().coerceAtLeast(1f) } }
    val scrollProgress by remember(thresholdPx) {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) {
                1f
            } else {
                (listState.firstVisibleItemScrollOffset / thresholdPx).coerceIn(0f, 1f)
            }
        }
    }
    val effectiveMetaballSplit = if (autoMergeMetaballOnScroll) {
        (1f - scrollProgress).coerceIn(0f, 1f)
    } else {
        metaballSplitProgress
    }

    LaunchedEffect(listState) {
        var prevKey = 0
        snapshotFlow {
            listState.firstVisibleItemIndex * 100_000 + listState.firstVisibleItemScrollOffset
        }
            .distinctUntilChanged()
            .collect { currentKey ->
                val delta = currentKey - prevKey
                if (delta > 6) {
                    isScrollingDown = true
                } else if (delta < -6 || currentKey < 12) {
                    isScrollingDown = false
                }
                prevKey = currentKey
            }
    }

    val navItems = remember {
        listOf(
            AuraBottomIslandItem(
                id = "home",
                label = "Home",
                icon = Icons.Outlined.Home,
                selectedIcon = Icons.Filled.Home,
            ),
            AuraBottomIslandItem(
                id = "diary",
                label = "Diary",
                icon = Icons.Outlined.LocalBar,
                selectedIcon = Icons.Filled.LocalBar,
            ),
            AuraBottomIslandItem(
                id = "finance",
                label = "Finance",
                icon = Icons.Outlined.AccountBalanceWallet,
                selectedIcon = Icons.Filled.AccountBalanceWallet,
            ),
            AuraBottomIslandItem(
                id = "planner",
                label = "Planner",
                icon = Icons.Outlined.CalendarMonth,
                selectedIcon = Icons.Filled.CalendarMonth,
            ),
        )
    }

    val moreActions = remember {
        listOf(
            AuraMoreMenuAction("hud", "Toggle Floating HUD"),
            AuraMoreMenuAction("mode", "Switch Frosted / Liquid"),
            AuraMoreMenuAction("split", "Toggle Bottom Split"),
            AuraMoreMenuAction("top", "Scroll to Hero Top"),
        )
    }

    val sampleFeed = remember(c.brand.primary, c.status.success.solid, c.status.warning.solid, c.status.info.solid) {
        listOf(
            SpatialFeedCardSpec(
                title = "2019 Barolo Monvigliero · Tasting Note",
                subtitle = "Garnet rim, wild strawberry, dried violet & balsamic structure.",
                tag = "My Drink Diary",
                accentStart = Color(red = 0.85f, green = 0.22f, blue = 0.36f),
                accentEnd = Color(red = 0.42f, green = 0.08f, blue = 0.28f),
                metric = "95 pts · 14.0% ABV",
            ),
            SpatialFeedCardSpec(
                title = "Global Core Dividend Allocation",
                subtitle = "Rebalanced +2.4% into fixed income ladder with automated cash buffer.",
                tag = "My Finance",
                accentStart = Color(red = 0.12f, green = 0.62f, blue = 0.56f),
                accentEnd = Color(red = 0.07f, green = 0.26f, blue = 0.42f),
                metric = "₩ 148,250,000 (+4.8%)",
            ),
            SpatialFeedCardSpec(
                title = "Morning Focus · Deep Architecture Block",
                subtitle = "Complete AGSL optical refraction pipeline & responsive pill alignment.",
                tag = "My Planner",
                accentStart = Color(red = 0.35f, green = 0.40f, blue = 0.96f),
                accentEnd = Color(red = 0.62f, green = 0.26f, blue = 0.88f),
                metric = "09:00 – 12:30",
            ),
            SpatialFeedCardSpec(
                title = "Wallpaper Depth & Frosted Dock Widgets",
                subtitle = "Chromatic prism bevel refracts underlying spectral orb wallpaper smoothly.",
                tag = "Home Launcher",
                accentStart = Color(red = 0.96f, green = 0.52f, blue = 0.14f),
                accentEnd = Color(red = 0.86f, green = 0.24f, blue = 0.46f),
                metric = "Depth Level 3 · 60fps",
            ),
            SpatialFeedCardSpec(
                title = "Yamazaki 12 Year · Single Malt Archive",
                subtitle = "Mizunara spice, candied orange peel, lingering honeyed oak finish.",
                tag = "My Drink Diary",
                accentStart = Color(red = 0.82f, green = 0.58f, blue = 0.16f),
                accentEnd = Color(red = 0.36f, green = 0.18f, blue = 0.08f),
                metric = "93 pts · 43.0% ABV",
            ),
            SpatialFeedCardSpec(
                title = "Q4 Tax-Loss Harvest Screening",
                subtitle = "Three candidate positions identified above target tracking band.",
                tag = "My Finance",
                accentStart = Color(red = 0.14f, green = 0.52f, blue = 0.92f),
                accentEnd = Color(red = 0.24f, green = 0.18f, blue = 0.68f),
                metric = "3 Signals Active",
            ),
            SpatialFeedCardSpec(
                title = "Evening Reflection · Paperless Entry",
                subtitle = "Notice how dark and bright pixels pass directly behind the bottom island.",
                tag = "My Planner",
                accentStart = Color(red = 0.18f, green = 0.72f, blue = 0.42f),
                accentEnd = Color(red = 0.08f, green = 0.32f, blue = 0.46f),
                metric = "Streak: 14 Days",
            ),
            SpatialFeedCardSpec(
                title = "Champagne Krug Grande Cuvée 171ème",
                subtitle = "Toasted brioche, chalk minerality, persistent microscopic mousse.",
                tag = "My Drink Diary",
                accentStart = Color(red = 0.90f, green = 0.72f, blue = 0.26f),
                accentEnd = Color(red = 0.52f, green = 0.32f, blue = 0.14f),
                metric = "97 pts · 12.5% ABV",
            ),
        )
    }

    val statusTopPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    FloatingGlassScaffold(
        overlayVisible = moreMenuExpanded,
        onDismissOverlay = { moreMenuExpanded = false },
        topBar = { backdropState ->
            LaunchedEffect(listState, backdropState, glassStyle, isDarkTheme, isActionSplit, selectedTab) {
                backdropState.invalidateBackdrop()
                snapshotFlow {
                    listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
                }
                    .distinctUntilChanged()
                    .collect {
                        backdropState.invalidateBackdrop()
                    }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                FloatingTopPillBar(
                    title = navItems[selectedTab].label,
                    subtitle = glassStyle.name,
                    scrollProgress = scrollProgress,
                    glassStyle = glassStyle,
                    isScrollingDown = isScrollingDown,
                    hideTitleOnScrollDown = hideTitleOnScrollDown,
                    metaballSplitProgress = effectiveMetaballSplit,
                    onBackClick = onBack,
                    onSearchClick = {
                        lastEventBanner = "Search tapped on ${navItems[selectedTab].label} (${glassStyle.name})"
                    },
                    isBookmarked = isBookmarked,
                    onBookmarkClick = {
                        isBookmarked = !isBookmarked
                        lastEventBanner = if (isBookmarked) "Bookmarked Spatial View" else "Removed Bookmark"
                    },
                    moreMenuExpanded = moreMenuExpanded,
                    onMoreMenuExpandedChange = { moreMenuExpanded = it },
                )

                AnimatedVisibility(
                    visible = showQuickHud && !moreMenuExpanded,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    FloatingDebugHudStrip(
                        glassStyle = glassStyle,
                        onToggleGlassStyle = {
                            glassStyle = if (glassStyle == GlassStyle.LIQUID) GlassStyle.FROSTED else GlassStyle.LIQUID
                            lastEventBanner = "HUD switched to GlassStyle.${glassStyle.name}"
                        },
                        metaballPercent = (effectiveMetaballSplit * 100).toInt(),
                        onCycleMetaball = {
                            autoMergeMetaballOnScroll = false
                            val next = when {
                                metaballSplitProgress < 0.20f -> 0.42f
                                metaballSplitProgress < 0.58f -> 0.72f
                                metaballSplitProgress < 0.88f -> 1.0f
                                else -> 0f
                            }
                            metaballSplitProgress = next
                            lastEventBanner = "Top Metaball -> ${(next * 100).toInt()}%"
                        },
                        isActionSplit = isActionSplit,
                        onToggleBottomSplit = {
                            isActionSplit = !isActionSplit
                            lastEventBanner = if (isActionSplit) "Bottom Split (+60dp Circle)" else "Bottom Merged"
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        },
        bottomBar = {
            FloatingBottomIsland(
                items = navItems,
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    lastEventBanner = "Tab -> ${navItems[index].label} (${glassStyle.name} squish/slide)"
                },
                glassStyle = glassStyle,
                splitActionIcon = if (splitActionUseSearch) Icons.Filled.Search else Icons.Filled.Add,
                splitActionLabel = if (splitActionUseSearch) "Quick Search" else "Quick Create",
                isActionSplit = isActionSplit,
                onSplitActionClick = {
                    lastEventBanner = if (splitActionUseSearch) "Split Search Triggered" else "Split Create Triggered"
                },
            )
        },
        overlay = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                MorphingMorePopover(
                    expanded = moreMenuExpanded,
                    actions = moreActions,
                    glassStyle = glassStyle,
                    onActionClick = { action ->
                        when (action.id) {
                            "hud" -> showQuickHud = !showQuickHud
                            "mode" -> glassStyle = if (glassStyle == GlassStyle.LIQUID) GlassStyle.FROSTED else GlassStyle.LIQUID
                            "split" -> isActionSplit = !isActionSplit
                            "top" -> scope.launch { listState.animateScrollToItem(0) }
                        }
                        lastEventBanner = "Popover Action: ${action.label}"
                    },
                    onDismissRequest = { moreMenuExpanded = false },
                )
            }
        },
    ) { scaffoldPadding ->
        // Layer 0 Full Edge-to-Edge Canvas starting at Y=0 under Status Bar & clearing Bottom Island via bottom contentPadding
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = scaffoldPadding.calculateBottomPadding(),
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Vivid Spatial Hero Banner extending directly behind State A transparent/subtle top pills
            item(key = "hero") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(statusTopPadding + 220.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    c.brand.primary,
                                    Color(red = 0.45f, green = 0.24f, blue = 0.88f),
                                    Color(red = 0.94f, green = 0.42f, blue = 0.34f),
                                ),
                            ),
                        ),
                ) {
                    // Geometric color orbs so backdrop blur & edge refraction produce unmistakable optical depth
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color.White.copy(alpha = 0.42f), Color.Transparent),
                            ),
                            radius = size.minDimension * 0.48f,
                            center = Offset(size.width * 0.82f, size.height * 0.28f),
                        )
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(red = 0.20f, green = 0.90f, blue = 0.96f, alpha = 0.55f), Color.Transparent),
                            ),
                            radius = size.minDimension * 0.42f,
                            center = Offset(size.width * 0.22f, size.height * 0.68f),
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 22.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AuraText(
                                text = "SPATIAL FLUID UI · ${glassStyle.name}",
                                style = t.labelMedium,
                                color = Color.White.copy(alpha = 0.88f),
                            )
                            AuraIconButton(
                                onClick = onToggleTheme,
                                variant = AuraIconButtonVariant.Floating,
                                contentDescription = "Toggle theme",
                            ) {
                                AuraIcon(
                                    imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                    contentDescription = null,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        AuraText(
                            text = "Adaptive Floating Glass",
                            style = t.displayMedium,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        AuraText(
                            text = "Scroll canvas up past 64dp to see State A -> State B capsule condensation and AGSL lens refraction.",
                            style = t.bodySmall,
                            color = Color.White.copy(alpha = 0.88f),
                        )
                    }
                }
            }

            // Live Optical Materiality & Metaball Inspector Deck
            item(key = "controls") {
                AuraCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    variant = AuraCardVariant.Outlined,
                    padding = AuraCardPadding.Medium,
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AuraText(
                                text = "Optical Materiality & Physics",
                                style = t.titleSmall,
                            )
                            AuraText(
                                text = "scroll ${(scrollProgress * 100).toInt()}%",
                                style = t.monoSmall,
                                color = c.brand.primary,
                            )
                        }

                        AuraText(
                            text = lastEventBanner,
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )

                        // Real-time Mode Toggle: FROSTED vs LIQUID
                        val modeIndex = if (glassStyle == GlassStyle.FROSTED) 0 else 1
                        AuraSegmentedButton(
                            options = listOf("FROSTED (Blur+Sat)", "LIQUID (Refraction)"),
                            selectedIndex = modeIndex,
                            onOptionSelected = { idx ->
                                glassStyle = if (idx == 0) GlassStyle.FROSTED else GlassStyle.LIQUID
                                lastEventBanner = "Switched to GlassStyle.${glassStyle.name}"
                            },
                            variant = AuraSegmentedVariant.Surface,
                            size = AuraSegmentedSize.Small,
                            fullWidth = true,
                        )

                        // Metaball Top Pill Split / Merge Inspector (0% -> 42% Neck -> 100% Split)
                        AuraText(
                            text = "Top Action Pill Metaball Split (${(effectiveMetaballSplit * 100).toInt()}%)",
                            style = t.labelLarge,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            listOf(
                                "Grouped" to 0f,
                                "Viscous" to 0.42f,
                                "Tear" to 0.72f,
                                "Split" to 1f,
                            ).forEach { (label, targetValue) ->
                                AuraChipButton(
                                    label = label,
                                    selected = !autoMergeMetaballOnScroll && kotlin.math.abs(metaballSplitProgress - targetValue) < 0.08f,
                                    variant = AuraChipVariant.Filter,
                                    size = AuraChipSize.Small,
                                    onClick = {
                                        autoMergeMetaballOnScroll = false
                                        metaballSplitProgress = targetValue
                                        lastEventBanner = "Metaball bridge set to ${(targetValue * 100).toInt()}%"
                                    },
                                )
                            }
                        }

                        AuraSwitch(
                            checked = autoMergeMetaballOnScroll,
                            onCheckedChange = {
                                autoMergeMetaballOnScroll = it
                                lastEventBanner = if (it) {
                                    "Top Pill Metaball coupled to scroll (0dp..64dp)"
                                } else {
                                    "Manual Metaball split control active"
                                }
                            },
                            label = "Auto-Merge Top Metaball on Scroll",
                            size = AuraSwitchSize.Small,
                        )

                        AuraSwitch(
                            checked = showQuickHud,
                            onCheckedChange = { showQuickHud = it },
                            label = "Show Floating Quick Debug HUD",
                            size = AuraSwitchSize.Small,
                        )

                        AuraSwitch(
                            checked = isActionSplit,
                            onCheckedChange = {
                                isActionSplit = it
                                lastEventBanner = if (it) "Bottom Island Split (+60dp Circle)" else "Bottom Island Merged"
                            },
                            label = "Split Bottom Action Island (Metaball)",
                            size = AuraSwitchSize.Small,
                        )

                        AuraSwitch(
                            checked = splitActionUseSearch,
                            onCheckedChange = { splitActionUseSearch = it },
                            enabled = isActionSplit,
                            label = "Use Search Icon on Split Island",
                            size = AuraSwitchSize.Small,
                        )

                        AuraSwitch(
                            checked = hideTitleOnScrollDown,
                            onCheckedChange = { hideTitleOnScrollDown = it },
                            label = "Auto-hide Title Pill on Scroll Down",
                            size = AuraSwitchSize.Small,
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            AuraButton(
                                text = "Morph [More] Menu",
                                variant = AuraButtonVariant.Secondary,
                                size = AuraButtonSize.Small,
                                onClick = { moreMenuExpanded = true },
                                modifier = Modifier.weight(1f),
                            )
                            AuraButton(
                                text = if (isDarkTheme) "Light Mode" else "Dark Mode",
                                variant = AuraButtonVariant.Outline,
                                size = AuraButtonSize.Small,
                                onClick = onToggleTheme,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }

            // Vivid Chromatic Pass-Through Cards so Top Bar & Bottom Island refract rich real colors beneath
            itemsIndexed(sampleFeed) { index, item ->
                SpatialFeedItemCard(
                    item = item,
                    index = index,
                    onClick = {
                        lastEventBanner = "Selected feed card #${index + 1}: ${item.title}"
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            // Explicit Bottom Verification Marker proving content clears FloatingBottomIsland via contentPadding
            item(key = "bottom-clearance") {
                AuraCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    variant = AuraCardVariant.Filled,
                    padding = AuraCardPadding.Compact,
                ) {
                    AuraText(
                        text = "Bottom Safe-Zone Clearance Verified",
                        style = t.labelLarge,
                        color = c.brand.primary,
                    )
                    AuraText(
                        text = "contentPadding = island(60.dp) + navBar + 16.dp + breathing clears this footer cleanly above FloatingBottomIsland.",
                        style = t.bodySmall,
                        color = c.content.secondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun SpatialFeedItemCard(
    item: SpatialFeedCardSpec,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography

    AuraCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        variant = AuraCardVariant.Outlined,
        padding = AuraCardPadding.None,
    ) {
        // Colorful visual band so scrolling under glass produces rich blur & chromatic bevel refraction
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .background(Brush.linearGradient(colors = listOf(item.accentStart, item.accentEnd)))
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    AuraText(
                        text = item.tag.uppercase(),
                        style = t.labelSmall,
                        color = Color.White.copy(alpha = 0.85f),
                    )
                    AuraText(
                        text = item.metric,
                        style = t.titleMedium,
                        color = Color.White,
                    )
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.22f))
                        .border(1.dp, Color.White.copy(alpha = 0.45f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    AuraText(
                        text = "#${index + 1}",
                        style = t.monoSmall,
                        color = Color.White,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            AuraText(
                text = item.title,
                style = t.titleSmall,
                color = c.content.primary,
            )
            AuraText(
                text = item.subtitle,
                style = t.bodySmall,
                color = c.content.secondary,
            )
        }
    }
}

@Composable
private fun FloatingDebugHudStrip(
    glassStyle: GlassStyle,
    onToggleGlassStyle: () -> Unit,
    metaballPercent: Int,
    onCycleMetaball: () -> Unit,
    isActionSplit: Boolean,
    onToggleBottomSplit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GlassSurface(
        modifier = modifier.fillMaxWidth(),
        glassStyle = glassStyle,
        shape = RoundedCornerShape(50),
        cornerRadiusHint = 22.dp,
        surfaceAlphaMultiplier = 0.92f,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AuraChipButton(
                label = if (glassStyle == GlassStyle.LIQUID) "LIQUID" else "FROSTED",
                selected = true,
                variant = AuraChipVariant.Glass,
                size = AuraChipSize.Small,
                onClick = onToggleGlassStyle,
            )
            AuraChipButton(
                label = "Metaball $metaballPercent%",
                selected = metaballPercent > 0,
                variant = AuraChipVariant.Glass,
                size = AuraChipSize.Small,
                onClick = onCycleMetaball,
            )
            AuraChipButton(
                label = if (isActionSplit) "Island Split" else "Island Merged",
                selected = isActionSplit,
                variant = AuraChipVariant.Glass,
                size = AuraChipSize.Small,
                onClick = onToggleBottomSplit,
            )
        }
    }
}

@Preview(name = "Floating Spatial UI · Light Liquid", showBackground = true)
@Composable
private fun FloatingNavigationLiquidLightPreview() {
    AuraTheme(colorMode = AuraColorMode.Light) {
        FloatingNavigationPreviewScreen(
            isDarkTheme = false,
            onToggleTheme = {},
            onBack = {},
        )
    }
}

@Preview(name = "Floating Spatial UI · Dark Frosted", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun FloatingNavigationFrostedDarkPreview() {
    AuraTheme(colorMode = AuraColorMode.Dark) {
        FloatingNavigationPreviewScreen(
            isDarkTheme = true,
            onToggleTheme = {},
            onBack = {},
            initialTabIndex = 1,
        )
    }
}

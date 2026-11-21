package io.github.bluemarlins.aura.motion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.component.AdaptiveFloatingBarPhase
import io.github.bluemarlins.aura.component.AuraAdaptiveBarState
import io.github.bluemarlins.aura.component.AuraBottomIsland
import io.github.bluemarlins.aura.component.AuraBottomIslandItem
import io.github.bluemarlins.aura.component.AuraButton
import io.github.bluemarlins.aura.component.AuraDialog
import io.github.bluemarlins.aura.component.AuraFab
import io.github.bluemarlins.aura.component.AuraIcon
import io.github.bluemarlins.aura.component.AuraIconButton
import io.github.bluemarlins.aura.component.AuraIconButtonVariant
import io.github.bluemarlins.aura.component.AuraSwitch
import io.github.bluemarlins.aura.component.AuraText
import io.github.bluemarlins.aura.component.AuraTopAppBar
import io.github.bluemarlins.aura.component.AuraTopAppBarScrollBehavior
import io.github.bluemarlins.aura.component.AuraTopAppBarSurface
import io.github.bluemarlins.aura.component.FloatingGlassScaffold
import io.github.bluemarlins.aura.component.GlassSurface
import io.github.bluemarlins.aura.component.rememberAuraAdaptiveBarState
import io.github.bluemarlins.aura.foundation.layout.AuraWindowInfo
import io.github.bluemarlins.aura.foundation.motion.AuraMotionDefaults
import io.github.bluemarlins.aura.foundation.motion.AuraMotionProfile
import io.github.bluemarlins.aura.foundation.motion.AuraMotionProgressKey
import io.github.bluemarlins.aura.foundation.motion.AuraMotionScaleKey
import io.github.bluemarlins.aura.foundation.theme.AuraMaterial
import io.github.bluemarlins.aura.foundation.theme.AuraTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class ComponentMotionBehaviorTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun testVector(name: String): ImageVector = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(2f, 2f)
            lineTo(22f, 2f)
            lineTo(22f, 22f)
            lineTo(2f, 22f)
            close()
        }
    }.build()

    @Test
    fun auraButton_pressReleaseAndMidFlightInterruption_animatesContinuously() {
        composeTestRule.mainClock.autoAdvance = false

        composeTestRule.setContent {
            AuraTheme {
                AuraButton(
                    text = "Confirm",
                    onClick = {},
                    modifier = Modifier.testTag("aura_btn"),
                )
            }
        }
        composeTestRule.mainClock.advanceTimeByFrame()

        val initialScale = composeTestRule.onNodeWithTag("aura_btn")
            .fetchSemanticsNode().config[AuraMotionScaleKey]
        assertEquals(1f, initialScale, 0.001f)

        composeTestRule.onNodeWithTag("aura_btn").performTouchInput { down(center) }
        composeTestRule.mainClock.advanceTimeByFrame()
        composeTestRule.mainClock.advanceTimeBy(32L)

        val midPressScale = composeTestRule.onNodeWithTag("aura_btn")
            .fetchSemanticsNode().config[AuraMotionScaleKey]
        assertTrue(
            "Expected button scale to compress mid-flight (< 0.998f), was $midPressScale",
            midPressScale < 0.998f,
        )

        // Release mid-flight and advance past minPressDurationMillis hold so release spring begins
        composeTestRule.onNodeWithTag("aura_btn").performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(AuraMotionDefaults.Standard.minPressDurationMillis + 16L)

        val midReboundScale = composeTestRule.onNodeWithTag("aura_btn")
            .fetchSemanticsNode().config[AuraMotionScaleKey]
        assertTrue(
            "Expected smooth rebound without snapping directly to 1f (was $midReboundScale)",
            midReboundScale in 0.90f..0.999f,
        )

        composeTestRule.mainClock.advanceTimeBy(800L)
        val settledScale = composeTestRule.onNodeWithTag("aura_btn")
            .fetchSemanticsNode().config[AuraMotionScaleKey]
        assertEquals(1f, settledScale, 0.005f)
    }

    @Test
    fun auraButton_reducedMotion_snapsScaleAndRetainsLoadingSemantics() {
        composeTestRule.mainClock.autoAdvance = false
        var loading by mutableStateOf(false)

        composeTestRule.setContent {
            AuraTheme(motion = AuraMotionDefaults.Standard.copy(reduceMotion = true)) {
                AuraButton(
                    text = "Confirm",
                    onClick = {},
                    loading = loading,
                    modifier = Modifier.testTag("aura_btn"),
                )
            }
        }
        composeTestRule.mainClock.advanceTimeByFrame()

        composeTestRule.onNodeWithTag("aura_btn").performTouchInput { down(center) }
        composeTestRule.mainClock.advanceTimeBy(64L)

        val pressedScale = composeTestRule.onNodeWithTag("aura_btn")
            .fetchSemanticsNode().config[AuraMotionScaleKey]
        assertEquals(1f, pressedScale, 0.001f)

        composeTestRule.onNodeWithTag("aura_btn").performTouchInput { up() }
        loading = true
        composeTestRule.mainClock.advanceTimeByFrame()

        composeTestRule.onNodeWithTag("aura_btn")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "In progress"))
    }

    @Test
    fun auraSwitch_toggle_animatesThumbProgressAndSnapsWhenReducedMotion() {
        composeTestRule.mainClock.autoAdvance = false
        var checked by mutableStateOf(false)
        var reduceMotion by mutableStateOf(false)

        composeTestRule.setContent {
            AuraTheme(motion = AuraMotionDefaults.Standard.copy(reduceMotion = reduceMotion)) {
                AuraSwitch(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    modifier = Modifier.testTag("aura_switch"),
                )
            }
        }
        composeTestRule.mainClock.advanceTimeByFrame()

        val initialProgress = composeTestRule.onNodeWithTag("aura_switch")
            .fetchSemanticsNode().config[AuraMotionProgressKey]
        assertEquals(0f, initialProgress, 0.001f)

        checked = true
        composeTestRule.mainClock.advanceTimeByFrame()

        val samples = buildList {
            repeat(8) {
                composeTestRule.mainClock.advanceTimeBy(16L)
                add(
                    composeTestRule.onNodeWithTag("aura_switch")
                        .fetchSemanticsNode().config[AuraMotionProgressKey],
                )
            }
        }
        assertTrue(
            "Expected intermediate thumb progress strictly between 0f and 1f, samples=$samples",
            samples.any { it in 0.05f..0.95f },
        )

        composeTestRule.mainClock.advanceTimeBy(800L)
        val settledOnProgress = composeTestRule.onNodeWithTag("aura_switch")
            .fetchSemanticsNode().config[AuraMotionProgressKey]
        assertEquals(1f, settledOnProgress, 0.01f)

        composeTestRule.runOnIdle {
            reduceMotion = true
            checked = false
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeByFrame()
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(48L)
        composeTestRule.waitForIdle()
        val snappedOffProgress = composeTestRule.onNodeWithTag("aura_switch")
            .fetchSemanticsNode().config[AuraMotionProgressKey]
        assertEquals(0f, snappedOffProgress, 0.001f)
    }

    @Test
    fun auraFab_expandedToggle_updatesMotionProgress() {
        composeTestRule.mainClock.autoAdvance = false
        var expanded by mutableStateOf(true)

        composeTestRule.setContent {
            AuraTheme {
                AuraFab(
                    onClick = {},
                    text = "Create",
                    expanded = expanded,
                    contentDescription = "Create item",
                    modifier = Modifier.testTag("aura_fab"),
                ) {}
            }
        }
        composeTestRule.mainClock.advanceTimeByFrame()

        val expandedProgress = composeTestRule.onNodeWithTag("aura_fab")
            .fetchSemanticsNode().config[AuraMotionProgressKey]
        assertEquals(1f, expandedProgress, 0.001f)

        expanded = false
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeByFrame()
        composeTestRule.mainClock.advanceTimeBy(400L)
        composeTestRule.waitForIdle()

        val collapsedProgress = composeTestRule.onNodeWithTag("aura_fab")
            .fetchSemanticsNode().config[AuraMotionProgressKey]
        assertEquals(0f, collapsedProgress, 0.001f)
    }

    @Test
    fun auraBottomIsland_expressiveSelection_animatesIndicatorProgressContinuously() {
        composeTestRule.mainClock.autoAdvance = false
        var selectedIndex by mutableIntStateOf(0)
        val items = listOf(
            AuraBottomIslandItem(id = "home", label = "Home", icon = testVector("Home")),
            AuraBottomIslandItem(id = "search", label = "Search", icon = testVector("Search")),
            AuraBottomIslandItem(id = "profile", label = "Profile", icon = testVector("Profile")),
        )

        composeTestRule.setContent {
            AuraTheme(motionProfile = AuraMotionProfile.Expressive) {
                AuraBottomIsland(
                    items = items,
                    selectedIndex = selectedIndex,
                    onItemSelected = { selectedIndex = it },
                    modifier = Modifier.testTag("aura_island"),
                )
            }
        }
        composeTestRule.mainClock.advanceTimeByFrame()

        val initialProgress = composeTestRule.onNodeWithTag("aura_island")
            .fetchSemanticsNode().config[AuraMotionProgressKey]
        assertEquals(0f, initialProgress, 0.001f)

        selectedIndex = 2
        composeTestRule.mainClock.advanceTimeByFrame()

        val midSamples = buildList {
            repeat(8) {
                composeTestRule.mainClock.advanceTimeBy(16L)
                add(
                    composeTestRule.onNodeWithTag("aura_island")
                        .fetchSemanticsNode().config[AuraMotionProgressKey],
                )
            }
        }
        assertTrue(
            "Expected continuous Expressive progress between 0f and 2f, samples=$midSamples",
            midSamples.any { it in 0.1f..1.9f },
        )

        composeTestRule.mainClock.advanceTimeBy(1000L)
        val settledProgress = composeTestRule.onNodeWithTag("aura_island")
            .fetchSemanticsNode().config[AuraMotionProgressKey]
        assertEquals(2f, settledProgress, 0.02f)
    }

    @Test
    fun auraDialog_expressiveEntry_animatesScaleAndProgressToOne() {
        composeTestRule.mainClock.autoAdvance = false

        composeTestRule.setContent {
            AuraTheme(motionProfile = AuraMotionProfile.Expressive) {
                AuraDialog(
                    visible = true,
                    title = "Confirm Action",
                    message = "Proceed with operation?",
                    onConfirm = {},
                    onDismissRequest = {},
                    modifier = Modifier.testTag("aura_dialog"),
                )
            }
        }
        composeTestRule.mainClock.advanceTimeByFrame()
        composeTestRule.mainClock.advanceTimeBy(16L)

        val entryNode = composeTestRule.onNodeWithTag("aura_dialog", useUnmergedTree = true)
            .fetchSemanticsNode().config
        assertTrue(
            "Expected entry scale in progress (<= 1.05f), was ${entryNode[AuraMotionScaleKey]}",
            entryNode[AuraMotionScaleKey] in 0.90f..1.05f,
        )

        composeTestRule.mainClock.advanceTimeBy(1000L)
        val settledNode = composeTestRule.onNodeWithTag("aura_dialog", useUnmergedTree = true)
            .fetchSemanticsNode().config
        assertEquals(1f, settledNode[AuraMotionScaleKey], 0.02f)
        assertEquals(1f, settledNode[AuraMotionProgressKey], 0.02f)
    }

    @Test
    fun auraDialog_hide_animatesOutBeforeLeavingComposition() {
        composeTestRule.mainClock.autoAdvance = false
        var visible by mutableStateOf(true)

        composeTestRule.setContent {
            AuraTheme {
                AuraDialog(
                    visible = visible,
                    title = "Confirm Action",
                    onConfirm = {},
                    onDismissRequest = { visible = false },
                    modifier = Modifier.testTag("aura_dialog"),
                )
            }
        }
        composeTestRule.mainClock.advanceTimeByFrame()
        composeTestRule.mainClock.advanceTimeBy(1000L)

        composeTestRule.runOnIdle { visible = false }
        // Let the restarted LaunchedEffect start its Hide animations before the clock moves;
        // otherwise the frames below elapse before the animations register their start time.
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeByFrame()
        composeTestRule.mainClock.advanceTimeBy(80L)

        val midExit = composeTestRule.onNodeWithTag("aura_dialog", useUnmergedTree = true)
            .fetchSemanticsNode().config
        assertTrue(
            "Expected Hide in progress (alpha between 0 and 1), was ${midExit[AuraMotionProgressKey]}",
            midExit[AuraMotionProgressKey] in 0.01f..0.99f,
        )
        assertTrue(
            "Hide must not overshoot below overlayExitScale, was ${midExit[AuraMotionScaleKey]}",
            midExit[AuraMotionScaleKey] >= AuraMotionDefaults.Standard.overlayExitScale - 0.001f,
        )

        composeTestRule.mainClock.advanceTimeBy(1000L)
        composeTestRule.onAllNodesWithTag("aura_dialog", useUnmergedTree = true).assertCountEquals(0)
    }

    @Test
    fun auraSwitch_selectionTravel_staysWithinOvershootBudget() {
        composeTestRule.mainClock.autoAdvance = false
        var checked by mutableStateOf(false)

        composeTestRule.setContent {
            AuraTheme {
                AuraSwitch(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    modifier = Modifier.testTag("aura_switch"),
                )
            }
        }
        composeTestRule.mainClock.advanceTimeByFrame()

        checked = true
        composeTestRule.mainClock.advanceTimeByFrame()
        val samples = buildList {
            repeat(40) {
                composeTestRule.mainClock.advanceTimeBy(16L)
                add(
                    composeTestRule.onNodeWithTag("aura_switch")
                        .fetchSemanticsNode().config[AuraMotionProgressKey],
                )
            }
        }
        val peak = samples.max()
        assertTrue("Thumb progress peak must stay <= 1.01f (MOT-RULE-008), was $peak", peak <= 1.01f)
        assertEquals(1f, samples.last(), 0.01f)
    }

    @Test
    fun glassSurface_reduceTransparency_togglesSolidFallbackTag() {
        var reduceTransparency by mutableStateOf(false)

        composeTestRule.setContent {
            AuraTheme(
                material = AuraMaterial.LiquidGlass,
                reduceTransparency = reduceTransparency,
            ) {
                GlassSurface(modifier = Modifier.size(120.dp, 60.dp)) {}
            }
        }
        composeTestRule.waitForIdle()

        composeTestRule.onAllNodesWithTag("aura_glass_surface_solid_fallback", useUnmergedTree = true)
            .assertCountEquals(0)

        composeTestRule.runOnIdle {
            reduceTransparency = true
        }
        composeTestRule.waitForIdle()

        composeTestRule.onAllNodesWithTag("aura_glass_surface_solid_fallback", useUnmergedTree = true)
            .assertCountEquals(1)
    }

    @Test
    fun auraTopAppBar_adaptiveFloating_animatesHideRevealAndReducesTransparencyCleanly() {
        composeTestRule.mainClock.autoAdvance = false
        var reduceTransparency by mutableStateOf(false)
        lateinit var adaptiveState: AuraAdaptiveBarState

        composeTestRule.setContent {
            AuraTheme(
                reduceTransparency = reduceTransparency,
                windowInfo = AuraWindowInfo.fromDimensions(720, 900, 900),
            ) {
                adaptiveState = rememberAuraAdaptiveBarState()
                FloatingGlassScaffold(
                    adaptiveBarState = adaptiveState,
                    topBar = {
                        AuraTopAppBar(
                            title = "Adaptive Archive",
                            surface = AuraTopAppBarSurface.TranslucentGlass,
                            scrollBehavior = AuraTopAppBarScrollBehavior.AdaptiveFloating,
                            adaptiveState = adaptiveState,
                            navigationIcon = {
                                AuraIconButton(
                                    onClick = {},
                                    variant = AuraIconButtonVariant.Ghost,
                                    contentDescription = "Back",
                                ) {
                                    AuraIcon(imageVector = testVector("Back"), contentDescription = null)
                                }
                            },
                            actions = {
                                AuraIconButton(
                                    onClick = {},
                                    variant = AuraIconButtonVariant.Ghost,
                                    contentDescription = "More",
                                ) {
                                    AuraIcon(imageVector = testVector("More"), contentDescription = null)
                                }
                            },
                        )
                    },
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AuraText("Body")
                    }
                }
            }
        }
        composeTestRule.mainClock.advanceTimeByFrame()

        // Initially DockedExpanded -> expanded title visible, visible (progress = 1f), no GlassSurface pills yet
        assertEquals(AdaptiveFloatingBarPhase.DockedExpanded, adaptiveState.phase)
        composeTestRule.onNodeWithText("Adaptive Archive").assertIsDisplayed()
        val initialVisibility = composeTestRule.onNodeWithTag("aura_adaptive_top_app_bar")
            .fetchSemanticsNode().config[AuraMotionProgressKey]
        assertEquals(1f, initialVisibility, 0.001f)

        // Scroll past collapse threshold into FloatingVisible -> title disappears & individual button GlassSurface pills mount
        composeTestRule.runOnIdle {
            adaptiveState.updateScrollBounds(scrollOffsetPx = 600f, maxScrollOffsetPx = 3000f)
        }
        composeTestRule.mainClock.advanceTimeByFrame()
        composeTestRule.mainClock.advanceTimeBy(400L)
        assertEquals(AdaptiveFloatingBarPhase.FloatingVisible, adaptiveState.phase)
        assertEquals(0, composeTestRule.onAllNodesWithText("Adaptive Archive").fetchSemanticsNodes().size)

        // Enable reduceTransparency -> individual Back & More GlassSurface pills switch to Solid 98% fallback tag
        composeTestRule.runOnIdle { reduceTransparency = true }
        composeTestRule.mainClock.advanceTimeByFrame()
        composeTestRule.onAllNodesWithTag("aura_glass_surface_solid_fallback", useUnmergedTree = true)
            .assertCountEquals(2)

        // Scroll down past +24dp -> animates smoothly toward FloatingHidden (180ms)
        composeTestRule.runOnIdle {
            adaptiveState.onScrollDelta(deltaYPx = 120f, isFromUserDrag = false)
        }
        composeTestRule.mainClock.advanceTimeByFrame()
        composeTestRule.mainClock.advanceTimeBy(90L)
        val midHideVisibility = composeTestRule.onNodeWithTag("aura_adaptive_top_app_bar")
            .fetchSemanticsNode().config[AuraMotionProgressKey]
        assertTrue(
            "Expected continuous hide transition between 0f and 1f, was $midHideVisibility",
            midHideVisibility in 0.05f..0.95f,
        )

        composeTestRule.mainClock.advanceTimeBy(250L)
        val hiddenVisibility = composeTestRule.onNodeWithTag("aura_adaptive_top_app_bar")
            .fetchSemanticsNode().config[AuraMotionProgressKey]
        assertEquals(0f, hiddenVisibility, 0.02f)

        // Reverse upward scroll past -12dp -> re-appears (250ms)
        composeTestRule.runOnIdle {
            adaptiveState.onScrollDelta(deltaYPx = -80f, isFromUserDrag = false)
        }
        composeTestRule.mainClock.advanceTimeBy(400L)
        val revealedVisibility = composeTestRule.onNodeWithTag("aura_adaptive_top_app_bar")
            .fetchSemanticsNode().config[AuraMotionProgressKey]
        assertEquals(1f, revealedVisibility, 0.02f)
    }
}

package io.github.bluemarlins.aura.component

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FloatingGlassNavigationTest {

    @Test
    fun `calculateScrollProgress interpolates continuously over 0dp to 64dp threshold`() {
        val thresholdPx = 192f
        assertEquals(0f, calculateScrollProgress(0, 0, thresholdPx), 0.0001f)
        assertEquals(0f, calculateScrollProgress(0, -10, thresholdPx), 0.0001f)
        assertEquals(0.5f, calculateScrollProgress(0, 96, thresholdPx), 0.0001f)
        assertEquals(1f, calculateScrollProgress(0, 192, thresholdPx), 0.0001f)
        assertEquals(1f, calculateScrollProgress(0, 400, thresholdPx), 0.0001f)
        assertEquals(1f, calculateScrollProgress(1, 0, thresholdPx), 0.0001f)
    }

    @Test
    fun `calculateBottomIslandContentPadding includes island height nav inset margin and breathing`() {
        val navInset = 24.dp
        val computed = calculateBottomIslandContentPadding(
            navBarBottomInset = navInset,
            islandHeight = 60.dp,
            bottomMargin = 16.dp,
            extraBreathing = 16.dp,
        )
        assertEquals(116.dp, computed)
    }

    @Test
    fun `calculateLiquidSquishStretch returns identity at rest`() {
        val (sx, sy) = calculateLiquidSquishStretch(
            currentIndex = 2f,
            targetIndex = 2f,
            velocity = 0f,
            isLiquid = true,
        )
        assertEquals(1f, sx, 0.0001f)
        assertEquals(1f, sy, 0.0001f)
    }

    @Test
    fun `calculateLiquidSquishStretch stretches horizontally at high mid-flight velocity`() {
        val (sx, sy) = calculateLiquidSquishStretch(
            currentIndex = 0.6f,
            targetIndex = 3f,
            velocity = 8.0f,
            isLiquid = true,
        )
        assertTrue("Expected horizontal stretch scaleX > 1f, got $sx", sx > 1.15f)
        assertTrue("Expected vertical compression scaleY < 1f, got $sy", sy < 0.92f)
    }

    @Test
    fun `calculateLiquidSquishStretch squishes elastically upon arrival deceleration`() {
        val (sx, sy) = calculateLiquidSquishStretch(
            currentIndex = 1.94f,
            targetIndex = 2f,
            velocity = 0.25f,
            isLiquid = true,
        )
        assertTrue("Expected arrival horizontal squish scaleX < 1f, got $sx", sx < 1f)
        assertTrue("Expected arrival vertical rebound scaleY > 1f, got $sy", sy > 1f)
    }

    @Test
    fun `calculateMetaballWaistRatio progresses monotonically with mergeProgress`() {
        val zero = calculateMetaballWaistRatio(0f)
        val tear = calculateMetaballWaistRatio(0.3f)
        val viscous = calculateMetaballWaistRatio(0.6f)
        val full = calculateMetaballWaistRatio(1f)

        assertEquals(0f, zero, 0.0001f)
        assertTrue(tear in 0.08f..0.86f)
        assertTrue(viscous > tear)
        assertEquals(0.86f, full, 0.0001f)
    }

    @Test
    fun `resolveAdaptiveFloatingBarPhase covers FAB-01 through FAB-09 and short-content edge case`() {
        val threshold = 480f
        val hideDelta = 72f
        val revealDelta = 36f
        val jitterSlop = 36f

        // FAB-01: Y == 0dp -> DockedExpanded
        assertEquals(
            AdaptiveFloatingBarPhase.DockedExpanded,
            resolveAdaptiveFloatingBarPhase(
                scrollOffsetPx = 0f,
                maxScrollOffsetPx = 2000f,
                collapseThresholdPx = threshold,
                accumulatedDeltaPx = 0f,
                hideDeltaPx = hideDelta,
                revealDeltaPx = revealDelta,
                jitterSlopPx = jitterSlop,
                isFocused = false,
                isMenuOpen = false,
                isImeVisible = false,
                windowHeightDp = 800,
                currentPhase = AdaptiveFloatingBarPhase.DockedExpanded,
            ),
        )

        // FAB-02: 0dp < Y < 160dp -> CollapsingTransition
        assertEquals(
            AdaptiveFloatingBarPhase.CollapsingTransition,
            resolveAdaptiveFloatingBarPhase(
                scrollOffsetPx = 240f,
                maxScrollOffsetPx = 2000f,
                collapseThresholdPx = threshold,
                accumulatedDeltaPx = 60f,
                hideDeltaPx = hideDelta,
                revealDeltaPx = revealDelta,
                jitterSlopPx = jitterSlop,
                isFocused = false,
                isMenuOpen = false,
                isImeVisible = false,
                windowHeightDp = 800,
                currentPhase = AdaptiveFloatingBarPhase.DockedExpanded,
            ),
        )
        assertEquals(0.5f, calculateCollapseProgress(240f, threshold), 0.0001f)

        // FAB-03: Y >= 160dp and deltaY > +24dp -> FloatingHidden
        assertEquals(
            AdaptiveFloatingBarPhase.FloatingHidden,
            resolveAdaptiveFloatingBarPhase(
                scrollOffsetPx = 600f,
                maxScrollOffsetPx = 2000f,
                collapseThresholdPx = threshold,
                accumulatedDeltaPx = 80f,
                hideDeltaPx = hideDelta,
                revealDeltaPx = revealDelta,
                jitterSlopPx = jitterSlop,
                isFocused = false,
                isMenuOpen = false,
                isImeVisible = false,
                windowHeightDp = 800,
                currentPhase = AdaptiveFloatingBarPhase.FloatingVisible,
            ),
        )

        // FAB-04: Y >= 160dp and deltaY < -12dp -> FloatingVisible
        assertEquals(
            AdaptiveFloatingBarPhase.FloatingVisible,
            resolveAdaptiveFloatingBarPhase(
                scrollOffsetPx = 600f,
                maxScrollOffsetPx = 2000f,
                collapseThresholdPx = threshold,
                accumulatedDeltaPx = -42f,
                hideDeltaPx = hideDelta,
                revealDeltaPx = revealDelta,
                jitterSlopPx = jitterSlop,
                isFocused = false,
                isMenuOpen = false,
                isImeVisible = false,
                windowHeightDp = 800,
                currentPhase = AdaptiveFloatingBarPhase.FloatingHidden,
            ),
        )

        // FAB-05: abs(deltaY) < 12dp -> keeps current phase (jitter ignored)
        assertEquals(
            AdaptiveFloatingBarPhase.FloatingHidden,
            resolveAdaptiveFloatingBarPhase(
                scrollOffsetPx = 600f,
                maxScrollOffsetPx = 2000f,
                collapseThresholdPx = threshold,
                accumulatedDeltaPx = -20f,
                hideDeltaPx = hideDelta,
                revealDeltaPx = revealDelta,
                jitterSlopPx = jitterSlop,
                isFocused = false,
                isMenuOpen = false,
                isImeVisible = false,
                windowHeightDp = 800,
                currentPhase = AdaptiveFloatingBarPhase.FloatingHidden,
            ),
        )

        // FAB-06: Snap threshold at 50%
        assertEquals(0f, calculateSnapTargetProgress(0.49f), 0.0001f)
        assertEquals(1f, calculateSnapTargetProgress(0.50f), 0.0001f)

        // FAB-07: Returning from deep scroll back to Y == 0 -> DockedExpanded
        assertEquals(
            AdaptiveFloatingBarPhase.DockedExpanded,
            resolveAdaptiveFloatingBarPhase(
                scrollOffsetPx = 0f,
                maxScrollOffsetPx = 2000f,
                collapseThresholdPx = threshold,
                accumulatedDeltaPx = -100f,
                hideDeltaPx = hideDelta,
                revealDeltaPx = revealDelta,
                jitterSlopPx = jitterSlop,
                isFocused = false,
                isMenuOpen = false,
                isImeVisible = false,
                windowHeightDp = 800,
                currentPhase = AdaptiveFloatingBarPhase.FloatingHidden,
            ),
        )

        // FAB-08: Focus or menu open pins bar in FloatingPinned even during downward scroll
        assertEquals(
            AdaptiveFloatingBarPhase.FloatingPinned,
            resolveAdaptiveFloatingBarPhase(
                scrollOffsetPx = 600f,
                maxScrollOffsetPx = 2000f,
                collapseThresholdPx = threshold,
                accumulatedDeltaPx = 150f,
                hideDeltaPx = hideDelta,
                revealDeltaPx = revealDelta,
                jitterSlopPx = jitterSlop,
                isFocused = true,
                isMenuOpen = false,
                isImeVisible = false,
                windowHeightDp = 800,
                currentPhase = AdaptiveFloatingBarPhase.FloatingVisible,
            ),
        )

        // FAB-09: IME visible or windowHeightDp < 480 -> CompactDocked
        assertEquals(
            AdaptiveFloatingBarPhase.CompactDocked,
            resolveAdaptiveFloatingBarPhase(
                scrollOffsetPx = 600f,
                maxScrollOffsetPx = 2000f,
                collapseThresholdPx = threshold,
                accumulatedDeltaPx = 150f,
                hideDeltaPx = hideDelta,
                revealDeltaPx = revealDelta,
                jitterSlopPx = jitterSlop,
                isFocused = false,
                isMenuOpen = false,
                isImeVisible = true,
                windowHeightDp = 800,
                currentPhase = AdaptiveFloatingBarPhase.FloatingVisible,
            ),
        )
        assertEquals(
            AdaptiveFloatingBarPhase.CompactDocked,
            resolveAdaptiveFloatingBarPhase(
                scrollOffsetPx = 600f,
                maxScrollOffsetPx = 2000f,
                collapseThresholdPx = threshold,
                accumulatedDeltaPx = 0f,
                hideDeltaPx = hideDelta,
                revealDeltaPx = revealDelta,
                jitterSlopPx = jitterSlop,
                isFocused = false,
                isMenuOpen = false,
                isImeVisible = false,
                windowHeightDp = 420,
                currentPhase = AdaptiveFloatingBarPhase.FloatingVisible,
            ),
        )

        // Edge Case 1: Short non-scrollable content (maxScrollOffset < 160dp) stays DockedExpanded
        assertEquals(
            AdaptiveFloatingBarPhase.DockedExpanded,
            resolveAdaptiveFloatingBarPhase(
                scrollOffsetPx = 200f,
                maxScrollOffsetPx = 300f,
                collapseThresholdPx = threshold,
                accumulatedDeltaPx = 120f,
                hideDeltaPx = hideDelta,
                revealDeltaPx = revealDelta,
                jitterSlopPx = jitterSlop,
                isFocused = false,
                isMenuOpen = false,
                isImeVisible = false,
                windowHeightDp = 800,
                currentPhase = AdaptiveFloatingBarPhase.DockedExpanded,
            ),
        )
    }

    @Test
    fun `AuraAdaptiveBarState accumulates delta filters jitter and resets on direction flip`() {
        val state = AuraAdaptiveBarState(
            collapseThresholdPx = 480f,
            hideDeltaPx = 72f,
            revealDeltaPx = 36f,
            jitterSlopPx = 36f,
        )

        // Scroll past collapse threshold
        state.updateScrollBounds(scrollOffsetPx = 600f, maxScrollOffsetPx = 3000f)
        assertEquals(AdaptiveFloatingBarPhase.FloatingVisible, state.phase)

        // Jitter inside 12dp slop does not hide
        state.onScrollDelta(deltaYPx = 20f, isFromUserDrag = true)
        assertEquals(AdaptiveFloatingBarPhase.FloatingVisible, state.phase)

        // Crossing +24dp hides
        state.onScrollDelta(deltaYPx = 60f, isFromUserDrag = true)
        assertEquals(AdaptiveFloatingBarPhase.FloatingHidden, state.phase)
        assertEquals(1f, state.hideFraction, 0.001f)

        // Reverse micro-jitter (-20px < 36px) stays hidden
        state.onScrollDelta(deltaYPx = -20f, isFromUserDrag = true)
        assertEquals(AdaptiveFloatingBarPhase.FloatingHidden, state.phase)

        // Reverse upward scroll past -12dp reveals immediately
        state.onScrollDelta(deltaYPx = -24f, isFromUserDrag = true)
        assertEquals(AdaptiveFloatingBarPhase.FloatingVisible, state.phase)
        assertEquals(0f, state.hideFraction, 0.001f)
    }

    @Test
    fun `AuraAdaptiveBarState minimizeOnScrollDown exposes fluid minimizeFraction and expands on interaction`() {
        val state = AuraAdaptiveBarState(
            collapseThresholdPx = 480f,
            hideDeltaPx = 72f,
            revealDeltaPx = 36f,
            jitterSlopPx = 36f,
            initialMinimizeOnScrollDown = true,
        )

        state.updateScrollBounds(scrollOffsetPx = 600f, maxScrollOffsetPx = 3000f)
        assertEquals(0f, state.minimizeFraction, 0.001f)

        // Downward scroll past +24dp minimizes rather than hiding when minimizeOnScrollDown is true
        state.onScrollDelta(deltaYPx = 90f, isFromUserDrag = true)
        assertEquals(AdaptiveFloatingBarPhase.FloatingHidden, state.phase)
        assertEquals(1f, state.minimizeFraction, 0.001f)

        // Tapping a minimized tab capsule expands the island immediately
        state.expandFromMinimized()
        assertEquals(AdaptiveFloatingBarPhase.FloatingVisible, state.phase)
        assertEquals(0f, state.minimizeFraction, 0.001f)
    }
}
